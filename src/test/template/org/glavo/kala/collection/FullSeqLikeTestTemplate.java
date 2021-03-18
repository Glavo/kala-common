package org.glavo.kala.collection;

import org.glavo.kala.collection.immutable.ImmutableArray;
import org.glavo.kala.collection.immutable.ImmutableList;
import org.glavo.kala.collection.immutable.ImmutableVector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public interface FullSeqLikeTestTemplate extends FullCollectionLikeTestTemplate, SeqLikeTestTemplate {
    @Override
    <E> FullSeqLike<E> of(E... elements);

    @Override
    <E> FullSeqLike<E> from(E[] elements);

    @Override
    <E> FullSeqLike<E> from(Iterable<? extends E> elements);

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
    default void sliceTest() {
        assertIterableEquals(List.of(), of().slice(0, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(0, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(Integer.MIN_VALUE, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> of().slice(Integer.MIN_VALUE, 1));

        for (Integer[] data : data1()) {
            int dl = data.length;
            assertIterableEquals(Arrays.asList(data), from(data).slice(0, data.length));

            if (dl >= 1) {
                assertIterableEquals(
                        Arrays.asList(Arrays.copyOfRange(data, 1, dl)),
                        from(data).slice(1, dl)
                );
                assertIterableEquals(
                        Arrays.asList(Arrays.copyOfRange(data, 0, dl - 1)),
                        from(data).slice(0, dl - 1)
                );
            }
        }
    }

    @Test
    default void dropTest() {
        var empty = of();
        assertIterableEquals(List.of(), empty.drop(0));
        assertIterableEquals(List.of(), empty.drop(1));
        assertIterableEquals(List.of(), empty.drop(Integer.MAX_VALUE));
        assertIterableEquals(List.of(), empty.drop(-1));
        assertIterableEquals(List.of(), empty.drop(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        var seq = from(list);
        assertIterableEquals(list, seq.drop(0));
        assertIterableEquals(list, seq.drop(-1));
        assertIterableEquals(list, seq.drop(Integer.MIN_VALUE));
        assertIterableEquals(list.subList(1, list.size()), seq.drop(1));
        assertIterableEquals(list.subList(2, list.size()), seq.drop(2));
        assertIterableEquals(list.subList(3, list.size()), seq.drop(3));
        assertIterableEquals(list.subList(4, list.size()), seq.drop(4));
        assertIterableEquals(List.of(), seq.drop(5));
    }

    @Test
    default void dropLastTest() {
        var empty = of();
        assertIterableEquals(List.of(), empty.dropLast(0));
        assertIterableEquals(List.of(), empty.dropLast(1));
        assertIterableEquals(List.of(), empty.dropLast(Integer.MAX_VALUE));
        assertIterableEquals(List.of(), empty.dropLast(-1));
        assertIterableEquals(List.of(), empty.dropLast(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        var seq = from(list);
        assertIterableEquals(list, seq.dropLast(0));
        assertIterableEquals(list, seq.dropLast(-1));
        assertIterableEquals(list, seq.dropLast(Integer.MIN_VALUE));
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
        assertIterableEquals(List.of(), empty.take(-1));
        assertIterableEquals(List.of(), empty.take(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        var seq =  from(list);
        assertIterableEquals(List.of(), seq.take(0));
        assertIterableEquals(List.of(), seq.take(-1));
        assertIterableEquals(List.of(), seq.take(Integer.MIN_VALUE));
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
        assertIterableEquals(List.of(), empty.takeLast(0));
        assertIterableEquals(List.of(), empty.takeLast(1));
        assertIterableEquals(List.of(), empty.takeLast(Integer.MAX_VALUE));
        assertIterableEquals(List.of(), empty.takeLast(-1));
        assertIterableEquals(List.of(), empty.takeLast(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        var seq = from(list);
        assertIterableEquals(List.of(), seq.takeLast(0));
        assertIterableEquals(List.of(), seq.takeLast(-1));
        assertIterableEquals(List.of(), seq.takeLast(Integer.MIN_VALUE));
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
        var empty = of();

        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(0, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(1, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(-1, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(Integer.MAX_VALUE, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(Integer.MIN_VALUE, "foo"));

        {
            var seq = from(List.of("foo"));
            assertIterableEquals(List.of("bar"), seq.updated(0, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(1, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(-1, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MAX_VALUE, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "bar"));
        }

        {
            var seq = from(List.of("foo", "bar"));
            assertIterableEquals(List.of("zzz", "bar"), seq.updated(0, "zzz"));
            assertIterableEquals(List.of("foo", "zzz"), seq.updated(1, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(2, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(-1, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "zzz"));
        }
    }

    @Test
    default void concatTest() {
        var empty = of();
        assertTrue(empty.concat(ImmutableList.empty()).isEmpty());
        assertTrue(empty.concat(ImmutableArray.empty()).isEmpty());
        assertTrue(empty.concat(ImmutableVector.empty()).isEmpty());

        assertIterableEquals(List.of("str1"), empty.concat(ImmutableList.of("str1")));
        assertIterableEquals(List.of("str1"), empty.concat(ImmutableArray.of("str1")));
        assertIterableEquals(List.of("str1"), empty.concat(ImmutableVector.of("str1")));
        assertIterableEquals(List.of("str1", "str2", "str3"), empty.concat(ImmutableList.of("str1", "str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), empty.concat(ImmutableArray.of("str1", "str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), empty.concat(ImmutableVector.of("str1", "str2", "str3")));


        assertIterableEquals(List.of("str1"), of("str1").concat(ImmutableList.empty()));
        assertIterableEquals(List.of("str1"), of("str1").concat(ImmutableArray.empty()));
        assertIterableEquals(List.of("str1"), of("str1").concat(ImmutableVector.empty()));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(ImmutableList.of("str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(ImmutableArray.of("str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(ImmutableVector.of("str2", "str3")));

        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(ImmutableList.empty()));
        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(ImmutableArray.empty()));
        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(ImmutableVector.empty()));

        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(ImmutableList.of("str3", "str4")));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(ImmutableArray.of("str3", "str4")));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(ImmutableVector.of("str3", "str4")));
    }
}
