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
package kala.collection.primitive;

import kala.Conditions;
import kala.annotations.DelegateBy;
import kala.collection.SeqLike;
import kala.collection.base.primitive.${Type}Arrays;
import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.primitive.internal.${Type}SeqIterators;
import kala.collection.mutable.primitive.Mutable${Type}ArrayList;
import kala.control.primitive.${Type}Option;
import kala.function.*;
import kala.index.Index;
import kala.index.Indexes;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.function.*;

public interface ${Type}SeqLike extends PrimitiveSeqLike<${WrapperType}>, ${Type}CollectionLike {
    @Override
    @NotNull
    default String className() {
        return "${Type}SeqLike";
    }

    @Override
    @NotNull ${Type}SeqView view();

    default @NotNull ${Type}Iterator iterator(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") < 0");
        }
        final int knownSize = knownSize();
        if (knownSize >= 0) {
            if (beginIndex > knownSize) {
                throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") > size(" + knownSize + ")");
            }
            if (beginIndex == knownSize) {
                return ${Type}Iterator.empty();
            }
        }

        final ${Type}Iterator it = iterator();
        for (int i = 0; i < beginIndex; i++) {
            if (!it.hasNext()) {
                throw new IndexOutOfBoundsException("beginIndex: " + beginIndex);
            }
            it.next${Type}();
        }
        return it;
    }

    default @NotNull ${Type}SeqIterator seqIterator() {
        return seqIterator(0);
    }

    default @NotNull ${Type}SeqIterator seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new ${Type}SeqIterators.Default${Type}SeqIterator<>(this, index);
    }

    //region Positional Access Operations

    @Contract(pure = true)
    default boolean isDefinedAt(@Index int index) {
        if (index >= 0) {
            return sizeGreaterThan(index);
        } else {
            return index != ~0 && sizeGreaterThanOrEquals(~index);
        }
    }

    @Override
    default ${PrimitiveType} elementAt(int index) {
        return get(index);
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true)
    default ${PrimitiveType} get(@Index int index) {
        return iterator(index).next${Type}();
    }

    @Contract(pure = true)
    @DelegateBy("get(int)")
    default @Nullable ${WrapperType} getOrNull(int index) {
        return isDefinedAt(index) ? get(index) : null;
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true, targetIsContainer = true)
    @DelegateBy("get(int)")
    default @NotNull ${Type}Option getOption(int index) {
        return isDefinedAt(index) ? ${Type}Option.some(get(index)) : ${Type}Option.none();
    }

    //endregion

    //region Reversal Operations

    default @NotNull ${Type}Iterator reverseIterator() {
        ${Type}Iterator it = this.iterator();
        if (!it.hasNext()) {
            return it;
        }
        Mutable${Type}ArrayList builder = new Mutable${Type}ArrayList();
        while (it.hasNext()) {
            builder.append(it.next${Type}());
        }
        return builder.reverseIterator();
    }

    //endregion

    //region Element Retrieval Operations

    @Override
    default @NotNull ${Type}Option find(@NotNull ${Type}Predicate predicate) {
        return findFirst(predicate);
    }

    default @NotNull ${Type}Option findFirst(@NotNull ${Type}Predicate predicate) {
        return iterator().find(predicate);
    }

    default @NotNull ${Type}Option findLast(@NotNull ${Type}Predicate predicate) {
        return reverseIterator().find(predicate);
    }

    default ${PrimitiveType} getFirst() {
        return this.iterator().next${Type}();
    }

    @DelegateBy("getFirst()")
    default @Nullable ${WrapperType} getFirstOrNull() {
        return isNotEmpty() ? getFirst() : null;
    }

    @DelegateBy("getFirst()")
    default @NotNull ${Type}Option getFirstOption() {
        return isNotEmpty() ? ${Type}Option.some(getFirst()) : ${Type}Option.none();
    }

    default ${PrimitiveType} getLast() {
        return reverseIterator().next${Type}();
    }

    @DelegateBy("getLast()")
    default @Nullable ${WrapperType} getLastOrNull() {
        return isNotEmpty() ? getLast() : null;
    }

    @DelegateBy("getLast()")
    default @NotNull ${Type}Option getLastOption() {
        return isNotEmpty() ? ${Type}Option.some(getLast()) : ${Type}Option.none();
    }

    @DelegateBy("findFirst(${Type}Predicate)")
    default ${PrimitiveType} first(@NotNull ${Type}Predicate predicate) {
        return findFirst(predicate).get();
    }

    @DelegateBy("findFirst(${Type}Predicate)")
    default @Nullable ${WrapperType} firstOrNull(@NotNull ${Type}Predicate predicate) {
        return findFirst(predicate).getOrNull();
    }

    @DelegateBy("findFirst(${Type}Predicate)")
    default @NotNull ${Type}Option firstOption(@NotNull ${Type}Predicate predicate) {
        return findFirst(predicate);
    }

    @DelegateBy("findLast(${Type}Predicate)")
    default ${PrimitiveType} last(@NotNull ${Type}Predicate predicate) {
        return findLast(predicate).get();
    }

    @DelegateBy("findLast(${Type}Predicate)")
    default @Nullable ${WrapperType} lastOrNull(@NotNull ${Type}Predicate predicate) {
        return findLast(predicate).getOrNull();
    }

    @DelegateBy("findLast(${Type}Predicate)")
    default @NotNull ${Type}Option lastOption(@NotNull ${Type}Predicate predicate) {
        return findLast(predicate);
    }

    //endregion

    //region Search Operations

    @Contract(pure = true)
    default int indexOf(${PrimitiveType} value) {
        int idx = 0;

        ${Type}Iterator it = this.iterator();
        while (it.hasNext()) {
            if (${PrimitiveEquals("value", "it.next${Type}()")}) {
                return idx;
            }
            ++idx;
        }

        return -1;
    }

    @Contract(pure = true)
    default int indexOf(${PrimitiveType} value, int from) {
        int idx = 0;

        ${Type}Iterator it = this.iterator();
        while (it.hasNext()) {
            if (idx >= from && ${PrimitiveEquals("value", "it.next${Type}()")}) {
                return idx;
            }
            ++idx;
        }

        return -1;
    }

    @Contract(pure = true)
    default int indexWhere(@NotNull ${Type}Predicate predicate) {
        int idx = 0;

        ${Type}Iterator it = this.iterator();
        while (it.hasNext()) {
            if (predicate.test(it.next${Type}())) { // implicit null check of predicate
                return idx;
            }
            ++idx;
        }

        return -1;
    }

    @Contract(pure = true)
    default int indexWhere(@NotNull ${Type}Predicate predicate, int from) {
        int idx = 0;

        ${Type}Iterator it = this.iterator();
        while (it.hasNext()) {
            if (idx >= from && predicate.test(it.next${Type}())) { // implicit null check of predicate
                return idx;
            }
            ++idx;
        }

        return -1;
    }

    @Contract(pure = true)
    default int lastIndexOf(${PrimitiveType} value) {
        int idx = size() - 1;
        ${Type}Iterator it = reverseIterator();
        while (it.hasNext()) {
            if (${PrimitiveEquals("value", "it.next${Type}()")}) {
                return idx;
            }
            --idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexOf(${PrimitiveType} value, int end) {
        int idx = size() - 1;
        ${Type}Iterator it = reverseIterator();
        while (it.hasNext()) {
            if (idx <= end && ${PrimitiveEquals("value", "it.next${Type}()")}) {
                return idx;
            }
            --idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexWhere(@NotNull ${Type}Predicate predicate) {
        int idx = size() - 1;
        ${Type}Iterator it = reverseIterator();
        while (it.hasNext()) {
            if (predicate.test(it.next${Type}())) { // implicit null check of predicate
                return idx;
            }
            --idx;
        }
        return -1;
    }

    @Contract(pure = true)
    default int lastIndexWhere(@NotNull ${Type}Predicate predicate, int end) {
        int idx = size() - 1;
        ${Type}Iterator it = reverseIterator();
        while (it.hasNext()) {
            if (idx <= end && predicate.test(it.next${Type}())) { // implicit null check of predicate
                return idx;
            }
            --idx;
        }
        return -1;
    }

    //endregion

    @Contract(pure = true)
    @NotNull ${Type}SeqLike slice(@Index int beginIndex, @Index int endIndex);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike drop(int n);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike dropLast(int n);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike dropWhile(@NotNull ${Type}Predicate predicate);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike take(int n);

    @NotNull ${Type}SeqLike takeLast(int n);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike takeWhile(@NotNull ${Type}Predicate predicate);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike updated(@Index int index, ${PrimitiveType} newValue);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike concat(@NotNull ${Type}SeqLike other);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike prepended(${PrimitiveType} value);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike prependedAll(${PrimitiveType} @NotNull [] values);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike prependedAll(@NotNull ${Type}Traversable values);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike appended(${PrimitiveType} value);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike appendedAll(@NotNull ${Type}Traversable values);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike appendedAll(${PrimitiveType} @NotNull [] values);

    @Contract(pure = true)
    @NotNull ${Type}SeqLike sorted();

    @Contract(pure = true)
    @NotNull ${Type}SeqLike reversed();

    @Override
    @Contract(pure = true)
    @NotNull ${Type}SeqLike filter(@NotNull ${Type}Predicate predicate);

    @Override
    @Contract(pure = true)
    @NotNull ${Type}SeqLike filterNot(@NotNull ${Type}Predicate predicate);

    @Override
    @Contract(pure = true)
    @NotNull ${Type}SeqLike map(@NotNull ${Type}UnaryOperator mapper);

    @Override
    @Contract(pure = true)
    <U> @NotNull SeqLike<U> mapToObj(@NotNull ${Type}Function<? extends U> mapper);

    @Override
    @Contract(pure = true)
    @NotNull ${Type}SeqLike flatMap(@NotNull ${Type}Function<? extends ${Type}Traversable> mapper);

    @Override
    @NotNull <T> SeqLike<T> flatMapToObj(@NotNull ${Type}Function<? extends Iterable<? extends T>> mapper);
}
