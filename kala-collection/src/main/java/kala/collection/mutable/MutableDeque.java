package kala.collection.mutable;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MutableDeque<E> extends MutableQueue<E> {

    boolean isEmpty();

    void prepend(E value);

    void append(E value);

    E removeFirst();

    default @Nullable E removeFirstOrNull() {
        return isEmpty() ? null : removeFirst();
    }

    default @NotNull Option<E> removeFirstOption() {
        return isEmpty() ? Option.none() : Option.some(removeFirst());
    }

    E removeLast();

    default @Nullable E removeLastOrNull() {
        return isEmpty() ? null : removeLast();
    }

    default @NotNull Option<E> removeLastOption() {
        return isEmpty() ? Option.none() : Option.some(removeLast());
    }

    E first();

    default @Nullable E firstOrNull() {
        return isEmpty() ? null : first();
    }

    default @NotNull Option<E> firstOption() {
        return isEmpty() ? Option.none() : Option.some(first());
    }

    E last();

    default @Nullable E lastOrNull() {
        return isEmpty() ? null : last();
    }

    default @NotNull Option<E> lastOption() {
        return isEmpty() ? Option.none() : Option.some(last());
    }

    @Override
    default void enqueue(E value) {
        prepend(value);
    }

    @Override
    default E dequeue() {
        return removeLast();
    }

    @Override
    default @Nullable E dequeueOrNull() {
        return removeLastOrNull();
    }

    @Override
    default @NotNull Option<E> dequeueOption() {
        return removeLastOption();
    }
}
