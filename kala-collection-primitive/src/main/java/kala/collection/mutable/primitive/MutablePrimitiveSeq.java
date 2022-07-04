package kala.collection.mutable.primitive;

import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import kala.collection.mutable.MutableAnySeq;
import kala.collection.primitive.PrimitiveSeq;
import org.jetbrains.annotations.NotNull;

public interface MutablePrimitiveSeq<E> extends MutablePrimitiveCollection<E>, PrimitiveSeq<E>, MutableAnySeq<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends MutablePrimitiveSeq<E>> iterableFactory();
}
