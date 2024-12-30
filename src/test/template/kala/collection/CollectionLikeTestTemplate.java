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
package kala.collection;

import kala.collection.base.TraversableTestTemplate;
import kala.collection.mutable.MutableArray;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public interface CollectionLikeTestTemplate extends TraversableTestTemplate {

    @Override
    @SuppressWarnings("unchecked")
    <E> CollectionLike<E> of(E... elements);

    @Override
    <E> CollectionLike<E> from(E[] elements);

    @Override
    <E> CollectionLike<E> from(Iterable<? extends E> elements);

    @Test
    default void copyToTest() {
        CollectionLike<Integer> collection = of(0, 1, 2, 3);

        MutableArray<Integer> target;

        target = MutableArray.create(0);
        assertEquals(0, collection.copyTo(target, 0));
        assertEquals(0, collection.copyTo(target, 10));

        target = MutableArray.create(5);
        assertEquals(4, collection.copyTo(target, 0));
        assertIterableEquals(Arrays.asList(0, 1, 2, 3, null), target);

        target = MutableArray.create(5);
        assertEquals(4, collection.copyTo(target, 1));
        assertIterableEquals(Arrays.asList(null, 0, 1, 2, 3), target);

        target = MutableArray.create(5);
        assertEquals(3, collection.copyTo(target, 2));
        assertIterableEquals(Arrays.asList(null, null, 0, 1, 2), target);

        target = MutableArray.create(5);
        assertEquals(2, collection.copyTo(target, 2, 2));
        assertIterableEquals(Arrays.asList(null, null, 0, 1, null), target);


        if (collection instanceof SeqLike<Integer> seq) {
            target = MutableArray.create(0);
            assertEquals(0, seq.copyTo(0, target, 0));
            assertEquals(0, seq.copyTo(10, target, 0));
            assertEquals(0, seq.copyTo(10, target, 10));

            target = MutableArray.create(5);
            assertEquals(4, seq.copyTo(0, target, 0));
            assertIterableEquals(Arrays.asList(0, 1, 2, 3, null), target);

            target = MutableArray.create(5);
            assertEquals(3, seq.copyTo(1, target, 0));
            assertIterableEquals(Arrays.asList(1, 2, 3, null, null), target);

            target = MutableArray.create(5);
            assertEquals(2, seq.copyTo(1, target, 2, 2));
            assertIterableEquals(Arrays.asList(null, null, 1, 2, null), target);
        }
    }
}
