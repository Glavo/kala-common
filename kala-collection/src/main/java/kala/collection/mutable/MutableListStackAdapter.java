package kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

final class MutableListStackAdapter<E> implements MutableStack<E>, Serializable {
    private static final long serialVersionUID = -146156819892776856L;

    private final @NotNull MutableList<E> seq;

    MutableListStackAdapter(@NotNull MutableList<E> seq) {
        this.seq = seq;
    }

    @Override
    public void push(E value) {
        seq.append(value);
    }

    @Override
    public E pop() {
        return seq.removeLast();
    }

    @Override
    public E peek() {
        return seq.last();
    }

    @Override
    public boolean isEmpty() {
        return seq.isEmpty();
    }

    @Override
    public String toString() {
        return "MutableListStackAdapter[" + seq + ']';
    }
}
