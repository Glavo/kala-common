package asia.kala.collection.mutable;

import asia.kala.factory.CollectionFactory;

public interface BufferTestTemplate extends MutableSeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends Buffer<? extends E>> factory();
}
