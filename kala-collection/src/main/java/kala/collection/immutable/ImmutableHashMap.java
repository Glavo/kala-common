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
package kala.collection.immutable;

import kala.collection.factory.MapBuilder;
import kala.collection.mutable.MutableHashMap;
import kala.control.Option;
import kala.collection.MapLike;
import kala.collection.base.MapIterator;
import kala.collection.factory.MapFactory;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@SuppressWarnings("unchecked")
public final class ImmutableHashMap<K, V> extends AbstractImmutableMap<K, V> implements Serializable {
    @SuppressWarnings("MissingSerialAnnotation")
    private static final long serialVersionUID = 4088221143962926192L;

    private static final ImmutableHashMap<?, ?> EMPTY = new ImmutableHashMap<>(new MutableHashMap<>());
    private static final Factory<?, ?> FACTORY = new Factory<>();

    final MutableHashMap<K, V> source;

    private ImmutableHashMap(MutableHashMap<K, V> source) {
        this.source = source;
    }

    //region Static Factories

    public static <K, V> @NotNull MapFactory<K, V, ?, ImmutableHashMap<K, V>> factory() {
        return (Factory<K, V>) FACTORY;
    }

    public static <K, V> @NotNull MapBuilder<K, V, ImmutableHashMap<K, V>> newMapBuilder() {
        return ImmutableHashMap.<K, V>factory().newMapBuilder();
    }

    public static <K, V> @NotNull MapBuilder<K, V, ImmutableHashMap<K, V>> newMapBuilder(int capacity) {
        return ImmutableHashMap.<K, V>factory().newMapBuilder(capacity);
    }

    static <T, K, V> @NotNull Collector<T, ?, ImmutableHashMap<K, V>> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return MapFactory.collector(factory(), keyMapper, valueMapper);
    }

    @SuppressWarnings("unchecked")
    public static <K, V> @NotNull ImmutableHashMap<K, V> empty() {
        return (ImmutableHashMap<K, V>) EMPTY;
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of() {
        return empty();
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(K k1, V v1) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(k1, v1);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(k1, v1);
        impl.set(k2, v2);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(k1, v1);
        impl.set(k2, v2);
        impl.set(k3, v3);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(k1, v1);
        impl.set(k2, v2);
        impl.set(k3, v3);
        impl.set(k4, v4);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(k1, v1);
        impl.set(k2, v2);
        impl.set(k3, v3);
        impl.set(k4, v4);
        impl.set(k5, v5);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> of(Object... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        if (values.length == 0) {
            return empty();
        }

        MutableHashMap<K, V> impl = new MutableHashMap<>();

        for (int i = 0; i < values.length; i += 2) {
            impl.set((K) values[i], (V) values[i + 1]);
        }

        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries() {
        return empty();
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(entry1);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(entry1);
        impl.set(entry2);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(entry1);
        impl.set(entry2);
        impl.set(entry3);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(entry1);
        impl.set(entry2);
        impl.set(entry3);
        impl.set(entry4);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.set(entry1);
        impl.set(entry2);
        impl.set(entry3);
        impl.set(entry4);
        impl.set(entry5);
        return new ImmutableHashMap<>(impl);
    }

    @SafeVarargs
    public static <K, V> @NotNull ImmutableHashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            impl.set(entry);
        }
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        if (values.isEmpty()) {
            return empty();
        }
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.putAll(values);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        if (values.isEmpty()) {
            return empty();
        }
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        impl.putAll(values);
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        if (values.length == 0) {
            return empty();
        }
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        for (Map.Entry<? extends K, ? extends V> value : values) {
            impl.set(value.getKey(), value.getValue());
        }
        return new ImmutableHashMap<>(impl);
    }

    public static <K, V> @NotNull ImmutableHashMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        Iterator<? extends Map.Entry<? extends K, ? extends V>> it = values.iterator();
        if (!it.hasNext()) {
            return empty();
        }
        MutableHashMap<K, V> impl = new MutableHashMap<>();
        while (it.hasNext()) {
            Map.Entry<? extends K, ? extends V> entry = it.next();
            impl.set(entry.getKey(), entry.getValue());
        }
        return new ImmutableHashMap<>(impl);
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "ImmutableHashMap";
    }

    @Override
    public @NotNull MapIterator<K, V> iterator() {
        return source.iterator();
    }

    //region Size Info

    @Override
    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public int knownSize() {
        return source.knownSize();
    }

    //endregion

    @Override
    public V get(K key) {
        return source.get(key);
    }

    @Override
    public @Nullable V getOrNull(K key) {
        return source.getOrNull(key);
    }

    @Override
    public @NotNull Option<V> getOption(K key) {
        return source.getOption(key);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        return source.getOrDefault(key, defaultValue);
    }

    @Override
    public V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
        return source.getOrElse(key, supplier);
    }

    @Override
    public <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
        return source.getOrThrow(key, supplier);
    }

    @Override
    public <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
        return source.getOrThrowException(key, exception);
    }

    //region Element Conditions

    @Override
    public boolean contains(K key, Object value) {
        return source.contains(key, value);
    }

    @Override
    public boolean containsKey(K key) {
        return source.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return source.containsValue(value);
    }

    @Override
    public boolean anyMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        return source.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        return source.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        return source.noneMatch(predicate);
    }

    //endregion

    @Override
    public void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        source.forEach(consumer);
    }

    private static final class Factory<K, V> implements MapFactory<K, V, Builder<K, V>, ImmutableHashMap<K, V>> {
        @Override
        public Builder<K, V> newBuilder() {
            return new Builder<>();
        }

        @Override
        public ImmutableHashMap<K, V> build(Builder<K, V> builder) {
            return builder.build();
        }

        @Override
        public void addToBuilder(Builder<K, V> builder, K key, V value) {
            builder.add(key, value);
        }

        @Override
        public Builder<K, V> mergeBuilder(Builder<K, V> builder1, Builder<K, V> builder2) {
            return builder1.merge(builder2);
        }

        @Override
        public void sizeHint(@NotNull Builder<K, V> builder, int size) {
            builder.sizeHint(size);
        }
    }

    static final class Builder<K, V> {
        MutableHashMap<K, V> source = new MutableHashMap<>();
        boolean aliased = false;

        private void ensureUnaliased() {
            if (aliased) {
                source = source.clone();
            }
        }

        void add(K key, V value) {
            ensureUnaliased();
            source.set(key, value);
        }

        Builder<K, V> merge(Builder<K, V> other) {
            ensureUnaliased();
            this.source.putAll(other.source);
            return this;
        }

        void sizeHint(int size) {
            ensureUnaliased();
            source.sizeHint(size);
        }

        ImmutableHashMap<K, V> build() {
            if (source.isEmpty()) {
                return ImmutableHashMap.empty();
            }
            aliased = true;
            return new ImmutableHashMap<>(source);
        }
    }
}