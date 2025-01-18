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
import kala.collection.base.AbstractMapIterator;
import kala.collection.base.MapIterator;
import kala.collection.factory.MapFactory;
import kala.collection.internal.tree.KVTree;
import kala.control.Option;
import kala.internal.ComparableUtils;
import kala.tuple.Tuple2;
import kala.value.Var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class ImmutableTreeMap<K, V> extends AbstractImmutableSortedMap<K, V> {

    private static final Factory<?, ?> DEFAULT_FACTORY = new Factory<>(null);

    private static <K, V> @NotNull Factory<K, V> factoryImpl() {
        return (Factory<K, V>) DEFAULT_FACTORY;
    }

    private static <K, V> @NotNull Factory<K, V> factoryImpl(Comparator<? super K> comparator) {
        return comparator == null ? factoryImpl() : new Factory<>(comparator);
    }

    //region Static Factories

    public static <K extends Comparable<? super K>, V> @NotNull MapFactory<K, V, ?, ImmutableTreeMap<K, V>> factory() {
        return factoryImpl();
    }

    public static <K, V> @NotNull MapFactory<K, V, ?, ImmutableTreeMap<K, V>> factory(Comparator<? super K> comparator) {
        return factoryImpl(comparator);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> empty() {
        return (ImmutableTreeMap<K, V>) DEFAULT_FACTORY.empty;
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> empty(Comparator<? super K> comparator) {
        return ImmutableTreeMap.<K, V>factoryImpl(comparator).empty;
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> of() {
        return empty();
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> of(
            @NotNull K k1, V v1
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> of(
            @NotNull K k1, V v1,
            @NotNull K k2, V v2
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        tree = tree.plus(k2, v2, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> of(
            @NotNull K k1, V v1,
            @NotNull K k2, V v2,
            @NotNull K k3, V v3
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        tree = tree.plus(k2, v2, factory.actualComparator);
        tree = tree.plus(k3, v3, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> of(
            @NotNull K k1, V v1,
            @NotNull K k2, V v2,
            @NotNull K k3, V v3,
            @NotNull K k4, V v4
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        tree = tree.plus(k2, v2, factory.actualComparator);
        tree = tree.plus(k3, v3, factory.actualComparator);
        tree = tree.plus(k4, v4, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> of(
            @NotNull K k1, V v1,
            @NotNull K k2, V v2,
            @NotNull K k3, V v3,
            @NotNull K k4, V v4,
            @NotNull K k5, V v5
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        tree = tree.plus(k2, v2, factory.actualComparator);
        tree = tree.plus(k3, v3, factory.actualComparator);
        tree = tree.plus(k4, v4, factory.actualComparator);
        tree = tree.plus(k5, v5, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> of(Comparator<? super K> comparator) {
        return empty(comparator);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> of(
            Comparator<? super K> comparator,
            K k1, V v1
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> of(
            Comparator<? super K> comparator,
            K k1, V v1,
            K k2, V v2
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        tree = tree.plus(k2, v2, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> of(
            Comparator<? super K> comparator,
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        tree = tree.plus(k2, v2, factory.actualComparator);
        tree = tree.plus(k3, v3, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> of(
            Comparator<? super K> comparator,
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        tree = tree.plus(k2, v2, factory.actualComparator);
        tree = tree.plus(k3, v3, factory.actualComparator);
        tree = tree.plus(k4, v4, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> of(
            Comparator<? super K> comparator,
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(k1, v1, factory.actualComparator);
        tree = tree.plus(k2, v2, factory.actualComparator);
        tree = tree.plus(k3, v3, factory.actualComparator);
        tree = tree.plus(k4, v4, factory.actualComparator);
        tree = tree.plus(k5, v5, factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> ofEntries() {
        return empty();
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        tree = tree.plus(entry2.getKey(), entry2.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        tree = tree.plus(entry2.getKey(), entry2.getValue(), factory.actualComparator);
        tree = tree.plus(entry3.getKey(), entry3.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        tree = tree.plus(entry2.getKey(), entry2.getValue(), factory.actualComparator);
        tree = tree.plus(entry3.getKey(), entry3.getValue(), factory.actualComparator);
        tree = tree.plus(entry4.getKey(), entry4.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        tree = tree.plus(entry2.getKey(), entry2.getValue(), factory.actualComparator);
        tree = tree.plus(entry3.getKey(), entry3.getValue(), factory.actualComparator);
        tree = tree.plus(entry4.getKey(), entry4.getValue(), factory.actualComparator);
        tree = tree.plus(entry5.getKey(), entry5.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    @SafeVarargs
    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        return from(entries);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> ofEntries(Comparator<? super K> comparator) {
        return empty(comparator);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        tree = tree.plus(entry2.getKey(), entry2.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        tree = tree.plus(entry2.getKey(), entry2.getValue(), factory.actualComparator);
        tree = tree.plus(entry3.getKey(), entry3.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        tree = tree.plus(entry2.getKey(), entry2.getValue(), factory.actualComparator);
        tree = tree.plus(entry3.getKey(), entry3.getValue(), factory.actualComparator);
        tree = tree.plus(entry4.getKey(), entry4.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();
        tree = tree.plus(entry1.getKey(), entry1.getValue(), factory.actualComparator);
        tree = tree.plus(entry2.getKey(), entry2.getValue(), factory.actualComparator);
        tree = tree.plus(entry3.getKey(), entry3.getValue(), factory.actualComparator);
        tree = tree.plus(entry4.getKey(), entry4.getValue(), factory.actualComparator);
        tree = tree.plus(entry5.getKey(), entry5.getValue(), factory.actualComparator);
        return new ImmutableTreeMap<>(factory, tree);
    }

    @SafeVarargs
    public static <K, V> @NotNull ImmutableTreeMap<K, V> ofEntries(
            Comparator<? super K> comparator,
            Tuple2<? extends K, ? extends V> @NotNull ... entries
    ) {
        return from(comparator, entries);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        if (values.isEmpty()) {
            return empty();
        }

        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        final var treeVar = new Var<>(KVTree.<K, V>empty());
        values.forEach((k, v) ->
                treeVar.value = treeVar.value.plus(k, v, factory.actualComparator));
        return treeVar.value.isEmpty() ? empty() : new ImmutableTreeMap<>(factory, treeVar.value);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        if (values.isEmpty()) {
            return empty();
        }

        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        final var treeVar = new Var<>(KVTree.<K, V>empty());
        values.forEach((k, v) ->
                treeVar.value = treeVar.value.plus(k, v, factory.actualComparator));
        return treeVar.value.isEmpty() ? empty() : new ImmutableTreeMap<>(factory, treeVar.value);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        if (values.length == 0) {
            return empty();
        }

        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();

        for (var entry : values) {
            tree = tree.plus(entry.getKey(), entry.getValue(), factory.actualComparator);
        }
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K extends Comparable<? super K>, V> @NotNull ImmutableTreeMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();

        for (var entry : values) {
            tree = tree.plus(entry.getKey(), entry.getValue(), factory.actualComparator);
        }
        return tree.isEmpty() ? empty() : new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> from(Comparator<? super K> comparator, java.util.@NotNull Map<? extends K, ? extends V> values) {
        if (values.isEmpty()) {
            return empty(comparator);
        }

        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        final var treeVar = new Var<>(KVTree.<K, V>empty());
        values.forEach((k, v) ->
                treeVar.value = treeVar.value.plus(k, v, factory.actualComparator));
        return treeVar.value.isEmpty() ? empty(comparator) : new ImmutableTreeMap<>(factory, treeVar.value);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> from(Comparator<? super K> comparator, @NotNull MapLike<? extends K, ? extends V> values) {
        if (values.isEmpty()) {
            return empty(comparator);
        }

        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        final var treeVar = new Var<>(KVTree.<K, V>empty());
        values.forEach((k, v) ->
                treeVar.value = treeVar.value.plus(k, v, factory.actualComparator));
        return treeVar.value.isEmpty() ? empty(comparator) : new ImmutableTreeMap<>(factory, treeVar.value);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> from(Comparator<? super K> comparator, java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        if (values.length == 0) {
            return empty(comparator);
        }

        final var factory = ImmutableTreeMap.<K, V>factoryImpl();
        var tree = KVTree.<K, V>empty();

        for (var entry : values) {
            tree = tree.plus(entry.getKey(), entry.getValue(), factory.actualComparator);
        }
        return new ImmutableTreeMap<>(factory, tree);
    }

    public static <K, V> @NotNull ImmutableTreeMap<K, V> from(Comparator<? super K> comparator, @NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        final var factory = ImmutableTreeMap.<K, V>factoryImpl(comparator);
        var tree = KVTree.<K, V>empty();

        for (var entry : values) {
            tree = tree.plus(entry.getKey(), entry.getValue(), factory.actualComparator);
        }
        return tree.isEmpty() ? empty(comparator) : new ImmutableTreeMap<>(factory, tree);
    }

    //endregion

    private final @NotNull Factory<K, V> factory;
    private final @NotNull KVTree<K, V> tree;

    private ImmutableTreeMap(@NotNull Factory<K, V> factory, @NotNull KVTree<K, V> tree) {
        this.factory = factory;
        this.tree = tree;
    }

    @Override
    public @Nullable Comparator<? super K> comparator() {
        return factory.comparator;
    }

    @Override
    public @NotNull MapFactory<K, V, ?, ImmutableTreeMap<K, V>> sortedMapFactory() {
        return factory;
    }

    @Override
    public @NotNull <NK, NV> MapFactory<NK, NV, ?, ImmutableTreeMap<NK, NV>> sortedMapFactory(Comparator<? super NK> comparator) {
        return factory(comparator);
    }

    @Override
    public @NotNull MapIterator<K, V> iterator() {
        return MapIterator.ofIterator(tree.entryIterator(true));
    }

    @Override
    public K firstKey() {
        return tree.getLeftmost().getKey();
    }

    @Override
    public K lastKey() {
        return tree.getRightmost().getKey();
    }

    @Override
    public boolean containsKey(K key) {
        return !tree.search(key, factory.actualComparator, KVTree.SearchType.EQ).isEmpty();
    }

    @Override
    public @NotNull Option<V> getOption(K key) {
        final KVTree<K, V> tree = this.tree.search(key, factory.actualComparator, KVTree.SearchType.EQ);
        return tree.isEmpty() ? Option.none() : Option.some(tree.getValue());
    }

    private @NotNull ImmutableTreeMap<K, V> withTree(@NotNull KVTree<K, V> newTree) {
        if (newTree.isEmpty()) {
            return factory.empty;
        }
        if (newTree == this.tree) {
            return this;
        }
        return new ImmutableTreeMap<>(factory, newTree);
    }

    @Override
    public @NotNull ImmutableMap<K, V> updated(K key, V value) {
        return withTree(tree.plus(key, value, factory.actualComparator));
    }

    @Override
    public @NotNull ImmutableMap<K, V> removed(K key) {
        return withTree(tree.minus(key, factory.actualComparator));
    }

    private static final class Builder<K, V> {
        private final @NotNull Factory<K, V> factory;
        private @NotNull KVTree<K, V> tree = KVTree.empty();

        private Builder(@NotNull Factory<K, V> factory) {
            this.factory = factory;
        }

        public void add(K key, V value) {
            tree = tree.plus(key, value, factory.actualComparator);
        }

        public @NotNull ImmutableTreeMap<K, V> build() {
            return new ImmutableTreeMap<>(factory, tree);
        }
    }

    private static final class Factory<K, V> implements MapFactory<K, V, Builder<K, V>, ImmutableTreeMap<K, V>> {

        private final @Nullable Comparator<? super K> comparator;
        private final @NotNull Comparator<? super K> actualComparator;
        private final transient ImmutableTreeMap<K, V> empty;

        private Factory(@Nullable Comparator<? super K> comparator) {
            this.comparator = comparator;
            this.actualComparator = Objects.requireNonNullElse(comparator, ComparableUtils.naturalOrder());
            this.empty = new ImmutableTreeMap<>(this, KVTree.empty());
        }

        @Override
        public ImmutableTreeMap<K, V> empty() {
            return empty;
        }

        @Override
        public Builder<K, V> newBuilder() {
            return new Builder<>(this);
        }

        @Override
        public ImmutableTreeMap<K, V> build(Builder<K, V> builder) {
            return builder.build();
        }

        @Override
        public void addToBuilder(Builder<K, V> builder, K key, V value) {
            builder.add(key, value);
        }

        @Override
        public Builder<K, V> mergeBuilder(Builder<K, V> builder1, Builder<K, V> builder2) {
            builder2.tree.entryIterator(true).forEachRemaining(
                    entry -> builder1.add(entry.getKey(), entry.getValue()));
            return builder1;
        }
    }
}
