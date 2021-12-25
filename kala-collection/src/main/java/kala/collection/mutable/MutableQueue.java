package kala.collection.mutable;

import kala.control.Option;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface MutableQueue<E> {

    @Contract("-> new")
    static <E> MutableQueue<E> create() {
        return new MutableLinkedList<>();
    }

    static <E> @NotNull MutableQueue<E> wrapJava(@NotNull java.util.Queue<E> queue) {
        Objects.requireNonNull(queue);

        return new MutableQueue<E>() {
            @Override
            public boolean isEmpty() {
                return queue.isEmpty();
            }

            @Override
            public void enqueue(E value) {
                queue.add(value);
            }

            @Override
            public @NotNull Option<E> dequeueOption() {
                return queue.isEmpty() ? Option.none() : Option.some(queue.remove());
            }
        };
    }

    boolean isEmpty();

    void enqueue(E value);

    default E dequeue() {
        return dequeueOption().get();
    }

    default @Nullable E dequeueOrNull() {
        return dequeueOption().getOrNull();
    }

    @NotNull Option<E> dequeueOption();
}
