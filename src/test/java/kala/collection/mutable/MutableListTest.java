package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class MutableListTest implements MutableListTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ? extends MutableList<E>> factory() {
        return MutableList.factory();
    }

    @Override
    public <E> MutableList<E> of(E... elements) {
        return MutableList.from(elements);
    }

    @Override
    public <E> MutableList<E> from(E[] elements) {
        return MutableList.from(elements);
    }

    @Override
    public <E> MutableList<E> from(Iterable<? extends E> elements) {
        return MutableList.from(elements);
    }

    @Test
    public void wrapJavaTest() {
        {
            MutableList<String> list = MutableList.wrapJava(new ArrayList<>());
            assertTrue(list.supportsFastRandomAccess());
            assertIterableEquals(List.of(), list);

            list.append("str0");
            assertIterableEquals(List.of("str0"), list);

            list.prepend("str1");
            assertIterableEquals(List.of("str1", "str0"), list);
        }

        {
            MutableList<String> list = MutableList.wrapJava(new LinkedList<>());
            assertFalse(list.supportsFastRandomAccess());
            assertIterableEquals(List.of(), list);

            list.append("str0");
            assertIterableEquals(List.of("str0"), list);

            list.prepend("str1");
            assertIterableEquals(List.of("str1", "str0"), list);
        }

        {
            MutableArrayList<String> source = new MutableArrayList<>();
            assertSame(source, MutableList.wrapJava(source.asJava()));
        }
    }

    @Override
    public void classNameTest() {
    }
}
