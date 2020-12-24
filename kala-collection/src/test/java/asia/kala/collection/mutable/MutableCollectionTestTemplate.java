package asia.kala.collection.mutable;

import asia.kala.collection.CollectionTestTemplate;
import asia.kala.factory.CollectionFactory;

public interface MutableCollectionTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableCollection<? extends E>> factory();
}
