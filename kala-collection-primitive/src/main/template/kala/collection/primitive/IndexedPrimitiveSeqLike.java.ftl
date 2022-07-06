package kala.collection.primitive;

import kala.Conditions;
import kala.collection.base.primitive.Abstract${Type}Iterator;
import kala.collection.base.primitive.${Type}Iterator;
import kala.control.primitive.${Type}Option;
import kala.function.*;
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
    default @NotNull ${Type}Iterator iterator(int beginIndex) {
        final int size = size();

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
    default boolean isDefinedAt(int index) {
        return index >= 0 && index < size();
    }

    @Override
    ${PrimitiveType} get(int index);

    //endregion

    //region Reversal Operations

    @Override
    default @NotNull ${Type}Iterator reverseIterator() {
        if (isEmpty()) return ${Type}Iterator.empty();

        return new Abstract${Type}Iterator() {
            private int idx = size() - 1;

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
        return binarySearch(value, 0, size());
    }

    default int binarySearch(${PrimitiveType} value, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size());
        int low = beginIndex;
        int high = endIndex - 1;

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
    default ${PrimitiveType} first() {
        if (isEmpty()) throw new NoSuchElementException();

        return get(0);
    }

    @Override
    default ${PrimitiveType} last() {
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
