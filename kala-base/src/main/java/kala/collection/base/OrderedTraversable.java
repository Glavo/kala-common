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
import kala.annotations.ReplaceWith;
import kala.control.Option;
import kala.function.CheckedIndexedConsumer;
import kala.function.IndexedBiFunction;
import kala.function.IndexedConsumer;
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
public interface OrderedTraversable<E> extends Traversable<E> {

    default @NotNull Iterator<E> reverseIterator() {
        final int ks = this.knownSize();
        if (ks == 0) {
            return Iterators.empty();
        }
        Iterator<E> it = this.iterator();
        if (!it.hasNext()) {
            return Iterators.empty();
        }
        List<E> buffer = ks > 0
                ? new ArrayList<>(ks)
                : new ArrayList<>();
        while (it.hasNext()) {
            buffer.add(it.next());
        }

        @SuppressWarnings("unchecked")
        Iterator<E> res = (Iterator<E>) GenericArrays.reverseIterator(buffer.toArray());
        return res;
    }

    //region Element Retrieval Operations

    @Override
    default @NotNull Option<E> find(@NotNull Predicate<? super E> predicate) {
        return findFirst(predicate);
    }

    default @NotNull Option<E> findFirst(@NotNull Predicate<? super E> predicate) {
        return Iterators.firstOption(iterator(), predicate);
    }

    default @NotNull Option<E> findLast(@NotNull Predicate<? super E> predicate) {
        return Iterators.firstOption(reverseIterator(), predicate);
    }

    default E getFirst() {
        return this.iterator().next();
    }

    @DelegateBy("getFirst()")
    default @Nullable E getFirstOrNull() {
        return isNotEmpty() ? getFirst() : null;
    }

    @DelegateBy("getFirst()")
    default @NotNull Option<E> getFirstOption() {
        return isNotEmpty() ? Option.some(getFirst()) : Option.none();
    }

    @Deprecated
    @ReplaceWith("getFirst()")
    default E first() {
        return getFirst();
    }

    @Deprecated
    @ReplaceWith("getFirstOrNull()")
    default E firstOrNull() {
        return getFirstOrNull();
    }

    @Deprecated
    @ReplaceWith("getFirstOption()")
    default Option<E> firstOption() {
        return getFirstOption();
    }

    default E getLast() {
        return reverseIterator().next();
    }

    @DelegateBy("getLast()")
    default @Nullable E getLastOrNull() {
        return isNotEmpty() ? getLast() : null;
    }

    @DelegateBy("getLast()")
    default @NotNull Option<E> getLastOption() {
        return isNotEmpty() ? Option.some(getLast()) : Option.none();
    }

    @Deprecated
    @ReplaceWith("getLast()")
    default E last() {
        return getLast();
    }

    @Deprecated
    @ReplaceWith("getLastOrNull()")
    default E lastOrNull() {
        return getLastOrNull();
    }

    @Deprecated
    @ReplaceWith("getLastOption()")
    default Option<E> lastOption() {
        return getLastOption();
    }

    @Deprecated
    @ReplaceWith("findFirst(Predicate<E>)")
    default E first(@NotNull Predicate<? super E> predicate) {
        return findFirst(predicate).get();
    }

    @Deprecated
    @ReplaceWith("findFirst(Predicate<E>)")
    default @Nullable E firstOrNull(@NotNull Predicate<? super E> predicate) {
        return findFirst(predicate).getOrNull();
    }

    @Deprecated
    @ReplaceWith("findFirst(Predicate<E>)")
    default @NotNull Option<E> firstOption(@NotNull Predicate<? super E> predicate) {
        return findFirst(predicate);
    }

    @Deprecated
    @ReplaceWith("findLast(Predicate<E>)")
    default E last(@NotNull Predicate<? super E> predicate) {
        return findLast(predicate).get();
    }

    @Deprecated
    @ReplaceWith("findLast(Predicate<E>)")
    default @Nullable E lastOrNull(@NotNull Predicate<? super E> predicate) {
        return findLast(predicate).getOrNull();
    }

    @Deprecated
    @ReplaceWith("findLast(Predicate<E>)")
    default @NotNull Option<E> lastOption(@NotNull Predicate<? super E> predicate) {
        return findLast(predicate);
    }

    //endregion

    //region Search Operations

    @Contract(pure = true)
    default int indexOf(Object value) {
        int idx = 0;
        if (value == null) {
            for (E e : this) {
                if (null == e) {
                    return idx;
                }
                ++idx;
            }
        } else {
            for (E e : this) {
                if (value.equals(e)) {
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
            for (E e : this) {
                if (idx >= from && null == e) {
                    return idx;
                }
                ++idx;
            }
        } else {
            for (E e : this) {
                if (idx >= from && value.equals(e)) {
                    return idx;
                }
                ++idx;
            }
        }
        return -1;
    }

    @Contract(pure = true)
    default int indexWhere(@NotNull Predicate<? super E> predicate) {
        int idx = 0;
        for (E e : this) {
            if (predicate.test(e)) { // implicit null check of predicate
                return idx;
            }
            ++idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
        int idx = 0;
        for (E e : this) {
            if (idx >= from && predicate.test(e)) { // implicit null check of predicate
                return idx;
            }
            ++idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexOf(Object value) {
        int idx = size() - 1;
        Iterator<E> it = reverseIterator();

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
        Iterator<E> it = reverseIterator();

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
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        int idx = size() - 1;
        Iterator<E> it = reverseIterator();
        while (it.hasNext()) {
            if (predicate.test(it.next())) { // implicit null check of predicate
                return idx;
            }
            --idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
        int idx = size() - 1;
        Iterator<E> it = reverseIterator();
        while (it.hasNext()) {
            if (idx <= end && predicate.test(it.next())) { // implicit null check of predicate
                return idx;
            }
            --idx;
        }
        return -1;
    }

    //endregion

    default E foldIndexed(E zero, @NotNull IndexedBiFunction<? super E, ? super E, ? extends E> op) {
        return foldLeftIndexed(zero, op);
    }

    default <U> U foldLeftIndexed(U zero, @NotNull IndexedBiFunction<? super U, ? super E, ? extends U> op) {
        return Iterators.foldLeftIndexed(this.iterator(), zero, op);
    }

    default <U> U foldRightIndexed(U zero, @NotNull IndexedBiFunction<? super E, ? super U, ? extends U> op) {
        return Iterators.foldRightIndexed(this.iterator(), zero, op);
    }

    @Override
    default E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        final Iterator<E> it = this.reverseIterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        E e = it.next();
        while (it.hasNext()) {
            e = op.apply(it.next(), e);
        }
        return e;
    }

    default void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        int idx = 0;
        for (E e : this) {
            action.accept(idx++, e); // implicit null check of action
        }
    }

    default <Ex extends Throwable> void forEachIndexedChecked(
            @NotNull CheckedIndexedConsumer<? super E, ? extends Ex> action) throws Ex {
        forEachIndexed(action);
    }

    default void forEachIndexedUnchecked(@NotNull CheckedIndexedConsumer<? super E, ?> action) {
        forEachIndexed(action);
    }
}
