package kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class MutableArrayStack<E> implements MutableStack<E>, Serializable {
    private static final long serialVersionUID = -146156819892776856L;

    private final DynamicArray<E> array;

    private MutableArrayStack(int initialCapacity) {
        this.array = new DynamicArray<>(initialCapacity);
    }

    //region Static Factories

    public static <E> @NotNull MutableArrayStack<E> create() {
        return new MutableArrayStack<>(DynamicArray.DEFAULT_CAPACITY);
    }

    public static <E> @NotNull MutableArrayStack<E> create(int initialCapacity) {
        return new MutableArrayStack<>(initialCapacity);
    }

    //endregion

    @Override
    public void push(E value) {
        array.append(value);
    }

    @Override
    public E pop() {
        return array.removeLast();
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
