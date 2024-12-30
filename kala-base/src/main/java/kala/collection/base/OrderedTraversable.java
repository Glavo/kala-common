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

import kala.annotations.DelegateBy;
import kala.control.Option;
import kala.function.CheckedIndexedConsumer;
import kala.function.IndexedBiFunction;
import kala.function.IndexedConsumer;
import kala.index.Index;
import kala.index.Indexes;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * A collection of elements that exist in some order.
 * <p>
 * This order can be insertion order, sort order, or any other meaningful order,
 * its iterators and iteration methods should follow this order.
 */
public interface OrderedTraversable<T> extends Traversable<T> {

    default @NotNull Iterator<T> iterator(@Index int beginIndex) {
        if (beginIndex >= 0) {
            final int knownSize = knownSize();
            if (knownSize >= 0) {
                if (beginIndex > knownSize) {
                    throw new IndexOutOfBoundsException(beginIndex);
                }
                if (beginIndex == knownSize) {
                    return Iterators.empty();
                }
            }
        } else {
            int size = size();
            beginIndex = Indexes.checkPositionIndex(beginIndex, size);

            if (beginIndex == size) {
                return Iterators.empty();
            }
        }

        final Iterator<T> it = iterator();
        for (int i = 0; i < beginIndex; i++) {
            if (!it.hasNext()) {
                throw new IndexOutOfBoundsException(beginIndex);
            }
            it.next();
        }
        return it;
    }

    default @NotNull Iterator<T> reverseIterator() {
        final int ks = this.knownSize();
        if (ks == 0) {
            return Iterators.empty();
        }
        Iterator<T> it = this.iterator();
        if (!it.hasNext()) {
            return Iterators.empty();
        }
        List<T> buffer = ks > 0
                ? new ArrayList<>(ks)
                : new ArrayList<>();
        while (it.hasNext()) {
            buffer.add(it.next());
        }

        @SuppressWarnings("unchecked")
        Iterator<T> res = (Iterator<T>) GenericArrays.reverseIterator(buffer.toArray());
        return res;
    }

    //region Element Retrieval Operations

    @Override
    default @NotNull Option<T> find(@NotNull Predicate<? super T> predicate) {
        return findFirst(predicate);
    }

    default @NotNull Option<T> findFirst(@NotNull Predicate<? super T> predicate) {
        return Iterators.firstOption(iterator(), predicate);
    }

    default @NotNull Option<T> findLast(@NotNull Predicate<? super T> predicate) {
        return Iterators.firstOption(reverseIterator(), predicate);
    }

    @Override
    default T getAny() {
        return getFirst();
    }

    default T getFirst() {
        return this.iterator().next();
    }

    @DelegateBy("getFirst()")
    default @Nullable T getFirstOrNull() {
        return isNotEmpty() ? getFirst() : null;
    }

    @DelegateBy("getFirst()")
    default @NotNull Option<T> getFirstOption() {
        return isNotEmpty() ? Option.some(getFirst()) : Option.none();
    }

    default T getLast() {
        return reverseIterator().next();
    }

    @DelegateBy("getLast()")
    default @Nullable T getLastOrNull() {
        return isNotEmpty() ? getLast() : null;
    }

    @DelegateBy("getLast()")
    default @NotNull Option<T> getLastOption() {
        return isNotEmpty() ? Option.some(getLast()) : Option.none();
    }

    //endregion

    //region Search Operations

    @Contract(pure = true)
    default int indexOf(Object value) {
        int idx = 0;
        if (value == null) {
            for (T t : this) {
                if (null == t) {
                    return idx;
                }
                ++idx;
            }
        } else {
            for (T t : this) {
                if (value.equals(t)) {
                    return idx;
                }
                ++idx;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    default int indexOf(Object value, int from) {
        int idx = 0;
        if (value == null) {
            for (T t : this) {
                if (idx >= from && null == t) {
                    return idx;
                }
                ++idx;
            }
        } else {
            for (T t : this) {
                if (idx >= from && value.equals(t)) {
                    return idx;
                }
                ++idx;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    default int indexWhere(@NotNull Predicate<? super T> predicate) {
        int idx = 0;
        for (T t : this) {
            if (predicate.test(t)) { // implicit null check of predicate
                return idx;
            }
            ++idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int indexWhere(@NotNull Predicate<? super T> predicate, int from) {
        int idx = 0;
        for (T t : this) {
            if (idx >= from && predicate.test(t)) { // implicit null check of predicate
                return idx;
            }
            ++idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexOf(Object value) {
        int idx = size() - 1;
        Iterator<T> it = reverseIterator();

        if (value == null) {
            while (it.hasNext()) {
                if (null == it.next()) {
                    return idx;
                }
                --idx;
            }
        } else {
            while (it.hasNext()) {
                if (value.equals(it.next())) {
                    return idx;
                }
                --idx;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexOf(Object value, int end) {
        int idx = size() - 1;
        Iterator<T> it = reverseIterator();

        if (value == null) {
            while (it.hasNext()) {
                if (idx <= end && null == it.next()) {
                    return idx;
                }
                --idx;
            }
        } else {
            while (it.hasNext()) {
                if (idx <= end && value.equals(it.next())) {
                    return idx;
                }
                --idx;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexWhere(@NotNull Predicate<? super T> predicate) {
        int idx = size() - 1;
        Iterator<T> it = reverseIterator();
        while (it.hasNext()) {
            if (predicate.test(it.next())) { // implicit null check of predicate
                return idx;
            }
            --idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexWhere(@NotNull Predicate<? super T> predicate, int end) {
        int idx = size() - 1;
        Iterator<T> it = reverseIterator();
        while (it.hasNext()) {
            if (idx <= end && predicate.test(it.next())) { // implicit null check of predicate
                return idx;
            }
            --idx;
        }
        return -1;
    }

    //endregion

    default T foldIndexed(T zero, @NotNull IndexedBiFunction<? super T, ? super T, ? extends T> op) {
        return foldLeftIndexed(zero, op);
    }

    default <U> U foldLeftIndexed(U zero, @NotNull IndexedBiFunction<? super U, ? super T, ? extends U> op) {
        return Iterators.foldLeftIndexed(this.iterator(), zero, op);
    }

    default <U> U foldRightIndexed(U zero, @NotNull IndexedBiFunction<? super T, ? super U, ? extends U> op) {
        return Iterators.foldRightIndexed(this.iterator(), zero, op);
    }

    @Override
    default T reduceRight(@NotNull BiFunction<? super T, ? super T, ? extends T> op) throws NoSuchElementException {
        final Iterator<T> it = this.reverseIterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        T t = it.next();
        while (it.hasNext()) {
            t = op.apply(it.next(), t);
        }
        return t;
    }

    //region Copy Operations

    @Override
    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(Object @NotNull [] dest, int destPos, int limit) {
        return copyToArray(0, dest, destPos, limit);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, Object @NotNull [] dest) {
        return copyToArray(srcPos, dest, 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, Object @NotNull [] dest, int destPos) {
        return copyToArray(srcPos, dest, destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + srcPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        if (limit <= 0) {
            return 0;
        }

        final int dl = dest.length; //implicit null check of dest
        if (destPos > dl) {
            return 0;
        }

        final int kn = this.knownSize();
        if (kn >= 0 && srcPos >= kn) {
            return 0;
        }

        int end = Math.min(dl - destPos, limit) + destPos;

        Iterator<T> it;
        try {
            it = this.iterator(srcPos);
        } catch (IndexOutOfBoundsException ignored) {
            return 0;
        }

        int idx = destPos;
        while (it.hasNext() && idx < end) {
            dest[idx++] = it.next();
        }
        return idx - destPos;
    }

    //endregion

    default void forEachIndexed(@NotNull IndexedConsumer<? super T> action) {
        int idx = 0;
        for (T t : this) {
            action.accept(idx++, t); // implicit null check of action
        }
    }

    default <Ex extends Throwable> void forEachIndexedChecked(
            @NotNull CheckedIndexedConsumer<? super T, ? extends Ex> action) throws Ex {
        forEachIndexed(action);
    }

    default void forEachIndexedUnchecked(@NotNull CheckedIndexedConsumer<? super T, ?> action) {
        forEachIndexed(action);
    }
}
