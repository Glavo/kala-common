package org.glavo.kala.collection;

import org.jetbrains.annotations.NotNull;

public interface SetLike<E> extends CollectionLike<E> {
    @Override
    @NotNull SetView<E> view();
}
