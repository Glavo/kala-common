package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.factory.CollectionFactory;

public final class ArrayBufferTest implements BufferTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, ArrayBuffer<E>> factory() {
        return ArrayBuffer.factory();
    }

    @Override
    public <E> ArrayBuffer<E> of(E... elements) {
        return ArrayBuffer.from(elements);
    }

    @Override
    public <E> ArrayBuffer<E> from(E[] elements) {
        return ArrayBuffer.from(elements);
    }

    @Override
    public <E> ArrayBuffer<E> from(Iterable<? extends E> elements) {
        return ArrayBuffer.from(elements);
    }

}
