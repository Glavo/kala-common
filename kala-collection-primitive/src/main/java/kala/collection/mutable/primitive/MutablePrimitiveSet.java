package kala.collection.mutable.primitive;

import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import kala.collection.mutable.MutableAnySet;
import kala.collection.primitive.PrimitiveSet;
import org.jetbrains.annotations.NotNull;

public interface MutablePrimitiveSet<E> extends MutablePrimitiveCollection<E>, PrimitiveSet<E>, MutableAnySet<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends MutablePrimitiveSet<E>> iterableFactory();
}
