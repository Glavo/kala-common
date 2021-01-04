package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.SeqTestTemplate;
import org.glavo.kala.factory.CollectionFactory;

public interface MutableSeqTestTemplate extends MutableCollectionTestTemplate, SeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableSeq<? extends E>> factory();
}
