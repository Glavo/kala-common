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
package kala.collection;

import kala.tuple.Tuple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public interface SeqViewTestTemplate extends SeqLikeTestTemplate, SequentialCollectionViewTestTemplate {
    @Override
    <E> SeqView<E> of(E... elements);

    @Override
    <E> SeqView<E> from(E[] elements);

    @Override
    <E> SeqView<E> from(Iterable<? extends E> elements);

    @Test
    @Override
    default void dropTest() {
        SeqLikeTestTemplate.super.dropTest();
        assertIterableEquals(List.of(), of(0, 1, 2).drop(1).drop(2));
        assertIterableEquals(List.of(1, 2), of(0, 1, 2).drop(1).drop(0));
        assertIterableEquals(List.of(3), of(0, 1, 2, 3).drop(1).drop(2));
    }

    @Test
    @Override
    default void takeTest() {
        SeqLikeTestTemplate.super.takeTest();
        assertIterableEquals(List.of(), of(0, 1, 2).take(2).take(0));
        assertIterableEquals(List.of(0), of(0, 1, 2).take(2).take(1));
        assertIterableEquals(List.of(0, 1), of(0, 1, 2).take(2).take(2));
        assertIterableEquals(List.of(0, 1), of(0, 1, 2).take(2).take(3));
        assertIterableEquals(List.of(0, 1, 2), of(0, 1, 2).take(3).take(3));
    }

    @Test
    @Override
    default void zipTest() {
        SequentialCollectionViewTestTemplate.super.zipTest();

        assertIterableEquals(List.of(), of(0, 1, 2).zip(of()));
        assertIterableEquals(List.of(), of().zip(of("A", "B", "C")));
        assertIterableEquals(List.of(Tuple.of(0, "A")), of(0, 1, 2).zip(of("A")));
        assertIterableEquals(List.of(Tuple.of(0, "A"), Tuple.of(1, "B"), Tuple.of(2, "C")), of(0, 1, 2).zip(of("A", "B", "C")));
        assertIterableEquals(List.of(Tuple.of(0, "A"), Tuple.of(1, "B"), Tuple.of(2, "C")), of(0, 1, 2).zip(of("A", "B", "C", "D", "E")));

        assertIterableEquals(List.of(), of(0, 1, 2).zip(of(), "%s%s"::formatted));
        assertIterableEquals(List.of(), of().zip(of("A", "B", "C")));
        assertIterableEquals(List.of("0A"), of(0, 1, 2).zip(of("A"), "%s%s"::formatted));
        assertIterableEquals(List.of("0A", "1B", "2C"), of(0, 1, 2).zip(of("A", "B", "C"), "%s%s"::formatted));
        assertIterableEquals(List.of("0A", "1B", "2C"), of(0, 1, 2).zip(of("A", "B", "C", "D", "E"), "%s%s"::formatted));

        assertEquals(0, of(0, 1, 2).zip(of()).size());
        assertEquals(0, of().zip(of("A", "B", "C")).size());
        assertEquals(1, of(0, 1, 2).zip(of("A")).size());
        assertEquals(3, of(0, 1, 2).zip(of("A", "B", "C")).size());
        assertEquals(3, of(0, 1, 2).zip(of("A", "B", "C", "D", "E")).size());

        assertTrue(of(0, 1, 2).zip(of()).isEmpty());
        assertTrue(of().zip(of("A", "B", "C")).isEmpty());
        assertFalse(of(0, 1, 2).zip(of("A")).isEmpty());
        assertFalse(of(0, 1, 2).zip(of("A", "B", "C")).isEmpty());
        assertFalse(of(0, 1, 2).zip(of("A", "B", "C", "D", "E")).isEmpty());
    }
}
