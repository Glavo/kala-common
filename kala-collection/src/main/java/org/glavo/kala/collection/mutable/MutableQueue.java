package org.glavo.kala.collection.mutable;

public interface MutableQueue<E> {
    void enqueue(E value);

    E dequeue();

    boolean isEmpty();
}
