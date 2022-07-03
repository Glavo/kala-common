package kala.collection;

import kala.collection.base.AnyTraversable;
import org.jetbrains.annotations.NotNull;

public interface AnyCollectionLike<E> extends AnyTraversable<E> {
    @NotNull String className();

    @NotNull AnyCollectionView<E> view();
}
