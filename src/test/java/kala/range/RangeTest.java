package kala.range;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class RangeTest {
    @Test
    void isTest() {
        Range<String> value = Range.is("value");
        assertTrue(value.contains("value"));
        assertFalse(value.contains("not value"));
    }
}
