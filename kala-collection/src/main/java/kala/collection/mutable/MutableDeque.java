package kala.collection.mutable;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MutableDeque<E> extends MutableQueue<E> {

    static <E> @NotNull MutableDeque<E> create() {
        return new MutableCircularArrayList<>();
    }

    boolean isEmpty();

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    void prepend(E value);

    void append(E value);

    default E removeFirst() {
        return removeFirstOption().get();
    }

    default @Nullable E removeFirstOrNull() {
        return removeFirstOption().getOrNull();
    }

    @NotNull Option<E> removeFirstOption();

    default E removeLast() {
        return removeLastOption().get();
    }

    default @Nullable E removeLastOrNull() {
        return removeLastOption().getOrNull();
    }

    @NotNull Option<E> removeLastOption();

    default E first() {
        return firstOption().get();
    }

    default @Nullable E firstOrNull() {
        return firstOption().getOrNull();
    }

    @NotNull Option<E> firstOption();

    default E last() {
        return lastOption().get();
    }

    default @Nullable E lastOrNull() {
        return lastOption().getOrNull();
    }

    @NotNull Option<E> lastOption();

    @Override
    default void enqueue(E value) {
        prepend(value);
    }

    @Override
    default E dequeue() {
        return dequeueOption().get();
    }

    @Override
    default @Nullable E dequeueOrNull() {
        return dequeueOption().getOrNull();
    }

    @Override
    default @NotNull Option<E> dequeueOption() {
        return removeLastOption();
    }
}
