package asia.kala.collection.immutable;

import asia.kala.collection.SeqTestTemplate;
import asia.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static asia.kala.collection.Assertions.*;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored", "rawtypes"})
public interface ImmutableSeqTestTemplate extends ImmutableCollectionTestTemplate, SeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableSeq<? extends E>> factory();

    @Test
    default void dropTest() {
        ImmutableSeq<?> empty = factory().empty();
        assertIsEmpty(empty.drop(0));
        assertIsEmpty(empty.drop(1));
        assertIsEmpty(empty.drop(Integer.MAX_VALUE));
        assertIsEmpty(empty.drop(-1));
        assertIsEmpty(empty.drop(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        ImmutableSeq<String> seq = (ImmutableSeq<String>) factory().from(list);
        assertIterableEquals(list, seq.drop(0));
        assertIterableEquals(list, seq.drop(-1));
        assertIterableEquals(list, seq.drop(Integer.MIN_VALUE));
        assertIterableEquals(list.subList(1, list.size()), seq.drop(1));
        assertIterableEquals(list.subList(2, list.size()), seq.drop(2));
        assertIterableEquals(list.subList(3, list.size()), seq.drop(3));
        assertIterableEquals(list.subList(4, list.size()), seq.drop(4));
        assertIsEmpty(seq.drop(5));
    }

    @Test
    default void dropLastTest() {
        ImmutableSeq<?> empty = factory().empty();
        assertIsEmpty(empty.dropLast(0));
        assertIsEmpty(empty.dropLast(1));
        assertIsEmpty(empty.dropLast(Integer.MAX_VALUE));
        assertIsEmpty(empty.dropLast(-1));
        assertIsEmpty(empty.dropLast(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        ImmutableSeq<String> seq = (ImmutableSeq<String>) factory().from(list);
        assertIterableEquals(list, seq.dropLast(0));
        assertIterableEquals(list, seq.dropLast(-1));
        assertIterableEquals(list, seq.dropLast(Integer.MIN_VALUE));
        assertIterableEquals(list.subList(0, list.size() - 1), seq.dropLast(1));
        assertIterableEquals(list.subList(0, list.size() - 2), seq.dropLast(2));
        assertIterableEquals(list.subList(0, list.size() - 3), seq.dropLast(3));
        assertIterableEquals(list.subList(0, list.size() - 4), seq.dropLast(4));
        assertIsEmpty(seq.dropLast(5));
    }

    @Test
    default void takeTest() {
        ImmutableSeq<?> empty = factory().empty();
        assertIsEmpty(empty.take(0));
        assertIsEmpty(empty.take(1));
        assertIsEmpty(empty.take(Integer.MAX_VALUE));
        assertIsEmpty(empty.take(-1));
        assertIsEmpty(empty.take(Integer.MIN_VALUE));

        List<String> list = List.of("str1", "str2", "str3", "str4", "str5");
        ImmutableSeq<String> seq = (ImmutableSeq<String>) factory().from(list);
        assertIsEmpty(seq.take(0));
        assertIsEmpty(seq.take(-1));
        assertIsEmpty(seq.take(Integer.MIN_VALUE));
        assertIterableEquals(list, seq.take(Integer.MAX_VALUE));
        assertIterableEquals(list, seq.take(5));
        assertIterableEquals(list.subList(0, 4), seq.take(4));
        assertIterableEquals(list.subList(0, 3), seq.take(3));
        assertIterableEquals(list.subList(0, 2), seq.take(2));
        assertIterableEquals(list.subList(0, 1), seq.take(1));
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
            assertElements(seq.updated(0, "bar"), "bar");
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(1, "bar"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(-1, "bar"));
        }

        {
            ImmutableSeq<String> seq = sf.from(List.of("foo", "bar"));
            assertElements(seq.updated(0, "zzz"), "zzz", "bar");
            assertElements(seq.updated(1, "zzz"), "foo", "zzz");
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(2, "zzz"));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.updated(-1, "zzz"));
        }
    }
}
