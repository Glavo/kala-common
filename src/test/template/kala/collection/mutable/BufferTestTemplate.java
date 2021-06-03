package kala.collection.mutable;

import kala.collection.base.GenericArrays;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface BufferTestTemplate extends MutableSeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends Buffer<? extends E>> factory();

    @Override
    <E> Buffer<E> of(E... elements);

    @Override
    <E> Buffer<E> from(E[] elements);

    @Override
    <E> Buffer<E> from(Iterable<? extends E> elements);

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
        Buffer<Object> b = of();
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

    @Test
    default void filterInPlaceTest() {
        final Buffer<?> empty = factory().empty();
        empty.filterInPlace(e -> true);
        assertIterableEquals(List.of(), empty);
        empty.filterInPlace(e -> false);
        assertIterableEquals(List.of(), empty);

        final Buffer<Integer> b1 = of(0, 1, 2, 3, 4, 5);
        b1.filterInPlace(it -> it > 2);
        assertIterableEquals(List.of(3, 4, 5), b1);

        final Buffer<Integer> b2 = of(0, 1, 2, 3, 4, 5);
        b2.filterInPlace(it -> it % 2 == 0);
        assertIterableEquals(List.of(0, 2, 4), b2);
    }

    @Test
    default void filterNotInPlaceTest() {
        final Buffer<?> empty = factory().empty();
        empty.filterNotInPlace(e -> true);
        assertIterableEquals(List.of(), empty);
        empty.filterNotInPlace(e -> false);
        assertIterableEquals(List.of(), empty);

        final Buffer<Integer> b1 = of(0, 1, 2, 3, 4, 5);
        b1.filterNotInPlace(it -> it > 2);
        assertIterableEquals(List.of(0, 1, 2), b1);

        final Buffer<Integer> b2 = of(0, 1, 2, 3, 4, 5);
        b2.filterNotInPlace(it -> it % 2 == 0);
        assertIterableEquals(List.of(1, 3, 5), b2);
    }
}
