package kala.collection.primitive;

import kala.collection.AnyCollection;
import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveCollection<E> extends PrimitiveCollectionLike<E>, AnyCollection<E> {
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends PrimitiveCollection<E>> iterableFactory();
}
