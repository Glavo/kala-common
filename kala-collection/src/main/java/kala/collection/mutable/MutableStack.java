package kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface MutableStack<E> {

    @Contract("-> new")
    static <E> @NotNull MutableStack<E> create() {
        return new DynamicLinkedSeq<>();
    }

    @Contract(mutates = "this")
    void push(E value);

    @Contract(mutates = "this")
    E pop();

    E peek();

    boolean isEmpty();
}
