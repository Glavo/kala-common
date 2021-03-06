package org.glavo.kala.collection.immutable;

import org.glavo.kala.tuple.Tuple;
import org.glavo.kala.tuple.Tuple2;
import org.glavo.kala.collection.CollectionTestTemplate;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SuppressWarnings({"unchecked", "rawtypes"})
public interface ImmutableCollectionTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableCollection<? extends E>> factory();

    @Test
    default void filterTest() {
        assertIterableEquals(List.of(), factory().empty().filter(o -> true));
        assertIterableEquals(List.of(), factory().empty().filter(o -> false));

        CollectionFactory<Integer, ?, ? extends ImmutableCollection<? extends Integer>> factory = factory();

        assertIterableEquals(
                List.of(),
                factory.from(List.of(-3, -2, -1, 0, 1, 2, 3)).filter(i -> i > 3)
        );

        assertIterableEquals(
                List.of(1, 2, 3),
                factory.from(List.of(-3, -2, -1, 0, 1, 2, 3)).filter(i -> i > 0)
        );

        assertIterableEquals(
                List.of(-3, -2, -1, 0, 1, 2, 3),
                factory.from(List.of(-3, -2, -1, 0, 1, 2, 3)).filter(i -> i > -4)
        );

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), factory.from(data).filter(i -> true));
            assertIterableEquals(List.of(), factory.from(data).filter(i -> false));
        }
    }

    @Test
    default void filterNotTest() {
        assertIterableEquals(List.of(), factory().empty().filterNot(o -> true));
        assertIterableEquals(List.of(), factory().empty().filterNot(o -> false));

        CollectionFactory<Integer, ?, ? extends ImmutableCollection<? extends Integer>> factory = factory();

        assertIterableEquals(
                List.of(-3, -2, -1, 0, 1, 2, 3),
                factory.from(List.of(-3, -2, -1, 0, 1, 2, 3)).filterNot(i -> i > 3)
        );

        assertIterableEquals(
                List.of(-3, -2, -1, 0),
                factory.from(List.of(-3, -2, -1, 0, 1, 2, 3)).filterNot(i -> i > 0)
        );

        assertIterableEquals(
                List.of(),
                factory.from(List.of(-3, -2, -1, 0, 1, 2, 3)).filterNot(i -> i > -4)
        );

        for (Integer[] data : data1()) {
            assertIterableEquals(List.of(), factory.from(data).filterNot(i -> true));
            assertIterableEquals(Arrays.asList(data), factory.from(data).filterNot(i -> false));
        }
    }

    @Test
    default void filterNotNullTest() {
        assertIterableEquals(List.of(), factory().empty().filterNotNull());
        assertIterableEquals(List.of(), factory().empty().filterNotNull());

        assertIterableEquals(List.of(), factory().from(Collections.singletonList(null)).filterNotNull());
        assertIterableEquals(List.of(), factory().from(Arrays.asList(null, null)).filterNotNull());
        assertIterableEquals(List.of(), factory().from(Arrays.asList(null, null, null)).filterNotNull());
        assertIterableEquals(List.of("foo"), factory().from(List.of("foo")).filterNotNull());
        assertIterableEquals(List.of("foo"), factory().from(Arrays.asList(null, "foo")).filterNotNull());
        assertIterableEquals(List.of("foo"), factory().from(Arrays.asList(null, "foo", null)).filterNotNull());
        assertIterableEquals(List.of("foo", "bar"), factory().from(Arrays.asList(null, "foo", null, "bar")).filterNotNull());

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), factory().from(data).filterNotNull(),
                    () -> String.format("expected: %s, actual: %s", Arrays.toString(data), factory().from(data).filterNotNull()));
        }
    }

    @Test
    default void mapTest() {
        assertIterableEquals(List.of(), factory().empty().map(i -> i));

        CollectionFactory<String, ?, ImmutableCollection<String>> sf = (CollectionFactory) factory();

        Integer[][] data1 = data1();
        String[][] data1s = data1s();

        for (int i = 0; i < data1.length; i++) {
            Integer[] d = data1[i];
            String[] ds = data1s[i];

            assertIterableEquals(Arrays.asList(d), sf.from(ds).map(Integer::parseInt));
        }
    }

    @Test
    default void flatMapTest() {
        assertIterableEquals(List.of(), factory().empty().flatMap(o -> List.of("foo")));

        CollectionFactory<Integer, ?, ? extends ImmutableCollection<? extends Integer>> factory = factory();

        assertIterableEquals(List.of(1, 2, 2), factory.from(List.of(0, 1, 2)).flatMap(i -> factory.fill(i, i)));
        assertIterableEquals(List.of(1, 2, 2, 3, 3, 3), factory.from(List.of(0, 1, 2, 3)).flatMap(i -> factory.fill(i, i)));
        assertIterableEquals(List.of(), factory.from(List.of(0, 1, 2, 3)).flatMap(i -> List.of()));
        assertIterableEquals(List.of(0, 1, 2, 3), factory.from(List.of(0, 1, 2, 3)).flatMap(List::of));
    }

    @Test
    default void zipTest() {
        assertIterableEquals(List.of(), factory().empty().zip(List.of()));
        assertIterableEquals(List.of(), factory().empty().zip(List.of("str")));

        assertIterableEquals(List.of(), factory().from(List.of(0)).zip(List.of()));

        Integer[][] data1 = data1();
        for (int i = 0; i < data1.length - 1; i++) {
            Integer[] d1 = data1[i];
            Integer[] d2 = data1[i + 1];

            Tuple2<Integer, Integer>[] d12 = new Tuple2[Math.min(d1.length, d2.length)];
            for (int j = 0; j < d12.length; j++) {
                d12[j] = Tuple.of(d1[j], d2[j]);
            }

            assertIterableEquals(Arrays.asList(d12), factory().from(d1).zip(Arrays.asList(d2)));
        }
    }
}
