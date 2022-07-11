package kala.collection;

import kala.annotations.Covariant;
import kala.collection.internal.view.SetViews;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public interface SetView<@Covariant E> extends CollectionView<E>, SetLike<E>, AnySetView<E> {

    @Override
    default @NotNull String className() {
        return "SetView";
    }

    @Override
    default @NotNull SetView<E> view() {
        return this;
    }

    @Override
    default @NotNull SetView<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SetViews.Filter<>(this, predicate);
    }

    @Override
    default @NotNull SetView<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SetViews.Filter<>(this, predicate.negate());
    }
}
