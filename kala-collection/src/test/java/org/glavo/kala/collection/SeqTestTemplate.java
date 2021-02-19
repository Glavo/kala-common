package org.glavo.kala.collection;

import org.glavo.kala.collection.immutable.ImmutableArray;
import org.glavo.kala.collection.immutable.ImmutableList;
import org.glavo.kala.collection.immutable.ImmutableSeq;
import org.glavo.kala.collection.immutable.ImmutableVector;
import org.glavo.kala.collection.mutable.MutableArray;
import org.glavo.kala.control.Option;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public interface SeqTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends Seq<? extends E>> factory();

    @Test
    default void isDefinedAtTest() {
        for (int i = -10; i < 10; i++) {
            assertFalse(factory().empty().isDefinedAt(i));
        }
        assertFalse(factory().empty().isDefinedAt(Integer.MIN_VALUE));
        assertFalse(factory().empty().isDefinedAt(Integer.MAX_VALUE));

        for (Integer[] data : data1()) {
            Seq<? extends Integer> seq = this.<Integer>factory().from(data);
            assertFalse(seq.isDefinedAt(-1));
            assertFalse(seq.isDefinedAt(Integer.MIN_VALUE));

            for (int i = 0; i < data.length; i++) {
                assertTrue(seq.isDefinedAt(i));
            }
            assertFalse(seq.isDefinedAt(data.length));
            assertFalse(seq.isDefinedAt(Integer.MAX_VALUE));
        }
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    default void getTest() {
        for (int i = -10; i < 10; i++) {
            final int iv = i;
            assertThrows(IndexOutOfBoundsException.class, () -> factory().empty().get(iv));
            assertNull(factory().empty().getOrNull(i));
            assertSame(Option.none(), factory().empty().getOption(i));
        }
        assertThrows(IndexOutOfBoundsException.class, () -> factory().empty().get(Integer.MIN_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> factory().empty().get(Integer.MAX_VALUE));

        for (Integer[] data : data1()) {
            Seq<? extends Integer> seq = this.<Integer>factory().from(data);
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(Integer.MIN_VALUE));
            assertNull(seq.getOrNull(-1));
            assertNull(seq.getOrNull(Integer.MIN_VALUE));
            assertSame(Option.none(), seq.getOption(-1));
            assertSame(Option.none(), seq.getOption(Integer.MIN_VALUE));

            for (int i = 0; i < data.length; i++) {
                assertSame(data[i], seq.get(i));
                assertSame(data[i], seq.getOrNull(i));
                assertEquals(Option.some(data[i]), seq.getOption(i));
            }
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(data.length));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(Integer.MAX_VALUE));
            assertNull(seq.getOrNull(data.length));
            assertNull(seq.getOrNull(Integer.MAX_VALUE));
            assertSame(Option.none(), seq.getOption(data.length));
            assertSame(Option.none(), seq.getOption(Integer.MAX_VALUE));
        }
    }

    @Test
    default void reversedIteratorTest() {
        assertFalse(factory().empty().reverseIterator().hasNext());

        assertIterableEquals(List.of(0), ImmutableSeq.from(factory().from(List.of(0)).reverseIterator()));
        assertIterableEquals(List.of(1, 0), ImmutableSeq.from(factory().from(List.of(0, 1)).reverseIterator()));

        for (Integer[] data : data1()) {
            Integer[] rdata = new Integer[data.length];
            for (int i = 0; i < data.length; i++) {
                rdata[data.length - i - 1] = data[i];
            }

            assertIterableEquals(Arrays.asList(rdata), ImmutableSeq.from(factory().from(data).reverseIterator()));
        }
    }

    @Test
    default void firstTest() {
        assertThrows(NoSuchElementException.class, () -> factory().empty().first());
        for (Integer[] data : data1()) {
            assertSame(data[0], factory().from(data).first());
        }
    }

    @Test
    default void lastTest() {
        assertThrows(NoSuchElementException.class, () -> factory().empty().last());
        for (Integer[] data : data1()) {
            assertSame(data[data.length - 1], factory().from(data).last());
        }
    }

    @Test
    default void indexOfTest() {
        assertEquals(-1, factory().empty().indexOf(0));
        assertEquals(-1, factory().empty().indexOf(10));
        assertEquals(-1, factory().empty().indexOf(0, 1));
        assertEquals(-1, factory().empty().indexOf(10, 1));
        assertEquals(-1, factory().empty().indexOf(0, -1));
        assertEquals(-1, factory().empty().indexOf(10, -1));

        CollectionFactory<String, ?, ? extends Seq<? extends String>> sf = this.factory();

        assertEquals(0, sf.from(List.of("foo")).indexOf("foo"));
        assertEquals(-1, sf.from(List.of("foo")).indexOf("bar"));
        assertEquals(-1, sf.from(List.of("foo")).indexOf(null));
        assertEquals(0, sf.from(List.of("foo", "bar")).indexOf("foo"));
        assertEquals(1, sf.from(List.of("foo", "bar")).indexOf("bar"));
        assertEquals(-1, sf.from(List.of("foo", "bar")).indexOf("zzz"));
        assertEquals(1, sf.from(List.of("foo", "bar", "zzz", "bar")).indexOf("bar"));
    }

    @Test
    default void lastIndexOfTest() {
        assertEquals(-1, factory().empty().lastIndexOf(0));
        assertEquals(-1, factory().empty().lastIndexOf(10));

        CollectionFactory<String, ?, ? extends Seq<? extends String>> sf = this.factory();

        assertEquals(0, sf.from(List.of("foo")).lastIndexOf("foo"));
        assertEquals(-1, sf.from(List.of("foo")).lastIndexOf("bar"));
        assertEquals(-1, sf.from(List.of("foo")).lastIndexOf(null));
        assertEquals(0, sf.from(List.of("foo", "bar")).lastIndexOf("foo"));
        assertEquals(1, sf.from(List.of("foo", "bar")).lastIndexOf("bar"));
        assertEquals(-1, sf.from(List.of("foo", "bar")).lastIndexOf("zzz"));
        assertEquals(3, sf.from(List.of("foo", "bar", "zzz", "bar")).lastIndexOf("bar"));
    }

    @Test
    default void equalsTest() {
        Assertions.assertEquals(ImmutableArray.empty(), factory().empty());
        assertEquals(ImmutableVector.empty(), factory().empty());
        Assertions.assertEquals(ImmutableList.empty(), factory().empty());
        assertEquals(MutableArray.empty(), factory().empty());

        Seq<?> foo = factory().from(List.of("foo"));
        assertNotEquals(ImmutableArray.empty(), foo);
        assertNotEquals(ImmutableVector.empty(), foo);
        assertNotEquals(MutableArray.empty(), foo);
        assertEquals(ImmutableArray.of("foo"), foo);
        assertEquals(ImmutableVector.of("foo"), foo);
        assertEquals(MutableArray.of("foo"), foo);

        for (Integer[] data : data1()) {
            assertEquals(ImmutableArray.from(data), factory().from(data));
            assertEquals(ImmutableVector.from(data), factory().from(data));
            assertEquals(MutableArray.from(data), factory().from(data));
        }


    }
}
