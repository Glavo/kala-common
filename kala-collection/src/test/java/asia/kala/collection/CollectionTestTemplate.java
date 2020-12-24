package asia.kala.collection;

import asia.kala.factory.CollectionFactory;
import asia.kala.traversable.JavaArray;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

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

    default void isEmptyTest() {
        assertTrue(factory().empty().isEmpty());
        assertTrue(factory().from(JavaArray.EMPTY_OBJECT_ARRAY).isEmpty());
        assertTrue(factory().from(Collections.emptyList()).isEmpty());

        for (Integer[] data : TestData.data1) {
            assertEquals(data.length == 0, factory().from(data).isEmpty());
        }
    }

}
