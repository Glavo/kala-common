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
package kala.collection;

import kala.collection.base.GenericArrays;
import kala.collection.mutable.MutableArray;
import kala.concurrent.ConcurrentScope;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static kala.ExtendedAssertions.assertMapEntries;
import static kala.ExtendedAssertions.assertSetElements;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface SequentialCollectionLikeTestTemplate extends CollectionLikeTestTemplate {
    @Test
    default void filterTest() {
        assertIterableEquals(List.of(), of().filter(o -> true));
        assertIterableEquals(List.of(), of().filter(o -> false));

        assertIterableEquals(
                List.of(),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filter(i -> i > 3)
        );

        assertIterableEquals(
                List.of(1, 2, 3),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filter(i -> i > 0)
        );

        assertIterableEquals(
                List.of(-3, -2, -1, 0, 1, 2, 3),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filter(i -> i > -4)
        );

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), from(data).filter(i -> true));
            assertIterableEquals(List.of(), from(data).filter(i -> false));
        }
    }

    @Test
    default void filterNotTest() {
        assertIterableEquals(List.of(), of().filterNot(o -> true));
        assertIterableEquals(List.of(), of().filterNot(o -> false));

        assertIterableEquals(
                List.of(-3, -2, -1, 0, 1, 2, 3),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filterNot(i -> i > 3)
        );

        assertIterableEquals(
                List.of(-3, -2, -1, 0),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filterNot(i -> i > 0)
        );

        assertIterableEquals(
                List.of(),
                from(List.of(-3, -2, -1, 0, 1, 2, 3)).filterNot(i -> i > -4)
        );

        for (Integer[] data : data1()) {
            assertIterableEquals(List.of(), from(data).filterNot(i -> true));
            assertIterableEquals(Arrays.asList(data), from(data).filterNot(i -> false));
        }
    }

    @Test
    default void filterNotNullTest() {
        assertIterableEquals(List.of(), of().filterNotNull());

        assertIterableEquals(List.of(), of((String) null).filterNotNull());
        assertIterableEquals(List.of(), of(null, null).filterNotNull());
        assertIterableEquals(List.of(), of(null, null, null).filterNotNull());
        assertIterableEquals(List.of("foo"), of("foo").filterNotNull());
        assertIterableEquals(List.of("foo"), of(null, "foo").filterNotNull());
        assertIterableEquals(List.of("foo"), of(null, "foo", null).filterNotNull());
        assertIterableEquals(List.of("foo", "bar"), of(null, "foo", null, "bar").filterNotNull());

        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), from(data).filterNotNull(),
                    () -> String.format("expected: %s, actual: %s", Arrays.toString(data), from(data).filterNotNull()));
        }
    }

    @Test
    default void filterIsInstanceTest() {
        assertIterableEquals(List.of(), of().filterIsInstance(String.class));
        assertIterableEquals(List.of(), of(1, 2, 3).filterIsInstance(String.class));
        assertIterableEquals(List.of("foo"), of(1, 2, "foo", 3).filterIsInstance(String.class));
        assertIterableEquals(List.of("str0", "str1", "str2"), of("str0", "str1", "str2").filterIsInstance(String.class));
    }

    @Test
    default void mapTest() {
        assertIterableEquals(List.of(), of().map(i -> i));
        assertIterableEquals(List.of(), of().map(i -> {
            throw new AssertionError();
        }));


        Integer[][] data1 = data1();
        String[][] data1s = data1s();

        for (int i = 0; i < data1.length; i++) {
            Integer[] d = data1[i];
            String[] ds = data1s[i];

            assertIterableEquals(Arrays.asList(d), from(ds).map(Integer::parseInt));
        }
    }

    @Test
    default void mapNotNullTest() {
        assertIterableEquals(List.of(), of().mapNotNull(i -> i));
        assertIterableEquals(List.of(), of().mapNotNull(i -> {
            throw new AssertionError();
        }));
        Integer[][] data1 = data1();
        String[][] data1s = data1s();

        for (int i = 0; i < data1.length; i++) {
            Integer[] d = data1[i];
            String[] ds = data1s[i];

            assertIterableEquals(Arrays.asList(d), from(ds).mapNotNull(Integer::parseInt));
            assertIterableEquals(
                    Arrays.stream(d).filter(it -> it > 0).collect(Collectors.toList()),
                    from(ds).mapNotNull(s -> {
                        final int pi = Integer.parseInt(s);
                        return pi > 0 ? pi : null;
                    })
            );
        }
    }

    @Test
    default void mapMultiTest() {
        BiConsumer<Object, Consumer<?>> emptyAction = (it, c) -> {
        };

        assertIterableEquals(List.of(), of().mapMulti(emptyAction));
        assertIterableEquals(List.of(), of().mapMulti((it, c) -> c.accept("foo")));
        assertIterableEquals(List.of(), of("str0", "str1", "str2").mapMulti(emptyAction));
        assertIterableEquals(List.of("str0", "str1", "str2"), of("str0", "str1", "str2").mapMulti((e, c) -> c.accept(e)));
        assertIterableEquals(List.of("str0", "str0", "str1", "str1", "str2", "str2"), of("str0", "str1", "str2").mapMulti((e, c) -> {
            c.accept(e);
            c.accept(e);
        }));
    }

    @Test
    default void flatMapTest() {
        assertIterableEquals(List.of(), of().flatMap(o -> List.of("foo")));

        assertIterableEquals(List.of(1, 2, 2), from(List.of(0, 1, 2)).flatMap(i -> Collections.nCopies(i, i)));
        assertIterableEquals(List.of(1, 2, 2, 3, 3, 3), from(List.of(0, 1, 2, 3)).flatMap(i -> Collections.nCopies(i, i)));
        assertIterableEquals(List.of(), from(List.of(0, 1, 2, 3)).flatMap(i -> List.of()));
        assertIterableEquals(List.of(0, 1, 2, 3), from(List.of(0, 1, 2, 3)).flatMap(List::of));
    }

    @Test
    default void zipTest() {
        assertIterableEquals(List.of(), of().zip(List.of()));
        assertIterableEquals(List.of(), of().zip(List.of("str")));

        assertIterableEquals(List.of(), from(List.of(0)).zip(List.of()));

        Integer[][] data1 = data1();
        for (int i = 0; i < data1.length - 1; i++) {
            Integer[] d1 = data1[i];
            Integer[] d2 = data1[i + 1];

            Tuple2<Integer, Integer>[] d12 = new Tuple2[Math.min(d1.length, d2.length)];
            for (int j = 0; j < d12.length; j++) {
                d12[j] = Tuple.of(d1[j], d2[j]);
            }

            assertIterableEquals(Arrays.asList(d12), from(d1).zip(Arrays.asList(d2)));
        }
    }

    @Test
    default void joinToTest() {
        assertEquals("", of().joinToString(", "));
        assertEquals("", of().joinToString(", ", it -> "str"));
        assertEquals("1, 2, 3", of(1, 2, 3).joinToString(", "));
        assertEquals("2, 4, 6", of(1, 2, 3).joinToString(", ", it -> String.valueOf(it * 2)));
    }

    @Test
    default void toSeqTest() {
        assertIterableEquals(List.of(), of().toSeq());
        assertIterableEquals(List.of(1), of(1).toSeq());
        assertIterableEquals(List.of(1, 2, 3), of(1, 2, 3).toSeq());
    }

    @Test
    default void toArraySeqTest() {
        assertIterableEquals(List.of(), of().toArraySeq());
        assertIterableEquals(List.of(1), of(1).toArraySeq());
        assertIterableEquals(List.of(1, 2, 3), of(1, 2, 3).toArraySeq());
    }


    @Test
    default void toSetTest() {
        assertSetElements(List.of(), of().toSet());
        assertSetElements(List.of(1), of(1).toSet());
        assertSetElements(List.of(1, 2, 3, 4), of(1, 2, 3, 1, 4, 2).toSet());
    }

    @Test
    default void associateTest() {
        assertMapEntries(List.of(), this.<Integer>of().associate(value -> Tuple.of(value, value.toString())));
        assertMapEntries(List.of(Tuple.of(1, "1")), this.of(1).associate(value -> Tuple.of(value, value.toString())));
        assertMapEntries(List.of(Tuple.of(1, "1"), Tuple.of(2, "2"), Tuple.of(3, "3")),
                this.of(1, 2, 3).associate(value -> Tuple.of(value, value.toString())));
        assertMapEntries(List.of(Tuple.of(1, "1"), Tuple.of(2, "2"), Tuple.of(3, "3")),
                this.of(1, 2, 3, 1).associate(value -> Tuple.of(value, value.toString())));
    }

    @Test
    default void toArrayTest() {
        assertArrayEquals(GenericArrays.EMPTY_OBJECT_ARRAY, of().toArray());
        assertArrayEquals(GenericArrays.EMPTY_OBJECT_ARRAY, of().toArray(Object.class));
        assertArrayEquals(GenericArrays.EMPTY_OBJECT_ARRAY, of().toArray(Object[]::new));

        for (Integer[] data : data1()) {
            Object[] oa = from(data).toArray();
            assertSame(Object[].class, oa.getClass());
            assertArrayEquals(data, oa);

            for (Integer[] ia : List.of(
                    from(data).toArray(Integer[]::new),
                    from(data).toArray(Integer.class),
                    from(data).toArray(new Integer[data.length])
            )) {
                assertSame(Integer[].class, ia.getClass());
                assertArrayEquals(data, ia);
            }
        }
    }

    @Test
    default void forEachTest() {
        ArrayList<Object> al = new ArrayList<>();
        of().forEach(al::add);
        assertIterableEquals(List.of(), al);

        from(List.of(0, 1, 2)).forEach(al::add);
        assertIterableEquals(List.of(0, 1, 2), al);
        al.clear();

        for (Integer[] data : data1()) {
            from(data).forEach(al::add);
            assertIterableEquals(Arrays.asList(data), al);
            al.clear();
        }
    }

    @Test
    default void forEachParallel() {
        var pools = List.of(
                ForkJoinPool.commonPool(),
                Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(4)
        );

        for (ExecutorService pool : pools) {
            try (var scope = ConcurrentScope.withExecutor(pool)) {
                LongAdder adder = new LongAdder();

                of().forEachParallel(value -> adder.increment());
                assertEquals(0L, adder.sumThenReset());

                of(0, 1, 2).forEachParallel(value -> adder.increment());
                assertEquals(3L, adder.sumThenReset());

                var set = ConcurrentHashMap.<String>newKeySet();
                var values = java.util.Set.of("value0", "value1", "value2", "value3", "value4", "value5");
                from(values).forEachParallel(set::add);
                assertEquals(values, set);
            }

            if (pool != ForkJoinPool.commonPool()) {
                pool.shutdown();
            }
        }
    }

    @Test
    default void onEachTest() {
        ArrayList<Object> al = new ArrayList<>();
        var empty = of();
        assertSame(empty, empty.onEach(al::add));
        assertIterableEquals(List.of(), al);

        CollectionLike<Integer> seq = from(List.of(0, 1, 2));
        assertSame(seq, seq.onEach(al::add));
        assertIterableEquals(List.of(0, 1, 2), al);
        al.clear();

        for (Integer[] data : data1()) {
            seq = from(data);
            assertSame(seq, seq.onEach(al::add));
            assertIterableEquals(Arrays.asList(data), al);
            al.clear();
        }
    }

    @Test
    default void copyToTest() {
        CollectionLike<Integer> collection = of(0, 1, 2, 3);

        MutableArray<Integer> target;

        target = MutableArray.create(0);
        assertEquals(0, collection.copyTo(target, 0));
        assertEquals(0, collection.copyTo(target, 10));

        target = MutableArray.create(5);
        assertEquals(4, collection.copyTo(target, 0));
        assertIterableEquals(Arrays.asList(0, 1, 2, 3, null), target);

        target = MutableArray.create(5);
        assertEquals(4, collection.copyTo(target, 1));
        assertIterableEquals(Arrays.asList(null, 0, 1, 2, 3), target);

        target = MutableArray.create(5);
        assertEquals(3, collection.copyTo(target, 2));
        assertIterableEquals(Arrays.asList(null, null, 0, 1, 2), target);

        target = MutableArray.create(5);
        assertEquals(2, collection.copyTo(target, 2, 2));
        assertIterableEquals(Arrays.asList(null, null, 0, 1, null), target);


        if (collection instanceof SeqLike<Integer> seq) {
            target = MutableArray.create(0);
            assertEquals(0, seq.copyTo(0, target, 0));
            assertEquals(0, seq.copyTo(10, target, 0));
            assertEquals(0, seq.copyTo(10, target, 10));

            target = MutableArray.create(5);
            assertEquals(4, seq.copyTo(0, target, 0));
            assertIterableEquals(Arrays.asList(0, 1, 2, 3, null), target);

            target = MutableArray.create(5);
            assertEquals(3, seq.copyTo(1, target, 0));
            assertIterableEquals(Arrays.asList(1, 2, 3, null, null), target);

            target = MutableArray.create(5);
            assertEquals(2, seq.copyTo(1, target, 2, 2));
            assertIterableEquals(Arrays.asList(null, null, 1, 2, null), target);
        }
    }
}
