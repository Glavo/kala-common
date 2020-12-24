package asia.kala.collection.immutable;

import asia.kala.collection.CollectionTestTemplate;
import asia.kala.factory.CollectionFactory;

public interface ImmutableCollectionTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableCollection<? extends E>> factory();
}
