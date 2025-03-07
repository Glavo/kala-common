/*
 * Copyright 2025 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.mutable;

import kala.collection.base.GenericArrays;
import kala.collection.factory.CollectionFactory;
import kala.control.Option;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface MutableListTestTemplate extends MutableSeqTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends MutableList<E>> factory();

    @Override
    default <E> MutableList<E> of(E... elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    default <E> MutableList<E> from(E[] elements) {
        return this.<E>factory().from(elements);
    }

    @Override
    default <E> MutableList<E> from(Iterable<? extends E> elements) {
        return this.<E>factory().from(elements);
    }

    @Test
    default void createTest() {
        final Class<?> klass = collectionType();
        if (klass == null) {
            return;
        }

        try {
            final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
            final MethodHandle create = lookup.findStatic(klass, "create", MethodType.methodType(klass));

            MutableList<Object> list = (MutableList<Object>) create.invoke();
            assertInstanceOf(klass, list);
            assertIterableEquals(List.of(), list);
        } catch (Throwable e) {
            fail(e);
        }
    }

    @Test
    @Override
    default void setAllTest() {
        MutableList<String> list = of();
        list.setAll();
        assertIterableEquals(List.of(), list);

        list.setAll("value");
        assertIterableEquals(List.of("value"), list);

        list.setAll("0", "1", "2");
        assertIterableEquals(List.of("0", "1", "2"), list);

        list.setAll("value");
        assertIterableEquals(List.of("value"), list);

        list.setAll(List.of());
        assertIterableEquals(List.of(), list);

        list.setAll(List.of("0", "1", "2"));
        assertIterableEquals(List.of("0", "1", "2"), list);

        list.setAll(List.of("0"));
        assertIterableEquals(List.of("0"), list);
    }

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
        {
            MutableList<Object> list = of();
            assertIterableEquals(List.of(), list);

            list.appendAll(List.of());
            assertIterableEquals(List.of(), list);

            list.appendAll(GenericArrays.EMPTY_OBJECT_ARRAY);
            assertIterableEquals(List.of(), list);

            list.appendAll(List.of("str1"));
            assertIterableEquals(List.of("str1"), list);

            list.appendAll("str2", "str3");
            assertIterableEquals(List.of("str1", "str2", "str3"), list);

            list.appendAll(List.of("str4", "str5", "str6"));
            assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), list);

            list = factory().empty();
            list.appendAll(list);
            assertIterableEquals(List.of(), list);

            list.append("str");
            list.appendAll(list);
            assertIterableEquals(List.of("str", "str"), list);
        }

        final Integer[][] data1 = data1();
        for (int i = 0; i < data1.length - 1; i++) {
            Integer[] d1 = data1[i];
            Integer[] d2 = data1[i + 1];
            ArrayList<Integer> d12 = new ArrayList<>();
            d12.addAll(Arrays.asList(d1));
            d12.addAll(Arrays.asList(d2));

            {
                MutableList<Integer> list = of();
                assertIterableEquals(List.of(), list);
                list.appendAll(d1);
                assertIterableEquals(Arrays.asList(d1), list);
                list.appendAll(d2);
                assertIterableEquals(d12, list);
            }
            {
                MutableList<Integer> list = of();
                assertIterableEquals(List.of(), list);
                list.appendAll(Arrays.asList(d1));
                assertIterableEquals(Arrays.asList(d1), list);
                list.appendAll(Arrays.asList(d2));
                assertIterableEquals(d12, list);
            }
        }
        for (Integer[] data : data1) {
            ArrayList<Integer> l = new ArrayList<>();
            l.addAll(Arrays.asList(data));
            l.addAll(Arrays.asList(data));

            MutableList<Integer> list = of();
            list.appendAll(data);
            list.appendAll(list);
            assertIterableEquals(l, list);
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
        {
            MutableList<Object> list = of();
            assertIterableEquals(List.of(), list);

            list.prependAll(List.of());
            assertIterableEquals(List.of(), list);

            list.prependAll(GenericArrays.EMPTY_OBJECT_ARRAY);
            assertIterableEquals(List.of(), list);

            list.prependAll(List.of("str1"));
            assertIterableEquals(List.of("str1"), list);

            list.prependAll("str2", "str3");
            assertIterableEquals(List.of("str2", "str3", "str1"), list);

            list.prependAll(List.of("str4", "str5", "str6"));
            assertIterableEquals(List.of("str4", "str5", "str6", "str2", "str3", "str1"), list);

            list = of();
            list.prependAll(list);
            assertIterableEquals(List.of(), list);

            list.prepend("str");
            list.prependAll(list);
            assertIterableEquals(List.of("str", "str"), list);
        }

        Integer[][] data1 = data1();
        for (int i = 0; i < data1.length - 1; i++) {
            Integer[] d1 = data1[i];
            Integer[] d2 = data1[i + 1];
            ArrayList<Integer> d21 = new ArrayList<>();
            d21.addAll(Arrays.asList(d2));
            d21.addAll(Arrays.asList(d1));

            {
                MutableList<Integer> list = of();
                assertIterableEquals(List.of(), list);
                list.prependAll(d1);
                assertIterableEquals(Arrays.asList(d1), list);
                list.prependAll(d2);
                assertIterableEquals(d21, list);
            }
            {
                MutableList<Integer> list = of();
                assertIterableEquals(List.of(), list);
                list.prependAll(Arrays.asList(d1));
                assertIterableEquals(Arrays.asList(d1), list);
                list.prependAll(Arrays.asList(d2));
                assertIterableEquals(d21, list);
            }
        }
        for (Integer[] data : data1) {
            ArrayList<Integer> l = new ArrayList<>();
            l.addAll(Arrays.asList(data));
            l.addAll(Arrays.asList(data));

            MutableList<Integer> list = of();
            list.prependAll(data);
            list.prependAll(list);
            assertIterableEquals(l, list);
        }
    }

    @Test
    default void removeTest() {
        MutableList<Object> empty = of();
        assertFalse(empty.remove("value"));
        assertFalse(empty.remove(null));

        MutableList<String> list = of("str0", "str1", "str2", "str3", "str4");

        list.remove("value");
        assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4"), list);

        list.remove("str0");
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), list);

        list.remove("str3");
        assertIterableEquals(List.of("str1", "str2", "str4"), list);

        list.remove("foo");
        assertIterableEquals(List.of("str1", "str2", "str4"), list);

        list.remove("str4");
        assertIterableEquals(List.of("str1", "str2"), list);

        list.remove("str1");
        assertIterableEquals(List.of("str2"), list);

        list.remove("bar");
        assertIterableEquals(List.of("str2"), list);

        list.remove("str2");
        assertIterableEquals(List.of(), list);
    }

    @Test
    default void removeAtTest() {
        MutableList<Object> empty = of();
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(0));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(1));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(Integer.MAX_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(~0));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(~1));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.removeAt(Integer.MIN_VALUE));

        MutableList<String> list = of("str0", "str1", "str2", "str3", "str4");
        list.removeAt(4);
        assertIterableEquals(List.of("str0", "str1", "str2", "str3"), list);
        list.removeAt(0);
        assertIterableEquals(List.of("str1", "str2", "str3"), list);
        list.removeAt(1);
        assertIterableEquals(List.of("str1", "str3"), list);
        list.removeAt(~2);
        assertIterableEquals(List.of("str3"), list);
        list.removeAt(~1);
        assertIterableEquals(List.of(), list);
    }

    @Test
    default void removeInRangeTest() {
        assertThrows(IndexOutOfBoundsException.class, () -> of().removeInRange(1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> of().removeInRange(1, 1));

        MutableList<String> list = of("str0", "str1", "str2", "str3", "str4", "str5", "str6", "str7", "str8", "str9");
        list.removeInRange(0, 0);
        assertIterableEquals(List.of("str0", "str1", "str2", "str3", "str4", "str5", "str6", "str7", "str8", "str9"), list);
        list.removeInRange(1, 3);
        assertIterableEquals(List.of("str0", "str3", "str4", "str5", "str6", "str7", "str8", "str9"), list);
        list.removeInRange(0, 5);
        assertIterableEquals(List.of("str7", "str8", "str9"), list);
        list.removeInRange(1, 2);
        assertIterableEquals(List.of("str7", "str9"), list);
        list.removeInRange(~2, ~1);
        assertIterableEquals(List.of("str9"), list);
        list.removeInRange(~1, ~0);
        assertIterableEquals(List.of(), list);
    }

    @Test
    default void retainIfTest() {
        final MutableList<?> empty = factory().empty();
        empty.retainIf(e -> true);
        assertIterableEquals(List.of(), empty);
        empty.retainIf(e -> false);
        assertIterableEquals(List.of(), empty);

        final MutableList<Integer> b1 = of(0, 1, 2, 3, 4, 5);
        b1.retainIf(it -> it > 2);
        assertIterableEquals(List.of(3, 4, 5), b1);

        final MutableList<Integer> b2 = of(0, 1, 2, 3, 4, 5);
        b2.retainIf(it -> it % 2 == 0);
        assertIterableEquals(List.of(0, 2, 4), b2);
    }

    @Test
    default void removeIfTest() {
        final MutableList<?> empty = factory().empty();
        empty.removeIf(e -> true);
        assertIterableEquals(List.of(), empty);
        empty.removeIf(e -> false);
        assertIterableEquals(List.of(), empty);

        final MutableList<Integer> b1 = of(0, 1, 2, 3, 4, 5);
        b1.removeIf(it -> it > 2);
        assertIterableEquals(List.of(0, 1, 2), b1);

        final MutableList<Integer> b2 = of(0, 1, 2, 3, 4, 5);
        b2.removeIf(it -> it % 2 == 0);
        assertIterableEquals(List.of(1, 3, 5), b2);
    }

    @Test
    default void insertTest() {
        MutableList<String> empty = of();
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(~1, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(Integer.MAX_VALUE, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(1, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(2, "value"));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.insert(Integer.MIN_VALUE, "value"));

        MutableList<String> l1 = of();
        l1.insert(0, "value0");
        assertIterableEquals(List.of("value0"), l1);

        l1.insert(~1, "value1");
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
                String value = "random insertNonFull value" + i;

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
        for (String[] array : new String[][]{
                {},
                {"value"},
                {"value0", "value1", "value2", "value3", "value4", "value5", "value6", "value7"}
        }) {
            Iterable<String> iterable = Arrays.asList(array);

            MutableList<String> list = of();

            assertThrows(IndexOutOfBoundsException.class, () -> list.insertAll(~1, array));
            assertThrows(IndexOutOfBoundsException.class, () -> list.insertAll(1, array));
            assertThrows(IndexOutOfBoundsException.class, () -> list.insertAll(Integer.MAX_VALUE, array));
            assertThrows(IndexOutOfBoundsException.class, () -> list.insertAll(Integer.MIN_VALUE, array));
            assertThrows(IndexOutOfBoundsException.class, () -> list.insertAll(~1, iterable));
            assertThrows(IndexOutOfBoundsException.class, () -> list.insertAll(1, iterable));
            assertThrows(IndexOutOfBoundsException.class, () -> list.insertAll(Integer.MAX_VALUE, iterable));
            assertThrows(IndexOutOfBoundsException.class, () -> list.insertAll(Integer.MIN_VALUE, iterable));

            list.insertAll(0, array);
            assertIterableEquals(Arrays.asList(array), list);

            list.clear();
            list.insertAll(0, iterable);
            assertIterableEquals(iterable, list);

            list.clear();
            list.insertAll(~0, array);
            assertIterableEquals(Arrays.asList(array), list);

            list.clear();
            list.insertAll(~0, iterable);
            assertIterableEquals(Arrays.asList(array), list);
        }
    }

    @Test
    default void removeFirstTest() {
        MutableList<Object> empty = of();
        assertThrows(NoSuchElementException.class, empty::removeFirst);
        assertNull(empty.removeFirstOrNull());
        assertEquals(Option.none(), empty.removeFirstOption());
    }

    @Test
    default void removeLastTest() {
        MutableList<Object> empty = of();
        assertThrows(NoSuchElementException.class, empty::removeLast);
        assertNull(empty.removeLastOrNull());
        assertEquals(Option.none(), empty.removeLastOption());
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

    @Test
    default void clearTest() {
        MutableList<Integer> list = of();
        list.clear();
        assertIterableEquals(List.of(), list);

        list.appendAll(List.of(1, 2, 3));
        list.clear();
        assertIterableEquals(List.of(), list);

        list.clear();
        assertIterableEquals(List.of(), list);

        list.append(1);
        list.prepend(2);
        list.clear();
        assertIterableEquals(List.of(), list);
    }
}
