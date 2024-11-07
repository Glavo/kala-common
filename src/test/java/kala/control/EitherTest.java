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
package kala.control;

import kala.TestValue;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public final class EitherTest {
    private final TestValue<?> value = new TestValue<>("value");
    private final TestValue<?> otherValue = new TestValue<>("otherValue");

    private final Either.Left<TestValue<?>, ?> left = Either.left(value);
    private final Either.Right<?, TestValue<?>> right = Either.right(value);

    @Test
    public void swapTest() {
        assertEquals(left, right.swap());
        assertEquals(right, left.swap());
    }

    @Test
    public void toResultTest() {
        assertEquals(Result.ok(value), right.toResult());
        assertEquals(Result.err(value), left.toResult());
    }

    @Test
    public void leftProjectionTest() {
        assertTrue(left.leftProjection().isDefined());
        assertFalse(right.leftProjection().isDefined());

        assertEquals(Either.left(otherValue), left.leftProjection().map(it -> otherValue).getEither());
        assertEquals(right, right.leftProjection().map(it -> otherValue).getEither());
    }

    @Test
    public void rightProjectionTest() {
        assertFalse(left.rightProjection().isDefined());
        assertTrue(right.rightProjection().isDefined());

        assertEquals(left, left.rightProjection().map(it -> otherValue).getEither());
        assertEquals(Either.right(otherValue), right.rightProjection().map(it -> otherValue).getEither());
    }

    private EitherTest() {
    }
}
