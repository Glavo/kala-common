package asia.kala.iterator;

import asia.kala.Tuple2;
import org.junit.jupiter.api.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
    public void testOf() {
        assertAll(
                () -> assertIteratorElements(Iterators.of()),
                () -> assertIteratorElements(Iterators.of("foo"), "foo"),
                () -> assertIteratorElements(Iterators.of("A", "B", "C"), "A", "B", "C")
        );
    }

    @Test
    @SuppressWarnings({"unchecked", "RedundantSuppression"})
    public void testConcat() {
        assertAll(
                () -> assertIteratorElements(Iterators.concat()),
                () -> assertIteratorElements(Iterators.concat(Iterators.of("A")), "A"),
                () -> assertIteratorElements(Iterators.concat(Iterators.of("A"), Iterators.of("B")), "A", "B"),
                () -> assertIteratorElements(Iterators.concat(Iterators.empty(), Iterators.of("A")), "A"),
                () -> assertIteratorElements(Iterators.concat(Iterators.of("A"), Iterators.of("B"), Iterators.of(("C"))), "A", "B", "C")
        );
    }

    @Test
    public void testSize() {
        assertAll(
                () -> assertEquals(0, Iterators.size(Iterators.empty())),
                () -> assertEquals(1, Iterators.size(Iterators.of("A"))),
                () -> assertEquals(2, Iterators.size(Iterators.of("A", "B"))),
                () -> assertEquals(3, Iterators.size(Iterators.of("A", "B", "C")))
        );
    }

    @Test
    public void testContains() {
        assertAll(
                () -> assertFalse(Iterators.contains(Iterators.empty(), "A")),
                () -> assertFalse(Iterators.contains(Iterators.empty(), 0)),
                () -> assertTrue(Iterators.contains(Iterators.of("A"), "A")),
                () -> assertFalse(Iterators.contains(Iterators.of("A"), 0)),
                () -> assertFalse(Iterators.contains(Iterators.of("A"), "B")),
                () -> assertFalse(Iterators.contains(Iterators.of("A", null, "foo", 10), "B")),
                () -> assertFalse(Iterators.contains(Iterators.of("A", null, "foo", 10), 20)),
                () -> assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), "A")),
                () -> assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), null)),
                () -> assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), "foo")),
                () -> assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), 10)),
                () -> assertTrue(Iterators.contains(Iterators.of("A", null, "foo", 10), 10))
        );
    }

    @Test
    public void testSameElements() {
        assertAll(
                () -> assertTrue(Iterators.sameElements(Iterators.empty(), Iterators.empty())),
                () -> assertFalse(Iterators.sameElements(Iterators.of("A"), Iterators.empty())),
                () -> assertFalse(Iterators.sameElements(Iterators.empty(), Iterators.of("A"))),
                () -> assertTrue(Iterators.sameElements(Iterators.of("A", "B"), Iterators.of("A", "B"))),
                () -> assertFalse(Iterators.sameElements(Iterators.of("A", "B", "C"), Iterators.of("A", "B"))),
                () -> assertFalse(Iterators.sameElements(Iterators.of("A", "B"), Iterators.of("A", "B", "C")))
        );
    }

    @Test
    public void testDrop() {
        assertAll(
                () -> {
                    assertIteratorElements(Iterators.drop(Iterators.empty(), 0));
                    assertIteratorElements(Iterators.drop(Iterators.empty(), 1));
                    assertIteratorElements(Iterators.drop(Iterators.empty(), Integer.MAX_VALUE));
                    assertIteratorElements(Iterators.drop(Iterators.empty(), -1));
                    assertIteratorElements(Iterators.drop(Iterators.empty(), Integer.MIN_VALUE));
                },
                () -> assertIteratorElements(Iterators.drop(Iterators.of("foo"), 0), "foo"),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("foo"), -1), "foo"),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("foo"), 1)),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("foo"), Integer.MAX_VALUE)),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 0), "A", "B", "C"),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), -1), "A", "B", "C"),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), Integer.MIN_VALUE), "A", "B", "C"),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 1), "B", "C"),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 2), "C"),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 3)),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), 4)),
                () -> assertIteratorElements(Iterators.drop(Iterators.of("A", "B", "C"), Integer.MAX_VALUE))
        );
    }

    @Test
    public void testDropWhile() {
        assertAll(
                () -> assertIteratorElements(Iterators.dropWhile(Iterators.empty(), t -> false)),
                () -> assertIteratorElements(Iterators.dropWhile(Iterators.empty(), t -> true)),
                () -> assertIteratorElements(Iterators.dropWhile(Iterators.of("A"), t -> true)),
                () -> assertIteratorElements(Iterators.dropWhile(Iterators.of("A", "B", "C"), t -> true)),
                () -> assertIteratorElements(Iterators.dropWhile(Iterators.of("A", "BB", "CCC"), t -> t.length() < 3), "CCC")
        );
    }

    @Test
    public void testTake() {
        assertAll(
                () -> assertIteratorElements(Iterators.take(Iterators.empty(), 0)),
                () -> assertIteratorElements(Iterators.take(Iterators.empty(), -1)),
                () -> assertIteratorElements(Iterators.take(Iterators.empty(), Integer.MIN_VALUE)),
                () -> assertIteratorElements(Iterators.take(Iterators.empty(), 1)),
                () -> assertIteratorElements(Iterators.take(Iterators.empty(), Integer.MAX_VALUE)),
                () -> assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 0)),
                () -> assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), -1)),
                () -> assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), Integer.MIN_VALUE)),
                () -> assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 1), "A"),
                () -> assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 2), "A", "B"),
                () -> assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 3), "A", "B", "C"),
                () -> assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), 4), "A", "B", "C"),
                () -> assertIteratorElements(Iterators.take(Iterators.of("A", "B", "C"), Integer.MAX_VALUE), "A", "B", "C")
        );
    }

    @Test
    public void testTakeWhile() {
        assertAll(
                () -> assertIteratorElements(Iterators.takeWhile(Iterators.empty(), s -> true)),
                () -> assertIteratorElements(Iterators.takeWhile(Iterators.empty(), s -> false)),
                () -> assertIteratorElements(Iterators.takeWhile(Iterators.of("A"), s -> false)),
                () -> assertIteratorElements(Iterators.takeWhile(Iterators.of("A"), s -> true), "A"),
                () -> assertIteratorElements(Iterators.takeWhile(Iterators.of("A", "B"), s -> false)),
                () -> assertIteratorElements(Iterators.takeWhile(Iterators.of("A", "B"), s -> true), "A", "B"),
                () -> assertIteratorElements(Iterators.takeWhile(Iterators.of("A", "AB", "foo", "bar"), s -> s.length() < 3), "A", "AB")
        );
    }

    @Test
    public void testUpdated() {
        assertAll(
                () -> assertIteratorElements(Iterators.updated(Iterators.empty(), 0, "foo")),
                () -> assertIteratorElements(Iterators.updated(Iterators.empty(), -1, "foo")),
                () -> assertIteratorElements(Iterators.updated(Iterators.empty(), Integer.MIN_VALUE, "foo")),
                () -> assertIteratorElements(Iterators.updated(Iterators.empty(), 1, "foo")),
                () -> assertIteratorElements(Iterators.updated(Iterators.empty(), Integer.MAX_VALUE, "foo")),
                () -> assertIteratorElements(Iterators.updated(Iterators.of("A"), 0, "foo"), "foo"),
                () -> assertIteratorElements(Iterators.updated(Iterators.of("A"), -1, "foo"), "A"),
                () -> assertIteratorElements(Iterators.updated(Iterators.of("A"), Integer.MIN_VALUE, "foo"), "A"),
                () -> assertIteratorElements(Iterators.updated(Iterators.of("A"), 1, "foo"), "A"),
                () -> assertIteratorElements(Iterators.updated(Iterators.of("A"), Integer.MAX_VALUE, "foo"), "A"),

                () -> assertIteratorElements(
                        Iterators.updated(Iterators.of("A", "B", "C"), 0, "foo"),
                        "foo", "B", "C"
                ),
                () -> assertIteratorElements(
                        Iterators.updated(Iterators.of("A", "B", "C"), 1, "foo"),
                        "A", "foo", "C"
                ),
                () -> assertIteratorElements(
                        Iterators.updated(Iterators.of("A", "B", "C"), 2, "foo"),
                        "A", "B", "foo"
                ),
                () -> assertIteratorElements(
                        Iterators.updated(Iterators.of("A", "B", "C"), 3, "foo"),
                        "A", "B", "C"
                ),
                () -> assertIteratorElements(
                        Iterators.updated(Iterators.of("A", "B", "C"), 4, "foo"),
                        "A", "B", "C"
                ),
                () -> assertIteratorElements(
                        Iterators.updated(Iterators.of("A", "B", "C"), Integer.MAX_VALUE, "foo"),
                        "A", "B", "C"
                ),
                () -> assertIteratorElements(
                        Iterators.updated(Iterators.of("A", "B", "C"), -1, "foo"),
                        "A", "B", "C"
                ),
                () -> assertIteratorElements(
                        Iterators.updated(Iterators.of("A", "B", "C"), Integer.MIN_VALUE, "foo"),
                        "A", "B", "C"
                )
        );
    }

    @Test
    public void testPrepended() {
        assertAll(
                () -> assertIteratorElements(Iterators.prepended(Iterators.empty(), "A"), "A"),
                () -> assertIteratorElements(Iterators.prepended(Iterators.of("foo"), "A"), "A", "foo"),
                () -> assertIteratorElements(Iterators.prepended(Iterators.of("foo", "bar"), "A"), "A", "foo", "bar")
        );
    }

    @Test
    public void testAppended() {
        assertAll(
                () -> assertIteratorElements(Iterators.appended(Iterators.empty(), "A"), "A"),
                () -> assertIteratorElements(Iterators.appended(Iterators.of("foo"), "A"), "foo", "A"),
                () -> assertIteratorElements(Iterators.appended(Iterators.of("foo", "bar"), "A"), "foo", "bar", "A")
        );
    }

    @Test
    public void testFilter() {
        assertAll(
                () -> assertIteratorElements(Iterators.filter(Iterators.empty(), t -> false)),
                () -> assertIteratorElements(Iterators.filter(Iterators.empty(), t -> true)),
                () -> assertIteratorElements(Iterators.filter(Iterators.of("foo", "bar"), t -> t.equals("foo")), "foo"),
                () -> assertIteratorElements(
                        Iterators.filter(Iterators.of("A", "B", "long string", "foo", "bar"), t -> t.length() == 3),
                        "foo", "bar"
                )
        );
    }

    @Test
    public void testFilterNot() {
        assertAll(
                () -> assertIteratorElements(Iterators.filterNot(Iterators.empty(), t -> false)),
                () -> assertIteratorElements(Iterators.filterNot(Iterators.empty(), t -> true)),
                () -> assertIteratorElements(Iterators.filterNot(Iterators.of("foo", "bar"), t -> t.equals("foo")), "bar"),
                () -> assertIteratorElements(
                        Iterators.filterNot(Iterators.of("A", "B", "long string", "foo", "bar", "foo bar"), t -> t.length() == 3),
                        "A", "B", "long string", "foo bar"
                )
        );
    }

    @Test
    public void testFilterNotNull() {
        assertAll(
                () -> assertIteratorElements(Iterators.filterNotNull(Iterators.empty())),
                () -> assertIteratorElements(Iterators.filterNotNull(Iterators.of("foo", "bar")), "foo", "bar"),
                () -> assertIteratorElements(
                        Iterators.filterNotNull(Iterators.of(null, "A", null, "B", null, "long string", null)),
                        "A", "B", "long string"
                )
        );
    }

    @Test
    public void testMap() {
        assertAll(
                () -> assertIteratorElements(Iterators.map(Iterators.empty(), t -> "A")),
                () -> assertIteratorElements(Iterators.map(Iterators.of("A"), String::length), 1),
                () -> assertIteratorElements(Iterators.map(Iterators.of("A", "foo"), String::length), 1, 3),
                () -> assertIteratorElements(Iterators.map(Iterators.of("A", "foo", "B"), String::length), 1, 3, 1)
        );
    }

    @Test
    public void testSpan() {
        assertAll(
                () -> {
                    Tuple2<Iterator<Object>, Iterator<Object>> t = Iterators.span(Iterators.empty(), e -> true);
                    assertIteratorElements(t.component1());
                    assertIteratorElements(t.component2());
                },
                () -> {
                    Tuple2<Iterator<Object>, Iterator<Object>> t = Iterators.span(Iterators.empty(), e -> false);
                    assertIteratorElements(t.component1());
                    assertIteratorElements(t.component2());
                },
                () -> {
                    Tuple2<Iterator<String>, Iterator<String>> t =
                            Iterators.span(Iterators.of("A", "B", "foo", "C"), s -> true);
                    assertIteratorElements(t.component1(), "A", "B", "foo", "C");
                    assertIteratorElements(t.component2());
                },
                () -> {
                    Tuple2<Iterator<String>, Iterator<String>> t =
                            Iterators.span(Iterators.of("A", "B", "foo", "C"), s -> false);
                    assertIteratorElements(t.component1());
                    assertIteratorElements(t.component2(), "A", "B", "foo", "C");
                },
                () -> {
                    Tuple2<Iterator<String>, Iterator<String>> t =
                            Iterators.span(Iterators.of("A", "B", "foo", "C"), s -> s.length() < 3);
                    assertIteratorElements(t.component1(), "A", "B");
                    assertIteratorElements(t.component2(), "foo", "C");
                }, () -> {
                    Tuple2<Iterator<String>, Iterator<String>> t =
                            Iterators.span(Iterators.of("A", "B", "foo", "C"), s -> s.length() >= 3);
                    assertIteratorElements(t.component1());
                    assertIteratorElements(t.component2(), "A", "B", "foo", "C");
                }
        );
    }


}
