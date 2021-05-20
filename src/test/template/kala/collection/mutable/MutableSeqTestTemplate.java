package kala.collection.mutable;

import kala.collection.SeqTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public interface MutableSeqTestTemplate extends MutableCollectionTestTemplate, SeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableSeq<? extends E>> factory();

    @Override
    <E> MutableSeq<E> of(E... elements);

    @Override
    <E> MutableSeq<E> from(E[] elements);

    @Override
    <E> MutableSeq<E> from(Iterable<? extends E> elements);

    @Test
    default void swapTest() {
        assertThrows(IndexOutOfBoundsException.class, () -> of().swap(0, 1));

        assertIterableEquals(
                List.of("A", "B", "C"),
                of("A", "C", "B").edit().swap(1, 2).done()
        );
        assertIterableEquals(
                List.of("A", "B", "C"),
                of("A", "C", "B").edit().swap(2, 1).done()
        );
    }

    @Test
    default void shuffleTest() {
        for (Integer[] data : data1()) {
            final MutableSeq<Integer> seq = from(data);
            seq.shuffle();

            Assertions.assertEquals(data.length, seq.size());
            Assertions.assertTrue(seq.containsAll(data));
        }

    }
}
