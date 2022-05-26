package kala.collection;

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
}
