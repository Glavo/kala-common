package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.factory.CollectionFactory;

public class SmartArrayBufferTest implements BufferTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, SmartArrayBuffer<E>> factory() {
        return SmartArrayBuffer.factory();
    }

    @Override
    public <E> SmartArrayBuffer<E> of(E... elements) {
        return SmartArrayBuffer.from(elements);
    }

    @Override
    public <E> SmartArrayBuffer<E> from(E[] elements) {
        return SmartArrayBuffer.from(elements);
    }

    @Override
    public <E> SmartArrayBuffer<E> from(Iterable<? extends E> elements) {
        return SmartArrayBuffer.from(elements);
    }
}
