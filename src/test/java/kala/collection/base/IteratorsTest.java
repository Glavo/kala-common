package kala.collection.base;

import kala.tuple.Tuple2;
import kala.tuple.primitive.IntObjTuple2;
import org.junit.jupiter.api.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class IteratorsTest {
    static void assertIteratorElements(Iterator<?> it, Object... values) {
        for (Object value : values) {
            if (it.hasNext()) {
                assertEquals(value, it.next());
            } else {
                fail("too few elements");
            }
        }
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void ofTest() {
        assertIteratorElements(Iterators.of());
        assertIteratorElements(Iterators.of((Object) null), (Object) null);
        assertIteratorElements(Iterators.of("A"), "A");
        assertIteratorElements(Iterators.of("A", "B"), "A", "B");
        assertIteratorElements(Iterators.of("A", "B", "C"), "A", "B", "C");
        assertIteratorElements(Iterators.of("A", "B", "C", "D"), "A", "B", "C", "D");
        assertIteratorElements(Iterators.of("A", "B", "C", "D", "E"), "A", "B", "C", "D", "E");
    }

    @Test
    public void fillTest() {
        assertIteratorElements(Iterators.fill(-1, "foo"));
        assertIteratorElements(Iterators.fill(Integer.MIN_VALUE, "foo"));
        assertIteratorElements(Iterators.fill(0, "foo"));
        assertIteratorElements(Iterators.fill(1, "foo"), "foo");
        assertIteratorElements(Iterators.fill(2, "foo"), "foo", "foo");
        assertIteratorElements(Iterators.fill(10, "foo"), ObjectArrays.fill(10, "foo"));

        class TestAdder implements Supplier<Integer> {
            private int i = 0;

            @Override
            public Integer get() {
                return i++;
            }
        }

        assertIteratorElements(Iterators.fill(-1, new TestAdder()));
        assertIteratorElements(Iterators.fill(Integer.MIN_VALUE, new TestAdder()));
        assertIteratorElements(Iterators.fill(0, new TestAdder()));
        assertIteratorElements(Iterators.fill(1, new TestAdder()), 0);
        assertIteratorElements(Iterators.fill(2, new TestAdder()), 0, 1);
        assertIteratorElements(Iterators.fill(10, new TestAdder()), ObjectArrays.fill(10, new TestAdder()));


        IntFunction<String> fun = i -> "foo" + i;
        assertIteratorElements(Iterators.fill(-1, fun));
        assertIteratorElements(Iterators.fill(Integer.MIN_VALUE, fun));
        assertIteratorElements(Iterators.fill(0, fun));
        assertIteratorElements(Iterators.fill(1, fun), "foo0");
        assertIteratorElements(Iterators.fill(2, fun), "foo0", "foo1");
        assertIteratorElements(Iterators.fill(10, fun), ObjectArrays.fill(10, fun));

    }

    @Test
    @SuppressWarnings({"unchecked", "RedundantSuppression"})
    public void concatTest() {
        assertIteratorElements(Iterators.concat());
        assertIteratorElements(Iterators.concat(Iterators.of("A")), "A");
        assertIteratorElements(Iterators.concat(Iterators.of("A"), Iterators.of("B")), "A", "B");
        assertIteratorElements(Iterators.concat(Iterators.empty(), Iterators.of("A")), "A");
        assertIteratorElements(Iterators.concat(new Iterator[]{Iterators.empty(), Iterators.of("A")}), "A");
        assertIteratorElements(Iterators.concat(Iterators.of("A"), Iterators.of("B"), Iterators.of(("C"))), "A", "B", "C");
    }

    @Test
    public void sizeTest() {
        assertEquals(0, Iterators.size(Iterators.empty()));
        assertEquals(1, Iterators.size(Iterators.of("A")));
        assertEquals(2, Iterators.size(Iterators.of("A", "B")));
        assertEquals(3, Iterators.size(Iterators.of("A", "B", "C")));
    }

    @Test
    public void reversedTest() {
        assertIteratorElements(Iterators.reversed(Iterators.empty()));
        assertIteratorElements(Iterators.reversed(Iterators.of("A")), "A");
        assertIteratorElements(Iterators.reversed(Iterators.of("A", "B")), "B", "A");
        assertIteratorElements(Iterators.reversed(Iterators.of("A", "B", "C")), "C", "B", "A");
    }

    @Test
    public void containsTest() {
        assertFalse(Iterators.contains(Iterators.empty(), "A"));
        assertFalse(Iterators.contains(Iterators.empty(), 0));
        assertTrue(Iterators.contains(Iterators.of("A"), "A"));
        assertFalse(Iterators.contains(Iterators.of("A"), 0));
        assertFalse(Iterators.contains(Iterators.of("A"), "B"));
        assertFalse(Iterators.contains(Iterators.of("A", null, "foo", 10), "B"));
        assertFalse(Iterators.contains(Iterators.of("A", null, "foo", 10), 20));
        assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), "A"));
        assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), null));
        assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), "foo"));
        assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), 10));
        assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), 10));
    }

    @Test
    public void sameElementsTest() {
        assertTrue(Iterators.sameElements(Iterators.empty(), Iterators.empty(), false));
        assertFalse(Iterators.sameElements(Iterators.of("A"), Iterators.empty(), false));
        assertFalse(Iterators.sameElements(Iterators.empty(), Iterators.of("A"), false));
        assertTrue(Iterators.sameElements(Iterators.of("A", "B"), Iterators.of("A", "B"), false));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B", "C"), Iterators.of("A", "B"), false));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B"), Iterators.of("A", "B", "C"), false));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B", "C"), Iterators.of("A", "B", "D"), false));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B", "C"), Iterators.of("A", "D", "C"), false));

        assertTrue(Iterators.sameElements(Iterators.empty(), Iterators.empty(), true));
        assertFalse(Iterators.sameElements(Iterators.of("A"), Iterators.empty(), true));
        assertFalse(Iterators.sameElements(Iterators.empty(), Iterators.of("A"), true));
        assertTrue(Iterators.sameElements(Iterators.of("A", "B"), Iterators.of("A", "B"), true));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B", "C"), Iterators.of("A", "B"), true));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B"), Iterators.of("A", "B", "C"), true));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B", "C"), Iterators.of("A", "B", "D"), true));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B", "C"), Iterators.of("A", "D", "C"), true));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B", "C"), Iterators.of("A", "B", new String("D")), true));
        assertFalse(Iterators.sameElements(Iterators.of("A", "B", "C"), Iterators.of("A", new String("D"), "C"), true));
    }

    @Test
    public void countTest() {
        assertEquals(0, Iterators.count(Iterators.empty(), i -> false));
        assertEquals(0, Iterators.count(Iterators.of(0, 1, 2, 3, 4, 5), i -> false));
        assertEquals(6, Iterators.count(Iterators.of(0, 1, 2, 3, 4, 5), i -> true));
        assertEquals(3, Iterators.count(Iterators.of(0, 1, 2, 3, 4, 5), i -> i % 2 == 0));
        assertEquals(2, Iterators.count(Iterators.of(0, 1, 2, 3, 4, 5), i -> i < 2));
    }

    @Test
    public void anyMatchTest() {
        assertFalse(Iterators.anyMatch(Iterators.empty(), i -> true));
        assertFalse(Iterators.anyMatch(Iterators.empty(), i -> false));
        assertFalse(Iterators.anyMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i > 5));
        assertTrue(Iterators.anyMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i == 0));
        assertTrue(Iterators.anyMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i == 3));
        assertTrue(Iterators.anyMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i == 5));
    }

    @Test
    public void allMatchTest() {
        assertTrue(Iterators.allMatch(Iterators.empty(), i -> true));
        assertTrue(Iterators.allMatch(Iterators.empty(), i -> false));
        assertFalse(Iterators.allMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i > 5));
        assertFalse(Iterators.allMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i == 0));
        assertFalse(Iterators.allMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i == 3));
        assertFalse(Iterators.allMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i == 5));
        assertTrue(Iterators.allMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i <= 5));
    }

    @Test
    public void noneMatchTest() {
        assertTrue(Iterators.noneMatch(Iterators.empty(), i -> true));
        assertTrue(Iterators.noneMatch(Iterators.empty(), i -> false));
        assertTrue(Iterators.noneMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i > 5));
        assertFalse(Iterators.noneMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i == 0));
        assertFalse(Iterators.noneMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i == 3));
        assertFalse(Iterators.noneMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i == 5));
        assertFalse(Iterators.noneMatch(Iterators.of(0, 1, 2, 3, 4, 5), i -> i <= 5));
    }

    @Test
    public void firstTest() {
        assertThrows(NoSuchElementException.class, () -> Iterators.first(Iterators.empty()));
        assertEquals("str0", Iterators.first(Iterators.of("str0")));
        assertEquals("str0", Iterators.first(Iterators.of("str0", "str1")));

        assertThrows(NoSuchElementException.class, () -> Iterators.first(Iterators.empty(), s -> true));
        assertEquals("0", Iterators.first(Iterators.of("0"), s -> true));
        assertThrows(NoSuchElementException.class, () -> Iterators.first(Iterators.of("str0"), s -> false));
        assertEquals("1", Iterators.first(Iterators.of("0", "1", "2"), s -> Integer.parseInt(s) == 1));
        assertEquals("2", Iterators.first(Iterators.of("0", "1", "2"), s -> Integer.parseInt(s) == 2));
    }

    @Test
    public void firstOrNullTest() {
        assertNull(Iterators.firstOrNull(Iterators.empty()));
        assertEquals("str0", Iterators.firstOrNull(Iterators.of("str0")));
        assertEquals("str0", Iterators.firstOrNull(Iterators.of("str0", "str1")));

        assertNull(Iterators.firstOrNull(Iterators.empty(), s -> true));
        assertEquals("0", Iterators.firstOrNull(Iterators.of("0"), s -> true));
        assertNull(Iterators.firstOrNull(Iterators.of("str0"), s -> false));
        assertEquals("1", Iterators.firstOrNull(Iterators.of("0", "1", "2"), s -> Integer.parseInt(s) == 1));
        assertEquals("2", Iterators.firstOrNull(Iterators.of("0", "1", "2"), s -> Integer.parseInt(s) == 2));
    }

    @Test
    public void dropTest() {
        assertThrows(IllegalArgumentException.class, () -> Iterators.drop(Iterators.empty(), -1));
        assertThrows(IllegalArgumentException.class, () -> Iterators.drop(Iterators.empty(), Integer.MIN_VALUE));
        assertIteratorElements(Iterators.drop(Iterators.empty(), 0));
        assertIteratorElements(Iterators.drop(Iterators.empty(), 1));
        assertIteratorElements(Iterators.drop(Iterators.empty(), Integer.MAX_VALUE));

        assertThrows(IllegalArgumentException.class, () -> Iterators.drop(Iterators.of("foo"), -1));
        assertThrows(IllegalArgumentException.class, () -> Iterators.drop(Iterators.of("foo"), Integer.MIN_VALUE));
        assertIteratorElements(Iterators.drop(Iterators.of("foo"), 0), "foo");
        assertIteratorElements(Iterators.drop(Iterators.of("foo"), 1));
        assertIteratorElements(Iterators.drop(Iterators.of("foo"), Integer.MAX_VALUE));

        assertThrows(IllegalArgumentException.class, () -> Iterators.drop(Iterators.of("A", "B", "C"), -1));
        assertThrows(IllegalArgumentException.class, () -> Iterators.drop(Iterators.of("A", "B", "C"), Integer.MIN_VALUE));
        assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 0), "A", "B", "C");
        assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 1), "B", "C");
        assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 2), "C");
        assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 3));
        assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 4));
        assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), Integer.MAX_VALUE));
    }

    @Test
    public void dropWhileTest() {
        assertIteratorElements(Iterators.dropWhile(Iterators.empty(), t -> false));
        assertIteratorElements(Iterators.dropWhile(Iterators.empty(), t -> true));
        assertIteratorElements(Iterators.dropWhile(Iterators.of("A"), t -> true));
        assertIteratorElements(Iterators.dropWhile(Iterators.of("A", "B", "C"), t -> true));
        assertIteratorElements(Iterators.dropWhile(Iterators.of("A", "BB", "CCC"), t -> t.length() < 3), "CCC");
    }

    @Test
    public void takeTest() {
        assertThrows(IllegalArgumentException.class, () -> Iterators.take(Iterators.empty(), -1));
        assertThrows(IllegalArgumentException.class, () -> Iterators.take(Iterators.empty(), Integer.MIN_VALUE));
        assertIteratorElements(Iterators.take(Iterators.empty(), 0));
        assertIteratorElements(Iterators.take(Iterators.empty(), 1));
        assertIteratorElements(Iterators.take(Iterators.empty(), Integer.MAX_VALUE));

        assertThrows(IllegalArgumentException.class, () -> Iterators.take(Iterators.of("A", "B", "C"), -1));
        assertThrows(IllegalArgumentException.class, () -> Iterators.take(Iterators.of("A", "B", "C"), Integer.MIN_VALUE));
        assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 0));
        assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 1), "A");
        assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 2), "A", "B");
        assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 3), "A", "B", "C");
        assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 4), "A", "B", "C");
        assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), Integer.MAX_VALUE), "A", "B", "C");
    }

    @Test
    public void takeWhileTest() {
        assertIteratorElements(Iterators.takeWhile(Iterators.empty(), s -> true));
        assertIteratorElements(Iterators.takeWhile(Iterators.empty(), s -> false));
        assertIteratorElements(Iterators.takeWhile(Iterators.of("A"), s -> false));
        assertIteratorElements(Iterators.takeWhile(Iterators.of("A"), s -> true), "A");
        assertIteratorElements(Iterators.takeWhile(Iterators.of("A", "B"), s -> false));
        assertIteratorElements(Iterators.takeWhile(Iterators.of("A", "B"), s -> true), "A", "B");
        assertIteratorElements(Iterators.takeWhile(Iterators.of("A", "AB", "foo", "bar"), s -> s.length() < 3), "A", "AB");
    }

    @Test
    public void updatedTest() {
        assertIteratorElements(Iterators.updated(Iterators.empty(), 0, "foo"));
        assertIteratorElements(Iterators.updated(Iterators.empty(), -1, "foo"));
        assertIteratorElements(Iterators.updated(Iterators.empty(), Integer.MIN_VALUE, "foo"));
        assertIteratorElements(Iterators.updated(Iterators.empty(), 1, "foo"));
        assertIteratorElements(Iterators.updated(Iterators.empty(), Integer.MAX_VALUE, "foo"));
        assertIteratorElements(Iterators.updated(Iterators.of("A"), 0, "foo"), "foo");
        assertIteratorElements(Iterators.updated(Iterators.of("A"), -1, "foo"), "A");
        assertIteratorElements(Iterators.updated(Iterators.of("A"), Integer.MIN_VALUE, "foo"), "A");
        assertIteratorElements(Iterators.updated(Iterators.of("A"), 1, "foo"), "A");
        assertIteratorElements(Iterators.updated(Iterators.of("A"), Integer.MAX_VALUE, "foo"), "A");

        assertIteratorElements(
                Iterators.updated(Iterators.of("A", "B", "C"), 0, "foo"),
                "foo", "B", "C"
        );
        assertIteratorElements(
                Iterators.updated(Iterators.of("A", "B", "C"), 1, "foo"),
                "A", "foo", "C"
        );
        assertIteratorElements(
                Iterators.updated(Iterators.of("A", "B", "C"), 2, "foo"),
                "A", "B", "foo"
        );
        assertIteratorElements(
                Iterators.updated(Iterators.of("A", "B", "C"), 3, "foo"),
                "A", "B", "C"
        );
        assertIteratorElements(
                Iterators.updated(Iterators.of("A", "B", "C"), 4, "foo"),
                "A", "B", "C"
        );
        assertIteratorElements(
                Iterators.updated(Iterators.of("A", "B", "C"), Integer.MAX_VALUE, "foo"),
                "A", "B", "C"
        );
        assertIteratorElements(
                Iterators.updated(Iterators.of("A", "B", "C"), -1, "foo"),
                "A", "B", "C"
        );
        assertIteratorElements(
                Iterators.updated(Iterators.of("A", "B", "C"), Integer.MIN_VALUE, "foo"),
                "A", "B", "C"
        );
    }

    @Test
    public void prependedTest() {
        assertIteratorElements(Iterators.prepended(Iterators.empty(), "A"), "A");
        assertIteratorElements(Iterators.prepended(Iterators.of("foo"), "A"), "A", "foo");
        assertIteratorElements(Iterators.prepended(Iterators.of("foo", "bar"), "A"), "A", "foo", "bar");

        assertIteratorElements(Iterators.prepended(Iterators.empty(), null), (Object) null);
        assertIteratorElements(Iterators.prepended(Iterators.of("foo"), null), null, "foo");
        assertIteratorElements(Iterators.prepended(Iterators.of("foo", "bar"), null), null, "foo", "bar");
    }

    @Test
    public void appendedTest() {
        assertIteratorElements(Iterators.appended(Iterators.empty(), "A"), "A");
        assertIteratorElements(Iterators.appended(Iterators.of("foo"), "A"), "foo", "A");
        assertIteratorElements(Iterators.appended(Iterators.of("foo", "bar"), "A"), "foo", "bar", "A");
    }

    @Test
    public void filterTest() {
        assertIteratorElements(Iterators.filter(Iterators.empty(), t -> false));
        assertIteratorElements(Iterators.filter(Iterators.empty(), t -> true));
        assertIteratorElements(Iterators.filter(Iterators.of("foo", "bar"), Predicate.isEqual("foo")), "foo");
        assertIteratorElements(Iterators.filter(Iterators.of("foo", "bar"), Predicate.isEqual("bar")), "bar");
        assertIteratorElements(Iterators.filter(Iterators.of("foo", "bar"), Predicate.isEqual("other")));
        assertIteratorElements(
                Iterators.filter(Iterators.of("A", "B", "long string", "foo", "bar", "other"), t -> t.length() == 3),
                "foo", "bar"
        );
    }

    @Test
    public void filterNotTest() {
        assertIteratorElements(Iterators.filterNot(Iterators.empty(), t -> false));
        assertIteratorElements(Iterators.filterNot(Iterators.empty(), t -> true));
        assertIteratorElements(Iterators.filterNot(Iterators.of("foo", "bar"), t -> t.equals("foo")), "bar");
        assertIteratorElements(
                Iterators.filterNot(Iterators.of("A", "B", "long string", "foo", "bar", "foo bar"), t -> t.length() == 3),
                "A", "B", "long string", "foo bar"
        );
    }

    @Test
    public void filterNotNullTest() {
        assertIteratorElements(Iterators.filterNotNull(Iterators.empty()));
        assertIteratorElements(Iterators.filterNotNull(Iterators.of("foo", "bar")), "foo", "bar");
        assertIteratorElements(
                Iterators.filterNotNull(Iterators.of(null, "A", null, "B", null, "long string", null)),
                "A", "B", "long string"
        );
    }

    @Test
    public void mapTest() {
        assertIteratorElements(Iterators.map(Iterators.empty(), t -> "A"));
        assertIteratorElements(Iterators.map(Iterators.of("A"), String::length), 1);
        assertIteratorElements(Iterators.map(Iterators.of("A", "foo"), String::length), 1, 3);
        assertIteratorElements(Iterators.map(Iterators.of("A", "foo", "B"), String::length), 1, 3, 1);
    }

    @Test
    public void spanTest() {
        {
            Tuple2<Iterator<Object>, Iterator<Object>> t = Iterators.span(Iterators.empty(), e -> true);
            assertIteratorElements(t.component1());
            assertIteratorElements(t.component2());
        }
        {
            Tuple2<Iterator<Object>, Iterator<Object>> t = Iterators.span(Iterators.empty(), e -> false);
            assertIteratorElements(t.component1());
            assertIteratorElements(t.component2());
        }
        {
            Tuple2<Iterator<String>, Iterator<String>> t =
                    Iterators.span(Iterators.of("A", "B", "foo", "C"), s -> true);
            assertIteratorElements(t.component1(), "A", "B", "foo", "C");
            assertIteratorElements(t.component2());
        }
        {
            Tuple2<Iterator<String>, Iterator<String>> t =
                    Iterators.span(Iterators.of("A", "B", "foo", "C"), s -> false);
            assertIteratorElements(t.component1());
            assertIteratorElements(t.component2(), "A", "B", "foo", "C");
        }
        {
            Tuple2<Iterator<String>, Iterator<String>> t =
                    Iterators.span(Iterators.of("A", "B", "foo", "C"), s -> s.length() < 3);
            assertIteratorElements(t.component1(), "A", "B");
            assertIteratorElements(t.component2(), "foo", "C");
        }
        {
            Tuple2<Iterator<String>, Iterator<String>> t =
                    Iterators.span(Iterators.of("A", "B", "foo", "C"), s -> s.length() >= 3);
            assertIteratorElements(t.component1());
            assertIteratorElements(t.component2(), "A", "B", "foo", "C");
        }
    }

    @Test
    public void withIndexTest() {
        assertIteratorElements(Iterators.withIndex(Iterators.empty()));
        assertIteratorElements(Iterators.withIndex(Iterators.of("str0")), IntObjTuple2.of(0, "str0"));
        assertIteratorElements(Iterators.withIndex(Iterators.of("str0", "str1")),
                IntObjTuple2.of(0, "str0"),
                IntObjTuple2.of(1, "str1")
        );
        assertIteratorElements(Iterators.withIndex(Iterators.of("str0", "str1", "str2")),
                IntObjTuple2.of(0, "str0"),
                IntObjTuple2.of(1, "str1"),
                IntObjTuple2.of(2, "str2")
        );
    }
}
