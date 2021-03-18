package org.glavo.kala.collection;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface SeqViewTestTemplate extends SeqLikeTestTemplate, FullSeqLikeTestTemplate {
    @Override
    <E> SeqView<E> of(E... elements);

    @Override
    <E> SeqView<E> from(E[] elements);

    @Override
    <E> SeqView<E> from(Iterable<? extends E> elements);

    @Override
    default void updatedTest() {
        var empty = of();

        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(0, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(1, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(-1, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(Integer.MAX_VALUE, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(Integer.MIN_VALUE, "foo"));

        {
            var seq = from(List.of("foo"));
            assertIterableEquals(List.of("bar"), seq.updated(0, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(1, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(-1, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MAX_VALUE, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "bar"));
        }
    }
}
