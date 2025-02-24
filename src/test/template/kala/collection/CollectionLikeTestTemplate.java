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
}
