package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.base.GenericArrays;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface BufferTestTemplate extends MutableSeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends Buffer<? extends E>> factory();

    @Test
    default void appendTest() {
        Buffer<Object> b = (Buffer<Object>) factory().empty();
        assertIterableEquals(List.of(), b);
        b.append("foo");
        assertIterableEquals(List.of("foo"), b);
        b.append("bar");
        assertIterableEquals(List.of("foo", "bar"), b);
        b.append(null);
        assertIterableEquals(Arrays.asList("foo", "bar", null), b);

        for (Integer[] data : data1()) {
            b = (Buffer<Object>) factory().empty();
            for (Integer i : data) {
                b.append(i);
            }
            assertIterableEquals(Arrays.asList(data), b);
        }
    }

    @Test
    default void appendAllTest() {
        Buffer<Object> b = (Buffer<Object>) factory().empty();
        assertIterableEquals(List.of(), b);

        b.appendAll(List.of());
        assertIterableEquals(List.of(), b);

        b.appendAll(GenericArrays.EMPTY_OBJECT_ARRAY);
        assertIterableEquals(List.of(), b);

        b.appendAll(List.of("str1"));
        assertIterableEquals(List.of("str1"), b);

        b.appendAll(new Object[]{"str2", "str3"});
        assertIterableEquals(List.of("str1", "str2", "str3"), b);

        b.appendAll(List.of("str4", "str5", "str6"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), b);

        b = (Buffer<Object>) factory().empty();
        b.appendAll(b);
        assertIterableEquals(List.of(), b);

        b.append("str");
        b.appendAll(b);
        assertIterableEquals(List.of("str", "str"), b);

        final Integer[][] data1 = data1();
        for (int i = 0; i < data1.length - 1; i++) {
            Integer[] d1 = data1[i];
            Integer[] d2 = data1[i + 1];
            ArrayList<Integer> d12 = new ArrayList<>();
            d12.addAll(Arrays.asList(d1));
            d12.addAll(Arrays.asList(d2));

            {
                b = (Buffer<Object>) factory().empty();
                assertIterableEquals(List.of(), b);
                b.appendAll(d1);
                assertIterableEquals(Arrays.asList(d1), b);
                b.appendAll(d2);
                assertIterableEquals(d12, b);
            }
            {
                b = (Buffer<Object>) factory().empty();
                assertIterableEquals(List.of(), b);
                b.appendAll(Arrays.asList(d1));
                assertIterableEquals(Arrays.asList(d1), b);
                b.appendAll(Arrays.asList(d2));
                assertIterableEquals(d12, b);
            }
        }
        for (Integer[] data : data1) {
            ArrayList<Integer> l = new ArrayList<>();
            l.addAll(Arrays.asList(data));
            l.addAll(Arrays.asList(data));

            b = (Buffer<Object>) factory().empty();
            b.appendAll(data);
            b.appendAll(b);
            assertIterableEquals(l, b);
        }
    }

    @Test
    default void prependTest() {
        Buffer<Object> b = (Buffer<Object>) factory().empty();
        assertIterableEquals(List.of(), b);
        b.prepend("foo");
        assertIterableEquals(List.of("foo"), b);
        b.prepend("bar");
        assertIterableEquals(List.of("bar", "foo"), b);
        b.prepend(null);
        assertIterableEquals(Arrays.asList(null, "bar", "foo"), b);
        for (Integer[] data : data1()) {
            b = (Buffer<Object>) factory().empty();
            for (int i = data.length - 1; i >= 0; i--) {
                b.prepend(data[i]);
            }
            assertIterableEquals(Arrays.asList(data), b);
        }
    }

    @Test
    default void prependAllTest() {
        Buffer<Object> b = (Buffer<Object>) factory().empty();
        assertIterableEquals(List.of(), b);

        b.prependAll(List.of());
        assertIterableEquals(List.of(), b);

        b.prependAll(GenericArrays.EMPTY_OBJECT_ARRAY);
        assertIterableEquals(List.of(), b);

        b.prependAll(List.of("str1"));
        assertIterableEquals(List.of("str1"), b);

        b.prependAll(new Object[]{"str2", "str3"});
        assertIterableEquals(List.of("str2", "str3", "str1"), b);

        b.prependAll(List.of("str4", "str5", "str6"));
        assertIterableEquals(List.of("str4", "str5", "str6", "str2", "str3", "str1"), b);

        b = (Buffer<Object>) factory().empty();
        b.prependAll(b);
        assertIterableEquals(List.of(), b);

        b.prepend("str");
        b.prependAll(b);
        assertIterableEquals(List.of("str", "str"), b);

        Integer[][] data1 = data1();
        for (int i = 0; i < data1.length - 1; i++) {
            Integer[] d1 = data1[i];
            Integer[] d2 = data1[i + 1];
            ArrayList<Integer> d21 = new ArrayList<>();
            d21.addAll(Arrays.asList(d2));
            d21.addAll(Arrays.asList(d1));

            {
                b = (Buffer<Object>) factory().empty();
                assertIterableEquals(List.of(), b);
                b.prependAll(d1);
                assertIterableEquals(Arrays.asList(d1), b);
                b.prependAll(d2);
                assertIterableEquals(d21, b);
            }
            {
                b = (Buffer<Object>) factory().empty();
                assertIterableEquals(List.of(), b);
                b.prependAll(Arrays.asList(d1));
                assertIterableEquals(Arrays.asList(d1), b);
                b.prependAll(Arrays.asList(d2));
                assertIterableEquals(d21, b);
            }
        }
        for (Integer[] data : data1) {
            ArrayList<Integer> l = new ArrayList<>();
            l.addAll(Arrays.asList(data));
            l.addAll(Arrays.asList(data));

            b = (Buffer<Object>) factory().empty();
            b.prependAll(data);
            b.prependAll(b);
            assertIterableEquals(l, b);
        }
    }
}
