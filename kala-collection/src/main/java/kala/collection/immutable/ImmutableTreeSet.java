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

import kala.collection.Collection;
import kala.collection.base.Iterators;
import kala.collection.factory.CollectionBuilder;
import kala.collection.factory.CollectionFactory;
import kala.collection.internal.tree.KVTree;
import kala.internal.ComparableUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class ImmutableTreeSet<E> extends AbstractImmutableSortedSet<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = -7146184321209692422L;

    private static final Factory<?> DEFAULT_FACTORY = new Factory<>(null);

    //region Static Factories

    @SuppressWarnings("unchecked")
    private static <E extends Comparable<? super E>> Factory<E> factoryImpl() {
        return (Factory<E>) DEFAULT_FACTORY;
    }

    @SuppressWarnings("unchecked")
    private static <E> Factory<E> factoryImpl(Comparator<? super E> comparator) {
        return comparator == null ? (ImmutableTreeSet.Factory<E>) DEFAULT_FACTORY : new Factory<>(comparator);
    }

    public static <E extends Comparable<? super E>> @NotNull CollectionFactory<E, ?, ImmutableTreeSet<E>> factory() {
        return factoryImpl();
    }

    public static <E> @NotNull CollectionFactory<E, ?, ImmutableTreeSet<E>> factory(Comparator<? super E> comparator) {
        return factoryImpl(comparator);
    }

    @Contract
    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<@NotNull E> empty() {
        return ImmutableTreeSet.<E>factory().empty();
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<@NotNull E> of() {
        return empty();
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<@NotNull E> of(@NotNull E value1) {
        Objects.requireNonNull(value1);

        final Comparator<E> actualComparator = Comparator.naturalOrder();
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(), tree);
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2
    ) {
        Objects.requireNonNull(value1);
        Objects.requireNonNull(value2);

        final Comparator<E> actualComparator = Comparator.naturalOrder();
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, actualComparator);
        tree = tree.plus(value2, null, actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(), tree);
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3
    ) {
        Objects.requireNonNull(value1);
        Objects.requireNonNull(value2);
        Objects.requireNonNull(value3);

        final Comparator<E> actualComparator = Comparator.naturalOrder();
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, actualComparator);
        tree = tree.plus(value2, null, actualComparator);
        tree = tree.plus(value3, null, actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(), tree);
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3, @NotNull E value4
    ) {
        Objects.requireNonNull(value1);
        Objects.requireNonNull(value2);
        Objects.requireNonNull(value3);
        Objects.requireNonNull(value4);

        final Comparator<E> actualComparator = Comparator.naturalOrder();
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, actualComparator);
        tree = tree.plus(value2, null, actualComparator);
        tree = tree.plus(value3, null, actualComparator);
        tree = tree.plus(value4, null, actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(), tree);
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<@NotNull E> of(
            @NotNull E value1, @NotNull E value2, @NotNull E value3, @NotNull E value4, @NotNull E value5
    ) {
        Objects.requireNonNull(value1);
        Objects.requireNonNull(value2);
        Objects.requireNonNull(value3);
        Objects.requireNonNull(value4);
        Objects.requireNonNull(value5);

        final Comparator<E> actualComparator = Comparator.naturalOrder();
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, actualComparator);
        tree = tree.plus(value2, null, actualComparator);
        tree = tree.plus(value3, null, actualComparator);
        tree = tree.plus(value4, null, actualComparator);
        tree = tree.plus(value5, null, actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(), tree);
    }

    @SafeVarargs
    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableTreeSet<E> empty(Comparator<? super E> comparator) {
        return ImmutableTreeSet.<E>factory(comparator).empty();
    }

    public static <E> @NotNull ImmutableTreeSet<E> of(Comparator<? super E> comparator) {
        return empty(comparator);
    }

    public static <E> @NotNull ImmutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1
    ) {
        final Factory<E> factory = factoryImpl(comparator);
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, factory.actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(comparator), tree);
    }

    public static <E> @NotNull ImmutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2
    ) {
        final Factory<E> factory = factoryImpl(comparator);
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, factory.actualComparator);
        tree = tree.plus(value2, null, factory.actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(comparator), tree);
    }

    public static <E> @NotNull ImmutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3) {
        final Factory<E> factory = factoryImpl(comparator);
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, factory.actualComparator);
        tree = tree.plus(value2, null, factory.actualComparator);
        tree = tree.plus(value3, null, factory.actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(comparator), tree);
    }

    public static <E> @NotNull ImmutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3, E value4
    ) {
        final Factory<E> factory = factoryImpl(comparator);
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, factory.actualComparator);
        tree = tree.plus(value2, null, factory.actualComparator);
        tree = tree.plus(value3, null, factory.actualComparator);
        tree = tree.plus(value4, null, factory.actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(comparator), tree);
    }

    public static <E> @NotNull ImmutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3, E value4, E value5
    ) {
        final Factory<E> factory = factoryImpl(comparator);
        KVTree<E, Void> tree = KVTree.empty();
        tree = tree.plus(value1, null, factory.actualComparator);
        tree = tree.plus(value2, null, factory.actualComparator);
        tree = tree.plus(value3, null, factory.actualComparator);
        tree = tree.plus(value4, null, factory.actualComparator);
        tree = tree.plus(value5, null, factory.actualComparator);
        return new ImmutableTreeSet<>(factoryImpl(comparator), tree);
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableTreeSet<E> of(Comparator<? super E> comparator, E... values) {
        return from(comparator, values);
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<E> from(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of values
            return empty();
        }

        final Comparator<E> comparator = Comparator.naturalOrder();
        KVTree<E, Void> tree = KVTree.empty();
        for (E value : values) {
            tree = tree.plus(value, null, comparator);
        }
        return new ImmutableTreeSet<>(factoryImpl(), tree);
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<E> from(
            @NotNull Iterable<? extends E> values
    ) {
        return from(values.iterator());
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<E> from(
            @NotNull Iterator<? extends E> it
    ) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }

        final Comparator<E> comparator = Comparator.naturalOrder();
        KVTree<E, Void> tree = KVTree.empty();
        while (it.hasNext()) {
            tree = tree.plus(it.next(), null, comparator);
        }
        return new ImmutableTreeSet<>(factoryImpl(), tree);
    }

    public static <E extends Comparable<? super E>> @NotNull ImmutableTreeSet<E> from(
            @NotNull Stream<? extends E> stream
    ) {
        return stream.collect(factory());
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableTreeSet<E> from(Comparator<? super E> comparator, E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of values
            return empty(comparator);
        }

        final Factory<E> factory = factoryImpl(comparator);
        KVTree<E, Void> tree = KVTree.empty();
        for (E value : values) {
            tree = tree.plus(value, null, factory.actualComparator);
        }
        return new ImmutableTreeSet<>(factory, tree);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableTreeSet<E> from(Comparator<? super E> comparator, @NotNull Iterable<? extends E> values) {
        return from(comparator, values.iterator());
    }

    @Contract(value = "_, _ -> new")
    public static <E> @NotNull ImmutableTreeSet<E> from(Comparator<? super E> comparator, @NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty(comparator);
        }

        final Factory<E> factory = factoryImpl(comparator);
        KVTree<E, Void> tree = KVTree.empty();
        while (it.hasNext()) {
            tree = tree.plus(it.next(), null, factory.actualComparator);
        }
        return new ImmutableTreeSet<>(factory, tree);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ImmutableTreeSet<E> from(Comparator<? super E> comparator, @NotNull Stream<? extends E> stream) {
        return stream.collect(factory(comparator));
    }

    //endregion

    private final @NotNull Factory<E> factory;
    private final @NotNull KVTree<E, Void> tree;

    private ImmutableTreeSet(@NotNull Factory<E> factory, @NotNull KVTree<E, Void> tree) {
        this.factory = factory;
        this.tree = tree;
    }

    @Override
    public @Nullable Comparator<? super E> comparator() {
        return factory.comparator;
    }

    @Override
    public @NotNull CollectionFactory<E, ?, ? extends ImmutableSortedSet<E>> sortedIterableFactory() {
        return factory;
    }

    @Override
    public @NotNull <U> CollectionFactory<U, ?, ? extends ImmutableSortedSet<U>> sortedIterableFactory(Comparator<? super U> comparator) {
        return factory(comparator);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return Iterators.map(tree.entryIterator(true), Map.Entry::getKey);
    }

    @Override
    public @NotNull Iterator<E> reverseIterator() {
        return Iterators.map(tree.entryIterator(false), Map.Entry::getKey);
    }

    @Override
    public boolean contains(Object value) {
        try {
            @SuppressWarnings("unchecked")
            E e = (E) value;
            return !tree.search(e, factory.actualComparator, KVTree.SearchType.EQ).isEmpty();
        } catch (ClassCastException | NullPointerException ignored) {
            return false;
        }
    }

    @Override
    public @NotNull ImmutableSet<E> added(E value) {
        KVTree<E, Void> newTree = tree.plus(value, null, factory.actualComparator);
        return newTree == tree ? this : new ImmutableTreeSet<>(factory, newTree);
    }

    @Override
    public @NotNull ImmutableSet<E> addedAll(@NotNull Iterable<? extends E> values) {
        KVTree<E, Void> newTree = this.tree;
        for (E value : values) {
            newTree = newTree.plus(value, null, factory.actualComparator);
        }
        return newTree == tree ? this : new ImmutableTreeSet<>(factory, newTree);
    }

    @Override
    public @NotNull ImmutableSet<E> removed(E value) {
        try {
            KVTree<E, Void> newTree = tree.minus(value, factory.actualComparator);
            return newTree == tree ? this : new ImmutableTreeSet<>(factory, newTree);
        } catch (NullPointerException ignored) {
            return this;
        }
    }

    @Override
    public @NotNull ImmutableSet<E> removedAll(@NotNull Iterable<? extends E> values) {
        try {
            KVTree<E, Void> newTree = this.tree;
            for (E value : values) {
                newTree = newTree.minus(value, factory.actualComparator);
            }
            return newTree == tree ? this : new ImmutableTreeSet<>(factory, newTree);
        } catch (NullPointerException ignored) {
            return this;
        }
    }

    @Serial
    private Object writeReplace() {
        return new Collection.SerializationWrapper<>(factory, this);
    }

    private static final class Builder<E> implements CollectionBuilder<E, ImmutableTreeSet<E>> {

        private final Factory<E> factory;
        private final @NotNull Comparator<? super E> actualComparator;
        private KVTree<E, Void> tree = KVTree.empty();

        private Builder(Factory<E> factory) {
            this.factory = factory;
            this.actualComparator = Objects.requireNonNullElse(factory.comparator, ComparableUtils.naturalOrder());
        }

        @Override
        public void plusAssign(E value) {
            tree = tree.plus(value, null, actualComparator);
        }

        @Override
        public ImmutableTreeSet<E> build() {
            if (tree.isEmpty()) {
                return factory.empty;
            }

            return new ImmutableTreeSet<>(factory, tree);
        }
    }

    private static final class Factory<E> implements CollectionFactory<E, Builder<E>, ImmutableTreeSet<E>>, Serializable {
        @Serial
        private static final long serialVersionUID = 2593042430467664697L;

        private final Comparator<? super E> comparator;
        private final @NotNull Comparator<? super E> actualComparator;
        private transient ImmutableTreeSet<E> empty;

        private Factory(Comparator<? super E> comparator) {
            this.comparator = comparator;
            this.actualComparator = Objects.requireNonNullElse(comparator, ComparableUtils.naturalOrder());

            this.empty = new ImmutableTreeSet<>(this, KVTree.empty());
        }

        @Override
        public ImmutableTreeSet<E> empty() {
            return empty;
        }

        @Override
        public @NotNull Builder<E> newCollectionBuilder(Builder<E> builder) {
            return builder;
        }

        @Override
        public Builder<E> newBuilder() {
            return new Builder<>(this);
        }

        @Override
        public ImmutableTreeSet<E> build(Builder<E> builder) {
            return builder.build();
        }

        @Override
        public void addToBuilder(@NotNull Builder<E> builder, E value) {
            builder.plusAssign(value);
        }

        @Override
        public Builder<E> mergeBuilder(@NotNull Builder<E> builder1, @NotNull Builder<E> builder2) {
            builder2.tree.entryIterator(true).forEachRemaining(entry -> builder1.plusAssign(entry.getKey()));
            return builder1;
        }

        @Serial
        private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            empty = new ImmutableTreeSet<>(this, KVTree.empty());
        }

        @Serial
        private Object readResolve() {
            return comparator == null ? DEFAULT_FACTORY : this;
        }
    }
}
