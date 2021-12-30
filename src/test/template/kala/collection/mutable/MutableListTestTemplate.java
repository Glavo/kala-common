package kala.collection.mutable;

import kala.collection.SimpleIterable;
import kala.collection.base.GenericArrays;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface MutableListTestTemplate extends MutableSeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableList<? extends E>> factory();

    @Override
    <E> MutableList<E> of(E... elements);

    @Override
    <E> MutableList<E> from(E[] elements);

    @Override
    <E> MutableList<E> from(Iterable<? extends E> elements);

    @Test
    default void appendTest() {
        MutableList<Object> b = (MutableList<Object>) factory().empty();
        assertIterableEquals(List.of(), b);
        b.append("foo");
        assertIterableEquals(List.of("foo"), b);
        b.append("bar");
        assertIterableEquals(List.of("foo", "bar"), b);
        b.append(null);
        assertIterableEquals(Arrays.asList("foo", "bar", null), b);

        for (Integer[] data : data1()) {
            b = (MutableList<Object>) factory().empty();
            for (Integer i : data) {
                b.append(i);
            }
            assertIterableEquals(Arrays.asList(data), b);
        }
    }

    @Test
    default void appendAllTest() {
        MutableList<Object> b = of();
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

        b = (MutableList<Object>) factory().empty();
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
                b = (MutableList<Object>) factory().empty();
                assertIterableEquals(List.of(), b);
                b.appendAll(d1);
                assertIterableEquals(Arrays.asList(d1), b);
                b.appendAll(d2);
                assertIterableEquals(d12, b);
            }
            {
                b = (MutableList<Object>) factory().empty();
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

            b = (MutableList<Object>) factory().empty();
            b.appendAll(data);
            b.appendAll(b);
            assertIterableEquals(l, b);
        }
    }

    @Test
    default void prependTest() {
        MutableList<Object> b = (MutableList<Object>) factory().empty();
        assertIterableEquals(List.of(), b);
        b.prepend("foo");
        assertIterableEquals(List.of("foo"), b);
        b.prepend("bar");
        assertIterableEquals(List.of("bar", "foo"), b);
        b.prepend(null);
        assertIterableEquals(Arrays.asList(null, "bar", "foo"), b);
        for (Integer[] data : data1()) {
            b = (MutableList<Object>) factory().empty();
            for (int i = data.length - 1; i >= 0; i--) {
                b.prepend(data[i]);
            }
            assertIterableEquals(Arrays.asList(data), b);
        }
    }

    @Test
    default void prependAllTest() {
        MutableList<Object> b = (MutableList<Object>) factory().empty();
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

        b = (MutableList<Object>) factory().empty();
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
                b = (MutableList<Object>) factory().empty();
                assertIterableEquals(List.of(), b);
                b.prependAll(d1);
                assertIterableEquals(Arrays.asList(d1), b);
                b.prependAll(d2);
                assertIterableEquals(d21, b);
            }
            {
                b = (MutableList<Object>) factory().empty();
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

            b = (MutableList<Object>) factory().empty();
            b.prependAll(data);
            b.prependAll(b);
            assertIterableEquals(l, b);
        }
    }

    @Test
    default void removeAtTest() {
        MutableList<Object> empty = of();
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(0));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(1));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(Integer.MAX_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(Integer.MIN_VALUE));

        MutableList<String> list = of("str0", "str1", "str2", "str3", "str4");
        list.removeAt(4);
        assertIterableEquals(List.of("str0", "str1", "str2", "str3"), list);
        list.removeAt(0);
        assertIterableEquals(List.of("str1", "str2", "str3"), list);
        list.removeAt(1);
        assertIterableEquals(List.of("str1", "str3"), list);


    }

    @Test
    default void retainAllTest() {
        final MutableList<?> empty = factory().empty();
        empty.retainAll(e -> true);
        assertIterableEquals(List.of(), empty);
        empty.retainAll(e -> false);
        assertIterableEquals(List.of(), empty);

        final MutableList<Integer> b1 = of(0, 1, 2, 3, 4, 5);
        b1.retainAll(it -> it > 2);
        assertIterableEquals(List.of(3, 4, 5), b1);

        final MutableList<Integer> b2 = of(0, 1, 2, 3, 4, 5);
        b2.retainAll(it -> it % 2 == 0);
        assertIterableEquals(List.of(0, 2, 4), b2);
    }

    @Test
    default void removeAllTest() {
        final MutableList<?> empty = factory().empty();
        empty.removeAll(e -> true);
        assertIterableEquals(List.of(), empty);
        empty.removeAll(e -> false);
        assertIterableEquals(List.of(), empty);

        final MutableList<Integer> b1 = of(0, 1, 2, 3, 4, 5);
        b1.removeAll(it -> it > 2);
        assertIterableEquals(List.of(0, 1, 2), b1);

        final MutableList<Integer> b2 = of(0, 1, 2, 3, 4, 5);
        b2.removeAll(it -> it % 2 == 0);
        assertIterableEquals(List.of(1, 3, 5), b2);
    }

    @Test
    default void insertTest() {
        MutableList<String> empty = of();
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(-1, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(Integer.MAX_VALUE, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(1, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(2, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(Integer.MIN_VALUE, "value"));

        MutableList<String> l1 = of();
        l1.insert(0, "value0");
        assertIterableEquals(List.of("value0"), l1);

        l1.insert(0, "value1");
        assertIterableEquals(List.of("value1", "value0"), l1);

        l1.insert(2, "value2");
        assertIterableEquals(List.of("value1", "value0", "value2"), l1);

        {
            final int testListCount = 4;

            List<MutableList<String>> listList = new ArrayList<>();
            List<LinkedList<String>> resList = new ArrayList<>();

            for (int i = 0; i < testListCount; i++) {
                listList.add(of());
                resList.add(new LinkedList<>());
            }

            for (int i = 0; i < 32; i++) {
                String value = "value" + i;

                listList.get(0).insert(0, value);
                resList.get(0).add(0, value);

                listList.get(1).insert(listList.get(1).size(), value);
                resList.get(1).add(resList.get(1).size(), value);

                if (i % 2 == 0) {
                    listList.get(2).insert(0, value);
                    resList.get(2).add(0, value);

                    listList.get(3).prepend(value);
                    resList.get(3).addFirst(value);
                } else {
                    listList.get(2).insert(listList.get(2).size(), value);
                    resList.get(2).add(resList.get(2).size(), value);

                    listList.get(3).append(value);
                    resList.get(3).addLast(value);
                }
            }
            // assert
            for (int i = 0; i < testListCount; i++) {
                assertIterableEquals(resList.get(i), listList.get(i));
            }

            Random random = new Random(0);
            for (int i = 0; i < 128; i++) {
                var idx = random.nextInt(32 + i);
                String value = "random insert value" + i;

                for (int li = 0; li < testListCount; li++) {
                    listList.get(li).insert(idx, value);
                    resList.get(li).add(idx, value);
                }
            }

            // assert
            for (int i = 0; i < testListCount; i++) {
                int finalI = i;
                assertIterableEquals(resList.get(i), listList.get(i), () ->
                        "expected: %s, actual: %s".formatted(resList.get(finalI), listList.get(finalI)));
            }
        }
    }

    @Test
    default void insertAllTest() {
        // empty test
        {
            String[][] arrays = {
                    {},
                    {"value"},
                    {"value0", "value1", "value2", "value3", "value4", "value5", "value6", "value7"}
            };

            Iterable<String>[] iterables =
                    Arrays.stream(arrays)
                            .map(it -> new SimpleIterable<>(Arrays.asList(it)))
                            .toArray(Iterable[]::new);

            MutableList<String> empty = of();

            for (String[] array : arrays) {
                assertThrows(IndexOutOfBoundsException.class, () -> empty.insertAll(-1, array));
                assertThrows(IndexOutOfBoundsException.class, () -> empty.insertAll(1, array));
                assertThrows(IndexOutOfBoundsException.class, () -> empty.insertAll(Integer.MAX_VALUE, array));
                assertThrows(IndexOutOfBoundsException.class, () -> empty.insertAll(Integer.MIN_VALUE, array));
            }

            for (Iterable<String> iterable : iterables) {
                assertThrows(IndexOutOfBoundsException.class, () -> empty.insertAll(-1, iterable));
                assertThrows(IndexOutOfBoundsException.class, () -> empty.insertAll(1, iterable));
                assertThrows(IndexOutOfBoundsException.class, () -> empty.insertAll(Integer.MAX_VALUE, iterable));
                assertThrows(IndexOutOfBoundsException.class, () -> empty.insertAll(Integer.MIN_VALUE, iterable));
            }
        }
    }

    @Test
    default void dropInPlaceTest() {
        MutableList<Object> empty = of();
        assertThrows(IllegalArgumentException.class, () -> empty.dropInPlace(-1));
        assertThrows(IllegalArgumentException.class, () -> empty.dropInPlace(Integer.MIN_VALUE));

        MutableList<String> seq = of("str0", "str1", "str2", "str3", "str4");
        seq.dropInPlace(0);
        assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4"), seq);
        seq.dropInPlace(1);
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), seq);
        seq.dropInPlace(seq.size());
        assertIterableEquals(List.of(), seq);
    }

    @Test
    default void takeInPlaceTest() {
        MutableList<Object> empty = of();
        assertThrows(IllegalArgumentException.class, () -> empty.takeInPlace(-1));
        assertThrows(IllegalArgumentException.class, () -> empty.takeInPlace(Integer.MIN_VALUE));

        MutableList<String> seq = of("str0", "str1", "str2", "str3", "str4");
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
