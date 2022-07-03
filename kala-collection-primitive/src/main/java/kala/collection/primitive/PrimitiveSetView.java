package kala.collection.primitive;

import kala.collection.AnySetView;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveSetView<E> extends PrimitiveSetLike<E>, PrimitiveCollectionView<E>, AnySetView<E> {
    @Override
    default @NotNull PrimitiveSetView<E> view() {
        return this;
    }
}
