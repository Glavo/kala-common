package kala.collection.immutable;

import kala.collection.SeqView;
import kala.collection.SeqViewTestTemplate;
import kala.collection.factory.CollectionFactory;
import kala.collection.mutable.MutableSinglyLinkedList;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class ImmutableLinkedSeqTest implements ImmutableSeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableLinkedSeq<E>> factory() {
        return ImmutableLinkedSeq.factory();
    }

    @Test
    public void ofTest() {
        ImmutableSeqTestTemplate.super.ofTest();

        assertIterableEquals(List.of(), ImmutableLinkedSeq.of());
        assertIterableEquals(List.of("str1"), ImmutableLinkedSeq.of("str1"));
        assertIterableEquals(List.of("str1", "str2"), ImmutableLinkedSeq.of("str1", "str2"));
        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableLinkedSeq.of("str1", "str2", "str3"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableLinkedSeq.of("str1", "str2", "str3", "str4"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableLinkedSeq.of("str1", "str2", "str3", "str4", "str5"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableLinkedSeq.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableLinkedSeq.of(data));
        }
    }

    @Test
    public void mergeBuilderTest() {
        MutableSinglyLinkedList<String> b1 = new MutableSinglyLinkedList<>();
        MutableSinglyLinkedList<String> b2 = new MutableSinglyLinkedList<>();

        assertTrue(ImmutableLinkedSeq.Builder.merge(b1, b2).isEmpty());

        b1 = MutableSinglyLinkedList.of("str1", "str2");
        b2 = MutableSinglyLinkedList.of("str3", "str4");

        var m = ImmutableLinkedSeq.Builder.merge(b1, b2);
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), m);
    }

    static final class ViewTest implements SeqViewTestTemplate {
        @Override
        public <E> SeqView<E> of(E... elements) {
            return ImmutableLinkedSeq.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(E[] elements) {
            return ImmutableLinkedSeq.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(Iterable<? extends E> elements) {
            return ImmutableLinkedSeq.<E>from(elements).view();
        }
    }

    public static final class NodeTest implements ImmutableSeqTestTemplate {
        @Override
        public <E> CollectionFactory<E, ?, ImmutableLinkedSeq.Node<E>> factory() {
            return ImmutableLinkedSeq.nodeFactory();
        }

        @Test
        public void ofTest() {
            assertIterableEquals(List.of(), ImmutableLinkedSeq.nodeOf());
            assertIterableEquals(List.of("str1"), ImmutableLinkedSeq.nodeOf("str1"));
            assertIterableEquals(List.of("str1", "str2"), ImmutableLinkedSeq.nodeOf("str1", "str2"));
            assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableLinkedSeq.nodeOf("str1", "str2", "str3"));
            assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableLinkedSeq.nodeOf("str1", "str2", "str3", "str4"));
            assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableLinkedSeq.nodeOf("str1", "str2", "str3", "str4", "str5"));
            assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableLinkedSeq.nodeOf("str1", "str2", "str3", "str4", "str5", "str6"));
            for (Integer[] data : data1()) {
                assertIterableEquals(Arrays.asList(data), ImmutableLinkedSeq.of(data));
            }
        }

        @Override
        public void fromTest() {
            // TODO
        }
    }

    static final class NodeViewTest implements SeqViewTestTemplate {
        @Override
        public <E> SeqView<E> of(E... elements) {
            return ImmutableLinkedSeq.nodeFrom(elements).view();
        }

        @Override
        public <E> SeqView<E> from(E[] elements) {
            return ImmutableLinkedSeq.nodeFrom(elements).view();
        }

        @Override
        public <E> SeqView<E> from(Iterable<? extends E> elements) {
            return ImmutableLinkedSeq.<E>nodeFrom(elements).view();
        }
    }
}
