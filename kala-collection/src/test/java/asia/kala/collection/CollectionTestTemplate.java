package asia.kala.collection;

import asia.kala.factory.CollectionFactory;
import asia.kala.traversable.JavaArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static asia.kala.collection.Assertions.*;

public interface CollectionTestTemplate {

    <E> CollectionFactory<E, ?, ? extends Collection<? extends E>> factory();

    @Test
    default void factoryTest() {
        CollectionFactory<Object, ?, ? extends Collection<?>> factory = factory();
        assertIsEmpty(factory.empty());
        assertIsEmpty(factory.from(JavaArray.EMPTY_OBJECT_ARRAY));
        assertIsEmpty(factory.from(java.util.List.of()));
        assertElements(factory.from(new Object[]{}));
        assertElements(factory.from(new Object[]{"foo"}), "foo");
        assertElements(factory.from(java.util.List.of("foo")), "foo");
        assertElements(factory.from(new Object[]{"foo", "bar"}), "foo", "bar");
        assertElements(factory.from(java.util.List.of("foo", "bar")), "foo", "bar");
    }
}
