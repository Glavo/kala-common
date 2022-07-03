package kala.collection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public interface SeqViewTestTemplate extends SeqLikeTestTemplate, CollectionViewTestTemplate {
    @Override
    <E> SeqView<E> of(E... elements);

    @Override
    <E> SeqView<E> from(E[] elements);

    @Override
    <E> SeqView<E> from(Iterable<? extends E> elements);

    @Test
    @Override
    default void dropTest() {
        SeqLikeTestTemplate.super.dropTest();
        assertIterableEquals(List.of(), of(0, 1, 2).drop(1).drop(2));
        assertIterableEquals(List.of(1, 2), of(0, 1, 2).drop(1).drop(0));
        assertIterableEquals(List.of(3), of(0, 1, 2, 3).drop(1).drop(2));
    }

    @Test
    @Override
    default void takeTest() {
        SeqLikeTestTemplate.super.takeTest();
        assertIterableEquals(List.of(), of(0, 1, 2).take(2).take(0));
        assertIterableEquals(List.of(0), of(0, 1, 2).take(2).take(1));
        assertIterableEquals(List.of(0, 1), of(0, 1, 2).take(2).take(2));
        assertIterableEquals(List.of(0, 1), of(0, 1, 2).take(2).take(3));
        assertIterableEquals(List.of(0, 1, 2), of(0, 1, 2).take(3).take(3));
    }
}
