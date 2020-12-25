package asia.kala.collection;

import asia.kala.factory.CollectionFactory;
import asia.kala.traversable.JavaArray;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static asia.kala.collection.Assertions.*;

@SuppressWarnings("unchecked")
public interface CollectionTestTemplate {

    <E> CollectionFactory<E, ?, ? extends Collection<? extends E>> factory();

    @Test
    default void factoryTest() {
        CollectionFactory<Object, Object, Collection<?>> factory =
                (CollectionFactory<Object, Object, Collection<?>>) factory();
        assertIsEmpty(factory.empty());
        assertIsEmpty(factory.from(JavaArray.EMPTY_OBJECT_ARRAY));
        assertIsEmpty(factory.from(java.util.List.of()));
        assertIsEmpty(factory.from(new Object[]{}));

        for (Integer[] data : TestData.data1) {
            assertElements(factory.from(data), ((Object[]) data));
            assertElements(factory.from(Arrays.asList(data)), ((Object[]) data));
        }

        assertIsEmpty(factory.build(factory.newBuilder()));

        for (Integer[] data : TestData.data1) {
            Object builder = factory.newBuilder();
            for (Integer i : data) {
                factory.addToBuilder(builder, i);
            }
            assertElements(factory.build(builder), (Object[]) data);
        }
    }

    @Test
    default void isEmptyTest() {
        assertTrue(factory().empty().isEmpty());
        assertTrue(factory().from(JavaArray.EMPTY_OBJECT_ARRAY).isEmpty());
        assertTrue(factory().from(Collections.emptyList()).isEmpty());

        for (Integer[] data : TestData.data1) {
            assertEquals(data.length == 0, factory().from(data).isEmpty());
        }
    }

    @Test
    default void sizeTest() {
        assertEquals(0, factory().empty().size());
        for (Integer[] data : TestData.data1) {
            assertEquals(data.length, factory().from(data).size());
        }
    }

    @Test
    default void knownSizeTest() {
        assertEquals(0, factory().empty().knownSize());

        for (Integer[] data : TestData.data1) {
            int ks = factory().from(data).knownSize();
            assertTrue(ks == data.length || ks == -1);
        }
    }

    @Test
    default void containsTest() {
        for (Integer[] data : TestData.data1) {
            Collection<? extends Integer> c = this.<Integer>factory().from(data);
            assertFalse(c.contains(0));
            for (int d : data) {
                assertTrue(c.contains(d));
                assertFalse(c.contains(-d));
            }
        }
    }
}
