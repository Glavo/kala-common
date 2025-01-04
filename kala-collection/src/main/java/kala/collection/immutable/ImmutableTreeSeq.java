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

import kala.collection.Collection;
import kala.collection.base.Traversable;
import kala.collection.factory.CollectionFactory;
import kala.collection.internal.tree.IndexedTree;
import kala.index.Index;
import kala.index.Indexes;
import kala.value.Var;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class ImmutableTreeSeq<E> extends AbstractImmutableSeq<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = 5783543850996772547L;

    private static final Factory<?> FACTORY = new Factory<>();
    private static final ImmutableTreeSeq<?> EMPTY = new ImmutableTreeSeq<>(IndexedTree.empty());

    private final IndexedTree<E> root;

    private ImmutableTreeSeq(IndexedTree<E> root) {
        this.root = root;
    }

    //region Static Factories

    @SuppressWarnings("unchecked")
    public static <E> @NotNull CollectionFactory<E, ?, ImmutableTreeSeq<E>> factory() {
        return (ImmutableTreeSeq.Factory<E>) FACTORY;
    }

    @SuppressWarnings("unchecked")
    public static <E> ImmutableTreeSeq<E> empty() {
        return (ImmutableTreeSeq<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableTreeSeq<E> of() {
        return empty();
    }

    public static <E> @NotNull ImmutableTreeSeq<E> of(E value1) {
        return new ImmutableTreeSeq<>(IndexedTree.<E>empty().plus(0, value1));
    }

    public static <E> @NotNull ImmutableTreeSeq<E> of(E value1, E value2) {
        return new ImmutableTreeSeq<>(IndexedTree.<E>empty()
                .plus(0, value1)
                .plus(1, value2)
        );
    }

    public static <E> @NotNull ImmutableTreeSeq<E> of(E value1, E value2, E value3) {
        return new ImmutableTreeSeq<>(IndexedTree.<E>empty()
                .plus(0, value1)
                .plus(1, value2)
                .plus(2, value3)
        );
    }

    public static <E> @NotNull ImmutableTreeSeq<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableTreeSeq<>(IndexedTree.<E>empty()
                .plus(0, value1)
                .plus(1, value2)
                .plus(2, value3)
                .plus(3, value4)
        );
    }

    public static <E> @NotNull ImmutableTreeSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableTreeSeq<>(IndexedTree.<E>empty()
                .plus(0, value1)
                .plus(1, value2)
                .plus(2, value3)
                .plus(3, value4)
                .plus(4, value5)
        );
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableTreeSeq<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableTreeSeq<E> from(E @NotNull [] values) {
        if (values.length == 0) {
            return empty();
        }

        IndexedTree<E> node = IndexedTree.empty();
        int i = 0;
        for (E value : values) {
            node = node.plus(i++, value);
        }

        return new ImmutableTreeSeq<>(node);
    }

    public static <E> @NotNull ImmutableTreeSeq<E> from(@NotNull java.util.Collection<? extends E> values) {
        if (values.isEmpty()) {
            return empty();
        }
        IndexedTree<E> node = IndexedTree.empty();
        int i = 0;
        for (E value : values) {
            node = node.plus(i++, value);
        }
        return node.size() != 0 ? new ImmutableTreeSeq<>(node) : empty();
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableTreeSeq<E> from(@NotNull Traversable<? extends E> values) {
        if (values instanceof ImmutableTreeSeq<? extends E>) {
            return ((ImmutableTreeSeq<E>) values);
        }
        if (values.knownSize() == 0) {
            return empty();
        }
        return from(values.iterator());
    }

    public static <E> @NotNull ImmutableTreeSeq<E> from(@NotNull Iterable<? extends E> values) {
        return from(values.iterator());
    }

    public static <E> @NotNull ImmutableTreeSeq<E> from(@NotNull Iterator<? extends E> it) {
        IndexedTree<E> node = IndexedTree.empty();
        int i = 0;
        while (it.hasNext()) {
            node = node.plus(i++, it.next());
        }
        return node.size() != 0 ? new ImmutableTreeSeq<>(node) : empty();
    }

    public static <E> @NotNull ImmutableTreeSeq<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull ImmutableTreeSeq<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }
        IndexedTree<E> node = IndexedTree.empty();
        for (int i = 0; i < n; i++) {
            node = node.plus(i, value);
        }
        return new ImmutableTreeSeq<>(node);
    }

    public static <E> @NotNull ImmutableTreeSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }
        IndexedTree<E> node = IndexedTree.empty();
        for (int i = 0; i < n; i++) {
            node = node.plus(i, init.apply(i));
        }
        return new ImmutableTreeSeq<>(node);
    }

    public static <E> @NotNull ImmutableTreeSeq<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        IndexedTree<E> node = IndexedTree.empty();
        int i = 0;
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;

            node = node.plus(i++, value);
        }
        return node.size() != 0 ? new ImmutableTreeSeq<>(node) : empty();
    }

    public static <E> @NotNull ImmutableTreeSeq<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        IndexedTree<E> node = IndexedTree.empty();
        int i = 0;
        while (true) {
            E value = supplier.get();
            if (value == null)
                break;
            node = node.plus(i++, value);
        }
        return node.size() != 0 ? new ImmutableTreeSeq<>(node) : empty();
    }


    //endregion

    @Override
    public @NotNull String className() {
        return "ImmutableTreeSeq";
    }

    @Override
    public boolean supportsFastRandomAccess() {
        return true;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return root.iterator();
    }

    @Override
    public int size() {
        return root.size();
    }

    @Override
    public int knownSize() {
        return size();
    }

    @Override
    public E get(@Index int index) {
        return root.get(Indexes.checkIndex(index, size()));
    }

    @Override
    public @NotNull ImmutableSeq<E> updated(@Index int index, E newValue) {
        index = Indexes.checkIndex(index, size());
        IndexedTree<E> newRoot = root.plus(index, newValue);
        return newRoot != root ? new ImmutableTreeSeq<>(newRoot) : this;
    }

    @Override
    public @NotNull ImmutableSeq<E> prepended(E value) {
        return new ImmutableTreeSeq<>(root.changeKeysAbove(0, 1).plus(0, value));
    }

    @Override
    public @NotNull ImmutableSeq<E> appended(E value) {
        return new ImmutableTreeSeq<>(root.plus(size(), value));
    }

    @Override
    public @NotNull ImmutableSeq<E> inserted(@Index int index, E value) {
        index = Indexes.checkPositionIndex(index, size());
        return new ImmutableTreeSeq<>(root.changeKeysAbove(index, 1).plus(index, value));
    }

    @Override
    public @NotNull ImmutableSeq<E> removedAt(@Index int index) {
        final int size = size();
        index = Indexes.checkIndex(index, size);
        return size > 1 ? new ImmutableTreeSeq<>(root.minus(index).changeKeysBelow(index, -1)) : empty();
    }

    @Serial
    private Object writeReplace() {
        return new Collection.SerializationWrapper<>(factory(), this);
    }

    private static final class Factory<E> implements CollectionFactory<E, Var<IndexedTree<E>>, ImmutableTreeSeq<E>>, Serializable {

        @Serial
        private static final long serialVersionUID = 0L;

        @Override
        public ImmutableTreeSeq<E> empty() {
            return ImmutableTreeSeq.empty();
        }

        @Override
        public Var<IndexedTree<E>> newBuilder() {
            return new Var<>(IndexedTree.empty());
        }

        @Override
        public ImmutableTreeSeq<E> build(Var<IndexedTree<E>> builder) {
            return builder.value.size() == 0 ? ImmutableTreeSeq.empty() : new ImmutableTreeSeq<>(builder.value);
        }

        @Override
        public void addToBuilder(@NotNull Var<IndexedTree<E>> builder, E value) {
            builder.value = builder.value.plus(builder.value.size(), value);
        }

        @Override
        public Var<IndexedTree<E>> mergeBuilder(@NotNull Var<IndexedTree<E>> builder1, @NotNull Var<IndexedTree<E>> builder2) {
            if (builder2.value.size() > 0) {
                if (builder1.value.size() > 0) {
                    IndexedTree<E> newValue = builder1.value;
                    for (E e : builder2.value) {
                        newValue = newValue.plus(newValue.size(), e);
                    }
                    builder1.value = newValue;
                } else {
                    builder1.value = builder2.value;
                }
            }

            return builder1;
        }

        @Serial
        private Object readResolve() {
            return factory();
        }
    }

}
