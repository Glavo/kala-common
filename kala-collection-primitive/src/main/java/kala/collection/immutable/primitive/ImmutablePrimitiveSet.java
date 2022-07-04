package kala.collection.immutable.primitive;

import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import kala.collection.immutable.ImmutableAnySet;
import kala.collection.primitive.PrimitiveSet;
import org.jetbrains.annotations.NotNull;

public interface ImmutablePrimitiveSet<E> extends ImmutablePrimitiveCollection<E>, PrimitiveSet<E>, ImmutableAnySet<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends ImmutablePrimitiveSet<E>> iterableFactory();
}
