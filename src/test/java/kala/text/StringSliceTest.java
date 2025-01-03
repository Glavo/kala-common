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

import kala.collection.base.Traversable;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StringSliceTest {

    private static void assertSliceEquals(String expected, StringSlice actual) {
        assertEquals(StringSlice.of(expected), actual);
    }

    private static void assertSlicesEquals(List<String> expected, Traversable<StringSlice> actual) {
        assertIterableEquals(expected, actual.stream().map(StringSlice::toString).toList());
    }

    @Test
    void charAtTest() {
        var slice = StringSlice.of("abc");
        assertEquals('a', slice.charAt(0));
        assertEquals('b', slice.charAt(1));
        assertEquals('c', slice.charAt(2));
        assertEquals('c', slice.charAt(~1));
        assertEquals('b', slice.charAt(~2));
        assertEquals('a', slice.charAt(~3));

        assertThrows(IndexOutOfBoundsException.class, () -> slice.charAt(4));
        assertThrows(IndexOutOfBoundsException.class, () -> slice.charAt(~4));
    }

    @Test
    void graphemes() {
        String family = "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66";

        var expected = List.of("a", "b", "c", " ", "你", "好", " ", family, "\uD83D\uDE0A");
        var slice = StringSlice.of("abc 你好 " + family + "\uD83D\uDE0A");

        var expected2 = List.of("b", "c", " ", "你", "好", " ", family, "\uD83D");
        var slice2 = slice.slice(1, ~1);

        ArrayList<String> result = new ArrayList<>();

        slice.forEachGrapheme(grapheme -> result.add(grapheme.toString()));
        assertEquals(expected, result);

        result.clear();
        slice2.forEachGrapheme(grapheme -> result.add(grapheme.toString()));
        assertEquals(expected2, result);

        result.clear();
        slice.graphemes().forEach(grapheme -> result.add(grapheme.toString()));
        assertEquals(expected, result);

        result.clear();
        slice2.graphemes().forEach(grapheme -> result.add(grapheme.toString()));
        assertEquals(expected2, result);
    }

    @Test
    void isEmptyTest() {
        assertTrue(StringSlice.of("").isEmpty());
        assertFalse(StringSlice.of("abc").isEmpty());
        assertFalse(StringSlice.of("abc", 1, 2).isEmpty());
        assertTrue(StringSlice.of("abc", 1, 1).isEmpty());
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
    void indexOfTest() {
        assertEquals(1, StringSlice.of("abc").indexOf('b'));
        assertEquals(0, StringSlice.of("abc", 1).indexOf('b'));
        assertEquals(-1, StringSlice.of("abc").indexOf('d'));
        assertEquals(-1, StringSlice.of("abc", 1, 2).indexOf('a'));
        assertEquals(-1, StringSlice.of("abc").indexOf('a', 1, 2));
        assertEquals(1, StringSlice.of("abc").indexOf('b', 1, 2));
        assertEquals(0, StringSlice.of("abc", 1, 3).indexOf('b', 0, 1));
    }

    @Test
    void sliceTest() {
        StringSlice slice = StringSlice.of("abc");
        assertSliceEquals("abc", slice.slice(0));
        assertSliceEquals("bc", slice.slice(1));
        assertSliceEquals("c", slice.slice(2));
        assertSliceEquals("", slice.slice(3));
        assertSliceEquals("ab", slice.slice(0, 2));
        assertSliceEquals("b", slice.slice(1, 2));
        assertSliceEquals("abc", slice.slice(0, ~0));
        assertSliceEquals("ab", slice.slice(0, ~1));
        assertSliceEquals("b", slice.slice(~2, ~1));
        assertSliceEquals("", slice.slice(~1, ~1));

        assertThrows(IndexOutOfBoundsException.class, () -> slice.slice(4));
        assertThrows(IndexOutOfBoundsException.class, () -> slice.slice(~4));
    }

    @Test
    void trimTest() {
        assertSliceEquals("", StringSlice.of("").trim());
        assertSliceEquals("", StringSlice.of("\t").trim());
        assertSliceEquals("", StringSlice.of("\t\t").trim());
        assertSliceEquals("abc", StringSlice.of("abc").trim());
        assertSliceEquals("abc", StringSlice.of(" abc ").trim());
        assertSliceEquals("abc", StringSlice.of(" abc\t").trim());
        assertSliceEquals("a b\tc", StringSlice.of(" \ta b\tc\t  \t\b").trim());
    }

    @Test
    void replaceTest() {
        assertSliceEquals("zbc", StringSlice.of("abc").replace('a', 'z'));
        assertSliceEquals("zzbcz", StringSlice.of("aabca").replace('a', 'z'));
    }

    @Test
    void replaceRangeTest() {
        assertSliceEquals("3foo6", StringSlice.of("123456789", 2, 6).replaceRange(1, 3, "foo"));
    }

    @Test
    void repeatTest() {
        assertSliceEquals("", StringSlice.empty().repeat(0));
        assertSliceEquals("", StringSlice.empty().repeat(1));
        assertSliceEquals("", StringSlice.empty().repeat(10));
        assertSliceEquals("", StringSlice.of("a").repeat(0));
        assertSliceEquals("a", StringSlice.of("a").repeat(1));
        assertSliceEquals("a".repeat(10), StringSlice.of("a").repeat(10));
        assertSliceEquals("abc", StringSlice.of("abc").repeat(1));
        assertSliceEquals("abc".repeat(10), StringSlice.of("abc").repeat(10));
    }

    @Test
    void linesTest() {
        assertSlicesEquals(List.of("123", "456", "", "789", "", "101112"), StringSlice.of("123\r\n456\n\n789\r\n\n101112\n").lines());
    }

    @Test
    void splitTest() {
        assertSlicesEquals(List.of(""), StringSlice.of("").split(':'));
        assertSlicesEquals(List.of("a", "b"), StringSlice.of("a:b").split(':'));
        assertSlicesEquals(List.of("a", "", "b"), StringSlice.of("a::b").split(':'));
        assertSlicesEquals(List.of("", "a", "", "b", ""), StringSlice.of(":a::b:").split(':'));
        assertSlicesEquals(List.of("", "a", "", "b", ""), StringSlice.of("\uD83D\uDE00a\uD83D\uDE00\uD83D\uDE00b\uD83D\uDE00").split(Character.toCodePoint('\uD83D', '\uDE00')));
        assertSlicesEquals(List.of("", "a", ":b:"), StringSlice.of(":a::b:").split(':', 3));
    }
}
