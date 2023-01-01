package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;

public final class MutableSeqTest implements MutableSeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ? extends MutableSeq<? extends E>> factory() {
        return MutableSeq.factory();
    }

    @Override
    public <E> MutableSeq<E> of(E... elements) {
        return MutableSeq.from(elements);
    }

    @Override
    public <E> MutableSeq<E> from(E[] elements) {
        return MutableSeq.from(elements);
    }

    @Override
    public <E> MutableSeq<E> from(Iterable<? extends E> elements) {
        return MutableSeq.from(elements);
    }

    @Override
    public void classNameTest() {
    }
}
