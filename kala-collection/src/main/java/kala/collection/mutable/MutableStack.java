package kala.collection.mutable;

import org.jetbrains.annotations.Contract;

public interface MutableStack<E> {

    @Contract("-> new")
    static <E> MutableStack<E> create() {
        return new DynamicLinkedSeq<>();
    }

    @Contract(mutates = "this")
    void push(E value);

    @Contract(mutates = "this")
    E pop();

    E peek();

    boolean isEmpty();
}
