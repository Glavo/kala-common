package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.factory.CollectionFactory;

public class SmartArrayBufferTest implements BufferTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, SmartArrayBuffer<E>> factory() {
        return SmartArrayBuffer.factory();
    }
}
