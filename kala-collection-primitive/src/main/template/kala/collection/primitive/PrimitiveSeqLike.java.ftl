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
    default boolean isDefinedAt(int index) {
        return index >= 0 && sizeGreaterThan(index);
    }

    @Override
    default ${PrimitiveType} elementAt(int index) {
        return get(index);
    }

    @Contract(pure = true)
    @Flow(sourceIsContainer = true)
    default ${PrimitiveType} get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
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

    default ${PrimitiveType} first() {
        return this.iterator().next${Type}();
    }

    @DelegateBy("first()")
    default @Nullable ${WrapperType} firstOrNull() {
        return isNotEmpty() ? first() : null;
    }

    @DelegateBy("first()")
    default @NotNull ${Type}Option firstOption() {
        return isNotEmpty() ? ${Type}Option.some(first()) : ${Type}Option.none();
    }

    default ${PrimitiveType} last() {
        return reverseIterator().next${Type}();
    }

    @DelegateBy("last()")
    default @Nullable ${WrapperType} lastOrNull() {
        return isNotEmpty() ? last() : null;
    }

    @DelegateBy("last()")
    default @NotNull ${Type}Option lastOption() {
        return isNotEmpty() ? ${Type}Option.some(last()) : ${Type}Option.none();
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
    @NotNull ${Type}SeqLike slice(int beginIndex, int endIndex);

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
    @NotNull ${Type}SeqLike updated(int index, ${PrimitiveType} newValue);

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
}
