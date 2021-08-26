package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public class DynamicSmartArraySeqTest implements DynamicSeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, DynamicSmartArraySeq<E>> factory() {
        return DynamicSmartArraySeq.factory();
    }

    @Override
    public <E> DynamicSmartArraySeq<E> of(E... elements) {
        return DynamicSmartArraySeq.from(elements);
    }

    @Override
    public <E> DynamicSmartArraySeq<E> from(E[] elements) {
        return DynamicSmartArraySeq.from(elements);
    }

    @Override
    public <E> DynamicSmartArraySeq<E> from(Iterable<? extends E> elements) {
        return DynamicSmartArraySeq.from(elements);
    }
}
