package asia.kala.collection.immutable;

import asia.kala.collection.CollectionTestTemplate;
import asia.kala.factory.CollectionFactory;
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
}
