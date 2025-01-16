/*
 * Copyright 2025 Glavo
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
package kala.range;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class GenericRangeTest {

    @Test
    void allTest() {
        GenericRange<Integer> all = GenericRange.all();

        assertTrue(all.contains(0));
        assertTrue(all.contains(1));
        assertTrue(all.contains(Integer.MAX_VALUE));
        assertTrue(all.contains(Integer.MIN_VALUE));

        assertThrows(UnsupportedOperationException.class, () -> all.withStep(i -> i + 1));
    }

    @Test
    void isTest() {
        List.of(
                GenericRange.is(10),
                GenericRange.is(10, Integer::compare),
                GenericRange.is(10, Comparator.naturalOrder()),
                GenericRange.is(10, Comparator.reverseOrder())
        ).forEach(value -> {
            assertTrue(value.contains(10));
            assertFalse(value.contains(0));
            assertFalse(value.contains(-10));

            ArrayList<Integer> tmp = new ArrayList<>();
            value.withStep(it -> it + 1).forEach(tmp::add);
            assertIterableEquals(List.of(10), tmp);
        });

        assertThrows(IllegalArgumentException.class, () -> GenericRange.is(new Object(), null));
    }
}
