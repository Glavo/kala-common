package kala.collection.primitive;

import kala.collection.AnySeqLike;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveSeqLike<E> extends PrimitiveCollectionLike<E>, AnySeqLike<E> {
    @Override
    @NotNull PrimitiveSeqView<E> view();
}
