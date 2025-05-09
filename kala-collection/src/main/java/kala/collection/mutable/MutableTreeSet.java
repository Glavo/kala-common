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
package kala.collection.mutable;

import kala.collection.AnySet;
import kala.collection.SortedSet;
import kala.collection.base.Iterators;
import kala.collection.Set;
import kala.collection.base.AbstractIterator;
import kala.collection.factory.CollectionBuilder;
import kala.collection.factory.CollectionFactory;
import kala.collection.internal.tree.RedBlackTree;
import kala.internal.ComparableUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class MutableTreeSet<E> extends RedBlackTree<E, MutableTreeSet.Node<E>>
        implements MutableSortedSet<E>, Cloneable, Serializable {

    @Serial
    private static final long serialVersionUID = 6211626172352429615L;

    private static final MutableTreeSet.Factory<? extends Comparable<?>> DEFAULT_FACTORY = new Factory<>(null);

    //region Constructors

    public MutableTreeSet() {
        this(null);
    }

    public MutableTreeSet(Comparator<? super E> comparator) {
        super(comparator);
    }

    //endregion

    //region Static Factories

    public static <E extends Comparable<? super E>> @NotNull CollectionFactory<E, ?, MutableTreeSet<E>> factory() {
        return (Factory<E>) DEFAULT_FACTORY;
    }

    public static <E> @NotNull CollectionFactory<E, ?, MutableTreeSet<E>> factory(Comparator<? super E> comparator) {
        return comparator == null ? (Factory<E>) DEFAULT_FACTORY : new Factory<>(comparator);
    }

    @Contract("-> new")
    public static <E extends Comparable<? super E>> @NotNull CollectionBuilder<E, MutableTreeSet<E>> newBuilder() {
        return MutableTreeSet.<E>factory().newCollectionBuilder();
    }

    @Contract("_ -> new")
    public static <E> @NotNull CollectionBuilder<E, MutableTreeSet<E>> newBuilder(Comparator<? super E> comparator) {
        return MutableTreeSet.<E>factory(comparator).newCollectionBuilder();
    }

    @Contract(value = " -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> create() {
        return new MutableTreeSet<>();
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> create(Comparator<? super E> comparator) {
        return new MutableTreeSet<>(comparator);
    }

    @Contract(value = " -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of() {
        return new MutableTreeSet<>();
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1, E value2) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        return s;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1, E value2, E value3) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1, E value2, E value3, E value4) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s;
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> of(E... values) {
        return from(values);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> of(Comparator<? super E> comparator) {
        return new MutableTreeSet<>(comparator);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        return s;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        return s;
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3, E value4
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s;
    }

    @Contract(value = "_, _, _, _, _, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> of(
            Comparator<? super E> comparator,
            E value1, E value2, E value3, E value4, E value5
    ) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> of(Comparator<? super E> comparator, E... values) {
        return from(comparator, values);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> from(E @NotNull [] values) {
        Objects.requireNonNull(values);
        MutableTreeSet<E> s = new MutableTreeSet<>();
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        Comparator<? super E> comparator = null;

        if (values instanceof SortedSet<?> sortedSet) {
            comparator = (Comparator<? super E>) sortedSet.comparator();
        } else if (values instanceof java.util.SortedSet<?> sortedSet) {
            comparator = (Comparator<? super E>) sortedSet.comparator();
        }

        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new")
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> from(@NotNull Iterator<? extends E> it) {
        MutableTreeSet<E> s = new MutableTreeSet<>();
        while (it.hasNext()) { // implicit null check of it
            s.add(it.next());
        }
        return s;
    }

    @Contract(value = "_ -> new")
    public static <E extends Comparable<? super E>> @NotNull MutableTreeSet<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> from(Comparator<? super E> comparator, E @NotNull [] values) {
        Objects.requireNonNull(values);
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull MutableTreeSet<E> from(Comparator<? super E> comparator, @NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        s.addAll(values);
        return s;
    }

    @Contract(value = "_, _ -> new")
    public static <E> @NotNull MutableTreeSet<E> from(Comparator<? super E> comparator, @NotNull Iterator<? extends E> it) {
        MutableTreeSet<E> s = new MutableTreeSet<>(comparator);
        while (it.hasNext()) { // implicit null check of it
            s.add(it.next());
        }
        return s;
    }

    @Contract(value = "_, _ -> new")
    public static <E> @NotNull MutableTreeSet<E> from(Comparator<? super E> comparator, @NotNull Stream<? extends E> stream) {
        return stream.collect(factory(comparator));
    }
    //endregion

    @Override
    public @NotNull String className() {
        return "MutableTreeSet";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, MutableTreeSet<U>> sortedIterableFactory(Comparator<? super U> comparator) {
        return factory(comparator);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        final Node<E> firstNode = firstNode();
        return firstNode == null ? Iterators.empty() : new MutableTreeSet.Itr<>(firstNode);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableTreeSet<E> clone() {
        return from(comparator, this); // need to optimize
    }

    @Override
    public boolean add(E value) {
        final Comparator<? super E> comparator = this.comparator;

        Node<E> node = root;

        if (node == null) {
            root = new Node<>(value, null);
            size = 1;
            return true;
        }

        int c;
        Node<E> parent;

        if (comparator == null) {
            do {
                parent = node;
                c = ComparableUtils.compare(value, node.getValue());
                if (c < 0) {
                    node = node.left;
                } else if (c > 0) {
                    node = node.right;
                } else {
                    return false;
                }

            } while (node != null);
        } else {
            do {
                parent = node;
                c = comparator.compare(value, node.getValue());
                if (c < 0) {
                    node = node.left;
                } else if (c > 0) {
                    node = node.right;
                } else {
                    return false;
                }

            } while (node != null);
        }

        Node<E> n = new Node<>(value, parent);
        if (c < 0) {
            parent.left = n;
        } else {
            parent.right = n;
        }
        size++;
        fixAfterInsert(n);
        return true;
    }

    @Override
    public boolean remove(Object value) {
        Node<E> node = getNode(value);
        if (node == null) {
            return false;
        }
        remove0(node);
        return true;
    }

    @Override
    public boolean contains(Object value) {
        return getNode(value) != null;
    }

    @Override
    public E getFirst() {
        final Node<E> node = firstNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.getValue();
    }

    @Override
    public E getLast() {
        final Node<E> node = lastNode();
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.getValue();
    }

    @Override
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        final int size = this.size;
        final U[] res = generator.apply(size);
        if (size == 0) {
            return res;
        }
        final Iterator<E> it = this.iterator();
        for (int i = 0; i < size; i++) {
            res[i] = (U) it.next();
        }
        return res;
    }

    @Override
    public int hashCode() {
        return Set.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnySet && Set.equals(this, (AnySet<?>) obj);
    }

    @Override
    public void forEach(@NotNull Consumer<? super E> action) {
        forEachKey0(action);
    }

    @Override
    public String toString() {
        return joinToString(", ", "MutableTreeSet[", "]");
    }

    //region Serialization Operations

    @Serial
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int n = in.readInt();
        for (int i = 0; i < n; i++) {
            this.add((E) in.readObject());
        }
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
        out.writeInt(size);
        for (E e : this) {
            out.writeObject(e);
        }
    }

    //endregion

    static final class Node<E> extends RedBlackTree.Node<E, Node<E>> {
        Node(E value, Node<E> parent) {
            super(value, parent);
        }

        void setValue(E newValue) {
            this.key = newValue;
        }

        E getValue() {
            return key;
        }

        @Override
        public String toString() {
            return String.format(
                    "MutableTreeSet.Node[value=%s, color=%s, parent=%s, left=%s, right=%s]",
                    key, color == RED ? "RED" : "BLACK", parent, left, right
            );
        }
    }

    private static final class Itr<E> extends AbstractIterator<E> {
        private Node<E> node;

        Itr(Node<E> node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            final Node<E> node = this.node;
            if (node == null) {
                throw new NoSuchElementException();
            }

            Node<E> n;
            if (node.right != null) {
                n = node.right;
                while (n.left != null) {
                    n = n.left;
                }
            } else {
                n = node.parent;
                Node<E> c = node;

                while (n != null && c == n.right) {
                    c = n;
                    n = n.parent;
                }
            }
            this.node = n;

            return node.getValue();
        }
    }

    private static final class Factory<E> extends AbstractMutableSetFactory<E, MutableTreeSet<E>> {

        private final @Nullable Comparator<? super E> comparator;

        Factory(@Nullable Comparator<? super E> comparator) {
            this.comparator = comparator;
        }

        @Override
        public MutableTreeSet<E> newBuilder() {
            return new MutableTreeSet<>(comparator);
        }
    }
}
