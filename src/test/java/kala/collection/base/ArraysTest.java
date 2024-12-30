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
package kala.collection.base;

import kala.ExtendedAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArraysTest {

    private static void assertArrayTypeAndEquals(Object[] expected, Object[] actual) {
        assertSame(expected.getClass(), actual.getClass());
        assertArrayEquals(expected, actual);
    }

    @Test
    void iteratorTest() {
        ExtendedAssertions.assertIteratorEquals(List.of(), ObjectArrays.iterator(new Object[]{}));
        ExtendedAssertions.assertIteratorEquals(List.of(1, 2, 3, 4, 5), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}));

        ExtendedAssertions.assertIteratorEquals(List.of(1, 2, 3, 4, 5), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 0));
        ExtendedAssertions.assertIteratorEquals(List.of(1, 2, 3, 4, 5), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, ~5));
        ExtendedAssertions.assertIteratorEquals(List.of(2, 3, 4, 5), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 1));
        ExtendedAssertions.assertIteratorEquals(List.of(2, 3, 4, 5), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, ~4));
        ExtendedAssertions.assertIteratorEquals(List.of(), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 5));
        ExtendedAssertions.assertIteratorEquals(List.of(), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, ~0));
        assertThrows(IndexOutOfBoundsException.class, () -> ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 6));
        assertThrows(IndexOutOfBoundsException.class, () -> ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, ~6));

        ExtendedAssertions.assertIteratorEquals(List.of(1, 2, 3, 4, 5), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 0, 5));
        ExtendedAssertions.assertIteratorEquals(List.of(1, 2, 3, 4, 5), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 0, ~0));
        ExtendedAssertions.assertIteratorEquals(List.of(2, 3, 4), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 1, 4));
        ExtendedAssertions.assertIteratorEquals(List.of(2, 3, 4), ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 1, ~1));
        assertThrows(IndexOutOfBoundsException.class, () -> ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 2, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> ObjectArrays.iterator(new Object[]{1, 2, 3, 4, 5}, 2, ~4));
    }

    @Test
    void insertedTest() {
        String[] values = {"A", "B", "C"};

        assertArrayTypeAndEquals(new String[]{"A"}, GenericArrays.inserted(new String[]{}, 0, "A"));
        assertArrayTypeAndEquals(new String[]{"foo", "A", "B", "C"}, GenericArrays.inserted(values, 0, "foo"));
        assertArrayTypeAndEquals(new String[]{"A", "foo", "B", "C"}, GenericArrays.inserted(values, 1, "foo"));
        assertArrayTypeAndEquals(new String[]{"A", "B", "C", "foo"}, GenericArrays.inserted(values, 3, "foo"));
        assertArrayTypeAndEquals(new String[]{"A", "B", "C", "foo"}, GenericArrays.inserted(values, ~0, "foo"));
        assertArrayTypeAndEquals(new String[]{"A", "B", "foo", "C"}, GenericArrays.inserted(values, ~1, "foo"));
        assertArrayTypeAndEquals(new String[]{"foo", "A", "B", "C"}, GenericArrays.inserted(values, ~3, "foo"));

        assertArrayTypeAndEquals(new Object[]{"A"}, ObjectArrays.inserted(new Object[]{}, 0, "A"));
        assertArrayTypeAndEquals(new Object[]{"foo", "A", "B", "C"}, ObjectArrays.inserted(values, 0, "foo"));
        assertArrayTypeAndEquals(new Object[]{"A", "foo", "B", "C"}, ObjectArrays.inserted(values, 1, "foo"));
        assertArrayTypeAndEquals(new Object[]{"A", "B", "C", "foo"}, ObjectArrays.inserted(values, 3, "foo"));
        assertArrayTypeAndEquals(new Object[]{"A", "B", "C", "foo"}, ObjectArrays.inserted(values, ~0, "foo"));
        assertArrayTypeAndEquals(new Object[]{"A", "B", "foo", "C"}, ObjectArrays.inserted(values, ~1, "foo"));
        assertArrayTypeAndEquals(new Object[]{"foo", "A", "B", "C"}, ObjectArrays.inserted(values, ~3, "foo"));

        assertThrows(IndexOutOfBoundsException.class, () -> GenericArrays.inserted(values, 4, "A"));
        assertThrows(IndexOutOfBoundsException.class, () -> GenericArrays.inserted(values, ~4, "A"));
        assertThrows(IndexOutOfBoundsException.class, () -> ObjectArrays.inserted(values, 4, "A"));
        assertThrows(IndexOutOfBoundsException.class, () -> ObjectArrays.inserted(values, ~4, "A"));
    }

    @Test
    void removedAtTest() {
        String[] values = {"A", "B", "C"};

        assertArrayTypeAndEquals(new String[]{"B", "C"}, GenericArrays.removedAt(values, 0));
        assertArrayTypeAndEquals(new String[]{"A", "C"}, GenericArrays.removedAt(values, 1));
        assertArrayTypeAndEquals(new String[]{"A", "B"}, GenericArrays.removedAt(values, 2));
        assertArrayTypeAndEquals(new String[]{"A", "B"}, GenericArrays.removedAt(values, ~1));
        assertArrayTypeAndEquals(new String[]{"A", "C"}, GenericArrays.removedAt(values, ~2));
        assertArrayTypeAndEquals(new String[]{"B", "C"}, GenericArrays.removedAt(values, ~3));

        assertArrayTypeAndEquals(new Object[]{"B", "C"}, ObjectArrays.removedAt(values, 0));
        assertArrayTypeAndEquals(new Object[]{"A", "C"}, ObjectArrays.removedAt(values, 1));
        assertArrayTypeAndEquals(new Object[]{"A", "B"}, ObjectArrays.removedAt(values, 2));
        assertArrayTypeAndEquals(new Object[]{"A", "B"}, ObjectArrays.removedAt(values, ~1));
        assertArrayTypeAndEquals(new Object[]{"A", "C"}, ObjectArrays.removedAt(values, ~2));
        assertArrayTypeAndEquals(new Object[]{"B", "C"}, ObjectArrays.removedAt(values, ~3));

        assertThrows(IndexOutOfBoundsException.class, () -> GenericArrays.removedAt(values, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> GenericArrays.removedAt(values, ~4));
        assertThrows(IndexOutOfBoundsException.class, () -> ObjectArrays.removedAt(values, 4));
        assertThrows(IndexOutOfBoundsException.class, () -> ObjectArrays.removedAt(values, ~4));
    }

    @Test
    void windowedTest() {
        String[] values = {"A", "B", "C", "D", "E", "F", "G", "H"};

        assertArrayEquals(
                new String[][]{{"A", "B", "C"}, {"B", "C", "D"}, {"C", "D", "E"}, {"D", "E", "F"}, {"E", "F", "G"}, {"F", "G", "H"}},
                GenericArrays.windowed(values, 3, 1)
        );
    }
}
