package org.glavo.kala.collection;

import org.glavo.kala.collection.immutable.ImmutableArray;
import org.glavo.kala.collection.immutable.ImmutableLinkedSeq;
import org.glavo.kala.collection.immutable.ImmutableVector;
import org.glavo.kala.comparator.Comparators;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

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
            assertIterableEquals(Arrays.asList(data), this.<Integer>of().prependedAll(ImmutableLinkedSeq.from(data)));
        }

        for (int i = 0; i < data1().length - 1; i++) {
            final Integer[] data = data1()[i];
            final Integer[] data2 = data1()[i + 1];

            final ArrayList<Integer> tmp = new ArrayList<>(Arrays.asList(data));

            assertIterableEquals(tmp, this.<Integer>of().prependedAll(data));
            assertIterableEquals(tmp, this.<Integer>of().prependedAll(Arrays.asList(data)));
            assertIterableEquals(tmp, this.<Integer>of().prependedAll(ImmutableArray.from(data)));
            assertIterableEquals(tmp, this.<Integer>of().prependedAll(ImmutableLinkedSeq.from(data)));

            tmp.addAll(0, Arrays.asList(data2));

            assertIterableEquals(tmp, from(data).prependedAll(data2));
            assertIterableEquals(tmp, from(data).prependedAll(Arrays.asList(data2)));
            assertIterableEquals(tmp, from(data).prependedAll(ImmutableArray.from(data2)));
            assertIterableEquals(tmp, from(data).prependedAll(ImmutableLinkedSeq.from(data2)));
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

            assertIterableEquals(al, from(data).appended(12345));
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
            assertIterableEquals(Arrays.asList(data), this.<Integer>of().appendedAll(ImmutableLinkedSeq.from(data)));
        }

        for (int i = 0; i < data1().length - 1; i++) {
            final Integer[] data = data1()[i];
            final Integer[] data2 = data1()[i + 1];

            final ArrayList<Integer> tmp = new ArrayList<>(Arrays.asList(data));

            assertIterableEquals(tmp, this.<Integer>of().appendedAll(data));
            assertIterableEquals(tmp, this.<Integer>of().appendedAll(Arrays.asList(data)));
            assertIterableEquals(tmp, this.<Integer>of().appendedAll(ImmutableArray.from(data)));
            assertIterableEquals(tmp, this.<Integer>of().appendedAll(ImmutableLinkedSeq.from(data)));

            tmp.addAll(Arrays.asList(data2));

            assertIterableEquals(tmp, from(data).appendedAll(data2));
            assertIterableEquals(tmp, from(data).appendedAll(Arrays.asList(data2)));
            assertIterableEquals(tmp, from(data).appendedAll(ImmutableArray.from(data2)));
            assertIterableEquals(tmp, from(data).appendedAll(ImmutableLinkedSeq.from(data2)));
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
        var seq = from(list);
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
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(1, "bar").toImmutableLinkedSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(-1, "bar").toImmutableLinkedSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MAX_VALUE, "bar").toImmutableLinkedSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "bar").toImmutableLinkedSeq());
        }

        {
            var seq = from(List.of("foo", "bar"));
            assertIterableEquals(List.of("zzz", "bar"), seq.updated(0, "zzz"));
            assertIterableEquals(List.of("foo", "zzz"), seq.updated(1, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(2, "zzz").toImmutableLinkedSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "zzz").toImmutableLinkedSeq());
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(-1, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(Integer.MIN_VALUE, "zzz").toImmutableLinkedSeq());
        }
    }

    @Test
    default void concatTest() {
        var empty = of();
        assertTrue(empty.concat(List.of()).isEmpty());
        assertTrue(empty.concat(ImmutableLinkedSeq.empty()).isEmpty());
        assertTrue(empty.concat(ImmutableArray.empty()).isEmpty());
        assertTrue(empty.concat(ImmutableVector.empty()).isEmpty());

        assertIterableEquals(List.of("str1"), empty.concat(List.of("str1")));
        assertIterableEquals(List.of("str1"), empty.concat(ImmutableLinkedSeq.of("str1")));
        assertIterableEquals(List.of("str1"), empty.concat(ImmutableArray.of("str1")));
        assertIterableEquals(List.of("str1"), empty.concat(ImmutableVector.of("str1")));
        assertIterableEquals(List.of("str1", "str2", "str3"), empty.concat(ImmutableLinkedSeq.of("str1", "str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), empty.concat(ImmutableArray.of("str1", "str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), empty.concat(ImmutableVector.of("str1", "str2", "str3")));


        assertIterableEquals(List.of("str1"), of("str1").concat(List.of()));
        assertIterableEquals(List.of("str1"), of("str1").concat(ImmutableLinkedSeq.empty()));
        assertIterableEquals(List.of("str1"), of("str1").concat(ImmutableArray.empty()));
        assertIterableEquals(List.of("str1"), of("str1").concat(ImmutableVector.empty()));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(ImmutableLinkedSeq.of("str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(List.of("str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(ImmutableArray.of("str2", "str3")));
        assertIterableEquals(List.of("str1", "str2", "str3"), of("str1").concat(ImmutableVector.of("str2", "str3")));

        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(List.of()));
        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(ImmutableLinkedSeq.empty()));
        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(ImmutableArray.empty()));
        assertIterableEquals(List.of("str1", "str2"), of("str1", "str2").concat(ImmutableVector.empty()));

        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(List.of("str3", "str4")));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(ImmutableLinkedSeq.of("str3", "str4")));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(ImmutableArray.of("str3", "str4")));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), of("str1", "str2").concat(ImmutableVector.of("str3", "str4")));

        // https://github.com/Glavo/kala-common/issues/28
        {
            final var fst = of(1).view().map(i -> i + 1);
            final var snd = of(2);
            final var res = fst.concat(snd).toImmutableSeq();
            assertIterableEquals(
                    List.of(2, 2),
                    res
            );
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
        assertIterableEquals(List.of(), of().sorted(Comparators.reverseOrder()));

        for (Integer[] data : data1()) {
            final List<Integer> res1 = Arrays.stream(data).sorted().collect(Collectors.toList());
            final List<Integer> res2 = Arrays.stream(data).sorted(Comparators.reverseOrder()).collect(Collectors.toList());

            assertIterableEquals(res1, from(data).sorted());
            assertIterableEquals(res1, from(data).sorted(null));
            assertIterableEquals(res2, from(data).sorted(Comparators.reverseOrder()));
        }

    }
}
