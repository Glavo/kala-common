package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.factory.CollectionFactory;

public final class MutableArrayTest implements MutableSeqTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, MutableArray<E>> factory() {
        return MutableArray.factory();
    }

}
