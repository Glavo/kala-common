package kala.range;

import kala.comparator.Comparators;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RangeTest {

    @Test
    void allTest() {
        Range<Integer> all = Range.all();

        assertTrue(all.contains(0));
        assertTrue(all.contains(1));
        assertTrue(all.contains(Integer.MAX_VALUE));
        assertTrue(all.contains(Integer.MIN_VALUE));

        assertThrows(UnsupportedOperationException.class, () -> all.withStep(i -> i + 1));
    }

    @Test
    void isTest() {
        List.of(
                Range.is(10),
                Range.is(10, Integer::compare),
                Range.is(10, Comparators.naturalOrder()),
                Range.is(10, Comparators.reverseOrder())
        ).forEach(value -> {
            assertTrue(value.contains(10));
            assertFalse(value.contains(0));
            assertFalse(value.contains(-10));

            ArrayList<Integer> tmp = new ArrayList<>();
            value.withStep(it -> it + 1).forEach(tmp::add);
            assertIterableEquals(List.of(10), tmp);
        });

        assertThrows(IllegalArgumentException.class, () -> Range.is(new Object(), null));
    }
}
