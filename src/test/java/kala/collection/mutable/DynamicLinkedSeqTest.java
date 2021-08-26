package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class DynamicLinkedSeqTest implements DynamicSeqTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, DynamicLinkedSeq<E>> factory() {
        return DynamicLinkedSeq.factory();
    }

    @Override
    public <E> DynamicLinkedSeq<E> of(E... elements) {
        return DynamicLinkedSeq.from(elements);
    }

    @Override
    public <E> DynamicLinkedSeq<E> from(E[] elements) {
        return DynamicLinkedSeq.from(elements);
    }

    @Override
    public <E> DynamicLinkedSeq<E> from(Iterable<? extends E> elements) {
        return DynamicLinkedSeq.from(elements);
    }


    @Test
    void removeTest() {
        var buffer1 = DynamicLinkedSeq.of("A", "B", "C");

        assertEquals("A", buffer1.removeAt(0));
        assertEquals(DynamicLinkedSeq.of("B", "C"), buffer1);

        assertEquals("C", buffer1.removeAt(1));
        assertEquals(DynamicLinkedSeq.of("B"), buffer1);

        assertEquals("B", buffer1.removeAt(0));
        assertEquals(DynamicLinkedSeq.of(), buffer1);

        var buffer2 = DynamicLinkedSeq.of("A", "B", "C", "D", "E");

        buffer2.removeAt(0, 2);
        assertEquals(DynamicLinkedSeq.of("C", "D", "E"), buffer2);

        buffer2.removeAt(1, 2);
        assertEquals(DynamicLinkedSeq.of("C"), buffer2);
    }
}
