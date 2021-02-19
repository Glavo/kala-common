package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.factory.CollectionFactory;

public final class ArrayBufferTest implements BufferTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, ArrayBuffer<E>> factory() {
        return ArrayBuffer.factory();
    }

}
