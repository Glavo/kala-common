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
package kala.collection.base;

import kala.function.BooleanPredicate;
import kala.tuple.Tuple2;
import kala.collection.base.primitive.BooleanIterator;
import org.junit.jupiter.api.*;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class BooleanIteratorTest {
    static void assertIteratorElements(BooleanIterator it, boolean... values) {
        for (boolean value : values) {
            if (it.hasNext()) {
                assertEquals(value, it.nextBoolean());
            } else {
                fail("too few elements");
            }
        }
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::nextBoolean);
    }

    @Test
    public void ofTest() {
        assertAll(
                () -> assertIteratorElements(BooleanIterator.of()),
                () -> assertIteratorElements(BooleanIterator.of(true), true),
                () -> assertIteratorElements(BooleanIterator.of(false), false),
                () -> assertIteratorElements(BooleanIterator.of(true, false), true, false),
                () -> assertIteratorElements(BooleanIterator.of(false, true), false, true),
                () -> assertIteratorElements(BooleanIterator.of(true, true), true, true),
                () -> assertIteratorElements(BooleanIterator.of(false, false), false, false),
                () -> assertIteratorElements(BooleanIterator.of(false, true, false), false, true, false)
        );
    }

    @Test
    public void sizeTest() {
        assertAll(
                () -> assertEquals(0, BooleanIterator.empty().size()),
                () -> assertEquals(1, BooleanIterator.of(true).size()),
                () -> assertEquals(2, BooleanIterator.of(true, true).size()),
                () -> assertEquals(2, BooleanIterator.of(true, false).size()),
                () -> assertEquals(3, BooleanIterator.of(true, false, true).size())
        );
    }

    @Test
    public void containsTest() {
        assertAll(
                () -> assertFalse(BooleanIterator.empty().contains(true)),
                () -> assertFalse(BooleanIterator.empty().contains(false)),
                () -> assertFalse(BooleanIterator.empty().contains(Boolean.TRUE)),
                () -> assertFalse(BooleanIterator.empty().contains(Boolean.FALSE)),
                () -> assertFalse(BooleanIterator.empty().contains(null)),
                () -> assertFalse(BooleanIterator.empty().contains("foo")),
                () -> assertTrue(BooleanIterator.of(true).contains(true)),
                () -> assertTrue(BooleanIterator.of(true).contains(Boolean.TRUE)),
                () -> assertFalse(BooleanIterator.of(true).contains(false)),
                () -> assertFalse(BooleanIterator.of(true).contains(Boolean.FALSE)),
                () -> assertFalse(BooleanIterator.of(true).contains("foo")),
                () -> assertFalse(BooleanIterator.of(true).contains(null)),
                () -> assertTrue(BooleanIterator.of(true, false).contains(true)),
                () -> assertTrue(BooleanIterator.of(true, false).contains(false)),
                () -> assertTrue(BooleanIterator.of(true, false).contains(Boolean.TRUE)),
                () -> assertTrue(BooleanIterator.of(true, false).contains(Boolean.FALSE)),
                () -> assertFalse(BooleanIterator.of(true, false).contains(null))
        );
    }

    @Test
    public void sameElementsTest() {
        assertAll(
                () -> assertTrue(BooleanIterator.empty().sameElements(BooleanIterator.empty())),
                () -> assertTrue(BooleanIterator.empty().sameElements(Iterators.empty())),
                () -> assertTrue(BooleanIterator.of(true).sameElements(BooleanIterator.of(true))),
                () -> assertFalse(BooleanIterator.of(true).sameElements(BooleanIterator.of(false))),
                () -> assertFalse(BooleanIterator.of(true, true).sameElements(BooleanIterator.of(true))),
                () -> assertFalse(BooleanIterator.of(true).sameElements(BooleanIterator.of(true, true))),
                () -> assertFalse(BooleanIterator.of(true, false).sameElements(BooleanIterator.of(true, true)))
        );
    }

    @Test
    public void dropTest() {
        assertIteratorElements(BooleanIterator.empty().drop(0));
        assertIteratorElements(BooleanIterator.empty().drop(1));
        assertIteratorElements(BooleanIterator.empty().drop(Integer.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.empty().drop(-1));
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.empty().drop(Integer.MIN_VALUE));

        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.of(true).drop(-1));
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.of(true).drop(Integer.MIN_VALUE));
        assertIteratorElements(BooleanIterator.of(true).drop(0), true);
        assertIteratorElements(BooleanIterator.of(true).drop(1));
        assertIteratorElements(BooleanIterator.of(true).drop(Integer.MAX_VALUE));

        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.of(true, false, true).drop(-1));
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.of(true, false, true).drop(Integer.MIN_VALUE));
        assertIteratorElements(BooleanIterator.of(true, false, true).drop(0), true, false, true);
        assertIteratorElements(BooleanIterator.of(true, false, true).drop(1), false, true);
        assertIteratorElements(BooleanIterator.of(true, false, true).drop(2), true);
        assertIteratorElements(BooleanIterator.of(true, false, true).drop(3));
        assertIteratorElements(BooleanIterator.of(true, false, true).drop(Integer.MAX_VALUE));
    }

    @Test
    public void dropWhileTest() {
        assertAll(
                () -> assertIteratorElements(BooleanIterator.empty().dropWhile(BooleanPredicate.isEqual(true))),
                () -> assertIteratorElements(BooleanIterator.empty().dropWhile(BooleanPredicate.isEqual(false))),
                () -> assertIteratorElements(BooleanIterator.of(true).dropWhile(BooleanPredicate.isEqual(true))),
                () -> assertIteratorElements(BooleanIterator.of(true, true).dropWhile(BooleanPredicate.isEqual(true))),
                () -> assertIteratorElements(BooleanIterator.of(true, true, true).dropWhile(BooleanPredicate.isEqual(true))),
                () -> assertIteratorElements(BooleanIterator.of(true, true, true, false).dropWhile(BooleanPredicate.isEqual(true)), false),
                () -> assertIteratorElements(BooleanIterator.of(true, true, true, false).dropWhile(BooleanPredicate.isEqual(false)), true, true, true, false)
        );
    }

    @Test
    public void takeTest() {
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.empty().take(-1));
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.empty().take(Integer.MIN_VALUE));

        assertIteratorElements(BooleanIterator.empty().take(0));
        assertIteratorElements(BooleanIterator.empty().take(1));
        assertIteratorElements(BooleanIterator.empty().take(Integer.MAX_VALUE));

        assertIteratorElements(BooleanIterator.of(true).take(0));
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.of(true).take(-1));
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.of(true).take(Integer.MIN_VALUE));
        assertIteratorElements(BooleanIterator.of(true).take(1), true);
        assertIteratorElements(BooleanIterator.of(true).take(2), true);
        assertIteratorElements(BooleanIterator.of(true).take(Integer.MAX_VALUE), true);

        assertIteratorElements(BooleanIterator.of(true, false, true).take(0));
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.of(true, false, true).take(-1));
        assertThrows(IllegalArgumentException.class, () -> BooleanIterator.of(true, false, true).take(Integer.MIN_VALUE));
        assertIteratorElements(BooleanIterator.of(true, false, true).take(1), true);
        assertIteratorElements(BooleanIterator.of(true, false, true).take(2), true, false);
        assertIteratorElements(BooleanIterator.of(true, false, true).take(3), true, false, true);
        assertIteratorElements(BooleanIterator.of(true, false, true).take(4), true, false, true);
        assertIteratorElements(BooleanIterator.of(true, false, true).take(Integer.MAX_VALUE), true, false, true);

    }

    @Test
    public void takeWhileTest() {
        assertAll(
                () -> assertIteratorElements(BooleanIterator.empty().takeWhile(BooleanPredicate.isEqual(true))),
                () -> assertIteratorElements(BooleanIterator.empty().takeWhile(BooleanPredicate.isEqual(false))),
                () -> assertIteratorElements(BooleanIterator.of(true).takeWhile(BooleanPredicate.isEqual(true)), true),
                () -> assertIteratorElements(BooleanIterator.of(true).takeWhile(BooleanPredicate.isEqual(false))),
                () -> assertIteratorElements(BooleanIterator.of(true, true).takeWhile(BooleanPredicate.isEqual(true)), true, true),
                () -> assertIteratorElements(BooleanIterator.of(true, true).takeWhile(BooleanPredicate.isEqual(false))),
                () -> assertIteratorElements(BooleanIterator.of(true, true, false).takeWhile(BooleanPredicate.isEqual(true)), true, true),
                () -> assertIteratorElements(BooleanIterator.of(true, true, false).takeWhile(BooleanPredicate.isEqual(false)))
        );
    }

    @Test
    public void updatedTest() {
        assertAll(
                () -> assertIteratorElements(BooleanIterator.empty().updated(0, true)),
                () -> assertIteratorElements(BooleanIterator.empty().updated(0, false)),
                () -> assertIteratorElements(BooleanIterator.empty().updated(1, true)),
                () -> assertIteratorElements(BooleanIterator.empty().updated(1, false)),
                () -> assertIteratorElements(BooleanIterator.empty().updated(Integer.MAX_VALUE, true)),
                () -> assertIteratorElements(BooleanIterator.empty().updated(Integer.MAX_VALUE, false)),
                () -> assertIteratorElements(BooleanIterator.empty().updated(-1, true)),
                () -> assertIteratorElements(BooleanIterator.empty().updated(-1, false)),
                () -> assertIteratorElements(BooleanIterator.of(true).updated(0, false), false),
                () -> assertIteratorElements(BooleanIterator.of(true).updated(1, false), true),
                () -> assertIteratorElements(BooleanIterator.of(true).updated(Integer.MAX_VALUE, false), true),
                () -> assertIteratorElements(BooleanIterator.of(true).updated(-1, false), true),
                () -> assertIteratorElements(BooleanIterator.of(true).updated(Integer.MIN_VALUE, false), true),
                () -> assertIteratorElements(BooleanIterator.of(true, false, true).updated(0, false), false, false, true),
                () -> assertIteratorElements(BooleanIterator.of(true, false, true).updated(1, false), true, false, true),
                () -> assertIteratorElements(BooleanIterator.of(true, false, true).updated(2, false), true, false, false),
                () -> assertIteratorElements(BooleanIterator.of(true, false, true).updated(3, false), true, false, true),
                () -> assertIteratorElements(BooleanIterator.of(true, false, true).updated(Integer.MAX_VALUE, false), true, false, true),
                () -> assertIteratorElements(BooleanIterator.of(true, false, true).updated(-1, false), true, false, true),
                () -> assertIteratorElements(BooleanIterator.of(true, false, true).updated(Integer.MIN_VALUE, false), true, false, true)
        );
    }

    @Test
    public void prependedTest() {
        assertAll(
                () -> assertIteratorElements(BooleanIterator.empty().prepended(true), true),
                () -> assertIteratorElements(BooleanIterator.empty().prepended(true).prepended(false), false, true),
                () -> assertIteratorElements(BooleanIterator.of(true).prepended(true), true, true),
                () -> assertIteratorElements(BooleanIterator.of(true).prepended(true).prepended(false), false, true, true)
        );
    }

    @Test
    public void appendedTest() {
        assertAll(
                () -> assertIteratorElements(BooleanIterator.empty().appended(true), true),
                () -> assertIteratorElements(BooleanIterator.empty().appended(true).appended(false), true, false),
                () -> assertIteratorElements(BooleanIterator.of(true).appended(true), true, true),
                () -> assertIteratorElements(BooleanIterator.of(true).appended(true).appended(false), true, true, false)
        );
    }

    @Test
    public void mapTest() {
        assertAll(
                () -> assertIteratorElements(BooleanIterator.empty().map(b -> b)),
                () -> assertIteratorElements(BooleanIterator.empty().map(b -> !b)),
                () -> assertIteratorElements(BooleanIterator.of(true).map(b -> b), true),
                () -> assertIteratorElements(BooleanIterator.of(true).map(b -> !b), false),
                () -> assertIteratorElements(BooleanIterator.of(true, false, true).map(b -> b), true, false, true),
                () -> assertIteratorElements(BooleanIterator.of(true, false, true).map(b -> !b), false, true, false)
        );
    }

    @Test
    public void mapToObjTest() {
        assertAll(
                () -> IteratorsTest.assertIteratorElements(BooleanIterator.empty().mapToObj(String::valueOf)),
                () -> IteratorsTest.assertIteratorElements(BooleanIterator.of(true).mapToObj(String::valueOf), "true"),
                () -> IteratorsTest.assertIteratorElements(
                        BooleanIterator.of(true, false, true).mapToObj(String::valueOf),
                        "true", "false", "true"
                ),
                () -> IteratorsTest.assertIteratorElements(
                        BooleanIterator.of(true, false, true).mapToObj(Boolean::valueOf),
                        Boolean.TRUE, Boolean.FALSE, Boolean.TRUE
                )
        );
    }

    @Test
    public void spanTest() {
        assertAll(
                () -> {
                    Tuple2<BooleanIterator, BooleanIterator> t = BooleanIterator.empty().span(b -> b);
                    assertIteratorElements(t.component1());
                    assertIteratorElements(t.component2());
                }, () -> {
                    Tuple2<BooleanIterator, BooleanIterator> t = BooleanIterator.empty().span(b -> !b);
                    assertIteratorElements(t.component1());
                    assertIteratorElements(t.component2());
                },
                () -> {
                    Tuple2<BooleanIterator, BooleanIterator> t = BooleanIterator.of(true, true, false, true).span(b -> b);
                    assertIteratorElements(t.component1(), true, true);
                    assertIteratorElements(t.component2(), false, true);
                },
                () -> {
                    Tuple2<BooleanIterator, BooleanIterator> t = BooleanIterator.of(true, true, false, true).span(b -> !b);
                    assertIteratorElements(t.component1());
                    assertIteratorElements(t.component2(), true, true, false, true);
                }
        );
    }
}
