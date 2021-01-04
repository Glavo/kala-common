package org.glavo.kala.collection;

import org.glavo.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public interface IndexedSeqView<@Covariant E> extends SeqView<E>, IndexedSeq<E> {

    //region Collection Operations

    @Override
    default String className() {
        return "IndexedSeqView";
    }

    @Override
    @Contract(value = "-> this", pure = true)
    default @NotNull IndexedSeqView<E> view() {
        return this;
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
