package org.glavo.kala.collection;

import org.glavo.kala.collection.immutable.ImmutableSeq;
import org.glavo.kala.control.Option;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface SeqLikeTestTemplate extends CollectionLikeTestTemplate {
    @Override
    @SuppressWarnings("unchecked")
    <E> SeqLike<E> of(E... elements);

    @Override
    <E> SeqLike<E> from(E[] elements);

    @Override
    <E> SeqLike<E> from(Iterable<? extends E> elements);

    @Test
    default void streamTest() {
        assertEquals(0, of().stream().count());
        assertIterableEquals(List.of("foo"), of("foo").stream().collect(Collectors.toList()));
        assertIterableEquals(List.of("foo", "bar"), of("foo", "bar").stream().collect(Collectors.toList()));

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), from(data).stream().collect(Collectors.toList()));
        }
    }

    @Test
    default void isDefinedAtTest() {
        for (int i = -10; i < 10; i++) {
            assertFalse(of().isDefinedAt(i));
        }
        assertFalse(of().isDefinedAt(Integer.MIN_VALUE));
        assertFalse(of().isDefinedAt(Integer.MAX_VALUE));

        for (Integer[] data : data1()) {
            SeqLike<Integer> seq = from(data);
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
            assertThrows(IndexOutOfBoundsException.class, () -> of().get(iv));
            assertNull(of().getOrNull(i));
            assertSame(Option.none(), of().getOption(i));
        }
        assertThrows(IndexOutOfBoundsException.class, () -> of().get(Integer.MIN_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> of().get(Integer.MAX_VALUE));

        for (Integer[] data : data1()) {
            SeqLike<Integer> seq = this.from(data);
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
        assertFalse(of().reverseIterator().hasNext());

        assertIterableEquals(List.of(0), ImmutableSeq.from(from(List.of(0)).reverseIterator()));
        assertIterableEquals(List.of(1, 0), ImmutableSeq.from(from(List.of(0, 1)).reverseIterator()));

        for (Integer[] data : data1()) {
            Integer[] rdata = new Integer[data.length];
            for (int i = 0; i < data.length; i++) {
                rdata[data.length - i - 1] = data[i];
            }

            assertIterableEquals(Arrays.asList(rdata), ImmutableSeq.from(from(data).reverseIterator()));
        }
    }

    @Test
    default void firstTest() {
        assertThrows(NoSuchElementException.class, () -> of().first());
        for (Integer[] data : data1()) {
            assertSame(data[0], from(data).first());
        }
    }

    @Test
    default void lastTest() {
        assertThrows(NoSuchElementException.class, () -> of().last());
        for (Integer[] data : data1()) {
            assertSame(data[data.length - 1], from(data).last());
        }
    }

    @Test
    default void indexOfTest() {
        assertEquals(-1, of().indexOf(0));
        assertEquals(-1, of().indexOf(10));
        assertEquals(-1, of().indexOf(0, 1));
        assertEquals(-1, of().indexOf(10, 1));
        assertEquals(-1, of().indexOf(0, -1));
        assertEquals(-1, of().indexOf(10, -1));

        assertEquals(0, from(List.of("foo")).indexOf("foo"));
        assertEquals(-1, from(List.of("foo")).indexOf("bar"));
        assertEquals(-1, from(List.of("foo")).indexOf(null));
        assertEquals(0, from(List.of("foo", "bar")).indexOf("foo"));
        assertEquals(1, from(List.of("foo", "bar")).indexOf("bar"));
        assertEquals(-1, from(List.of("foo", "bar")).indexOf("zzz"));
        assertEquals(1, from(List.of("foo", "bar", "zzz", "bar")).indexOf("bar"));
    }

    @Test
    default void lastIndexOfTest() {
        assertEquals(-1, of().lastIndexOf(0));
        assertEquals(-1, of().lastIndexOf(10));

        assertEquals(0, from(List.of("foo")).lastIndexOf("foo"));
        assertEquals(-1, from(List.of("foo")).lastIndexOf("bar"));
        assertEquals(-1, from(List.of("foo")).lastIndexOf(null));
        assertEquals(0, from(List.of("foo", "bar")).lastIndexOf("foo"));
        assertEquals(1, from(List.of("foo", "bar")).lastIndexOf("bar"));
        assertEquals(-1, from(List.of("foo", "bar")).lastIndexOf("zzz"));
        assertEquals(3, from(List.of("foo", "bar", "zzz", "bar")).lastIndexOf("bar"));
    }

    @Test
    default void sliceViewTest() {
        assertIterableEquals(List.of(), of().sliceView(0, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(-1, 0).toImmutableSeq());
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(0, 1).toImmutableSeq());
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(-1, 1).toImmutableSeq());
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(Integer.MIN_VALUE, 0).toImmutableSeq());
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(Integer.MIN_VALUE, 1).toImmutableSeq());

        for (Integer[] data : data1()) {
            int dl = data.length;
            assertIterableEquals(Arrays.asList(data), from(data).sliceView(0, data.length));

            if (dl >= 1) {
                assertIterableEquals(
                        Arrays.asList(Arrays.copyOfRange(data, 1, dl)),
                        from(data).sliceView(1, dl)
                );
                assertIterableEquals(
                        Arrays.asList(Arrays.copyOfRange(data, 0, dl - 1)),
                        from(data).sliceView(0, dl - 1)
                );
            }
        }
    }

}
