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
package kala.collection.mutable;

import kala.collection.SequentialCollectionTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public interface MutableSequentialCollectionTestTemplate extends MutableCollectionTestTemplate, SequentialCollectionTestTemplate {

    @Test
    default void cloneTest() {
        List<MutableCollection<Integer>> collections = List.of(of(), of(1, 2, 3));
        for (var collection : collections) {
            MutableCollection<Integer> cloned = collection.clone();
            assertIterableEquals(collection, cloned);
            if (!collection.isEmpty()) {
                assertNotSame(collection, cloned);
            }
        }
    }
}
