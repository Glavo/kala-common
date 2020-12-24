package asia.kala.collection.immutable;

import asia.kala.collection.SeqTestTemplate;
import asia.kala.factory.CollectionFactory;

public interface ImmutableSeqTestTemplate extends ImmutableCollectionTestTemplate, SeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableSeq<? extends E>> factory();
}
