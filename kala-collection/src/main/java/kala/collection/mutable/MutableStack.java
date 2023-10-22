package kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface MutableStack<E> extends MutableCollection<E> {

    @Contract("-> new")
    static <E> @NotNull MutableStack<E> create() {
        return MutableArrayList.create();
    }

    // boolean isEmpty();

    @Contract(mutates = "this")
    void push(E value);

    @Contract(mutates = "this")
    E pop();

    E peek();
}
