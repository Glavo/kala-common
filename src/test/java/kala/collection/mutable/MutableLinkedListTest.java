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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public final class MutableLinkedListTest implements MutableListTestTemplate {

    @Override
    public <E> CollectionFactory<E, ?, MutableLinkedList<E>> factory() {
        return MutableLinkedList.factory();
    }

    @Override
    public <E> MutableLinkedList<E> of(E... elements) {
        return MutableLinkedList.from(elements);
    }

    @Override
    public <E> MutableLinkedList<E> from(E[] elements) {
        return MutableLinkedList.from(elements);
    }

    @Override
    public <E> MutableLinkedList<E> from(Iterable<? extends E> elements) {
        return MutableLinkedList.from(elements);
    }

    @Test
    void ensureUnaliasedTest() {
        ImmutableArray<String> values = ImmutableArray.of("value1", "value2", "value3");

        MutableLinkedList<String> seq = MutableLinkedList.from(values);
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
        var buffer1 = MutableLinkedList.of("A", "B", "C");

        assertEquals("A", buffer1.removeAt(0));
        assertEquals(MutableLinkedList.of("B", "C"), buffer1);

        assertEquals("C", buffer1.removeAt(1));
        assertEquals(MutableLinkedList.of("B"), buffer1);

        assertEquals("B", buffer1.removeAt(0));
        assertEquals(MutableLinkedList.of(), buffer1);
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
