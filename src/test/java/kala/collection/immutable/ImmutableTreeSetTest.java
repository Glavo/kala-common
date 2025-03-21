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

import kala.collection.SetLikeTestTemplate;
import kala.collection.SetView;
import kala.collection.factory.CollectionFactory;

import java.util.Comparator;

public final class ImmutableTreeSetTest implements ImmutableSortedSetTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableTreeSet<E>> factory(Comparator<? super E> comparator) {
        return ImmutableTreeSet.factory(comparator);
    }

    static final class ViewTest implements SetLikeTestTemplate {
        @Override
        public <E> SetView<E> of(E... elements) {
            return ImmutableTreeSet.from(null, elements).view();
        }

        @Override
        public <E> SetView<E> from(E[] elements) {
            return ImmutableTreeSet.from(null, elements).view();
        }

        @Override
        public <E> SetView<E> from(Iterable<? extends E> elements) {
            return ImmutableTreeSet.<E>from(null, elements).view();
        }
    }
}
