package org.glavo.kala.collection;

import org.glavo.kala.collection.immutable.ImmutableList;
import org.glavo.kala.factory.CollectionFactory;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.base.JavaArray;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

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
        assertIterableEquals(List.of(), factory.empty());
        assertIterableEquals(List.of(), factory.from(JavaArray.EMPTY_OBJECT_ARRAY));
        assertIterableEquals(List.of(), factory.from(java.util.List.of()));
        assertIterableEquals(List.of(), factory.from(new Object[]{}));
        assertIterableEquals(List.of(), factory.from(Iterators.empty()));

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), factory.from(data));
            assertIterableEquals(Arrays.asList(data), factory.from(Arrays.asList(data)));
            assertIterableEquals(Arrays.asList(data), factory.from(Arrays.asList(data).iterator()));
        }

        assertIterableEquals(List.of(), factory.build(factory.newBuilder()));

        for (Integer[] data : data1()) {
            Object builder = factory.newBuilder();
            for (Integer i : data) {
                factory.addToBuilder(builder, i);
            }
            assertIterableEquals(Arrays.asList(data), factory.build(builder));
        }

        assertIterableEquals(List.of(), factory.fill(0, "foo"));
        assertIterableEquals(List.of(), factory.fill(-1, "foo"));
        assertIterableEquals(List.of(), factory.fill(Integer.MIN_VALUE, "foo"));

        assertIterableEquals(List.of(), factory.fill(0, () -> "foo"));
        assertIterableEquals(List.of(), factory.fill(-1, () -> "foo"));
        assertIterableEquals(List.of(), factory.fill(Integer.MIN_VALUE, () -> "foo"));

        assertIterableEquals(List.of(), factory.fill(0, i -> "foo"));
        assertIterableEquals(List.of(), factory.fill(-1, i -> "foo"));
        assertIterableEquals(List.of(), factory.fill(Integer.MIN_VALUE, i -> "foo"));

        assertIterableEquals(List.of("foo"), factory.fill(1, "foo"));
        assertIterableEquals(List.of("foo", "foo"), factory.fill(2, "foo"));
        assertIterableEquals(List.of("foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo", "foo"), factory.fill(10, "foo"));


        assertIterableEquals(List.of("foo"), factory.fill(1, () -> "foo"));
        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                factory.fill(10, new Supplier<>() {
                    int i = 0;

                    @Override
                    public Object get() {
                        return i++;
                    }
                })
        );

        assertIterableEquals(List.of("foo: 0"), factory.fill(1, i -> "foo: " + i));
        assertIterableEquals(List.of("foo: 0", "foo: 1"), factory.fill(2, i -> "foo: " + i));
        assertIterableEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), factory.fill(10, i -> i));

        Object b1;
        Object b2;

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b1, List.of("foo", "bar"));
            assertIterableEquals(List.of("foo", "bar"), factory.build(factory.mergeBuilder(b1, b2)));
        }

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b2, List.of("foo", "bar"));
            assertIterableEquals(List.of("foo", "bar"), factory.build(factory.mergeBuilder(b1, b2)));
        }

        {
            b1 = factory.newBuilder();
            b2 = factory.newBuilder();
            factory.addAllToBuilder(b1, List.of("foo", "bar"));
            factory.addAllToBuilder(b2, List.of("A", "B"));
            assertIterableEquals(List.of("foo", "bar", "A", "B"), factory.build(factory.mergeBuilder(b1, b2)));
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
        assertTrue(factory().empty().knownSize() == 0 || factory().empty().knownSize() == -1);

        for (Integer[] data : data1()) {
            int ks = factory().from(data).knownSize();
            assertTrue(ks == data.length || ks == -1);
        }
    }

    @Test
    default void sizeCompareTest() {
        assertEquals(0, factory().empty().sizeCompare(0));
        assertTrue(factory().empty().sizeCompare(1) < 0);
        assertTrue(factory().empty().sizeCompare(Integer.MAX_VALUE) < 0);
        assertTrue(factory().empty().sizeCompare(-1) > 0);
        assertTrue(factory().empty().sizeCompare(Integer.MIN_VALUE) > 0);

        for (Integer[] data : data1()) {
            Collection<?> c = factory().from(data);
            List<Integer> dl = Arrays.asList(data);

            assertEquals(0, c.sizeCompare(data.length));
            assertEquals(0, c.sizeCompare(dl));
            assertEquals(0, c.sizeCompare(new SimpleIterable<>(dl)));
            assertEquals(0, c.sizeCompare(ArraySeq.wrap(data)));
            assertEquals(0, c.sizeCompare(ImmutableList.from(data)));

            List<Integer> dl1 = dl.subList(0, data.length - 1);

            assertTrue(c.sizeCompare(data.length - 1) > 0);
            assertTrue(c.sizeCompare(dl1) > 0);
            assertTrue(c.sizeCompare(new SimpleIterable<>(dl1)) > 0);
            assertTrue(c.sizeCompare(ArraySeq.from(dl1)) > 0);
            assertTrue(c.sizeCompare(ImmutableList.from(dl1)) > 0);


            ArrayList<Integer> dl2 = new ArrayList<>(dl);
            dl2.add(0);

            assertTrue(c.sizeCompare(data.length + 1) < 0);
            assertTrue(c.sizeCompare(dl2) < 0);
            assertTrue(c.sizeCompare(new SimpleIterable<>(dl2)) < 0);
            assertTrue(c.sizeCompare(ArraySeq.from(dl2)) < 0);
            assertTrue(c.sizeCompare(ImmutableList.from(dl2)) < 0);
        }
    }

    @Test
    default void containsTest() {
        Collection<?> empty = factory().empty();
        assertFalse(empty.contains(null));
        assertFalse(empty.contains(0));

        Collection<?> c = factory().from(Arrays.asList(null, 0, 1, 2));
        assertTrue(c.contains(null));
        assertTrue(c.contains(0));
        assertTrue(c.contains(1));
        assertTrue(c.contains(2));
        assertFalse(c.contains(3));
        assertFalse(c.contains("foo"));

        for (Integer[] data : data1()) {
            c = this.<Integer>factory().from(data);
            assertFalse(c.contains(0));
            assertFalse(c.contains(null));
            for (int d : data) {
                assertTrue(c.contains(d));
                assertFalse(c.contains(-d));
            }
        }
    }

    @Test
    default void anyMatchTest() {
        assertFalse(factory().empty().anyMatch(e -> true));
        assertFalse(factory().empty().anyMatch(e -> false));

        assertFalse(this.<Integer>factory().from(List.of(0, 1, 2, 3)).anyMatch(i -> i < 0));
        assertTrue(this.<Integer>factory().from(List.of(0, 1, 2, 3)).anyMatch(i -> i > 0));
        assertFalse(this.<Integer>factory().from(List.of(0, 1, 2, 3)).anyMatch(i -> i > 3));
    }

    @Test
    default void allMatchTest() {
        assertTrue(factory().empty().allMatch(e -> true));
        assertTrue(factory().empty().allMatch(e -> false));

        assertFalse(this.<Integer>factory().from(List.of(0, 1, 2, 3)).allMatch(i -> i < 0));
        assertFalse(this.<Integer>factory().from(List.of(0, 1, 2, 3)).allMatch(i -> i > 0));
        assertTrue(this.<Integer>factory().from(List.of(0, 1, 2, 3)).allMatch(i -> i >= 0));
        assertFalse(this.<Integer>factory().from(List.of(0, 1, 2, 3)).allMatch(i -> i > 3));
    }

    @Test
    default void noneMatchTest() {
        assertTrue(factory().empty().noneMatch(e -> true));
        assertTrue(factory().empty().noneMatch(e -> false));

        assertTrue(this.<Integer>factory().from(List.of(0, 1, 2, 3)).noneMatch(i -> i < 0));
        assertFalse(this.<Integer>factory().from(List.of(0, 1, 2, 3)).noneMatch(i -> i > 0));
        assertFalse(this.<Integer>factory().from(List.of(0, 1, 2, 3)).noneMatch(i -> i >= 0));
        assertTrue(this.<Integer>factory().from(List.of(0, 1, 2, 3)).noneMatch(i -> i > 3));
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

    @Test
    default void forEachTest() {
        ArrayList<Object> al = new ArrayList<>();
        factory().empty().forEach(al::add);
        assertIterableEquals(List.of(), al);

        factory().from(List.of(0, 1, 2)).forEach(al::add);
        assertIterableEquals(List.of(0, 1, 2), al);
        al.clear();

        for (Integer[] data : data1()) {
            factory().from(data).forEach(al::add);
            assertIterableEquals(Arrays.asList(data), al);
            al.clear();
        }
    }

    @Test
    default void serializationTest() throws IOException, ClassNotFoundException {
        try {
            for (Integer[] data : data1()) {
                Collection<?> c = factory().from(data);
                ByteArrayOutputStream out = new ByteArrayOutputStream(4 * 128);
                new ObjectOutputStream(out).writeObject(c);
                byte[] buffer = out.toByteArray();
                ByteArrayInputStream in = new ByteArrayInputStream(buffer);
                Object obj = new ObjectInputStream(in).readObject();
                assertIterableEquals(c, (Iterable<?>) obj);
            }
        } catch (NotSerializableException ignored) {
        }
    }
}
