package kala.collection.mutable.primitive;

import kala.collection.base.primitive.PrimitiveGrowable;
import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import kala.collection.mutable.MutableAnyList;
import kala.collection.mutable.MutableAnySeq;
import kala.collection.primitive.PrimitiveSeq;
import org.jetbrains.annotations.NotNull;

public interface MutablePrimitiveList<E> extends MutablePrimitiveSeq<E>, MutableAnyList<E>, PrimitiveGrowable<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends MutablePrimitiveList<E>> iterableFactory();
}
