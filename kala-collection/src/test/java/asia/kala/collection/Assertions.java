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
}
