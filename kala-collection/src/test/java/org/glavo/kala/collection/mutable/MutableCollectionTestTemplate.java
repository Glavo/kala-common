package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.CollectionTestTemplate;
import org.glavo.kala.factory.CollectionFactory;

public interface MutableCollectionTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableCollection<? extends E>> factory();
}
