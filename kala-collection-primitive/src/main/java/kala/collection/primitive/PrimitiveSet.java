package kala.collection.primitive;

import kala.collection.AnySet;
import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveSet<E> extends PrimitiveCollection<E>, PrimitiveSetLike<E>, AnySet<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends PrimitiveSet<E>> iterableFactory();
}
