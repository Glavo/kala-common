package kala.collection.mutable.primitive;

import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import kala.collection.mutable.MutableAnyCollection;
import kala.collection.primitive.PrimitiveCollection;
import org.jetbrains.annotations.NotNull;

public interface MutablePrimitiveCollection<E> extends PrimitiveCollection<E>, MutableAnyCollection<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends MutablePrimitiveCollection<E>> iterableFactory();
}
