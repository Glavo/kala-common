package kala.collection;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public interface SetLike<E> extends CollectionLike<E>, AnySetLike<E> {
    @Override
    default @NotNull String className() {
        return "SetLike";
    }

    @Override
    @NotNull SetView<E> view();

    @Override
    @NotNull SetLike<E> filter(@NotNull Predicate<? super E> predicate);

    @Override
    @NotNull SetLike<E> filterNot(@NotNull Predicate<? super E> predicate);
}
