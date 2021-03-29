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
    default void iteratorTest() {
        assertFalse(of().iterator(0).hasNext());
        assertThrows(IndexOutOfBoundsException.class, () -> of().iterator(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().iterator(Integer.MIN_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> of().iterator(1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().iterator(Integer.MAX_VALUE));

        assertIterableEquals(
                List.of("str"),
                ImmutableSeq.from(of("str").iterator(0))
        );
        assertIterableEquals(
                List.of(),
                ImmutableSeq.from(of("str").iterator(1))
        );
        assertThrows(IndexOutOfBoundsException.class, () -> of("str").iterator(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> of("str").iterator(Integer.MIN_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> of("str").iterator(2));
        assertThrows(IndexOutOfBoundsException.class, () -> of("str").iterator(Integer.MAX_VALUE));

        assertIterableEquals(
                List.of("str1", "str2", "str3"),
                ImmutableSeq.from(of("str1", "str2", "str3").iterator(0))
        );
        assertIterableEquals(
                List.of("str2", "str3"),
                ImmutableSeq.from(of("str1", "str2", "str3").iterator(1))
        );
        assertIterableEquals(
                List.of("str3"),
                ImmutableSeq.from(of("str1", "str2", "str3").iterator(2))
        );
        assertIterableEquals(
                List.of(),
                ImmutableSeq.from(of("str1", "str2", "str3").iterator(3))
        );
    }

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

    @Test
    default void foldTest() {
        assertEquals(10, this.<Integer>of().fold(10, Integer::sum));
        assertEquals(30, of(20).fold(10, Integer::sum));
        assertEquals(60, of(20, 30).fold(10, Integer::sum));
        assertEquals(150, of(20, 30, 40, 50).fold(10, Integer::sum));

        assertEquals("A", this.<String>of().foldLeft("A", (a, b) -> a + b));
        assertEquals("ABC", of("B", "C").foldLeft("A", (a, b) -> a + b));
        assertEquals("AB", of("B").foldLeft("A", (a, b) -> a + b));
        assertEquals("ABCDEF", of("B", "C", "D", "E", "F").foldLeft("A", (a, b) -> a + b));

        assertEquals("A", this.<String>of().foldRight("A", (a, b) -> a + b));
        assertEquals("AB", of("A").foldRight("B", (a, b) -> a + b));
        assertEquals("ABC", of("A", "B").foldRight("C", (a, b) -> a + b));
        assertEquals("ABCDEF", of("A", "B", "C", "D", "E").foldRight("F", (a, b) -> a + b));

        assertEquals(10, this.<Integer>of().foldIndexed(10, (idx, i1, i2) -> idx + i1 + i2));
        assertEquals(30, of(20).foldIndexed(10, (idx, i1, i2) -> idx + i1 + i2));
        assertEquals(61, of(20, 30).foldIndexed(10, (idx, i1, i2) -> idx + i1 + i2));
        assertEquals(156, of(20, 30, 40, 50).foldIndexed(10, (idx, i1, i2) -> idx + i1 + i2));

        assertEquals("init", this.<String>of().foldLeftIndexed("init", (idx, a, b) -> a + b));
        assertEquals("0 init A", of("A")
                .foldLeftIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("2 1 0 init A B C", of("A", "B", "C")
                .foldLeftIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("5 4 3 2 1 0 init A B C D E F", of("A", "B", "C", "D", "E", "F")
                .foldLeftIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));

        assertEquals("init", this.<String>of()
                .foldRightIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("0 A init", of("A")
                .foldRightIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("0 A 1 B 2 C init", of("A", "B", "C")
                .foldRightIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("0 A 1 B 2 C 3 D 4 E 5 F init", of("A", "B", "C", "D", "E", "F")
                .foldRightIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
    }

    @Test
    default void reduceTest() {
        assertThrows(NoSuchElementException.class, () -> this.<Integer>of().reduce(Integer::sum));
        assertThrows(NoSuchElementException.class, () -> this.<Integer>of().reduceLeft(Integer::sum));
        assertThrows(NoSuchElementException.class, () -> this.<Integer>of().reduceRight(Integer::sum));

        assertEquals(10, of(10).reduce(Integer::sum));
        assertEquals(30, of(10, 20).reduce(Integer::sum));
        assertEquals(60, of(10, 20, 30).reduce(Integer::sum));
        assertEquals(210, of(10, 20, 30, 40, 50, 60).reduce(Integer::sum));

        assertEquals("A", of("A")
                .reduceLeft((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, B)", of("A", "B")
                .reduceLeft((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("((A, B), C)", of("A", "B", "C")
                .reduceLeft((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(((((A, B), C), D), E), F)", of("A", "B", "C", "D", "E", "F")
                .reduceLeft((s1, s2) -> String.format("(%s, %s)", s1, s2)));

        assertEquals("A", of("A")
                .reduceLeftOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, B)", of("A", "B")
                .reduceLeftOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("((A, B), C)", of("A", "B", "C")
                .reduceLeftOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(((((A, B), C), D), E), F)", of("A", "B", "C", "D", "E", "F")
                .reduceLeftOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));

        assertEquals("A", of("A")
                .reduceLeftOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(A, B)", of("A", "B")
                .reduceLeftOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("((A, B), C)", of("A", "B", "C")
                .reduceLeftOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(((((A, B), C), D), E), F)", of("A", "B", "C", "D", "E", "F")
                .reduceLeftOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());

        assertEquals("A", of("A")
                .reduceRight((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, B)", of("A", "B")
                .reduceRight((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, (B, C))", of("A", "B", "C")
                .reduceRight((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, (B, (C, (D, (E, F)))))", of("A", "B", "C", "D", "E", "F")
                .reduceRight((s1, s2) -> String.format("(%s, %s)", s1, s2)));

        assertEquals("A", of("A")
                .reduceRightOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, B)", of("A", "B")
                .reduceRightOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, (B, C))", of("A", "B", "C")
                .reduceRightOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, (B, (C, (D, (E, F)))))", of("A", "B", "C", "D", "E", "F")
                .reduceRightOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));

        assertEquals("A", of("A")
                .reduceRightOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(A, B)", of("A", "B")
                .reduceRightOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(A, (B, C))", of("A", "B", "C")
                .reduceRightOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(A, (B, (C, (D, (E, F)))))", of("A", "B", "C", "D", "E", "F")
                .reduceRightOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
    }

}
