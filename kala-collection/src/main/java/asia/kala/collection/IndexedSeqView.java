package asia.kala.collection;

import asia.kala.annotations.Covariant;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public interface IndexedSeqView<@Covariant E> extends SeqView<E>, IndexedSeq<E> {

    //region SeqView members

    @NotNull
    @Override
    default IndexedSeqView<E> updated(int index, E newValue) {
        return new IndexedSeqViews.Updated<>(this, index, newValue);
    }

    @NotNull
    @Override
    default IndexedSeqView<E> drop(int n) {
        return new IndexedSeqViews.Drop<>(this, n);
    }

    @NotNull
    @Override
    default IndexedSeqView<E> take(int n) {
        return new IndexedSeqViews.Take<>(this, n);
    }

    @NotNull
    @Override
    default IndexedSeqView<E> prepended(E value) {
        return new IndexedSeqViews.Prepended<>(this, value);
    }

    @NotNull
    @Override
    default IndexedSeqView<E> appended(E value) {
        return new IndexedSeqViews.Appended<>(this, value);
    }

    //endregion

    //region View members

    @NotNull
    @Override
    default IndexedSeqView<E> view() {
        return this;
    }

    @Override
    default String className() {
        return "IndexedSeqView";
    }

    @NotNull
    @Override
    default <U> IndexedSeqView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new IndexedSeqViews.Mapped<>(this, mapper);
    }

    //endregion
}
