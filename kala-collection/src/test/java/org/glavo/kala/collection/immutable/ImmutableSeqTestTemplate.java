package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.SeqTestTemplate;
import org.glavo.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored", "rawtypes"})
public interface ImmutableSeqTestTemplate extends ImmutableCollectionTestTemplate, SeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableSeq<? extends E>> factory();

    @Test
    default void dropTest() {
        ImmutableSeq<?> empty = factory().empty();
        assertIterableEquals(List.of(), empty.drop(0));
        assertIterableEquals(List.of(), empty.drop(1));
        assertIterableEquals(List.of(), empty.drop(Integer.MAX_VALUE));
        assertIterableEquals(List.of(), empty.drop(-1));
        assertIterableEquals(List.of(), empty.drop(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        ImmutableSeq<String> seq = (ImmutableSeq<String>) factory().from(list);
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
        ImmutableSeq<?> empty = factory().empty();
        assertIterableEquals(List.of(), empty.dropLast(0));
        assertIterableEquals(List.of(), empty.dropLast(1));
        assertIterableEquals(List.of(), empty.dropLast(Integer.MAX_VALUE));
        assertIterableEquals(List.of(), empty.dropLast(-1));
        assertIterableEquals(List.of(), empty.dropLast(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        ImmutableSeq<String> seq = (ImmutableSeq<String>) factory().from(list);
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
        assertIterableEquals(List.of(), factory().empty().dropWhile(o -> {
            fail();
            return true;
        }));

        ImmutableSeq<Integer> seq = (ImmutableSeq<Integer>) factory().from(List.of(0, 1, 2, 3, 4, 5, 6));
        assertIterableEquals(List.of(), seq.dropWhile(i -> true));
        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6), seq.dropWhile(i -> i < 0));
        assertIterableEquals(List.of(1, 2, 3, 4, 5, 6), seq.dropWhile(i -> i < 1));
        assertIterableEquals(List.of(2, 3, 4, 5, 6), seq.dropWhile(i -> i < 2));
        assertIterableEquals(List.of(6), seq.dropWhile(i -> i < 6));
        assertIterableEquals(List.of(), seq.dropWhile(i -> i < 7));
    }

    @Test
    default void takeTest() {
        ImmutableSeq<?> empty = factory().empty();
        assertIterableEquals(List.of(), empty.take(0));
        assertIterableEquals(List.of(), empty.take(1));
        assertIterableEquals(List.of(), empty.take(Integer.MAX_VALUE));
        assertIterableEquals(List.of(), empty.take(-1));
        assertIterableEquals(List.of(), empty.take(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        ImmutableSeq<String> seq = (ImmutableSeq<String>) factory().from(list);
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
        ImmutableSeq<?> empty = factory().empty();
        assertIterableEquals(List.of(), empty.takeLast(0));
        assertIterableEquals(List.of(), empty.takeLast(1));
        assertIterableEquals(List.of(), empty.takeLast(Integer.MAX_VALUE));
        assertIterableEquals(List.of(), empty.takeLast(-1));
        assertIterableEquals(List.of(), empty.takeLast(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        ImmutableSeq<String> seq = (ImmutableSeq<String>) factory().from(list);
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
        assertIterableEquals(List.of(), factory().empty().takeWhile(o -> {
            fail();
            return true;
        }));

        ImmutableSeq<Integer> seq = (ImmutableSeq<Integer>) factory().from(List.of(0, 1, 2, 3, 4, 5, 6));
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
        CollectionFactory<String, ?, ImmutableSeq<String>> sf = (CollectionFactory) this.factory();

        ImmutableSeq<Object> empty = (ImmutableSeq<Object>) this.<Object>factory().empty();

        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(0, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(1, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(-1, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(Integer.MAX_VALUE, "foo"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.updated(Integer.MIN_VALUE, "foo"));

        {
            ImmutableSeq<String> seq = sf.from(List.of("foo"));
            assertIterableEquals(List.of("bar"), seq.updated(0, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(1, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(-1, "bar"));
        }

        {
            ImmutableSeq<String> seq = sf.from(List.of("foo", "bar"));
            assertIterableEquals(List.of("zzz", "bar"), seq.updated(0, "zzz"));
            assertIterableEquals(List.of("foo", "zzz"), seq.updated(1, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(2, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(-1, "zzz"));
        }
    }

}
