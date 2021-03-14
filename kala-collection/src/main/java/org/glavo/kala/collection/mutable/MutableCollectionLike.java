package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.CollectionLike;
import org.jetbrains.annotations.NotNull;

public interface MutableCollectionLike<E> extends CollectionLike<E> {
    @Override
    default @NotNull String className() {
        return "MutableCollectionLike";
    }
}
