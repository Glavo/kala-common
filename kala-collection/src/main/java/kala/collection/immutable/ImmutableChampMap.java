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
import kala.collection.base.MapIterator;
import kala.collection.factory.MapFactory;
import kala.collection.internal.champ.BitmapIndexedChampMapNode;
import kala.collection.internal.champ.ChampMapBuilder;
import kala.collection.internal.champ.ChampMapKeyValueTupleIterator;
import kala.collection.internal.champ.ChampMapNode;
import kala.control.Option;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static kala.collection.internal.champ.ChampNode.improve;

@ApiStatus.Experimental
public final class ImmutableChampMap<K, V> extends AbstractImmutableMap<K, V> {

    private static final Factory<?, ?> FACTORY = new Factory<>();
    private static final ImmutableChampMap<?, ?> EMPTY = new ImmutableChampMap<>(ChampMapNode.empty());

    @SuppressWarnings("unchecked")
    public static <K, V> @NotNull MapFactory<K, V, ?, ImmutableChampMap<K, V>> factory() {
        return (Factory<K, V>) FACTORY;
    }

    public static <K, V> ImmutableChampMap<K, V> empty() {
        @SuppressWarnings("unchecked")
        ImmutableChampMap<K, V> empty = (ImmutableChampMap<K, V>) EMPTY;
        return empty;
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> of() {
        return empty();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> of(K k1, V v1) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(k1, v1);
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(k1, v1);
        builder.add(k2, v2);
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(k1, v1);
        builder.add(k2, v2);
        builder.add(k3, v3);
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(k1, v1);
        builder.add(k2, v2);
        builder.add(k3, v3);
        builder.add(k4, v4);
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(k1, v1);
        builder.add(k2, v2);
        builder.add(k3, v3);
        builder.add(k4, v4);
        builder.add(k5, v5);
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> ofEntries() {
        return empty();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(Tuple2.narrow(entry1));
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(Tuple2.narrow(entry1));
        builder.add(Tuple2.narrow(entry2));
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(Tuple2.narrow(entry1));
        builder.add(Tuple2.narrow(entry2));
        builder.add(Tuple2.narrow(entry3));
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(Tuple2.narrow(entry1));
        builder.add(Tuple2.narrow(entry2));
        builder.add(Tuple2.narrow(entry3));
        builder.add(Tuple2.narrow(entry4));
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        var builder = new ChampMapBuilder<K, V>();
        builder.add(Tuple2.narrow(entry1));
        builder.add(Tuple2.narrow(entry2));
        builder.add(Tuple2.narrow(entry3));
        builder.add(Tuple2.narrow(entry4));
        builder.add(Tuple2.narrow(entry5));
        return builder.build();
    }

    @SafeVarargs
    public static <K, V> @NotNull ImmutableChampMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        var builder = new ChampMapBuilder<K, V>();
        for (var entry : entries) {
            builder.add(Tuple2.narrow(entry));
        }
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        var builder = new ChampMapBuilder<K, V>();
        values.forEach(builder::add);
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        var builder = new ChampMapBuilder<K, V>();
        values.forEach(builder::add);
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        var builder = new ChampMapBuilder<K, V>();
        for (var entry : values) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    public static <K, V> @NotNull ImmutableChampMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        var builder = new ChampMapBuilder<K, V>();
        for (var entry : values) {
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }


    private final BitmapIndexedChampMapNode<K, V> rootNode;

    private ImmutableChampMap(BitmapIndexedChampMapNode<K, V> rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public int size() {
        return rootNode.size();
    }

    @Override
    public @NotNull MapIterator<K, V> iterator() {
        return isEmpty() ? MapIterator.empty() : MapIterator.ofIterator(new ChampMapKeyValueTupleIterator<>(rootNode));
    }

    @Override
    public boolean containsKey(K key) {
        int keyUnimprovedHash = Objects.hashCode(key);
        int keyHash = improve(keyUnimprovedHash);
        return rootNode.containsKey(key, keyHash, keyHash, 0);
    }

    @Override
    public @NotNull Option<V> getOption(K key) {
        int keyUnimprovedHash = Objects.hashCode(key);
        int keyHash = improve(keyUnimprovedHash);
        return rootNode.getOption(key, keyUnimprovedHash, keyHash, 0);
    }

    private @NotNull ImmutableChampMap<K, V> withTree(@NotNull BitmapIndexedChampMapNode<K, V> node) {
        if (node == rootNode) {
            return this;
        }

        if (node.size() == 0) {
            return empty();
        }

        return new ImmutableChampMap<>(node);
    }

    @Override
    public @NotNull ImmutableMap<K, V> putted(K key, V value) {
        int keyUnimprovedHash = Objects.hashCode(key);
        return withTree(rootNode.updated(key, value, keyUnimprovedHash, improve(keyUnimprovedHash), 0, true));
    }

    @Override
    public @NotNull ImmutableMap<K, V> removed(K key) {
        int keyUnimprovedHash = Objects.hashCode(key);
        return withTree(rootNode.removed(key, keyUnimprovedHash, improve(keyUnimprovedHash), 0));
    }

    private static final class Factory<K, V> implements MapFactory<K, V, ChampMapBuilder<K, V>, ImmutableChampMap<K, V>> {

        @Override
        public ChampMapBuilder<K, V> newBuilder() {
            return new ChampMapBuilder<>();
        }

        @Override
        public ImmutableChampMap<K, V> build(ChampMapBuilder<K, V> builder) {
            return builder.build();
        }

        @Override
        public void addToBuilder(ChampMapBuilder<K, V> builder, K key, V value) {
            builder.add(key, value);
        }

        @Override
        public ChampMapBuilder<K, V> mergeBuilder(ChampMapBuilder<K, V> builder1, ChampMapBuilder<K, V> builder2) {
            builder1.addAll(builder2);
            return builder1;
        }
    }
}
