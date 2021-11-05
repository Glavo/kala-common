package kala.collection.mutable;

import kala.collection.base.GenericArrays;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface DynamicSeqTestTemplate extends MutableSeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends DynamicSeq<? extends E>> factory();

    @Override
    <E> DynamicSeq<E> of(E... elements);

    @Override
    <E> DynamicSeq<E> from(E[] elements);

    @Override
    <E> DynamicSeq<E> from(Iterable<? extends E> elements);

    @Test
    default void appendTest() {
        DynamicSeq<Object> b = (DynamicSeq<Object>) factory().empty();
        assertIterableEquals(List.of(), b);
        b.append("foo");
        assertIterableEquals(List.of("foo"), b);
        b.append("bar");
        assertIterableEquals(List.of("foo", "bar"), b);
        b.append(null);
        assertIterableEquals(Arrays.asList("foo", "bar", null), b);

        for (Integer[] data : data1()) {
            b = (DynamicSeq<Object>) factory().empty();
            for (Integer i : data) {
                b.append(i);
            }
            assertIterableEquals(Arrays.asList(data), b);
        }
    }

    @Test
    default void appendAllTest() {
        DynamicSeq<Object> b = of();
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

        b = (DynamicSeq<Object>) factory().empty();
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
                b = (DynamicSeq<Object>) factory().empty();
                assertIterableEquals(List.of(), b);
                b.appendAll(d1);
                assertIterableEquals(Arrays.asList(d1), b);
                b.appendAll(d2);
                assertIterableEquals(d12, b);
            }
            {
                b = (DynamicSeq<Object>) factory().empty();
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

            b = (DynamicSeq<Object>) factory().empty();
            b.appendAll(data);
            b.appendAll(b);
            assertIterableEquals(l, b);
        }
    }

    @Test
    default void prependTest() {
        DynamicSeq<Object> b = (DynamicSeq<Object>) factory().empty();
        assertIterableEquals(List.of(), b);
        b.prepend("foo");
        assertIterableEquals(List.of("foo"), b);
        b.prepend("bar");
        assertIterableEquals(List.of("bar", "foo"), b);
        b.prepend(null);
        assertIterableEquals(Arrays.asList(null, "bar", "foo"), b);
        for (Integer[] data : data1()) {
            b = (DynamicSeq<Object>) factory().empty();
            for (int i = data.length - 1; i >= 0; i--) {
                b.prepend(data[i]);
            }
            assertIterableEquals(Arrays.asList(data), b);
        }
    }

    @Test
    default void prependAllTest() {
        DynamicSeq<Object> b = (DynamicSeq<Object>) factory().empty();
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

        b = (DynamicSeq<Object>) factory().empty();
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
                b = (DynamicSeq<Object>) factory().empty();
                assertIterableEquals(List.of(), b);
                b.prependAll(d1);
                assertIterableEquals(Arrays.asList(d1), b);
                b.prependAll(d2);
                assertIterableEquals(d21, b);
            }
            {
                b = (DynamicSeq<Object>) factory().empty();
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

            b = (DynamicSeq<Object>) factory().empty();
            b.prependAll(data);
            b.prependAll(b);
            assertIterableEquals(l, b);
        }
    }

    @Test
    default void remoteAtTest() {
        DynamicSeq<Object> empty = of();
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(0));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(1));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(Integer.MAX_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(Integer.MIN_VALUE));

        DynamicSeq<String> seq = of("str0", "str1", "str2", "str3", "str4");
        seq.removeAt(4);
        assertIterableEquals(List.of("str0", "str1", "str2", "str3"), seq);
        seq.removeAt(0);
        assertIterableEquals(List.of("str1", "str2", "str3"), seq);
        seq.removeAt(1);
        assertIterableEquals(List.of("str1", "str3"), seq);
    }

    @Test
    default void retainAllTest() {
        final DynamicSeq<?> empty = factory().empty();
        empty.retainAll(e -> true);
        assertIterableEquals(List.of(), empty);
        empty.retainAll(e -> false);
        assertIterableEquals(List.of(), empty);

        final DynamicSeq<Integer> b1 = of(0, 1, 2, 3, 4, 5);
        b1.retainAll(it -> it > 2);
        assertIterableEquals(List.of(3, 4, 5), b1);

        final DynamicSeq<Integer> b2 = of(0, 1, 2, 3, 4, 5);
        b2.retainAll(it -> it % 2 == 0);
        assertIterableEquals(List.of(0, 2, 4), b2);
    }

    @Test
    default void removeAllTest() {
        final DynamicSeq<?> empty = factory().empty();
        empty.removeAll(e -> true);
        assertIterableEquals(List.of(), empty);
        empty.removeAll(e -> false);
        assertIterableEquals(List.of(), empty);

        final DynamicSeq<Integer> b1 = of(0, 1, 2, 3, 4, 5);
        b1.removeAll(it -> it > 2);
        assertIterableEquals(List.of(0, 1, 2), b1);

        final DynamicSeq<Integer> b2 = of(0, 1, 2, 3, 4, 5);
        b2.removeAll(it -> it % 2 == 0);
        assertIterableEquals(List.of(1, 3, 5), b2);
    }

    @Test
    default void dropInPlaceTest() {
        DynamicSeq<Object> empty = of();
        assertThrows(IllegalArgumentException.class, () -> empty.dropInPlace(-1));
        assertThrows(IllegalArgumentException.class, () -> empty.dropInPlace(Integer.MIN_VALUE));

        DynamicSeq<String> seq = of("str0", "str1", "str2", "str3", "str4");
        seq.dropInPlace(0);
        assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4"), seq);
        seq.dropInPlace(1);
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), seq);
        seq.dropInPlace(seq.size());
        assertIterableEquals(List.of(), seq);
    }

    @Test
    default void takeInPlaceTest() {
        DynamicSeq<Object> empty = of();
        assertThrows(IllegalArgumentException.class, () -> empty.takeInPlace(-1));
        assertThrows(IllegalArgumentException.class, () -> empty.takeInPlace(Integer.MIN_VALUE));

        DynamicSeq<String> seq = of("str0", "str1", "str2", "str3", "str4");
        seq.takeInPlace(seq.size());
        assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4"), seq);
        seq.takeInPlace(seq.size() + 1);
        assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4"), seq);
        seq.takeInPlace(Integer.MAX_VALUE);
        assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4"), seq);
        seq.takeInPlace(seq.size() - 1);
        assertIterableEquals(List.of("str0", "str1", "str2", "str3"), seq);
        seq.takeInPlace(0);
        assertIterableEquals(List.of(), seq);
    }

}
