package kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface MutableStack<E> {

    @Contract("-> new")
    static <E> @NotNull MutableStack<E> create() {
        return new MutableSinglyLinkedList<>();
    }

    @SuppressWarnings("unchecked")
    static <E> @NotNull MutableStack<E> wrapMutableList(@NotNull MutableList<E> seq) {
        Objects.requireNonNull(seq);
        if (seq instanceof MutableStack) {
            return ((MutableStack<E>) seq);
        }
        return new MutableListStackAdapter<>(seq);
    }

    @Contract(mutates = "this")
    void push(E value);

    @Contract(mutates = "this")
    E pop();

    E peek();

    boolean isEmpty();
}
