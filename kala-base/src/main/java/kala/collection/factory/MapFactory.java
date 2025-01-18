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
package kala.collection.factory;

import kala.collection.base.AnyTraversable;
import kala.collection.base.Sized;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;

public interface MapFactory<K, V, Builder, R> extends Factory<Builder, R> {
    static <T, K, V, Builder, R> Collector<T, ?, R> collector(
            @NotNull MapFactory<K, V, Builder, R> factory,
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper) {
        return factory.collector(keyMapper, valueMapper);
    }

    Builder newBuilder();

    R build(Builder builder);

    void addToBuilder(Builder builder, K key, V value);

    default void addToBuilder(Builder builder, java.util.Map.@NotNull Entry<? extends K, ? extends V> entry) {
        addToBuilder(builder, entry.getKey(), entry.getValue());
    }

    default void addToBuilder(Builder builder, Tuple2<? extends K, ? extends V> entry) {
        addToBuilder(builder, (java.util.Map.Entry<? extends K, ? extends V>) entry);
    }

    Builder mergeBuilder(Builder builder1, Builder builder2);

    default void addAllToBuilder(Builder builder, java.util.@NotNull Map<? extends K, ? extends V> values) {
        values.forEach((k, v) -> addToBuilder(builder, k, v));
    }

    @SuppressWarnings("unchecked")
    default R ofEntries(@NotNull Tuple2<? extends K, ? extends V>... entries) {
        final Builder builder = newBuilder();
        for (var entry : entries) {
            addToBuilder(builder, entry.getKey(), entry.getValue());
        }
        return build(builder);
    }

    default R from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> entries) {
        final Builder builder = newBuilder();
        for (Map.Entry<? extends K, ? extends V> entry : entries) {
            addToBuilder(builder, entry.getKey(), entry.getValue());
        }
        return build(builder);
    }

    default <T> @NotNull Collector<T, Builder, R> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper) {
        return Collector.of(
                this::newBuilder,
                (builder, t) -> addToBuilder(builder, keyMapper.apply(t), valueMapper.apply(t)),
                this::mergeBuilder,
                this::build
        );
    }

    default void sizeHint(@NotNull Builder builder, int size) {

    }

    default void sizeHint(@NotNull Builder builder, @NotNull Iterable<?> it) {
        this.sizeHint(builder, it, 0);
    }

    default void sizeHint(@NotNull Builder builder, @NotNull Iterable<?> it, int delta) {
        if (it instanceof Collection<?>) {
            int s = ((Collection<?>) it).size();
            this.sizeHint(builder, s + delta);
        } else if (it instanceof AnyTraversable) {
            int ks = ((AnyTraversable<?>) it).knownSize();
            if (ks > 0) {
                this.sizeHint(builder, ks + delta);
            }
        }
    }

    default void sizeHint(@NotNull Builder builder, java.util.@NotNull Map<?, ?> it) {
        sizeHint(builder, it.size());
    }

    default void sizeHint(@NotNull Builder builder, Sized it) {
        int ks = it.knownSize();
        if (ks >= 0) {
            sizeHint(builder, ks);
        }
    }

    @Override
    default <U> @NotNull MapFactory<K, V, Builder, U> mapResult(@NotNull Function<? super R, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        final class MappedFactory implements MapFactory<K, V, Builder, U> {

            @Override
            public Builder newBuilder() {
                return MapFactory.this.newBuilder();
            }

            @Override
            public U build(Builder builder) {
                return mapper.apply(MapFactory.this.build(builder));
            }

            @Override
            public void addToBuilder(Builder builder, K key, V value) {
                MapFactory.this.addToBuilder(builder, key, value);
            }

            @Override
            public Builder mergeBuilder(Builder builder1, Builder builder2) {
                return MapFactory.this.mergeBuilder(builder1, builder2);
            }

            @Override
            public void sizeHint(@NotNull Builder builder, int size) {
                MapFactory.this.sizeHint(builder, size);
            }
        }
        return new MappedFactory();
    }
}
