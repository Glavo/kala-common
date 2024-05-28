package kala.collection;

import kala.collection.base.GenericArrays;
import kala.collection.immutable.ImmutableLinkedSeq;
import kala.collection.mutable.MutableArray;
import kala.comparator.Comparators;
import kala.concurrent.ConcurrentScope;
import kala.control.Option;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public interface CollectionLikeTestTemplate {
    default Integer[][] data1() {
        return TestData.data1;
    }

    default String[][] data1s() {
        return TestData.data1s;
    }

    @SuppressWarnings("unchecked")
    <E> CollectionLike<E> of(E... elements);

    <E> CollectionLike<E> from(E[] elements);

    <E> CollectionLike<E> from(Iterable<? extends E> elements);

    @Test
    default void streamTest() {
        assertEquals(0, of().stream().count());
        assertEquals(0, of().parallelStream().count());

        assertIterableEquals(List.of("foo"), of("foo").stream().collect(Collectors.toList()));
        assertIterableEquals(List.of("foo"), of("foo").parallelStream().collect(Collectors.toList()));
    }

    @Test
    default void isEmptyTest() {
        assertTrue(of().isEmpty());
        assertTrue(from(GenericArrays.EMPTY_OBJECT_ARRAY).isEmpty());
        assertTrue(from(Collections.emptyList()).isEmpty());

        for (Integer[] data : data1()) {
            assertEquals(data.length == 0, from(data).isEmpty());
        }
    }

    @Test
    default void sizeTest() {
        assertEquals(0, of().size());
        for (Integer[] data : data1()) {
            assertEquals(data.length, from(data).size());
        }
    }

    @Test
    default void knownSizeTest() {
        assertTrue(of().knownSize() == 0 || of().knownSize() == -1);

        for (Integer[] data : data1()) {
            int ks = from(data).knownSize();
            assertTrue(ks == data.length || ks == -1);
        }
    }

    @Test
    default void sizeCompareTest() {
        assertEquals(0, of().sizeCompare(0));
        assertTrue(of().sizeCompare(1) < 0);
        assertTrue(of().sizeCompare(Integer.MAX_VALUE) < 0);
        assertTrue(of().sizeCompare(-1) > 0);
        assertTrue(of().sizeCompare(Integer.MIN_VALUE) > 0);

        for (Integer[] data : data1()) {
            CollectionLike<?> c = from(data);
            List<Integer> dl = Arrays.asList(data);

            assertEquals(0, c.sizeCompare(data.length));
            assertEquals(0, c.sizeCompare(dl));
            assertEquals(0, c.sizeCompare(new SimpleIterable<>(dl)));
            assertEquals(0, c.sizeCompare(ArraySeq.wrap(data)));

            List<Integer> dl1 = dl.subList(0, data.length - 1);

            assertTrue(c.sizeCompare(data.length - 1) > 0);
            assertTrue(c.sizeCompare(dl1) > 0);
            assertTrue(c.sizeCompare(new SimpleIterable<>(dl1)) > 0);
            assertTrue(c.sizeCompare(ArraySeq.from(dl1)) > 0);


            ArrayList<Integer> dl2 = new ArrayList<>(dl);
            dl2.add(0);

            assertTrue(c.sizeCompare(data.length + 1) < 0);
            assertTrue(c.sizeCompare(dl2) < 0);
            assertTrue(c.sizeCompare(new SimpleIterable<>(dl2)) < 0);
            assertTrue(c.sizeCompare(ArraySeq.from(dl2)) < 0);
        }
    }

    @Test
    default void containsTest() {
        CollectionLike<?> empty = of();
        assertFalse(empty.contains(null));
        assertFalse(empty.contains(0));

        CollectionLike<?> c = from(Arrays.asList(0, 1, 2));
        assertTrue(c.contains(0));
        assertTrue(c.contains(1));
        assertTrue(c.contains(2));
        assertFalse(c.contains(3));
        assertFalse(c.contains("foo"));

        for (Integer[] data : data1()) {
            c = this.<Integer>from(data);
            assertFalse(c.contains(0));
            assertFalse(c.contains(null));
            for (int d : data) {
                assertTrue(c.contains(d));
                assertFalse(c.contains(-d));
            }
        }
    }

    @Test
    default void containsAllTest() {
        CollectionLike<?> empty = of();
        assertTrue(empty.containsAll(List.of()));
        assertFalse(empty.containsAll(List.of("foo")));
        assertTrue(empty.containsAll(new Object[0]));
        assertTrue(empty.containsAll(new String[0]));
        assertFalse(empty.containsAll(new Object[]{"foo"}));
        assertFalse(empty.containsAll(new String[]{"foo"}));


        CollectionLike<String> c1 = this.of(
                "str1", "str2", "str3", "str4", "str5"
        );

        assertTrue(c1.containsAll(List.of()));
        assertTrue(c1.containsAll(new String[0]));
        assertTrue(c1.containsAll(List.of("str1")));
        assertTrue(c1.containsAll(new String[]{"str1"}));
        assertTrue(c1.containsAll(List.of("str1", "str2")));
        assertTrue(c1.containsAll(new String[]{"str1", "str2"}));
        assertTrue(c1.containsAll(List.of("str2", "str5", "str4")));
        assertTrue(c1.containsAll(new String[]{"str2", "str5", "str4"}));
        assertFalse(c1.containsAll(Collections.singletonList((String) null)));
        assertFalse(c1.containsAll(new String[]{null}));
        assertFalse(c1.containsAll(Arrays.asList(null, "str1")));
        assertFalse(c1.containsAll(new String[]{null, "str1"}));
        assertFalse(c1.containsAll(Arrays.asList("", "str1")));
        assertFalse(c1.containsAll(new String[]{"", "str1"}));
        assertFalse(c1.containsAll(Arrays.asList("other", "str1")));
        assertFalse(c1.containsAll(new String[]{"other", "str1"}));
    }

    @Test
    default void anyMatchTest() {
        assertFalse(of().anyMatch(e -> true));
        assertFalse(of().anyMatch(e -> false));

        assertFalse(this.<Integer>from(List.of(0, 1, 2, 3)).anyMatch(i -> i < 0));
        assertTrue(this.<Integer>from(List.of(0, 1, 2, 3)).anyMatch(i -> i > 0));
        assertFalse(this.<Integer>from(List.of(0, 1, 2, 3)).anyMatch(i -> i > 3));
    }

    @Test
    default void allMatchTest() {
        assertTrue(of().allMatch(e -> true));
        assertTrue(of().allMatch(e -> false));

        assertFalse(this.<Integer>from(List.of(0, 1, 2, 3)).allMatch(i -> i < 0));
        assertFalse(this.<Integer>from(List.of(0, 1, 2, 3)).allMatch(i -> i > 0));
        assertTrue(this.<Integer>from(List.of(0, 1, 2, 3)).allMatch(i -> i >= 0));
        assertFalse(this.<Integer>from(List.of(0, 1, 2, 3)).allMatch(i -> i > 3));
    }

    @Test
    default void noneMatchTest() {
        assertTrue(of().noneMatch(e -> true));
        assertTrue(of().noneMatch(e -> false));

        assertTrue(this.<Integer>from(List.of(0, 1, 2, 3)).noneMatch(i -> i < 0));
        assertFalse(this.<Integer>from(List.of(0, 1, 2, 3)).noneMatch(i -> i > 0));
        assertFalse(this.<Integer>from(List.of(0, 1, 2, 3)).noneMatch(i -> i >= 0));
        assertTrue(this.<Integer>from(List.of(0, 1, 2, 3)).noneMatch(i -> i > 3));
    }

    @Test
    default void countTest() {
        CollectionLike<?> empty = of();
        assertEquals(0, empty.count(Predicate.isEqual(true)));
        assertEquals(0, empty.count(Predicate.isEqual(false)));

        List<Predicate<Integer>> predicates = List.of(
                it -> it > 0,
                it -> it <= 0,
                it -> it % 3 == 0
        );

        for (Integer[] data : data1()) {
            for (Predicate<Integer> predicate : predicates) {
                assertEquals(
                        Arrays.stream(data).filter(predicate).count(),
                        from(data).count(predicate)
                );
            }
        }
    }

    @Test
    default void maxTest() {
        assertThrows(NoSuchElementException.class, () -> of().max());
        assertThrows(NoSuchElementException.class, () -> of().max(Comparators.naturalOrder()));
        assertNull(of().maxOrNull());
        assertNull(of().maxOrNull(Comparators.naturalOrder()));
        assertEquals(Option.none(), of().maxOption());
        assertEquals(Option.none(), of().maxOption(Comparators.naturalOrder()));

        Comparator<Integer> reversedComparator = Comparators.<Integer>naturalOrder().reversed();

        assertEquals(0, of(0).max());
        assertEquals(0, of(0).maxOrNull());
        assertEquals(Option.some(0), of(0).maxOption());

        assertEquals(0, of(0).max(reversedComparator));
        assertEquals(0, of(0).maxOrNull(reversedComparator));
        assertEquals(Option.some(0), of(0).maxOption(reversedComparator));

        assertEquals(2, of(0, 1, 2).max());
        assertEquals(2, of(0, 1, 2).maxOrNull());
        assertEquals(Option.some(2), of(0, 1, 2).maxOption());

        assertEquals(2, of(1, 0, 2, 1).max());
        assertEquals(2, of(1, 0, 2, 1).maxOrNull());
        assertEquals(Option.some(2), of(1, 0, 2, 1).maxOption());

        assertEquals(0, of(0, 1, 2).max(reversedComparator));
        assertEquals(0, of(0, 1, 2).maxOrNull(reversedComparator));
        assertEquals(Option.some(0), of(0, 1, 2).maxOption(reversedComparator));

        assertEquals(0, of(1, 0, 2, 1).max(reversedComparator));
        assertEquals(0, of(1, 0, 2, 1).maxOrNull(reversedComparator));
        assertEquals(Option.some(0), of(1, 0, 2, 1).maxOption(reversedComparator));
    }

    @Test
    default void minTest() {
        assertThrows(NoSuchElementException.class, () -> of().min());
        assertThrows(NoSuchElementException.class, () -> of().min(Comparators.naturalOrder()));
        assertNull(of().minOrNull());
        assertNull(of().minOrNull(Comparators.naturalOrder()));
        assertEquals(Option.none(), of().minOption());
        assertEquals(Option.none(), of().minOption(Comparators.naturalOrder()));

        Comparator<Integer> reversedComparator = Comparators.<Integer>naturalOrder().reversed();

        assertEquals(0, of(0).min());
        assertEquals(0, of(0).minOrNull());
        assertEquals(Option.some(0), of(0).minOption());

        assertEquals(0, of(0).min(reversedComparator));
        assertEquals(0, of(0).minOrNull(reversedComparator));
        assertEquals(Option.some(0), of(0).minOption(reversedComparator));

        assertEquals(0, of(0, 1, 2).min());
        assertEquals(0, of(0, 1, 2).minOrNull());
        assertEquals(Option.some(0), of(0, 1, 2).minOption());

        assertEquals(0, of(1, 0, 2, 1).min());
        assertEquals(0, of(1, 0, 2, 1).minOrNull());
        assertEquals(Option.some(0), of(1, 0, 2, 1).minOption());


        assertEquals(2, of(0, 1, 2).min(reversedComparator));
        assertEquals(2, of(0, 1, 2).minOrNull(reversedComparator));
        assertEquals(Option.some(2), of(0, 1, 2).minOption(reversedComparator));

        assertEquals(2, of(1, 0, 2, 1).min(reversedComparator));
        assertEquals(2, of(1, 0, 2, 1).minOrNull(reversedComparator));
        assertEquals(Option.some(2), of(1, 0, 2, 1).minOption(reversedComparator));
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
