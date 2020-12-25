package asia.kala.collection;

import asia.kala.factory.CollectionFactory;
import asia.kala.iterator.Iterators;
import asia.kala.traversable.JavaArray;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static asia.kala.collection.Assertions.*;

@SuppressWarnings("unchecked")
public interface CollectionTestTemplate {

    default Integer[][] data1() {
        return TestData.data1;
    }

    default String[][] data1s() {
        return TestData.data1s;
    }

    <E> CollectionFactory<E, ?, ? extends Collection<? extends E>> factory();

    @Test
    default void factoryTest() {
        CollectionFactory<Object, Object, Collection<?>> factory =
                (CollectionFactory<Object, Object, Collection<?>>) factory();
        assertIsEmpty(factory.empty());
        assertIsEmpty(factory.from(JavaArray.EMPTY_OBJECT_ARRAY));
        assertIsEmpty(factory.from(java.util.List.of()));
        assertIsEmpty(factory.from(new Object[]{}));
        assertIsEmpty(factory.from(Iterators.empty()));

        for (Integer[] data : data1()) {
            assertElements(factory.from(data), ((Object[]) data));
            assertElements(factory.from(Arrays.asList(data)), ((Object[]) data));
            assertElements(factory.from(Arrays.asList(data).iterator()), ((Object[]) data));
        }

        assertIsEmpty(factory.build(factory.newBuilder()));

        for (Integer[] data : data1()) {
            Object builder = factory.newBuilder();
            for (Integer i : data) {
                factory.addToBuilder(builder, i);
            }
            assertElements(factory.build(builder), (Object[]) data);
        }

        assertIsEmpty(factory.fill(0, "foo"));
        assertIsEmpty(factory.fill(-1, "foo"));
        assertIsEmpty(factory.fill(Integer.MIN_VALUE, "foo"));

        assertIsEmpty(factory.fill(0, () -> "foo"));
        assertIsEmpty(factory.fill(-1, () -> "foo"));
        assertIsEmpty(factory.fill(Integer.MIN_VALUE, () -> "foo"));

        assertIsEmpty(factory.fill(0, i -> "foo"));
        assertIsEmpty(factory.fill(-1, i -> "foo"));
        assertIsEmpty(factory.fill(Integer.MIN_VALUE, i -> "foo"));

        assertElements(factory.fill(1, "foo"), "foo");
        assertElements(factory.fill(2, "foo"), "foo", "foo");
        assertElements(factory.fill(10, "foo"), "foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo");


        assertElements(factory.fill(1, () -> "foo"), "foo");
        assertElements(factory.fill(10, new Supplier<Object>() {
            int i = 0;
            @Override
            public Object get() {
                return i++;
            }
        }), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        assertElements(factory.fill(1, i -> "foo: " + i), "foo: 0");
        assertElements(factory.fill(2, i -> "foo: " + i), "foo: 0", "foo: 1");
        assertElements(factory.fill(10, i -> i), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Object b1;
        Object b2;

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b1, List.of("foo", "bar"));
            assertElements(factory.build(factory.mergeBuilder(b1, b2)), "foo", "bar");
        }

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b2, List.of("foo", "bar"));
            assertElements(factory.build(factory.mergeBuilder(b1, b2)), "foo", "bar");
        }

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b1, List.of("foo", "bar"));
            factory.addAllToBuilder(b2, List.of("A", "B"));
            assertElements(factory.build(factory.mergeBuilder(b1, b2)), "foo", "bar", "A", "B");
        }
    }

    @Test
    default void isEmptyTest() {
        assertTrue(factory().empty().isEmpty());
        assertTrue(factory().from(JavaArray.EMPTY_OBJECT_ARRAY).isEmpty());
        assertTrue(factory().from(Collections.emptyList()).isEmpty());

        for (Integer[] data : data1()) {
            assertEquals(data.length == 0, factory().from(data).isEmpty());
        }
    }

    @Test
    default void sizeTest() {
        assertEquals(0, factory().empty().size());
        for (Integer[] data : data1()) {
            assertEquals(data.length, factory().from(data).size());
        }
    }

    @Test
    default void knownSizeTest() {
        assertEquals(0, factory().empty().knownSize());

        for (Integer[] data : data1()) {
            int ks = factory().from(data).knownSize();
            assertTrue(ks == data.length || ks == -1);
        }
    }

    @Test
    default void containsTest() {
        for (Integer[] data : data1()) {
            Collection<? extends Integer> c = this.<Integer>factory().from(data);
            assertFalse(c.contains(0));
            for (int d : data) {
                assertTrue(c.contains(d));
                assertFalse(c.contains(-d));
            }
        }
    }

    @Test
    default void toArrayTest() {
        assertArrayEquals(JavaArray.EMPTY_OBJECT_ARRAY, factory().empty().toArray());
        for (Integer[] data : data1()) {
            Object[] oa = factory().from(data).toArray();
            assertSame(Object[].class, oa.getClass());
            assertArrayEquals(data, oa);

            for (Integer[] ia : List.of(
                    factory().from(data).toArray(Integer[]::new),
                    factory().from(data).toArray(Integer.class),
                    factory().from(data).toArray(new Integer[data.length])
            )) {
                assertSame(Integer[].class, ia.getClass());
                assertArrayEquals(data, ia);
            }
        }
    }
}
