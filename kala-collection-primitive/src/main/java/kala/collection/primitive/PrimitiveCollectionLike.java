package kala.collection.primitive;

import kala.collection.AnyCollectionLike;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveCollectionLike<E> extends AnyCollectionLike<E> {
    @Override
    @NotNull PrimitiveCollectionView<E> view();
}
