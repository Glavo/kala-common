package org.glavo.kala.collection;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.Conditions;
import org.glavo.kala.collection.internal.view.IndexedSeqViews;
import org.glavo.kala.collection.internal.view.SeqViews;
import org.glavo.kala.tuple.primitive.IntObjTuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Function;

public interface IndexedSeqView<@Covariant E> extends SeqView<E>, IndexedSeqLike<E>, RandomAccess {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> IndexedSeqView<E> narrow(IndexedSeqView<? extends E> view) {
        return (IndexedSeqView<E>) view;
    }

    @SuppressWarnings("unchecked")
    static <E> @NotNull IndexedSeqView<E> empty() {
        return ((IndexedSeqView<E>) IndexedSeqViews.Empty.INSTANCE);
    }

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "IndexedSeqView";
    }

    @Override
    default @NotNull IndexedSeqView<E> view() {
        return this;
    }

    //endregion

    @Override
    default @NotNull SeqView<E> concat(@NotNull SeqLike<? extends E> other) {
        Objects.requireNonNull(other);
        return other instanceof RandomAccess
                ? new IndexedSeqViews.Concat<>(this, other)
                : new SeqViews.Concat<>(this, other);
    }

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
    default @NotNull IndexedSeqView<E> sliceView(int beginIndex, int endIndex) {
        return slice(beginIndex, endIndex);
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
        Conditions.checkElementIndex(index, this.size());
        return new IndexedSeqViews.Updated<>(this, index, newValue);
    }

    @Override
    default <U> @NotNull IndexedSeqView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new IndexedSeqViews.Mapped<>(this, mapper);
    }

    @Override
    default @NotNull IndexedSeqView<IntObjTuple2<E>> withIndex() {
        return new IndexedSeqViews.WithIndex<>(this);
    }
}
