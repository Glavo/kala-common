package kala.text;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringSliceTest {
    @Test
    void containsTest() {
        StringSlice slice = StringSlice.of("abc");
        assertTrue(slice.contains(""));
        assertTrue(slice.contains("a"));
        assertTrue(slice.contains("b"));
        assertTrue(slice.contains("c"));
        assertTrue(slice.contains("ab"));
        assertTrue(slice.contains("bc"));
        assertTrue(slice.contains("abc"));
        assertFalse(slice.contains("ac"));

        slice = StringSlice.of("abc", 0, 2);
        assertTrue(slice.contains("a"));
        assertTrue(slice.contains("b"));
        assertFalse(slice.contains("c"));
        assertTrue(slice.contains("ab"));
        assertFalse(slice.contains("bc"));
        assertFalse(slice.contains("abc"));
        assertFalse(slice.contains("ac"));
    }
}
