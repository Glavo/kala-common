package kala.collection.mutable;

import kala.annotations.DelegateBy;
import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;

public interface MutableDeque<E> extends MutableQueue<E> {

    static <E> @NotNull MutableDeque<E> create() {
        return new MutableArrayDeque<>();
    }

    static <E> @NotNull MutableDeque<E> wrapJava(java.util.@NotNull Deque<E> deque) {
        Objects.requireNonNull(deque);
        return new AbstractMutableDeque<E>() {
            @Override
            public @NotNull Iterator<E> iterator() {
                return deque.iterator();
            }

            @Override
            public boolean isEmpty() {
                return deque.isEmpty();
            }

            @Override
            public void prepend(E value) {
                deque.addFirst(value);
            }

            @Override
            public void append(E value) {
                deque.addLast(value);
            }

            @Override
            public E removeFirst() {
                return deque.removeFirst();
            }

            @Override
            public E removeLast() {
                return deque.removeLast();
            }

            @Override
            public E getFirst() {
                return deque.getFirst();
            }

            @Override
            public E getLast() {
                return deque.getLast();
            }

            @Override
            public void enqueue(E value) {
                deque.add(value);
            }

            @Override
            public E dequeue() {
                return deque.remove();
            }
        };
    }

    @Override
    default @NotNull String className() {
        return "MutableDeque";
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

    E getFirst();

    @DelegateBy("getFirst()")
    default @Nullable E getFirstOrNull() {
        return isNotEmpty() ? getFirst() : null;
    }

    @DelegateBy("getFirst()")
    default @NotNull Option<E> getFirstOption() {
        return isNotEmpty() ? Option.some(getFirst()) : Option.none();
    }

    E getLast();

    @DelegateBy("getLast()")
    default @Nullable E getLastOrNull() {
        return isNotEmpty() ? getLast() : null;
    }

    @DelegateBy("getLast()")
    default @NotNull Option<E> getLastOption() {
        return isNotEmpty() ? Option.some(getLast()) : Option.none();
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
    @DelegateBy("dequeue()")
    default @Nullable E dequeueOrNull() {
        return isNotEmpty() ? dequeue() : null;
    }

    @Override
    @DelegateBy("dequeue()")
    default @NotNull Option<E> dequeueOption() {
        return isNotEmpty() ? Option.some(dequeue()) : Option.none();
    }
}
