package kala.collection.primitive;

import kala.collection.AnySetLike;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveSetLike<E> extends PrimitiveCollectionLike<E>, AnySetLike<E> {
    @Override
    @NotNull PrimitiveSetView<E> view();
}
