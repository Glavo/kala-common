package org.glavo.kala.collection;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.control.Conditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Function;

public interface IndexedSeqView<@Covariant E> extends SeqView<E>, RandomAccess {

    //region Collection Operations

    @Override
    default String className() {
        return "IndexedSeqView";
    }

    //endregion

    //region Addition Operations

    @Override
    default @NotNull IndexedSeqView<E> prepended(E value) {
        return new IndexedSeqViews.Prepended<>(this, value);
    }

    @Override
    default @NotNull IndexedSeqView<E> appended(E value) {
        return new IndexedSeqViews.Appended<>(this, value);
    }

    //endregion


    @Override
    default @NotNull IndexedSeqView<E> slice(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size());
        return new IndexedSeqViews.Slice<>(this, beginIndex, endIndex);
    }

    @Override
    default @NotNull IndexedSeqView<E> drop(int n) {
        return new IndexedSeqViews.Drop<>(this, n);
    }

    @Override
    default @NotNull IndexedSeqView<E> take(int n) {
        return new IndexedSeqViews.Take<>(this, n);
    }

    @Override
    default @NotNull IndexedSeqView<E> updated(int index, E newValue) {
        return new IndexedSeqViews.Updated<>(this, index, newValue);
    }

    @Override
    default <U> @NotNull IndexedSeqView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new IndexedSeqViews.Mapped<>(this, mapper);
    }
}
