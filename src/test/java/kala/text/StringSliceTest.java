/*
 * Copyright 2024 Glavo
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
package kala.text;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class StringSliceTest {

    private static void assertSliceEquals(String expected, StringSlice actual) {
        assertEquals(StringSlice.of(expected), actual);
    }

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

    @Test
    void substringTest() {
        StringSlice slice = StringSlice.of("abc");
        assertSliceEquals("abc", slice.substring(0));
        assertSliceEquals("bc", slice.substring(1));
        assertSliceEquals("c", slice.substring(2));
        assertSliceEquals("", slice.substring(3));
        assertThrows(IndexOutOfBoundsException.class, () -> slice.substring(4));
    }
}
