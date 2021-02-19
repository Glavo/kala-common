package org.glavo.kala.collection;

import org.glavo.kala.collection.factory.CollectionFactory;

public class ArraySeqTest implements SeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ? extends ArraySeq<E>> factory() {
        return ArraySeq.factory();
    }
}
