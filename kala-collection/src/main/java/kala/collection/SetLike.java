package kala.collection;

import org.jetbrains.annotations.NotNull;

public interface SetLike<E> extends CollectionLike<E> {
    @Override
    default @NotNull String className() {
        return "SetLike";
    }

    @Override
    @NotNull SetView<E> view();
}
