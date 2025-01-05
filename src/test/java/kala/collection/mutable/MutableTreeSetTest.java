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

import kala.collection.factory.CollectionFactory;

import java.util.Comparator;

public class MutableTreeSetTest implements MutableSortedSetTestTemplate {

    @Override
    public <E> CollectionFactory<E, ?, ? extends MutableSortedSet<E>> factory(Comparator<? super E> comparator) {
        return MutableTreeSet.factory(comparator);
    }

    @Override
    public <E> MutableSortedSet<E> of(Comparator<? super E> comparator, E... elements) {
        return MutableTreeSet.of(comparator, elements);
    }

    @Override
    public <E> MutableSortedSet<E> from(Comparator<? super E> comparator, E[] elements) {
        return MutableTreeSet.from(comparator, elements);
    }

    @Override
    public <E> MutableSortedSet<E> from(Comparator<? super E> comparator, Iterable<? extends E> elements) {
        return MutableTreeSet.from(comparator, elements);
    }
}
