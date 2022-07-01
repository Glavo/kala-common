package kala.collection.factory.primitive;

import kala.annotations.Covariant;
import kala.collection.factory.CollectionFactory;

public interface PrimitiveCollectionFactory<E, Builder, @Covariant R> extends CollectionFactory<E, Builder, R> {
}
