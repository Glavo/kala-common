package kala.collection;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public interface SetLikeTestTemplate  extends CollectionLikeTestTemplate {
    default void assertSetElements(Iterable<?> expected, SetLike<?> actual) {
        int count = 0;
        for (Object value : expected) {
            count++;
            if (!actual.contains(value)) fail(actual + " does not contain element " + value);
        }

        if (count != actual.size()) fail(actual + " contains redundant elements");
    }

    @Override
    @SuppressWarnings("unchecked")
    <E> SetLike<E> of(E... elements);

    @Override
    <E> SetLike<E> from(E[] elements);

    @Override
    <E> SetLike<E> from(Iterable<? extends E> elements);

    @Test
    default void iteratorTest() {
        assertFalse(of().iterator().hasNext());
        assertThrows(NoSuchElementException.class, () -> of().iterator().next());

        List<String> values = List.of("str0", "str1", "str2", "str3", "str0", "str1");
        HashSet<String> expected = new HashSet<>(values);
        SetLike<String> set0 = from(values);

        int n = 0;
        for (var v : set0) {
            assertTrue(expected.contains(v), v);
            n++;
        }
        assertEquals(expected.size(), n);
    }
}
