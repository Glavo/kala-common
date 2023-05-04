package kala.function;

import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class PredicatesTest {

    private static final Object obj = new Object();

    @Test
    void alwaysTrueTest() {
        assertTrue(Predicates.alwaysTrue().test(obj));
        assertTrue(Predicates.alwaysTrue().test(""));
        assertTrue(Predicates.alwaysTrue().test(null));

        assertTrue(Predicates.alwaysTrue().and(it -> true).test(null));
        assertFalse(Predicates.alwaysTrue().and(it -> false).test(null));
        assertTrue(Predicates.alwaysTrue().or(it -> true).test(null));
        assertTrue(Predicates.alwaysTrue().or(it -> true).test(null));
    }

    @Test
    void alwaysFalseTest() {
        assertFalse(Predicates.alwaysFalse().test(obj));
        assertFalse(Predicates.alwaysFalse().test(""));
        assertFalse(Predicates.alwaysFalse().test(null));

        assertFalse(Predicates.alwaysFalse().and(it -> true).test(null));
        assertFalse(Predicates.alwaysFalse().and(it -> false).test(null));
        assertTrue(Predicates.alwaysFalse().or(it -> true).test(null));
        assertFalse(Predicates.alwaysFalse().or(it -> false).test(null));
    }

    @Test
    void isNullTest() {
        assertFalse(Predicates.isNull().test());
    }

}
