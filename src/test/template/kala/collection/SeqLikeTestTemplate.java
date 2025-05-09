/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection;

import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableVector;
import kala.collection.mutable.MutableList;
import kala.control.Option;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static kala.ExtendedAssertions.assertIteratorEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface SeqLikeTestTemplate extends CollectionLikeTestTemplate, SequentialCollectionLikeTestTemplate {
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
        assertFalse(of().iterator(~0).hasNext());

        assertThrows(IndexOutOfBoundsException.class, () -> of().iterator(~1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().iterator(Integer.MIN_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> of().iterator(1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().iterator(Integer.MAX_VALUE));

        assertIteratorEquals(List.of("str"), of("str").iterator(0));
        assertIteratorEquals(List.of("str"), of("str").iterator(~1));
        assertIteratorEquals(List.of(), of("str").iterator(1));
        assertIteratorEquals(List.of(), of("str").iterator(~0));

        assertThrows(IndexOutOfBoundsException.class, () -> of("str").iterator(~2));
        assertThrows(IndexOutOfBoundsException.class, () -> of("str").iterator(Integer.MIN_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> of("str").iterator(2));
        assertThrows(IndexOutOfBoundsException.class, () -> of("str").iterator(Integer.MAX_VALUE));

        assertIteratorEquals(List.of("str1", "str2", "str3"), of("str1", "str2", "str3").iterator(0));
        assertIteratorEquals(List.of("str1", "str2", "str3"), of("str1", "str2", "str3").iterator(~3));
        assertIteratorEquals(List.of("str2", "str3"), of("str1", "str2", "str3").iterator(1));
        assertIteratorEquals(List.of("str2", "str3"), of("str1", "str2", "str3").iterator(~2));
        assertIteratorEquals(List.of("str3"), of("str1", "str2", "str3").iterator(2));
        assertIteratorEquals(List.of("str3"), of("str1", "str2", "str3").iterator(~1));
        assertIteratorEquals(List.of(), of("str1", "str2", "str3").iterator(3));
        assertIteratorEquals(List.of(), of("str1", "str2", "str3").iterator(~0));
    }

    @Test
    default void seqIteratorTest() {
        SeqIterator<Object> empty = of().seqIterator();
        assertFalse(empty.hasNext());
        assertThrows(NoSuchElementException.class, empty::next);
        assertFalse(empty.hasPrevious());
        assertThrows(NoSuchElementException.class, empty::previous);
        assertEquals(0, empty.nextIndex());
        assertEquals(-1, empty.previousIndex());

        assertThrows(IndexOutOfBoundsException.class, () -> of().seqIterator(~1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().seqIterator(Integer.MIN_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> of().seqIterator(1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().seqIterator(Integer.MAX_VALUE));

        // TODO
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
            assertFalse(seq.isDefinedAt(~0));
            assertFalse(seq.isDefinedAt(Integer.MIN_VALUE));

            for (int i = 0; i < data.length; i++) {
                assertTrue(seq.isDefinedAt(i));
                assertTrue(seq.isDefinedAt(~(i + 1)));
            }

            assertFalse(seq.isDefinedAt(~(data.length + 1)));
            assertFalse(seq.isDefinedAt(data.length));
            assertFalse(seq.isDefinedAt(Integer.MAX_VALUE));
        }
    }

    @Test
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
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(~0));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(Integer.MIN_VALUE));
            assertNull(seq.getOrNull(~0));
            assertNull(seq.getOrNull(Integer.MIN_VALUE));
            assertSame(Option.none(), seq.getOption(~0));
            assertSame(Option.none(), seq.getOption(Integer.MIN_VALUE));

            for (int i = 0; i < data.length; i++) {
                assertSame(data[i], seq.get(i));
                assertSame(data[i], seq.getOrNull(i));
                assertEquals(Option.some(data[i]), seq.getOption(i));

                assertSame(data[data.length - i - 1], seq.get(~(i + 1)));
                assertSame(data[data.length - i - 1], seq.getOrNull(~(i + 1)));
                assertEquals(Option.some(data[data.length - i - 1]), seq.getOption(~(i + 1)));
            }
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(data.length));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(~(data.length + 1)));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(Integer.MAX_VALUE));
            assertNull(seq.getOrNull(data.length));
            assertNull(seq.getOrNull(~(data.length + 1)));
            assertNull(seq.getOrNull(Integer.MAX_VALUE));
            assertSame(Option.none(), seq.getOption(data.length));
            assertSame(Option.none(), seq.getOption(~(data.length + 1)));
            assertSame(Option.none(), seq.getOption(Integer.MAX_VALUE));
        }
    }

    @Test
    default void reversedIteratorTest() {
        assertFalse(of().reverseIterator().hasNext());

        assertIteratorEquals(List.of(0), from(List.of(0)).reverseIterator());
        assertIteratorEquals(List.of(1, 0), from(List.of(0, 1)).reverseIterator());

        for (Integer[] data : data1()) {
            Integer[] rdata = new Integer[data.length];
            for (int i = 0; i < data.length; i++) {
                rdata[data.length - i - 1] = data[i];
            }

            assertIteratorEquals(Arrays.asList(rdata), from(data).reverseIterator());
        }
    }

    @Test
    default void getFirstTest() {
        assertThrows(NoSuchElementException.class, () -> of().getFirst());
        assertNull(of().getFirstOrNull());
        assertEquals(Option.none(), of().getFirstOption());
        for (Integer[] data : data1()) {
            assertSame(data[0], from(data).getFirst());
            assertSame(data[0], from(data).getFirstOrNull());
            assertEquals(Option.some(data[0]), from(data).getFirstOption());
        }
    }

    @Test
    default void getLastTest() {
        assertThrows(NoSuchElementException.class, () -> of().getLast());
        assertNull(of().getLastOrNull());
        assertEquals(Option.none(), of().getLastOption());
        for (Integer[] data : data1()) {
            assertSame(data[data.length - 1], from(data).getLast());
            assertSame(data[data.length - 1], from(data).getLastOrNull());
            assertEquals(Option.some(data[data.length - 1]), from(data).getLastOption());
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
        assertEquals(-1, of().lastIndexOf(0, 0));
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
    default void binarySearchTest() {
        assertEquals(-1, of().binarySearch(0));
        assertEquals(-1, of().binarySearch(10));

        assertEquals(0, of("str0").binarySearch("str0"));
        assertEquals(-2, of("str0").binarySearch("str1"));
        assertEquals(0, of("str0", "str1").binarySearch("str0"));
        assertEquals(1, of("str0", "str1").binarySearch("str1"));
        assertEquals(-3, of("str0", "str1").binarySearch("str2"));

        assertEquals(0, of("str0").binarySearch("str0", Comparator.naturalOrder()));
        assertEquals(-2, of("str0").binarySearch("str1", Comparator.naturalOrder()));
        assertEquals(0, of("str0", "str1").binarySearch("str0", Comparator.naturalOrder()));
        assertEquals(1, of("str0", "str1").binarySearch("str1", Comparator.naturalOrder()));
        assertEquals(-3, of("str0", "str1").binarySearch("str2", Comparator.naturalOrder()));
    }

    @Test
    default void sliceViewTest() {
        assertIterableEquals(List.of(), of().sliceView(0, 0));
        assertIterableEquals(List.of(), of().sliceView(~0, 0));
        assertIterableEquals(List.of(), of().sliceView(0, ~0));
        assertIterableEquals(List.of(), of().sliceView(~0, ~0));
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(~1, 0).toSeq());
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(0, 1).toSeq());
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(-1, 1).toSeq());
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(Integer.MIN_VALUE, 0).toSeq());
        assertThrows(IndexOutOfBoundsException.class, () -> of().sliceView(Integer.MIN_VALUE, 1).toSeq());

        for (Integer[] data : data1()) {
            int dl = data.length;
            assertIterableEquals(Arrays.asList(data), from(data).sliceView(0, data.length));
            assertIterableEquals(Arrays.asList(data), from(data).sliceView(0, ~0));

            assertIterableEquals(Arrays.asList(Arrays.copyOfRange(data, 1, dl)), from(data).sliceView(1, dl));
            assertIterableEquals(Arrays.asList(Arrays.copyOfRange(data, 0, dl - 1)), from(data).sliceView(0, dl - 1));
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
        assertEquals("0 init A", of("A").foldLeftIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("2 1 0 init A B C", of("A", "B", "C").foldLeftIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("5 4 3 2 1 0 init A B C D E F", of("A", "B", "C", "D", "E", "F").foldLeftIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));

        assertEquals("init", this.<String>of().foldRightIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("0 A init", of("A").foldRightIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("0 A 1 B 2 C init", of("A", "B", "C").foldRightIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
        assertEquals("0 A 1 B 2 C 3 D 4 E 5 F init", of("A", "B", "C", "D", "E", "F").foldRightIndexed("init", (idx, a, b) -> String.format("%d %s %s", idx, a, b)));
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

        assertEquals("A", of("A").reduceLeft((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, B)", of("A", "B").reduceLeft((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("((A, B), C)", of("A", "B", "C").reduceLeft((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(((((A, B), C), D), E), F)", of("A", "B", "C", "D", "E", "F").reduceLeft((s1, s2) -> String.format("(%s, %s)", s1, s2)));

        assertEquals("A", of("A").reduceLeftOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, B)", of("A", "B").reduceLeftOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("((A, B), C)", of("A", "B", "C").reduceLeftOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(((((A, B), C), D), E), F)", of("A", "B", "C", "D", "E", "F").reduceLeftOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));

        assertEquals("A", of("A").reduceLeftOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(A, B)", of("A", "B").reduceLeftOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("((A, B), C)", of("A", "B", "C").reduceLeftOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(((((A, B), C), D), E), F)", of("A", "B", "C", "D", "E", "F").reduceLeftOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());

        assertEquals("A", of("A").reduceRight((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, B)", of("A", "B").reduceRight((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, (B, C))", of("A", "B", "C").reduceRight((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, (B, (C, (D, (E, F)))))", of("A", "B", "C", "D", "E", "F").reduceRight((s1, s2) -> String.format("(%s, %s)", s1, s2)));

        assertEquals("A", of("A").reduceRightOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, B)", of("A", "B").reduceRightOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, (B, C))", of("A", "B", "C").reduceRightOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));
        assertEquals("(A, (B, (C, (D, (E, F)))))", of("A", "B", "C", "D", "E", "F").reduceRightOrNull((s1, s2) -> String.format("(%s, %s)", s1, s2)));

        assertEquals("A", of("A").reduceRightOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(A, B)", of("A", "B").reduceRightOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(A, (B, C))", of("A", "B", "C").reduceRightOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
        assertEquals("(A, (B, (C, (D, (E, F)))))", of("A", "B", "C", "D", "E", "F").reduceRightOption((s1, s2) -> String.format("(%s, %s)", s1, s2)).get());
    }

    @Test
    default void reversedTest() {
        assertIterableEquals(List.of(), of().reversed());
        assertIterableEquals(List.of("str1"), from(List.of("str1")).reversed());
        assertIterableEquals(List.of("str2", "str1"), from(List.of("str1", "str2")).reversed());
        assertIterableEquals(List.of("str3", "str2", "str1"), from(List.of("str1", "str2", "str3")).reversed());

        for (Integer[] data : data1()) {
            ArrayList<Integer> l = new ArrayList<>(Arrays.asList(data));
            Collections.reverse(l);
            assertIterableEquals(l, from(data).reversed());
        }
    }

    @Test
    default void prependedTest() {
        assertIterableEquals(List.of("str"), this.<String>of().prepended("str"));
        assertIterableEquals(Collections.singletonList(null), this.<String>of().prepended(null));
        assertIterableEquals(List.of("str1", "str2"), of("str2").prepended("str1"));

        final int head = 12345;
        for (Integer[] data : data1()) {
            final ArrayList<Integer> al = new ArrayList<>();
            al.add(head);
            al.addAll(Arrays.asList(data));

            assertIterableEquals(al, from(data).prepended(12345));
        }
    }

    @Test
    default void prependedAllTest() {
        assertIterableEquals(List.of(), of().prependedAll(List.of()));
        assertIterableEquals(List.of("str"), of("str").prependedAll(List.of()));
        assertIterableEquals(List.of("str"), this.<String>of().prependedAll(List.of("str")));

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), this.<Integer>of().prependedAll(data));
            assertIterableEquals(Arrays.asList(data), this.<Integer>of().prependedAll(Arrays.asList(data)));
            assertIterableEquals(Arrays.asList(data), this.<Integer>of().prependedAll(ImmutableArray.from(data)));
        }

        for (int i = 0; i < data1().length - 1; i++) {
            final Integer[] data = data1()[i];
            final Integer[] data2 = data1()[i + 1];

            final ArrayList<Integer> tmp = new ArrayList<>(Arrays.asList(data));

            assertIterableEquals(tmp, this.<Integer>of().prependedAll(data));
            assertIterableEquals(tmp, this.<Integer>of().prependedAll(Arrays.asList(data)));
            assertIterableEquals(tmp, this.<Integer>of().prependedAll(ImmutableArray.from(data)));

            tmp.addAll(0, Arrays.asList(data2));

            assertIterableEquals(tmp, from(data).prependedAll(data2));
            assertIterableEquals(tmp, from(data).prependedAll(Arrays.asList(data2)));
            assertIterableEquals(tmp, from(data).prependedAll(ImmutableArray.from(data2)));
        }
    }

    @Test
    default void appendedTest() {
        assertIterableEquals(List.of("str"), this.<String>of().appended("str"));
        assertIterableEquals(Collections.singletonList(null), this.<String>of().appended(null));
        assertIterableEquals(List.of("str1", "str2"), of("str1").appended("str2"));

        final int last = 12345;
        for (Integer[] data : data1()) {
            final ArrayList<Integer> al = new ArrayList<>(Arrays.asList(data));
            al.add(last);

            assertIterableEquals(al, from(data).appended(last));
        }
    }

    @Test
    default void appendedAllTest() {
        assertIterableEquals(List.of(), of().appendedAll(List.of()));
        assertIterableEquals(List.of("str"), of("str").appendedAll(List.of()));
        assertIterableEquals(List.of("str"), this.<String>of().appendedAll(List.of("str")));

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), this.<Integer>of().appendedAll(data));
            assertIterableEquals(Arrays.asList(data), this.<Integer>of().appendedAll(Arrays.asList(data)));
            assertIterableEquals(Arrays.asList(data), this.<Integer>of().appendedAll(ImmutableArray.from(data)));
        }

        for (int i = 0; i < data1().length - 1; i++) {
            final Integer[] data = data1()[i];
            final Integer[] data2 = data1()[i + 1];

            final ArrayList<Integer> tmp = new ArrayList<>(Arrays.asList(data));

            assertIterableEquals(tmp, this.<Integer>of().appendedAll(data));
            assertIterableEquals(tmp, this.<Integer>of().appendedAll(Arrays.asList(data)));
            assertIterableEquals(tmp, this.<Integer>of().appendedAll(ImmutableArray.from(data)));

            tmp.addAll(Arrays.asList(data2));

            assertIterableEquals(tmp, from(data).appendedAll(data2));
            assertIterableEquals(tmp, from(data).appendedAll(Arrays.asList(data2)));
            assertIterableEquals(tmp, from(data).appendedAll(ImmutableArray.from(data2)));
        }
    }

    @Test
    default void insertedTest() {
        assertIterableEquals(List.of("str"), this.<String>of().inserted(0, "str"));
        assertIterableEquals(List.of("str"), this.<String>of().inserted(~0, "str"));
        assertThrows(IndexOutOfBoundsException.class, () -> this.<String>of().inserted(1, "str"));
        assertThrows(IndexOutOfBoundsException.class, () -> this.<String>of().inserted(~1, "str"));
        assertThrows(IndexOutOfBoundsException.class, () -> this.<String>of().inserted(Integer.MAX_VALUE, "str"));
        assertThrows(IndexOutOfBoundsException.class, () -> this.<String>of().inserted(Integer.MIN_VALUE, "str"));

        assertIterableEquals(List.of("str1", "str0"), this.of("str0").inserted(0, "str1"));
        assertIterableEquals(List.of("str1", "str0"), this.of("str0").inserted(~1, "str1"));
        assertIterableEquals(List.of("str0", "str1"), this.of("str0").inserted(1, "str1"));
        assertIterableEquals(List.of("str0", "str1"), this.of("str0").inserted(~0, "str1"));
        assertThrows(IndexOutOfBoundsException.class, () -> this.of("str0").inserted(2, "str1"));
        assertThrows(IndexOutOfBoundsException.class, () -> this.of("str0").inserted(~2, "str1"));

        assertIterableEquals(List.of("str2", "str0", "str1"), this.of("str0", "str1").inserted(0, "str2"));
        assertIterableEquals(List.of("str2", "str0", "str1"), this.of("str0", "str1").inserted(~2, "str2"));
        assertIterableEquals(List.of("str0", "str2", "str1"), this.of("str0", "str1").inserted(1, "str2"));
        assertIterableEquals(List.of("str0", "str2", "str1"), this.of("str0", "str1").inserted(~1, "str2"));
        assertIterableEquals(List.of("str0", "str1", "str2"), this.of("str0", "str1").inserted(2, "str2"));
        assertIterableEquals(List.of("str0", "str1", "str2"), this.of("str0", "str1").inserted(~0, "str2"));
        assertThrows(IndexOutOfBoundsException.class, () -> this.of("str0", "str1").inserted(3, "str2"));
    }

    @Test
    default void removedAtTest() {
        {
            var empty = of();

            assertThrows(IndexOutOfBoundsException.class, () -> empty.removedAt(0));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.removedAt(1));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.removedAt(~0));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.removedAt(~1));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.removedAt(~2));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.removedAt(Integer.MAX_VALUE));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.removedAt(Integer.MIN_VALUE));
        }

        {
            var seq = from(List.of("str0"));
            assertIterableEquals(List.of(), seq.removedAt(0));
            assertIterableEquals(List.of(), seq.removedAt(~1));

            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(1));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(~0));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(~2));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(Integer.MAX_VALUE));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(Integer.MIN_VALUE));
        }

        {
            var seq = from(List.of("str0", "str1"));
            assertIterableEquals(List.of("str1"), seq.removedAt(0));
            assertIterableEquals(List.of("str1"), seq.removedAt(~2));
            assertIterableEquals(List.of("str0"), seq.removedAt(1));
            assertIterableEquals(List.of("str0"), seq.removedAt(~1));

            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(2));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(3));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(~0));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(~3));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(Integer.MAX_VALUE));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(Integer.MIN_VALUE));
        }

        {
            var seq = from(List.of("str0", "str1", "str2"));
            assertIterableEquals(List.of("str1", "str2"), seq.removedAt(0));
            assertIterableEquals(List.of("str1", "str2"), seq.removedAt(~3));
            assertIterableEquals(List.of("str0", "str2"), seq.removedAt(1));
            assertIterableEquals(List.of("str0", "str2"), seq.removedAt(~2));
            assertIterableEquals(List.of("str0", "str1"), seq.removedAt(2));
            assertIterableEquals(List.of("str0", "str1"), seq.removedAt(~1));

            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(3));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(~0));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(~4));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(Integer.MAX_VALUE));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.removedAt(Integer.MIN_VALUE));
        }
    }

    @Test
    default void sliceTest() {
        assertIterableEquals(List.of(), of().slice(0, 0));
        assertIterableEquals(List.of(), of().slice(0, ~0));
        assertIterableEquals(List.of(), of().slice(~0, 0));
        assertIterableEquals(List.of(), of().slice(~0, ~0));

        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(~1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(0, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(~1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(Integer.MIN_VALUE, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(Integer.MIN_VALUE, 1));

        for (Integer[] data : data1()) {
            int dl = data.length;
            assertIterableEquals(Arrays.asList(data), from(data).slice(0, data.length));
            assertIterableEquals(Arrays.asList(data), from(data).slice(0, ~0));
            assertIterableEquals(Arrays.asList(Arrays.copyOfRange(data, 1, dl)), from(data).slice(1, dl));
            assertIterableEquals(Arrays.asList(Arrays.copyOfRange(data, 0, dl - 1)), from(data).slice(0, dl - 1));
        }
    }

    @Test
    default void dropTest() {
        var empty = of();
        assertIterableEquals(List.of(), empty.drop(0));
        assertIterableEquals(List.of(), empty.drop(1));
        assertIterableEquals(List.of(), empty.drop(Integer.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> empty.drop(-1));
        assertThrows(IllegalArgumentException.class, () -> empty.drop(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        var seq = from(list);
        assertIterableEquals(list, seq.drop(0));
        assertThrows(IllegalArgumentException.class, () -> seq.drop(-1));
        assertThrows(IllegalArgumentException.class, () -> seq.drop(Integer.MIN_VALUE));
        assertIterableEquals(list.subList(1, list.size()), seq.drop(1));
        assertIterableEquals(list.subList(2, list.size()), seq.drop(2));
        assertIterableEquals(list.subList(3, list.size()), seq.drop(3));
        assertIterableEquals(list.subList(4, list.size()), seq.drop(4));
        assertIterableEquals(List.of(), seq.drop(5));
    }

    @Test
    default void dropLastTest() {
        var empty = of();
        assertThrows(IllegalArgumentException.class, () -> empty.dropLast(-1));
        assertThrows(IllegalArgumentException.class, () -> empty.dropLast(Integer.MIN_VALUE));
        assertIterableEquals(List.of(), empty.dropLast(0));
        assertIterableEquals(List.of(), empty.dropLast(1));
        assertIterableEquals(List.of(), empty.dropLast(Integer.MAX_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        var seq = from(list);

        assertThrows(IllegalArgumentException.class, () -> seq.dropLast(-1));
        assertThrows(IllegalArgumentException.class, () -> seq.dropLast(Integer.MIN_VALUE));
        assertIterableEquals(list, seq.dropLast(0));
        assertIterableEquals(list.subList(0, list.size() - 1), seq.dropLast(1));
        assertIterableEquals(list.subList(0, list.size() - 2), seq.dropLast(2));
        assertIterableEquals(list.subList(0, list.size() - 3), seq.dropLast(3));
        assertIterableEquals(list.subList(0, list.size() - 4), seq.dropLast(4));
        assertIterableEquals(List.of(), seq.dropLast(5));
    }

    @Test
    default void dropWhileTest() {
        assertIterableEquals(List.of(), of().dropWhile(o -> {
            fail();
            return true;
        }));

        var seq = from(List.of(0, 1, 2, 3, 4, 5, 6));
        assertIterableEquals(List.of(), seq.dropWhile(i -> true));
        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6), seq.dropWhile(i -> i < 0));
        assertIterableEquals(List.of(1, 2, 3, 4, 5, 6), seq.dropWhile(i -> i < 1));
        assertIterableEquals(List.of(2, 3, 4, 5, 6), seq.dropWhile(i -> i < 2));
        assertIterableEquals(List.of(6), seq.dropWhile(i -> i < 6));
        assertIterableEquals(List.of(), seq.dropWhile(i -> i < 7));
    }

    @Test
    default void takeTest() {
        var empty = of();
        assertIterableEquals(List.of(), empty.take(0));
        assertIterableEquals(List.of(), empty.take(1));
        assertIterableEquals(List.of(), empty.take(Integer.MAX_VALUE));

        assertThrows(IllegalArgumentException.class, () -> empty.take(-1));
        assertThrows(IllegalArgumentException.class, () -> empty.take(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        var seq = from(list);
        assertThrows(IllegalArgumentException.class, () -> seq.take(-1));
        assertThrows(IllegalArgumentException.class, () -> seq.take(Integer.MIN_VALUE));
        assertIterableEquals(List.of(), seq.take(0));
        assertIterableEquals(list, seq.take(Integer.MAX_VALUE));
        assertIterableEquals(list, seq.take(5));
        assertIterableEquals(list.subList(0, 4), seq.take(4));
        assertIterableEquals(list.subList(0, 3), seq.take(3));
        assertIterableEquals(list.subList(0, 2), seq.take(2));
        assertIterableEquals(list.subList(0, 1), seq.take(1));
    }

    @Test
    default void takeLastTest() {
        var empty = of();
        assertThrows(IllegalArgumentException.class, () -> empty.takeLast(-1));
        assertThrows(IllegalArgumentException.class, () -> empty.takeLast(Integer.MIN_VALUE));
        assertIterableEquals(List.of(), empty.takeLast(0));
        assertIterableEquals(List.of(), empty.takeLast(1));
        assertIterableEquals(List.of(), empty.takeLast(Integer.MAX_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        var seq = from(list);
        assertThrows(IllegalArgumentException.class, () -> seq.takeLast(-1));
        assertThrows(IllegalArgumentException.class, () -> seq.takeLast(Integer.MIN_VALUE));
        assertIterableEquals(List.of(), seq.takeLast(0));
        assertIterableEquals(list, seq.takeLast(Integer.MAX_VALUE));
        assertIterableEquals(list, seq.takeLast(5));
        assertIterableEquals(list.subList(1, list.size()), seq.takeLast(4));
        assertIterableEquals(list.subList(2, list.size()), seq.takeLast(3));
        assertIterableEquals(list.subList(3, list.size()), seq.takeLast(2));
        assertIterableEquals(list.subList(4, list.size()), seq.takeLast(1));
    }

    @Test
    default void takeWhileTest() {
        assertIterableEquals(List.of(), of().takeWhile(o -> {
            fail();
            return true;
        }));

        var seq = from(List.of(0, 1, 2, 3, 4, 5, 6));
        assertIterableEquals(seq, seq.takeWhile(i -> true));
        assertIterableEquals(List.of(), seq.takeWhile(i -> i < 0));
        assertIterableEquals(List.of(0), seq.takeWhile(i -> i < 1));
        assertIterableEquals(List.of(0, 1), seq.takeWhile(i -> i < 2));
        assertIterableEquals(List.of(0, 1, 2), seq.takeWhile(i -> i < 3));
        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5), seq.takeWhile(i -> i < 6));
        assertIterableEquals(seq, seq.takeWhile(i -> i < 7));
    }

    @Test
    default void updatedTest() {
        {
            var empty = of();

            assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(0, "foo"));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(1, "foo"));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(~0, "foo"));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(Integer.MAX_VALUE, "foo"));
            assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(Integer.MIN_VALUE, "foo"));
        }

        {
            var seq = from(List.of("foo"));
            assertIterableEquals(List.of("foo"), seq.updated(0, "foo"));
            assertIterableEquals(List.of("bar"), seq.updated(0, "bar"));
            assertIterableEquals(List.of("foo"), seq.updated(~1, "foo"));
            assertIterableEquals(List.of("bar"), seq.updated(~1, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(1, "bar").toSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(~0, "bar").toSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(~2, "bar").toSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MAX_VALUE, "bar").toSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "bar").toSeq());
        }

        {
            var seq = from(List.of("foo", "bar"));
            assertIterableEquals(List.of("zzz", "bar"), seq.updated(0, "zzz"));
            assertIterableEquals(List.of("zzz", "bar"), seq.updated(~2, "zzz"));
            assertIterableEquals(List.of("foo", "zzz"), seq.updated(1, "zzz"));
            assertIterableEquals(List.of("foo", "zzz"), seq.updated(~1, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(2, "zzz").toSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "zzz").toSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(~0, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(~3, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "zzz").toSeq());
        }
    }

    @Test
    default void concatTest() {
        var empty = of();
        assertTrue(empty.concat(List.of()).isEmpty());
        assertTrue(empty.concat(ImmutableArray.empty()).isEmpty());
        assertTrue(empty.concat(ImmutableVector.empty()).isEmpty());

        assertIterableEquals(List.of("str1"), empty.concat(List.of("str1")));
        assertIterableEquals(List.of("str1"), empty.concat(ImmutableArray.of("str1")));
        assertIterableEquals(List.of("str1"), empty.concat(ImmutableVector.of("str1")));
        assertIterableEquals(List.of("str1", "str2", "str3"), empty.concat(ImmutableArray.of("str1", "str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), empty.concat(ImmutableVector.of("str1", "str2", "str3")));


        assertIterableEquals(List.of("str1"), of("str1").concat(List.of()));
        assertIterableEquals(List.of("str1"), of("str1").concat(ImmutableArray.empty()));
        assertIterableEquals(List.of("str1"), of("str1").concat(ImmutableVector.empty()));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(List.of("str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(ImmutableArray.of("str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(ImmutableVector.of("str2", "str3")));

        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(List.of()));
        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(ImmutableArray.empty()));
        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(ImmutableVector.empty()));

        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(List.of("str3", "str4")));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(ImmutableArray.of("str3", "str4")));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(ImmutableVector.of("str3", "str4")));

        // https://github.com/Glavo/kala-common/issues/28
        {
            final var fst = of(1).view().map(i -> i + 1);
            final var snd = of(2);
            final var res = fst.concat(snd).toSeq();
            assertIterableEquals(List.of(2, 2), res);
        }
    }

    @Test
    default void mapIndexedTest() {
        assertIterableEquals(List.of(), of().mapIndexed((i, v) -> i));
        assertIterableEquals(List.of(), of().mapIndexed((i, v) -> {
            throw new AssertionError();
        }));


        Integer[][] data1 = data1();
        String[][] data1s = data1s();

        for (int i = 0; i < data1.length; i++) {
            Integer[] d = data1[i];
            String[] ds = data1s[i];

            List<String> res = new ArrayList<>();
            for (int j = 0; j < d.length; j++) {
                res.add(j + ds[j]);
            }

            assertIterableEquals(res, from(d).mapIndexed((idx, v) -> String.format("%d%d", idx, v)));
        }
    }

    @Test
    default void mapIndexedNotNullTest() {
        assertIterableEquals(List.of(), of().mapIndexedNotNull((i, v) -> i));
        assertIterableEquals(List.of(), of().mapIndexedNotNull((i, v) -> {
            throw new AssertionError();
        }));

        Integer[][] data1 = data1();
        String[][] data1s = data1s();

        for (int i = 0; i < data1.length; i++) {
            Integer[] d = data1[i];
            String[] ds = data1s[i];

            {
                final Integer[] ta = new Integer[d.length];
                for (int j = 0; j < d.length; j++) {
                    ta[j] = d[j] + j;
                }
                assertIterableEquals(Arrays.asList(ta), from(d).mapIndexedNotNull(Integer::sum));
            }
            List<String> res = new ArrayList<>();
            for (int j = 0; j < d.length; j++) {
                if (d[j] > 0) {
                    res.add(j + ds[j]);
                }
            }

            assertIterableEquals(res, from(d).mapIndexedNotNull((idx, v) -> v > 0 ? String.format("%d%d", idx, v) : null));
        }
    }

    @Test
    default void sortedTest() {
        assertIterableEquals(List.of(), of().sorted());
        assertIterableEquals(List.of(), of().sorted(null));
        assertIterableEquals(List.of(), this.<Integer>of().sorted(Comparator.reverseOrder()));

        for (Integer[] data : data1()) {
            final List<Integer> res1 = Arrays.stream(data).sorted().collect(Collectors.toList());
            final List<Integer> res2 = Arrays.stream(data).sorted(Comparator.reverseOrder()).collect(Collectors.toList());

            assertIterableEquals(res1, from(data).sorted());
            assertIterableEquals(res1, from(data).sorted(null));
            assertIterableEquals(res2, from(data).sorted(Comparator.reverseOrder()));
        }

    }

    @Test
    default void collectTest() {
        assertEquals("", this.<String>of().collect(Collectors.joining()));
        assertEquals("str0|str1|str2", this.of("str0", "str1", "str2").collect(Collectors.joining("|")));

        assertIterableEquals(List.of(), this.<String>of().collect(Seq.factory()));
        assertIterableEquals(List.of("str0", "str1", "str2"), this.of("str0", "str1", "str2").collect(Seq.factory()));

        {
            MutableList<String> mutableList = MutableList.create();

            this.<String>of().collect(mutableList);
            assertIterableEquals(List.of(), mutableList);

            this.of("str0", "str1", "str2").collect(mutableList);
            assertIterableEquals(List.of("str0", "str1", "str2"), mutableList);
        }

        {
            java.util.List<String> list = new ArrayList<>();

            this.<String>of().collect(list);
            assertIterableEquals(List.of(), list);

            this.of("str0", "str1", "str2").collect(list);
            assertIterableEquals(List.of("str0", "str1", "str2"), list);
        }
    }

    @Test
    default void forEachIndexedTest() {
        ArrayList<Map.Entry<Integer, String>> al = new ArrayList<>();
        this.<String>of().forEachIndexed((i, v) -> al.add(Map.entry(i, v)));
        assertIterableEquals(List.of(), al);

        from(List.of("A", "B", "C")).forEachIndexed((i, v) -> al.add(Map.entry(i, v)));
        assertIterableEquals(List.of(Map.entry(0, "A"), Map.entry(1, "B"), Map.entry(2, "C")), al);
    }

    @Test
    default void forEachWithTest() {
        var result = new ArrayList<String>();
        BiConsumer<String, Integer> adder = (s, i) -> result.add(s + i);

        this.<String>of().forEachWith(of(1, 2, 3), adder);
        assertIterableEquals(List.of(), result);

        result.clear();
        of("A", "B", "C").forEachWith(of(), adder);
        assertIterableEquals(List.of(), result);

        result.clear();
        of("A", "B", "C").forEachWith(of(1), adder);
        assertIterableEquals(List.of("A1"), result);

        result.clear();
        of("A", "B", "C").forEachWith(of(1, 2, 3, 4), adder);
        assertIterableEquals(List.of("A1", "B2", "C3"), result);
    }

    @Test
    default void forEachCrossTest() {
        var result = new ArrayList<String>();
        BiConsumer<String, Integer> adder = (s, i) -> result.add(s + i);

        this.<String>of().forEachCross(of(1, 2, 3), adder);
        assertIterableEquals(List.of(), result);

        result.clear();
        of("A", "B", "C").forEachCross(of(), adder);
        assertIterableEquals(List.of(), result);

        result.clear();
        of("A", "B", "C").forEachCross(of(1), adder);
        assertIterableEquals(List.of("A1", "B1", "C1"), result);

        result.clear();
        of("A", "B", "C").forEachCross(of(1, 2, 3), adder);
        assertIterableEquals(List.of("A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3"), result);
    }
}
