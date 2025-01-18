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

import kala.collection.MapLike;
import kala.collection.MapLikeTestTemplate;
import kala.collection.factory.MapFactory;
import kala.tuple.Tuple2;

import java.util.Comparator;

public final class ImmutableTreeMapTest implements ImmutableSortedMapTestTemplate {
    @Override
    public <K, V> MapFactory<K, V, ?, ImmutableTreeMap<K, V>> factory(Comparator<? super K> comparator) {
        return ImmutableTreeMap.factory(comparator);
    }

    static final class ViewTest implements MapLikeTestTemplate {
        @Override
        public <K, V> MapLike<K, V> from(Iterable<Tuple2<K, V>> entries) {
            return ImmutableTreeMap.from(null, entries).view();
        }
    }
}
