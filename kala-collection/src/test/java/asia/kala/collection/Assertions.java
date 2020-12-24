package asia.kala.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public final class Assertions {
    private Assertions() {
    }

    public static void assertIsEmpty(Iterable<?> c) {
        assertIsEmpty(c.iterator());
    }

    public static void assertIsEmpty(Iterator<?> it) {
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    public static void assertElements(Iterable<?> c, Object... elements) {
        assertElements(c.iterator(), elements);
    }

    public static void assertElements(Iterator<?> it, Object... elements) {
        for (Object e : elements) {
            assertTrue(it.hasNext());
            assertEquals(e, it.next());
        }
        assertIsEmpty(it);
    }

    public static void assertElementsEquals(Iterable<?> c1, Iterable<?> c2) {
        assertElementsEquals(c1.iterator(), c2.iterator());
    }

    public static void assertElementsEquals(Iterator<?> it1, Iterator<?> it2) {
        while (it1.hasNext() && it2.hasNext()) {
            assertEquals(it1.next(), it2.next());
        }
        assertIsEmpty(it1);
        assertIsEmpty(it2);
    }
}
