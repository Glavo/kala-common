package kala.collection.primitive;

import kala.collection.AnyCollectionView;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveCollectionView<E> extends PrimitiveCollectionLike<E>, AnyCollectionView<E> {
    @Override
    default @NotNull PrimitiveCollectionView<E> view() {
        return this;
    }
}
