package kala.collection.immutable.primitive;

import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import kala.collection.immutable.ImmutableAnySeq;
import kala.collection.primitive.PrimitiveSeq;
import org.jetbrains.annotations.NotNull;

public interface ImmutablePrimitiveSeq<E> extends ImmutablePrimitiveCollection<E>, PrimitiveSeq<E>, ImmutableAnySeq<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends ImmutablePrimitiveSeq<E>> iterableFactory();
}
