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
package kala.index;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IndexesTest {
    @Test
    void checkIndexTest() {
        assertEquals(0, Indexes.checkIndex(0, 10));
        assertEquals(5, Indexes.checkIndex(5, 10));
        assertEquals(9, Indexes.checkIndex(9, 10));
        assertEquals(9, Indexes.checkIndex(~1, 10));
        assertEquals(0, Indexes.checkIndex(~10, 10));

        assertThrows(IndexOutOfBoundsException.class, () -> Indexes.checkIndex(10, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> Indexes.checkIndex(~0, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> Indexes.checkIndex(~11, 10));
    }

    @Test
    void checkPositionIndexTest() {
        assertEquals(0, Indexes.checkPositionIndex(0, 10));
        assertEquals(5, Indexes.checkPositionIndex(5, 10));
        assertEquals(10, Indexes.checkPositionIndex(10, 10));
        assertEquals(10, Indexes.checkPositionIndex(~0, 10));
        assertEquals(9, Indexes.checkPositionIndex(~1, 10));
        assertEquals(0, Indexes.checkPositionIndex(~10, 10));

        assertThrows(IndexOutOfBoundsException.class, () -> Indexes.checkPositionIndex(11, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> Indexes.checkPositionIndex(~11, 10));
    }
}
