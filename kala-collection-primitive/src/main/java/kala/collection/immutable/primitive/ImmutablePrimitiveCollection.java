package kala.collection.immutable.primitive;

import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import kala.collection.immutable.ImmutableAnyCollection;
import kala.collection.primitive.PrimitiveCollection;
import org.jetbrains.annotations.NotNull;

public interface ImmutablePrimitiveCollection<E> extends PrimitiveCollection<E>, ImmutableAnyCollection<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends ImmutablePrimitiveCollection<E>> iterableFactory();
}
