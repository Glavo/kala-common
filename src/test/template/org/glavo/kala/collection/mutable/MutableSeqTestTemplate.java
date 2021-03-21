package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.SeqTestTemplate;
import org.glavo.kala.collection.factory.CollectionFactory;

public interface MutableSeqTestTemplate extends MutableCollectionTestTemplate, SeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableSeq<? extends E>> factory();

    @Override
    <E> MutableSeq<E> of(E... elements);

    @Override
    <E> MutableSeq<E> from(E[] elements);

    @Override
    <E> MutableSeq<E> from(Iterable<? extends E> elements);
}
