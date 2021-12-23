package kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
            public void enqueue(E value) {
                queue.add(value);
            }

            @Override
            public E dequeue() {
                return queue.remove();
            }

            @Override
            public boolean isEmpty() {
                return queue.isEmpty();
            }
        };
    }

    void enqueue(E value);

    E dequeue();

    boolean isEmpty();
}
