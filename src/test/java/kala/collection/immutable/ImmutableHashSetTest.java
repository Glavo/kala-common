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
package kala.collection.immutable;

import kala.collection.SetLike;
import kala.collection.SetLikeTestTemplate;
import kala.collection.factory.CollectionFactory;

public final class ImmutableHashSetTest implements ImmutableSetTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableHashSet<E>> factory() {
        return ImmutableHashSet.factory();
    }

    static final class ViewTest implements SetLikeTestTemplate {

        @Override
        public <E> SetLike<E> of(E... elements) {
            return ImmutableHashSet.of(elements).view();
        }

        @Override
        public <E> SetLike<E> from(E[] elements) {
            return ImmutableHashSet.from(elements).view();
        }

        @Override
        public <E> SetLike<E> from(Iterable<? extends E> elements) {
            return ImmutableHashSet.<E>from(elements).view();
        }
    }
}
