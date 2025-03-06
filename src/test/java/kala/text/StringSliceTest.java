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
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public final class StringSliceTest {

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
        assertEquals('a', slice.charAt(~3));
        assertEquals('b', slice.charAt(1));
        assertEquals('b', slice.charAt(~2));
        assertEquals('c', slice.charAt(2));
        assertEquals('c', slice.charAt(~1));

        assertThrows(IndexOutOfBoundsException.class, () -> slice.charAt(3));
        assertThrows(IndexOutOfBoundsException.class, () -> slice.charAt(~0));
        assertThrows(IndexOutOfBoundsException.class, () -> slice.charAt(~4));
    }

    @Test
    void codePointAtTest() {
        var slice = StringSlice.of("a\uD83D\uDE0Ab");
        assertEquals('a', slice.codePointAt(0));
        assertEquals('a', slice.codePointAt(~4));
        assertEquals(Character.toCodePoint('\uD83D', '\uDE0A'), slice.codePointAt(1));
        assertEquals(Character.toCodePoint('\uD83D', '\uDE0A'), slice.codePointAt(~3));
        assertEquals('\uDE0A', slice.codePointAt(2));
        assertEquals('\uDE0A', slice.codePointAt(~2));
        assertEquals('b', slice.codePointAt(3));
        assertEquals('b', slice.codePointAt(~1));

        assertThrows(IndexOutOfBoundsException.class, () -> slice.codePointAt(4));
        assertThrows(IndexOutOfBoundsException.class, () -> slice.codePointAt(~0));
        assertThrows(IndexOutOfBoundsException.class, () -> slice.codePointAt(~5));
    }

    @Test
    void forEachCodePointTest() {
        ArrayList<Integer> result = new ArrayList<>();

        StringSlice.of("a\uD83D\uDE0Ab").forEachCodePoint(result::add);
        assertIterableEquals(List.of((int) 'a', Character.toCodePoint('\uD83D', '\uDE0A'), (int) 'b'), result);
    }

    @Test
    void forEachGraphemesTest() {
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
    void isNotEmptyTest() {
        assertFalse(StringSlice.of("").isNotEmpty());
        assertTrue(StringSlice.of("abc").isNotEmpty());
        assertTrue(StringSlice.of("abc", 1, 2).isNotEmpty());
        assertFalse(StringSlice.of("abc", 1, 1).isNotEmpty());
    }

    @Test
    void isBlankTest() {
        assertTrue(StringSlice.of("").isBlank());
        assertTrue(StringSlice.of(" \t\f").isBlank());
        assertTrue(StringSlice.of("a \t\fb", 1, ~1).isBlank());
        assertTrue(StringSlice.of("a \t\fb", 0, 0).isBlank());

        assertFalse(StringSlice.of(" \ta\f").isBlank());
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
        assertSliceEquals("cde", StringSlice.of("abcdef").trim('a', 'b', 'f'));
    }

    @Test
    void removePrefixTest() {
        assertSliceEquals("abc", StringSlice.of("abc").removePrefix("b"));
        assertSliceEquals("abc", StringSlice.of("abc").removePrefix(StringSlice.of("b")));
        assertSliceEquals("bc", StringSlice.of("abc").removePrefix("a"));
        assertSliceEquals("bc", StringSlice.of("abc").removePrefix(StringSlice.of("a")));
        assertSliceEquals("c", StringSlice.of("abc").removePrefix("ab"));
        assertSliceEquals("c", StringSlice.of("abc").removePrefix(StringSlice.of("ab")));
        assertSliceEquals("", StringSlice.of("abc").removePrefix("abc"));
        assertSliceEquals("", StringSlice.of("abc").removePrefix(StringSlice.of("abc")));
    }

    @Test
    void removeSuffixTest() {
        assertSliceEquals("abc", StringSlice.of("abc").removeSuffix("b"));
        assertSliceEquals("abc", StringSlice.of("abc").removeSuffix(StringSlice.of("b")));
        assertSliceEquals("ab", StringSlice.of("abc").removeSuffix("c"));
        assertSliceEquals("ab", StringSlice.of("abc").removeSuffix(StringSlice.of("c")));
        assertSliceEquals("a", StringSlice.of("abc").removeSuffix("bc"));
        assertSliceEquals("a", StringSlice.of("abc").removeSuffix(StringSlice.of("bc")));
        assertSliceEquals("", StringSlice.of("abc").removeSuffix("abc"));
        assertSliceEquals("", StringSlice.of("abc").removeSuffix(StringSlice.of("abc")));
    }


    @Test
    void replaceTest() {
        assertSliceEquals("abc", StringSlice.of("abc").replace('a', 'a'));
        assertSliceEquals("abc", StringSlice.of("abc").replace('d', 'z'));
        assertSliceEquals("zbc", StringSlice.of("abc").replace('a', 'z'));
        assertSliceEquals("zzbcz", StringSlice.of("aabca").replace('a', 'z'));
    }

    @Test
    void replaceRangeTest() {
        assertSliceEquals("123", StringSlice.of("abc").replaceRange(0, ~0, "123"));
        assertSliceEquals("3foo6", StringSlice.of("123456789", 2, 6).replaceRange(1, 3, "foo"));
    }

    @Test
    void repeatTest() {
        assertThrows(IllegalArgumentException.class, () -> StringSlice.of("abc").repeat(-1));
        assertThrows(IllegalArgumentException.class, () -> StringSlice.of("abc").repeat(-2));
        assertThrows(IllegalArgumentException.class, () -> StringSlice.of("abc").repeat(Integer.MIN_VALUE));
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
    void toLowerCaseTest() {
        assertSliceEquals("", StringSlice.of("").toLowerCase());
        assertSliceEquals("abc", StringSlice.of("abc").toLowerCase());
        assertSliceEquals("abc", StringSlice.of("ABC").toLowerCase());
        assertSliceEquals("abc", StringSlice.of("AbC").toLowerCase());
        assertSliceEquals("abc", StringSlice.of(" AbC ", 1, ~1).toLowerCase());

        //noinspection UnnecessaryUnicodeEscape
        assertSliceEquals("i\u0307", StringSlice.of("İ").toLowerCase());
        assertSliceEquals("i", StringSlice.of("İ").toLowerCase(Locale.of("tr")));
    }

    @Test
    void toUpperCaseTest() {
        assertSliceEquals("", StringSlice.of("").toUpperCase());
        assertSliceEquals("ABC", StringSlice.of("ABC").toUpperCase());
        assertSliceEquals("ABC", StringSlice.of("abc").toUpperCase());
        assertSliceEquals("ABC", StringSlice.of("aBc").toUpperCase());
        assertSliceEquals("ABC", StringSlice.of(" aBc ", 1, ~1).toUpperCase());

        assertSliceEquals("I", StringSlice.of("i").toUpperCase());
        assertSliceEquals("İ", StringSlice.of("i").toUpperCase(Locale.of("tr")));
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

        assertSlicesEquals(List.of(""), StringSlice.of("").split(":"));
        assertSlicesEquals(List.of(""), StringSlice.of("").split(""));
        assertSlicesEquals(List.of("a", "b", "c"), StringSlice.of("abc").split(""));
        assertSlicesEquals(List.of("a", "b"), StringSlice.of("a:b").split(":"));
        assertSlicesEquals(List.of("a", "b"), StringSlice.of("a::b").split("::"));
        assertSlicesEquals(List.of("a", "", "b"), StringSlice.of("a::b").split(":"));
        assertSlicesEquals(List.of("a", "", "b"), StringSlice.of("a::::b").split("::"));
        assertSlicesEquals(List.of("", "a", "", "b", ""), StringSlice.of(":a::b:").split(":"));
        assertSlicesEquals(List.of("", "a", "", "b", ""), StringSlice.of("::a::::b::").split("::"));
        assertSlicesEquals(List.of("", "a", ":b:"), StringSlice.of(":a::b:").split(":", 3));
        assertSlicesEquals(List.of("", "a", "::b::"), StringSlice.of("::a::::b::").split("::", 3));
    }

    @Test
    void contextEqualsTest() {
        StringSlice slice1 = StringSlice.of("abc");
        StringSlice slice2 = StringSlice.of("  abc  ", 2, 5);

        assertTrue(slice1.contentEquals("abc"));
        assertTrue(slice2.contentEquals("abc"));
        assertTrue(slice1.contentEquals(slice1));
        assertTrue(slice1.contentEquals(slice2));
        assertTrue(slice2.contentEquals(slice1));
        assertTrue(slice2.contentEquals(slice2));

        assertFalse(slice1.contentEquals("ab"));
        assertFalse(slice1.contentEquals("cba"));
        assertFalse(slice1.contentEquals(StringSlice.of("cba")));
        assertFalse(slice1.contentEquals(slice1.slice(1)));
    }

    @Test
    void hashCodeTest() {
        StringSlice slice1 = StringSlice.of("abc");
        StringSlice slice2 = StringSlice.of("  abc  ", 2, 5);

        final int hash = slice1.hashCode();
        assertEquals(hash, slice1.hashCode());
        assertEquals(hash, slice2.hashCode());
    }
}
