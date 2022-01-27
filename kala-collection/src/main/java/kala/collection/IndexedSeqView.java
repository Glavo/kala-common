package kala.collection;

import kala.Conditions;
import kala.annotations.Covariant;
import kala.collection.internal.view.IndexedSeqViews;
import kala.collection.internal.view.SeqViews;
import kala.tuple.primitive.IntObjTuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Function;

public interface IndexedSeqView<@Covariant E> extends IndexedSeqLike<E>, SeqView<E> {
    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "IndexedSeqView";
    }

    @Override
    default @NotNull SeqView<E> view() {
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
    default @NotNull SeqView<E> prepended(E value) {
        return new IndexedSeqViews.Prepended<>(this, value);
    }

    @Override
    default @NotNull SeqView<E> appended(E value) {
        return new IndexedSeqViews.Appended<>(this, value);
    }

    //endregion

    @Override
    default @NotNull SeqView<E> slice(int beginIndex, int endIndex) {
        final int size = this.size();
        Conditions.checkPositionIndices(beginIndex, endIndex, size);

        switch (endIndex - beginIndex) {
            case 0:
                return SeqView.empty();
            case 1:
                return SeqView.of(get(beginIndex));
        }

        if (beginIndex == 0) {
            return new IndexedSeqViews.Take<>(this, endIndex);
        }
        if (endIndex == size) {
            return new IndexedSeqViews.Drop<>(this, beginIndex);
        }
        return new IndexedSeqViews.Slice<>(this, beginIndex, endIndex);
    }

    @Override
    default @NotNull SeqView<E> sliceView(int beginIndex, int endIndex) {
        return slice(beginIndex, endIndex);
    }

    @Override
    default @NotNull SeqView<E> drop(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this;
        }
        final int size = this.size();
        if (size == 0 || n >= size) {
            return SeqView.empty();
        }
        return new IndexedSeqViews.Drop<>(this, n);
    }

    @Override
    default @NotNull SeqView<E> dropLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this;
        }
        final int size = this.size();
        if (size == 0 || n >= size) {
            return SeqView.empty();
        }
        return new IndexedSeqViews.Take<>(this, size - n);
    }

    @Override
    default @NotNull SeqView<E> take(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return SeqView.empty();
        }
        final int size = this.size();
        if (size == 0) {
            return SeqView.empty();
        }
        if (n >= size) {
            return this;
        }
        return new IndexedSeqViews.Take<>(this, n);
    }

    @Override
    default @NotNull SeqView<E> takeLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return SeqView.empty();
        }
        final int size = this.size();
        if (size == 0) {
            return SeqView.empty();
        }
        if (n >= size) {
            return this;
        }
        return new IndexedSeqViews.Drop<>(this, size - n);
    }

    @Override
    default @NotNull SeqView<E> updated(int index, E newValue) {
        Conditions.checkElementIndex(index, this.size());
        return new IndexedSeqViews.Updated<>(this, index, newValue);
    }

    @Override
    default <U> @NotNull SeqView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new IndexedSeqViews.Mapped<>(this, mapper);
    }

    @Override
    default @NotNull SeqView<IntObjTuple2<E>> withIndex() {
        return new IndexedSeqViews.WithIndex<>(this);
    }
}
