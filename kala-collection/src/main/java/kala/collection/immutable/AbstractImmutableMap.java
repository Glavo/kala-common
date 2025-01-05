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

import kala.collection.AbstractMap;
import kala.collection.factory.MapFactory;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public abstract class AbstractImmutableMap<K, V> extends AbstractMap<K, V> implements ImmutableMap<K, V> {
    static <K, V, T extends ImmutableMap<K, ? extends V>, Builder> T putted(
            @NotNull ImmutableMap<K, ? extends V> map,
            K key, V value,
            MapFactory<K, V, Builder, ? extends T> factory
    ) {
        if (map.contains(key, value)) {
            return (T) map;
        }

        Builder builder = factory.newBuilder();
        int ks = map.knownSize();
        if (ks >= 0) {
            factory.sizeHint(builder, ks + 1);
        }
        map.forEach((k, v) -> factory.addToBuilder(builder, k, v));
        factory.addToBuilder(builder, key, value);
        return factory.build(builder);
    }

    static <K, V, T extends ImmutableMap<K, ? extends V>, Builder> T removed(
            @NotNull ImmutableMap<K, ? extends V> map,
            K key,
            MapFactory<K, V, Builder, ? extends T> factory
    ) {
        if (!map.containsKey(key)) {
            return (T) map;
        }

        Builder builder = factory.newBuilder();
        int ks = map.knownSize();
        if (ks > 0) {
            factory.sizeHint(builder,ks - 1);
        }
        if (key == null) {
            map.forEach((k, v) -> {
                if (null != k) {
                    factory.addToBuilder(builder, k, v);
                }
            });
        } else {
            map.forEach((k, v) -> {
                if (!key.equals(k)) {
                    factory.addToBuilder(builder, k, v);
                }
            });
        }
        return factory.build(builder);
    }
}
