/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.base;

import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.stream.BaseStream;
import java.util.stream.Collector;
import java.util.stream.StreamSupport;

public interface AnyTraversable<T> extends Sized {

    @NotNull Traversable<T> asGeneric();

    static int knownSize(@NotNull Object c) {
        Objects.requireNonNull(c);
        if (c instanceof Sized) {
            return ((AnyTraversable<?>) c).knownSize();
        }
        if (c instanceof Collection<?>) {
            return ((Collection<?>) c).size();
        }
        return -1;
    }

    @NotNull Iterator<T> iterator();

    default Spliterator<T> spliterator() {
        final int ks = this.knownSize();
        if (ks == 0) {
            return Spliterators.emptySpliterator();
        } else if (ks > 0) {
            return Spliterators.spliterator(iterator(), ks, characteristics());
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), characteristics());
        }
    }

    default int characteristics() {
        return 0;
    }

    default @NotNull BaseStream<T, ?> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default @NotNull BaseStream<T, ?> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    //region Size Info

    @Override
    default boolean isEmpty() {
        final int knownSize = knownSize();
        if (knownSize < 0) {
            return !iterator().hasNext();
        } else {
            return knownSize == 0;
        }
    }

    @Override
    default boolean isNotEmpty() {
        return !isEmpty();
    }

    /**
     * Returns the size of this {@code AnyTraversable} if is a finite structure.
     *
     * @return the number of elements in this {@code AnyTraversable}
     */
    @Override
    default int size() {
        return Iterators.size(iterator());
    }

    @Override
    @Range(from = -1, to = Integer.MAX_VALUE)
    default int knownSize() {
        return -1;
    }

    //endregion

    //region Size Compare Operations

    default int sizeCompare(int otherSize) {
        if (otherSize < 0) {
            return 1;
        }
        final int knownSize = knownSize();
        if (knownSize >= 0) {
            return Integer.compare(knownSize, otherSize);
        }
        int i = 0;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            it.next();
            if (i == otherSize) {
                return 1;
            }
            i++;
        }
        return i - otherSize;
    }

    default int sizeCompare(@NotNull Iterable<?> other) {
        final int os = knownSize(other);

        if (os >= 0) {
            return sizeCompare(os);
        }
        int ks = this.knownSize();
        if (ks == 0) {
            return other.iterator().hasNext() ? -1 : 0;
        } else if (ks > 0) {
            Iterator<?> it = other.iterator();
            while (it.hasNext()) {
                it.next();
                --ks;
                if (ks == 0) {
                    return it.hasNext() ? -1 : 0;
                }
            }
            return 1;
        }

        Iterator<?> it1 = this.iterator();
        Iterator<?> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            it1.next();
            it2.next();
        }

        if (it1.hasNext()) {
            return 1;
        }
        if (it2.hasNext()) {
            return -1;
        }
        return 0;
    }

    default boolean sizeIs(int otherSize) {
        return sizeCompare(otherSize) == 0;
    }

    default boolean sizeEquals(int otherSize) {
        return sizeIs(otherSize);
    }

    default boolean sizeEquals(@NotNull Iterable<?> other) {
        return sizeCompare(other) == 0;
    }

    default boolean sizeLessThan(int otherSize) {
        return sizeCompare(otherSize) < 0;
    }

    default boolean sizeLessThan(@NotNull Iterable<?> other) {
        return sizeCompare(other) < 0;
    }

    default boolean sizeLessThanOrEquals(int otherSize) {
        return sizeCompare(otherSize) <= 0;
    }

    default boolean sizeLessThanOrEquals(@NotNull Iterable<?> other) {
        return sizeCompare(other) <= 0;
    }

    default boolean sizeGreaterThan(int otherSize) {
        return sizeCompare(otherSize) > 0;
    }

    default boolean sizeGreaterThan(@NotNull Iterable<?> other) {
        return sizeCompare(other) > 0;
    }

    default boolean sizeGreaterThanOrEquals(int otherSize) {
        return sizeCompare(otherSize) >= 0;
    }

    default boolean sizeGreaterThanOrEquals(@NotNull Iterable<?> other) {
        return sizeCompare(other) >= 0;
    }

    //endregion

    //boolean containsAll(@NotNull Iterable<?> values);

    default <R, Builder> R collect(@NotNull Collector<? super T, Builder, ? extends R> collector) {
        return Iterators.collect(iterator(), collector);
    }

    default <R, Builder> R collect(@NotNull CollectionFactory<? super T, Builder, ? extends R> factory) {
        return Iterators.collect(iterator(), factory);
    }

    @Contract(value = "_ -> param1", mutates = "param1")
    default <G extends Growable<? super T>> G collect(G destination) {
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            destination.plusAssign(it.next());
        }
        return destination;
    }

    @Contract(value = "_ -> param1", mutates = "param1")
    default <C extends java.util.Collection<? super T>> C collect(C collection) {
        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            collection.add(it.next());
        }
        return collection;
    }

    @NotNull Object toArray();

    //region String Representation

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
        return joinTo(buffer, ", ");
    }

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator) {
        return joinTo(buffer, separator, "", "");
    }

    @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
    <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    );

    default @NotNull String joinToString() {
        return joinTo(new StringBuilder()).toString();
    }

    default @NotNull String joinToString(CharSequence separator) {
        return joinTo(new StringBuilder(), separator).toString();
    }

    default @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return joinTo(new StringBuilder(), separator, prefix, postfix).toString();
    }

    //endregion
}
