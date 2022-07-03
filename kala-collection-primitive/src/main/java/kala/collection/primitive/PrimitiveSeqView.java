package kala.collection.primitive;

import kala.collection.AnySeqView;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveSeqView<E> extends PrimitiveSeqLike<E>, PrimitiveCollectionView<E>, AnySeqView<E> {
    @Override
    default @NotNull PrimitiveSeqView<E> view() {
        return this;
    }
}
