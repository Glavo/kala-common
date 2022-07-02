package kala.collection.mutable;

import kala.annotations.DelegateBy;
import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MutableDeque<E> extends MutableQueue<E> {

    static <E> @NotNull MutableDeque<E> create() {
        return new MutableArrayDeque<>();
    }

    boolean isEmpty();

    void prepend(E value);

    void append(E value);

    E removeFirst();

    @DelegateBy("removeFirst()")
    default @Nullable E removeFirstOrNull() {
        return isNotEmpty() ? removeFirst() : null;
    }

    @DelegateBy("removeFirst()")
    default @NotNull Option<E> removeFirstOption() {
        return isNotEmpty() ? Option.some(removeFirst()) : Option.none();
    }

    E removeLast();

    @DelegateBy("removeLast()")
    default @Nullable E removeLastOrNull() {
        return isNotEmpty() ? removeLast() : null;
    }

    @DelegateBy("removeLast()")
    default @NotNull Option<E> removeLastOption() {
        return isNotEmpty() ? Option.some(removeLast()) : Option.none();
    }

    E first();

    @DelegateBy("first()")
    default @Nullable E firstOrNull() {
        return isNotEmpty() ? first() : null;
    }

    @DelegateBy("first()")
    default @NotNull Option<E> firstOption() {
        return isNotEmpty() ? Option.some(first()) : Option.none();
    }

    E last();

    @DelegateBy("last()")
    default @Nullable E lastOrNull() {
        return isNotEmpty() ? last() : null;
    }

    @DelegateBy("last()")
    default @NotNull Option<E> lastOption() {
        return isNotEmpty() ? Option.some(last()) : Option.none();
    }

    @Override
    @DelegateBy("prepend()")
    default void enqueue(E value) {
        prepend(value);
    }

    @Override
    @DelegateBy("removeLast()")
    default E dequeue() {
        return removeLast();
    }

    @Override
    @DelegateBy("removeLastOrNull()")
    default @Nullable E dequeueOrNull() {
        return removeLastOrNull();
    }

    @Override
    @DelegateBy("removeLastOption()")
    default @NotNull Option<E> dequeueOption() {
        return removeLastOption();
    }
}
