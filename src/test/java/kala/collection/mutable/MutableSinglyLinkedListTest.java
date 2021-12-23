package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableLinkedSeq;
import kala.comparator.Comparators;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class MutableSinglyLinkedListTest implements MutableListTestTemplate {

    @Override
    public <E> CollectionFactory<E, ?, MutableSinglyLinkedList<E>> factory() {
        return MutableSinglyLinkedList.factory();
    }

    @Override
    public <E> MutableSinglyLinkedList<E> of(E... elements) {
        return MutableSinglyLinkedList.from(elements);
    }

    @Override
    public <E> MutableSinglyLinkedList<E> from(E[] elements) {
        return MutableSinglyLinkedList.from(elements);
    }

    @Override
    public <E> MutableSinglyLinkedList<E> from(Iterable<? extends E> elements) {
        return MutableSinglyLinkedList.from(elements);
    }

    @Test
    void ensureUnaliasedTest() {
        ImmutableArray<String> values = ImmutableArray.of("value1", "value2", "value3");

        MutableSinglyLinkedList<String> seq = MutableSinglyLinkedList.from(values);
        ImmutableLinkedSeq<String> immSeq = seq.toImmutableLinkedSeq();

        assertIterableEquals(values, immSeq);

        seq.append("value4");
        assertIterableEquals(values.appended("value4"), seq);
        assertIterableEquals(values, immSeq);

        seq.prepend("value0");
        assertIterableEquals(values.appended("value4").prepended("value0"), seq);
        assertIterableEquals(values, immSeq);
    }

    @Test
    void removeTest() {
        var buffer1 = MutableSinglyLinkedList.of("A", "B", "C");

        assertEquals("A", buffer1.removeAt(0));
        assertEquals(MutableSinglyLinkedList.of("B", "C"), buffer1);

        assertEquals("C", buffer1.removeAt(1));
        assertEquals(MutableSinglyLinkedList.of("B"), buffer1);

        assertEquals("B", buffer1.removeAt(0));
        assertEquals(MutableSinglyLinkedList.of(), buffer1);
    }

    @Test
    public void sortTest() {
        List<Comparator<Integer>> comparators = Arrays.asList(
                null,
                Comparators.naturalOrder(),
                Comparators.reverseOrder()
        );

        for (Integer[] data : data1()) {
            for (Comparator<Integer> comparator : comparators) {
                MutableSeq<Integer> seq = from(data);
                ArrayList<Integer> list = new ArrayList<>(Arrays.asList(data));

                seq.sort(comparator);
                list.sort(comparator);

                assertIterableEquals(list, seq);
            }
            for (Comparator<Integer> comparator : comparators) {
                MutableSeq<Integer> seq = from(data);
                ArrayList<Integer> list = new ArrayList<>(Arrays.asList(data));

                ImmutableLinkedSeq<Integer> frozen = seq.toImmutableLinkedSeq();
                @SuppressWarnings({"unchecked", "rawtypes"})
                List<Integer> frozenValues = (List) Arrays.asList(frozen.toArray());

                seq.sort(comparator);
                list.sort(comparator);

                assertIterableEquals(list, seq);
                assertIterableEquals(frozenValues, frozen);
            }
        }

    }
}
