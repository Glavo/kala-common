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

import kala.collection.base.primitive.Abstract${Type}Iterator;
import kala.collection.base.primitive.${Type}Iterator;
import kala.control.primitive.${Type}Option;
import kala.function.*;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.function.*;

public interface Indexed${Type}SeqLike extends ${Type}SeqLike, RandomAccess {
    @Override
    default @NotNull ${Type}Iterator iterator() {
        return iterator(0);
    }

    @Override
    default @NotNull ${Type}Iterator iterator(@Index int beginIndex) {
        final int size = size();
        beginIndex = Indexes.checkPositionIndex(beginIndex, size);
        if (size == 0) return ${Type}Iterator.empty();

        final class Itr extends Abstract${Type}Iterator {
            private int idx;

            Itr(int beginIndex) {
                this.idx = beginIndex;
            }

            @Override
            public boolean hasNext() {
                return idx < size;
            }

            @Override
            public ${PrimitiveType} next${Type}() {
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
    ${PrimitiveType} get(@Index int index);

    //endregion

    //region Reversal Operations

    @Override
    default @NotNull ${Type}Iterator reverseIterator() {
        if (isEmpty()) return ${Type}Iterator.empty();

        return new Abstract${Type}Iterator() {
            private int idx = Indexed${Type}SeqLike.this.size() - 1;

            @Override
            public boolean hasNext() {
                return idx >= 0;
            }

            @Override
            public ${PrimitiveType} next${Type}() {
                if (idx < 0) throw new NoSuchElementException();

                return get(idx--);
            }
        };
    }

    //endregion

    default int binarySearch(${PrimitiveType} value) {
        return binarySearch(0, ~0, value);
    }

    default int binarySearch(@Index int beginIndex, @Index int endIndex, ${PrimitiveType} value) {
        final int size = size();

        int low = Indexes.checkBeginIndex(beginIndex, size);
        int high = Indexes.checkEndIndex(low, endIndex, size) - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final ${PrimitiveType} midVal = get(mid);
            final int cmp = ${WrapperType}.compare(midVal, value);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    //region Element Retrieval Operations

    @Override
    default @NotNull ${Type}Option findFirst(@NotNull ${Type}Predicate predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            ${PrimitiveType} e = get(i);
            if (predicate.test(e)) {
                return ${Type}Option.some(e);
            }
        }
        return ${Type}Option.none();
    }

    @Override
    default @NotNull ${Type}Option findLast(@NotNull ${Type}Predicate predicate) {
        final int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            ${PrimitiveType} e = get(i);
            if (predicate.test(e)) {
                return ${Type}Option.some(e);
            }
        }
        return ${Type}Option.none();
    }

    @Override
    default ${PrimitiveType} getFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        return get(0);
    }

    @Override
    default ${PrimitiveType} getLast() {
        final int size = size();
        if (size == 0) throw new NoSuchElementException();

        return get(size - 1);
    }

    //endregion

    @Override
    default void forEach(@NotNull ${Type}Consumer action) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            action.accept(get(i));
        }
    }
}
