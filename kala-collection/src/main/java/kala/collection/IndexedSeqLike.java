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
package kala.collection;

import kala.collection.base.AbstractIterator;
import kala.collection.base.Growable;
import kala.collection.base.Iterators;
import kala.collection.factory.MapFactory;
import kala.collection.immutable.ImmutableMap;
import kala.collection.mutable.MutableSeq;
import kala.control.Option;
import kala.function.IndexedBiFunction;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;

public interface IndexedSeqLike<E> extends SeqLike<E>, RandomAccess {

    @Override
    default @NotNull Iterator<E> iterator() {
        return iterator(0);
    }

    @Override
    default @NotNull Iterator<E> iterator(@Index int beginIndex) {
        final int size = size();
        beginIndex = Indexes.checkPositionIndex(beginIndex, size);

        if (beginIndex == size) return Iterators.empty();

        final class Itr extends AbstractIterator<E> {
            private int idx;

            Itr(int beginIndex) {
                this.idx = beginIndex;
            }

            @Override
            public boolean hasNext() {
                return idx < size;
            }

            @Override
            public E next() {
                if (idx >= size) {
                    throw new NoSuchElementException();
                }
                return get(idx++);
            }
        }

        return new Itr(beginIndex);
    }

    //region Size Info

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default int knownSize() {
        return size();
    }

    //endregion

    //region Positional Access Operations

    @Override
    default boolean supportsFastRandomAccess() {
        return true;
    }

    @Override
    default boolean isDefinedAt(int index) {
        return index >= 0 && index < size();
    }

    @Override
    E get(int index);

    //endregion

    //region Reversal Operations

    @Override
    default @NotNull Iterator<E> reverseIterator() {
        if (isEmpty()) return Iterators.empty();

        return new AbstractIterator<E>() {
            private int idx = size() - 1;

            @Override
            public boolean hasNext() {
                return idx >= 0;
            }

            @Override
            public E next() {
                if (idx < 0) throw new NoSuchElementException();

                return get(idx--);
            }
        };
    }

    //endregion

    //region Element Retrieval Operations

    @Override
    default @NotNull Option<E> findFirst(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = get(i);
            if (predicate.test(e)) {
                return Option.some(e);
            }
        }
        return Option.none();
    }

    @Override
    default @NotNull Option<E> findLast(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            E e = get(i);
            if (predicate.test(e)) {
                return Option.some(e);
            }
        }
        return Option.none();
    }

    @Override
    default E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return get(0);
    }

    @Override
    default E getLast() {
        final int size = size();
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return get(size - 1);
    }

    //endregion

    //region Element Conditions

    @Override
    default boolean contains(Object value) {
        final int size = size();

        if (size == 0) {
            return false;
        }

        if (value == null) {
            for (int i = 0; i < size; i++) {
                if (null == get(i)) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (value.equals(get(i))) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    default boolean anyMatch(@NotNull Predicate<? super E> predicate) {
        final int size = size();

        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean allMatch(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (!predicate.test(get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean noneMatch(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Search Operations

    @Override
    default int indexOf(Object value) {
        final int size = size();

        if (value == null) {
            for (int i = 0; i < size; i++) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    default int indexOf(Object value, int from) {
        final int size = size();

        if (from >= size) {
            return -1;
        }

        if (value == null) {
            for (int i = Math.max(from, 0); i < size; i++) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = Math.max(from, 0); i < size; i++) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    default int indexWhere(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
        final int size = size();

        if (from >= size) {
            return -1;
        }

        for (int i = Math.max(from, 0); i < size; i++) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default int lastIndexOf(Object value) {
        if (value == null) {
            for (int i = size() - 1; i >= 0; i--) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = size() - 1; i >= 0; i--) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    default int lastIndexOf(Object value, int end) {
        if (end < 0) {
            return -1;
        }
        if (value == null) {
            for (int i = Integer.min(end, size() - 1); i >= 0; i--) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = Integer.min(end, size() - 1); i >= 0; i--) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        for (int i = size() - 1; i >= 0; i--) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
        if (end < 0) {
            return -1;
        }
        for (int i = end; i >= 0; i--) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    //endregion

    @Override
    default <G extends Growable<? super E>> @NotNull G filterTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = this.get(i);
            if (predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Override
    default <G extends Growable<? super E>> @NotNull G filterNotTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = this.get(i);
            if (!predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Override
    default <G extends Growable<? super E>> @NotNull G filterNotNullTo(@NotNull G destination) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = this.get(i);
            if (e != null) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<? super U>> @NotNull G mapTo(@NotNull G destination, @NotNull Function<? super E, ? extends U> mapper) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            destination.plusAssign(mapper.apply(this.get(i)));
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<? super U>> @NotNull G mapNotNullTo(
            @NotNull G destination,
            @NotNull Function<? super E, ? extends U> mapper) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            U u = mapper.apply(this.get(i));
            if (u != null) {
                destination.plusAssign(u);
            }
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<? super U>> @NotNull G mapIndexedTo(@NotNull G destination, @NotNull IndexedFunction<? super E, ? extends U> mapper) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            destination.plusAssign(mapper.apply(i, this.get(i)));
        }
        return destination;
    }

    //region Aggregate Operations

    @Override
    default int count(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        int c = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                ++c;
            }
        }
        return c;
    }

    @Override
    @SuppressWarnings("unchecked")
    default E max(Comparator<? super E> comparator) {
        final int size = size();
        if (size == 0) {
            throw new NoSuchElementException();
        }

        E res = get(0);

        if (comparator == null) {
            for (int i = 1; i < size; i++) {
                E e = get(i);
                if (((Comparable<E>) res).compareTo(e) < 0) {
                    res = e;
                }
            }
        } else {
            for (int i = 1; i < size; i++) {
                E e = get(i);
                if (comparator.compare(res, e) < 0) {
                    res = e;
                }
            }
        }

        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    default E min(Comparator<? super E> comparator) {
        final int size = size();
        if (size == 0) {
            throw new NoSuchElementException();
        }

        E res = get(0);

        if (comparator == null) {
            for (int i = 1; i < size; i++) {
                E e = get(i);
                if (((Comparable<E>) res).compareTo(e) > 0) {
                    res = e;
                }
            }
        } else {
            for (int i = 1; i < size; i++) {
                E e = get(i);
                if (comparator.compare(res, e) > 0) {
                    res = e;
                }
            }
        }
        return res;
    }

    @Override
    default <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op) {
        final int size = size();

        for (int i = 0; i < size; i++) {
            zero = op.apply(zero, get(i));
        }
        return zero;
    }

    @Override
    default <U> U foldRight(U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op) {
        final int size = size();

        for (int i = size - 1; i >= 0; i--) {
            zero = op.apply(get(i), zero);
        }
        return zero;
    }

    @Override
    default <U> U foldLeftIndexed(U zero, @NotNull IndexedBiFunction<? super U, ? super E, ? extends U> op) {
        final int size = size();

        for (int i = 0; i < size; i++) {
            zero = op.apply(i, zero, get(i));
        }
        return zero;
    }

    @Override
    default <U> U foldRightIndexed(U zero, @NotNull IndexedBiFunction<? super E, ? super U, ? extends U> op) {
        final int size = size();

        for (int i = size - 1; i >= 0; i--) {
            zero = op.apply(i, get(i), zero);
        }
        return zero;
    }

    @Override
    default E reduceLeft(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E e = get(0);
        for (int i = 1; i < size; i++) {
            e = op.apply(e, get(i));
        }
        return e;
    }

    @Override
    default E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E e = get(size - 1);
        for (int i = size - 2; i >= 0; i--) {
            e = op.apply(get(i), e);
        }
        return e;
    }

    //endregion

    @Override
    default int copyTo(int srcPos, @NotNull MutableSeq<? super E> dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        final int sourceSize = size();
        final int destSize = dest.size();

        if (destPos >= destSize || srcPos >= sourceSize) {
            return 0;
        }

        final int n = Math.min(Math.min(sourceSize - srcPos, destSize - destPos), limit);

        for (int i = 0; i < n; i++) {
            dest.set(i + destPos, get(i + srcPos));
        }

        return n;
    }

    @Override
    default int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        final int sourceSize = size();
        final int destSize = dest.length;

        if (destPos >= destSize || srcPos >= sourceSize) {
            return 0;
        }

        final int n = Math.min(Math.min(sourceSize - srcPos, destSize - destPos), limit);

        for (int i = 0; i < n; i++) {
            dest[i + destPos] = get(i + srcPos);
        }

        return n;
    }

    @Override
    default Object @NotNull [] toArray() {
        final int size = size();
        Object[] arr = new Object[size];

        for (int i = 0; i < size; i++) {
            arr[i] = get(i);
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    default <U> U @NotNull [] toArray(@NotNull Class<U> type) {
        final int size = size();
        U[] arr = (U[]) Array.newInstance(type, size); // implicit null check of type

        for (int i = 0; i < size; i++) {
            arr[i] = (U) get(i);
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    default <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        final int size = size();
        U[] arr = generator.apply(size); // implicit null check of generator

        for (int i = 0; i < size; i++) {
            arr[i] = (U) get(i);
        }
        return arr;
    }

    @Override
    default @NotNull <K, V> ImmutableMap<K, V> toImmutableMap() {
        final int size = this.size();
        if (size == 0) {
            return ImmutableMap.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, V, Object, ImmutableMap<K, V>> factory =
                (MapFactory) kala.collection.Map.factory();
        final Object builder = factory.newBuilder();
        factory.sizeHint(builder, size);

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked") final java.util.Map.Entry<K, V> v = (java.util.Map.Entry<K, V>) this.get(i);
            factory.addToBuilder(builder, v.getKey(), v.getValue());
        }

        return factory.build(builder);
    }

    @Override
    default <K, V> kala.collection.@NotNull Map<K, V> associate(
            @NotNull Function<? super E, ? extends java.util.Map.Entry<? extends K, ? extends V>> transform) {
        final int size = this.size();
        if (size == 0) {
            return kala.collection.Map.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, V, Object, kala.collection.Map<K, V>> factory =
                (MapFactory) kala.collection.Map.factory();
        final Object builder = factory.newBuilder();
        factory.sizeHint(builder, size);

        for (int i = 0; i < size; i++) {
            final java.util.Map.Entry<? extends K, ? extends V> v = transform.apply(this.get(i));
            factory.addToBuilder(builder, v.getKey(), v.getValue());
        }

        return factory.build(builder);
    }

    @Override
    default <K> kala.collection.@NotNull Map<K, E> associateBy(@NotNull Function<? super E, ? extends K> keySelector) {
        final int size = this.size();
        if (size == 0) {
            return kala.collection.Map.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, E, Object, kala.collection.Map<K, E>> factory =
                (MapFactory) kala.collection.Map.factory();
        final Object builder = factory.newBuilder();
        factory.sizeHint(builder, size);

        for (int i = 0; i < size; i++) {
            final E e = this.get(i);
            factory.addToBuilder(builder, keySelector.apply(e), e);
        }

        return factory.build(builder);
    }

    @Override
    default <K, V> kala.collection.@NotNull Map<K, V> associateBy(
            @NotNull Function<? super E, ? extends K> keySelector, @NotNull Function<? super E, ? extends V> valueTransform) {
        final int size = this.size();
        if (size == 0) {
            return kala.collection.Map.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, V, Object, kala.collection.Map<K, V>> factory =
                (MapFactory) kala.collection.Map.factory();
        final Object builder = factory.newBuilder();
        factory.sizeHint(builder, size);

        for (int i = 0; i < size; i++) {
            final E e = this.get(i);
            factory.addToBuilder(builder, keySelector.apply(e), valueTransform.apply(e));
        }

        return factory.build(builder);
    }

    @Override
    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        final int size = size();

        try {
            buffer.append(prefix);
            if (size > 0) {
                buffer.append(Objects.toString(get(0)));
                for (int i = 1; i < size; i++) {
                    buffer.append(separator).append(Objects.toString(get(i)));
                }
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return buffer;
    }

    @Override
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix, @NotNull Function<? super E, ? extends CharSequence> transform) {
        final int size = size();

        try {
            buffer.append(prefix);
            if (size > 0) {
                buffer.append(transform.apply(get(0)));
                for (int i = 1; i < size; i++) {
                    buffer.append(separator).append(transform.apply(get(i)));
                }
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return buffer;
    }

    @Override
    default void forEach(@NotNull Consumer<? super E> action) {
        final int size = this.size();

        for (int i = 0; i < size; i++) {
            action.accept(get(i));
        }
    }

    @Override
    default void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        final int size = this.size();

        for (int i = 0; i < size; i++) {
            action.accept(i, get(i));
        }
    }
}
