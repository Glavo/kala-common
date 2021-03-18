package org.glavo.kala.collection;

import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.immutable.ImmutableCollection;
import org.glavo.kala.tuple.Tuple;
import org.glavo.kala.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SuppressWarnings("unchecked")
public interface FullCollectionLikeTestTemplate extends CollectionLikeTestTemplate {
    @Override
    @SuppressWarnings("unchecked")
    <E> FullCollectionLike<E> of(E... elements);

    @Override
    <E> FullCollectionLike<E> from(E[] elements);

    @Override
    <E> FullCollectionLike<E> from(Iterable<? extends E> elements);

    @Test
    default void filterTest() {
        assertIterableEquals(List.of(), of().filter(o -> true));
        assertIterableEquals(List.of(), of().filter(o -> false));

        assertIterableEquals(
                List.of(),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filter(i -> i > 3)
        );

        assertIterableEquals(
                List.of(1, 2, 3),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filter(i -> i > 0)
        );

        assertIterableEquals(
                List.of(-3, -2, -1, 0, 1, 2, 3),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filter(i -> i > -4)
        );

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), from(data).filter(i -> true));
            assertIterableEquals(List.of(), from(data).filter(i -> false));
        }
    }

    @Test
    default void filterNotTest() {
        assertIterableEquals(List.of(), of().filterNot(o -> true));
        assertIterableEquals(List.of(), of().filterNot(o -> false));

        assertIterableEquals(
                List.of(-3, -2, -1, 0, 1, 2, 3),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filterNot(i -> i > 3)
        );

        assertIterableEquals(
                List.of(-3, -2, -1, 0),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filterNot(i -> i > 0)
        );

        assertIterableEquals(
                List.of(),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filterNot(i -> i > -4)
        );

        for (Integer[] data : data1()) {
            assertIterableEquals(List.of(), from(data).filterNot(i -> true));
            assertIterableEquals(Arrays.asList(data), from(data).filterNot(i -> false));
        }
    }

    @Test
    default void filterNotNullTest() {
        assertIterableEquals(List.of(), of().filterNotNull());
        assertIterableEquals(List.of(), of().filterNotNull());

        assertIterableEquals(List.of(), from(Collections.singletonList(null)).filterNotNull());
        assertIterableEquals(List.of(), from(Arrays.asList(null, null)).filterNotNull());
        assertIterableEquals(List.of(), from(Arrays.asList(null, null, null)).filterNotNull());
        assertIterableEquals(List.of("foo"), from(List.of("foo")).filterNotNull());
        assertIterableEquals(List.of("foo"), from(Arrays.asList(null, "foo")).filterNotNull());
        assertIterableEquals(List.of("foo"), from(Arrays.asList(null, "foo", null)).filterNotNull());
        assertIterableEquals(List.of("foo", "bar"), from(Arrays.asList(null, "foo", null, "bar")).filterNotNull());

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), from(data).filterNotNull(),
                    () -> String.format("expected: %s, actual: %s", Arrays.toString(data), from(data).filterNotNull()));
        }
    }

    @Test
    default void mapTest() {
        assertIterableEquals(List.of(), of().map(i -> i));


        Integer[][] data1 = data1();
        String[][] data1s = data1s();

        for (int i = 0; i < data1.length; i++) {
            Integer[] d = data1[i];
            String[] ds = data1s[i];

            assertIterableEquals(Arrays.asList(d), from(ds).map(Integer::parseInt));
        }
    }

    @Test
    default void flatMapTest() {
        assertIterableEquals(List.of(), of().flatMap(o -> List.of("foo")));

        assertIterableEquals(List.of(1, 2, 2), from(List.of(0, 1, 2)).flatMap(i -> Collections.nCopies(i, i)));
        assertIterableEquals(List.of(1, 2, 2, 3, 3, 3), from(List.of(0, 1, 2, 3)).flatMap(i -> Collections.nCopies(i, i)));
        assertIterableEquals(List.of(), from(List.of(0, 1, 2, 3)).flatMap(i -> List.of()));
        assertIterableEquals(List.of(0, 1, 2, 3), from(List.of(0, 1, 2, 3)).flatMap(List::of));
    }

    @Test
    default void zipTest() {
        assertIterableEquals(List.of(), of().zip(List.of()));
        assertIterableEquals(List.of(), of().zip(List.of("str")));

        assertIterableEquals(List.of(), from(List.of(0)).zip(List.of()));

        Integer[][] data1 = data1();
        for (int i = 0; i < data1.length - 1; i++) {
            Integer[] d1 = data1[i];
            Integer[] d2 = data1[i + 1];

            Tuple2<Integer, Integer>[] d12 = new Tuple2[Math.min(d1.length, d2.length)];
            for (int j = 0; j < d12.length; j++) {
                d12[j] = Tuple.of(d1[j], d2[j]);
            }

            assertIterableEquals(Arrays.asList(d12), from(d1).zip(Arrays.asList(d2)));
        }
    }
}
