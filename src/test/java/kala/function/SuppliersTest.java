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
package kala.function;

import kala.value.primitive.IntVar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public final class SuppliersTest {

    @Test
    void constantTest() {
        Object obj = new Object();
        var supplier = Suppliers.constant(obj);

        assertSame(obj, supplier.get());
        assertSame(obj, supplier.get());
    }

    @Test
    void memoizedTest() {
        final var count = new IntVar();

        final class TestObject {
            public TestObject() {
                count.value++;
            }
        }
        var supplier = Suppliers.memoized(TestObject::new);

        assertEquals(0, count.value);

        TestObject firstValue = supplier.get();
        assertEquals(1, count.value);

        assertSame(firstValue, supplier.get());
        assertEquals(1, count.value);
    }
}
