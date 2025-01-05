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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PredicatesTest {

    private static final Object obj = new Object();

    @Test
    void alwaysTrueTest() {
        assertTrue(Predicates.alwaysTrue().test(obj));
        assertTrue(Predicates.alwaysTrue().test(""));
        assertTrue(Predicates.alwaysTrue().test(null));

        assertTrue(Predicates.alwaysTrue().and(it -> true).test(null));
        assertFalse(Predicates.alwaysTrue().and(it -> false).test(null));
        assertTrue(Predicates.alwaysTrue().or(it -> true).test(null));
        assertTrue(Predicates.alwaysTrue().or(it -> true).test(null));
    }

    @Test
    void alwaysFalseTest() {
        assertFalse(Predicates.alwaysFalse().test(obj));
        assertFalse(Predicates.alwaysFalse().test(""));
        assertFalse(Predicates.alwaysFalse().test(null));

        assertFalse(Predicates.alwaysFalse().and(it -> true).test(null));
        assertFalse(Predicates.alwaysFalse().and(it -> false).test(null));
        assertTrue(Predicates.alwaysFalse().or(it -> true).test(null));
        assertFalse(Predicates.alwaysFalse().or(it -> false).test(null));
    }

    @Test
    void isNullTest() {
        assertFalse(Predicates.isNull().test(obj));
        assertFalse(Predicates.isNull().test(""));
        assertTrue(Predicates.isNull().test(null));
    }

    @Test
    void isNotNullTest() {
        assertTrue(Predicates.isNotNull().test(obj));
        assertTrue(Predicates.isNotNull().test(""));
        assertFalse(Predicates.isNotNull().test(null));
    }

    @Test
    void isEqualTest() {
        var testObject = List.of("A");
        Predicate<Object> predicate = Predicates.isEqual(testObject);

        assertFalse(predicate.test(null));
        assertFalse(predicate.test("A"));
        assertTrue(predicate.test(testObject));
        assertTrue(predicate.test(new ArrayList<>(testObject)));
    }

    @Test
    void isSameTest() {
        var testObject = List.of("A");
        Predicate<Object> predicate = Predicates.isSame(testObject);

        assertFalse(predicate.test(null));
        assertFalse(predicate.test("A"));
        assertTrue(predicate.test(testObject));
        assertFalse(predicate.test(new ArrayList<>(testObject)));
    }

    @Test
    void andTest() {
        assertFalse(Predicates.and(Predicates.alwaysFalse(), Predicates.alwaysFalse()).test(obj));
        assertFalse(Predicates.and(Predicates.alwaysFalse(), Predicates.alwaysTrue()).test(obj));
        assertFalse(Predicates.and(Predicates.alwaysTrue(), Predicates.alwaysFalse()).test(obj));
        assertTrue(Predicates.and(Predicates.alwaysTrue(), Predicates.alwaysTrue()).test(obj));
    }

    @Test
    void orTest() {
        assertFalse(Predicates.or(Predicates.alwaysFalse(), Predicates.alwaysFalse()).test(obj));
        assertTrue(Predicates.or(Predicates.alwaysFalse(), Predicates.alwaysTrue()).test(obj));
        assertTrue(Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysFalse()).test(obj));
        assertTrue(Predicates.or(Predicates.alwaysTrue(), Predicates.alwaysTrue()).test(obj));
    }
}
