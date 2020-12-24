package asia.kala.collection.mutable;

import asia.kala.collection.SeqTestTemplate;
import asia.kala.collection.immutable.ImmutableSeq;
import asia.kala.factory.CollectionFactory;

public interface MutableSeqTestTemplate extends MutableCollectionTestTemplate, SeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableSeq<? extends E>> factory();
}
