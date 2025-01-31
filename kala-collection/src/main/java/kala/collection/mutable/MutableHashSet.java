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
package kala.collection.mutable;

import kala.collection.Set;
import kala.collection.factory.CollectionBuilder;
import kala.collection.internal.hash.HashBase;
import kala.collection.internal.hash.HashNode;
import kala.collection.internal.hash.HashUtils;
import kala.collection.factory.CollectionFactory;
import kala.function.Hasher;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class MutableHashSet<E> extends HashBase<E, MutableHashSet.Node<E>> implements MutableSet<E>, Serializable {
    @Serial
    private static final long serialVersionUID = 2267952928135789371L;
    private static final MutableHashSet.Factory<?> FACTORY = new Factory<>();

    //region Constructors

    public MutableHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashSet(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashSet(int initialCapacity, double loadFactor) {
        super(Hasher.optimizedHasher(), initialCapacity, loadFactor);
    }

    public MutableHashSet(@NotNull Hasher<? super E> hasher) {
        this(hasher, DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashSet(@NotNull Hasher<? super E> hasher, int initialCapacity) {
        this(hasher, initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashSet(@NotNull Hasher<? super E> hasher, int initialCapacity, double loadFactor) {
        super(hasher, initialCapacity, loadFactor);
    }

    private MutableHashSet(@NotNull HashBase<E, Node<E>> old) {
        super(old);
    }

    //endregion

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, MutableHashSet<E>> factory() {
        return ((Factory<E>) FACTORY);
    }

    @Contract("-> new")
    public static <E> @NotNull CollectionBuilder<E, MutableHashSet<E>> newBuilder() {
        return MutableHashSet.<E>factory().newCollectionBuilder();
    }

    @Contract(value = "-> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> create() {
        return new MutableHashSet<>();
    }

    @Contract(value = "-> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> of() {
        return new MutableHashSet<>();
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> of(E value1) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> of(E value1, E value2) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        s.add(value2);
        return s;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> of(E value1, E value2, E value3) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> of(E value1, E value2, E value3, E value4) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s;
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> of(E... values) {
        return from(values);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> from(E @NotNull [] values) {
        MutableHashSet<E> s = new MutableHashSet<>(values.length); // implicit null check of values
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> from(@NotNull Iterable<? extends E> values) {
        MutableHashSet<E> s = new MutableHashSet<>();
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new")
    public static <E> @NotNull MutableHashSet<E> from(@NotNull Iterator<? extends E> it) {
        MutableHashSet<E> s = new MutableHashSet<>();
        while (it.hasNext()) {
            s.add(it.next());
        }
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableHashSet<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    //endregion

    //region HashSet helpers

    private int indexOf(int hash) {
        return hash & (table.length - 1);
    }

    @Override
    protected Node<E>[] createNodeArray(int length) {
        return (Node<E>[]) new Node<?>[length];
    }

    protected void growTable(int newLen) {
        int oldLen = table.length;
        threshold = newThreshold(newLen);
        if (isEmpty()) {
            table = createNodeArray(newLen);
        } else {
            table = Arrays.copyOf(table, newLen);
            Node<E> preLow = new Node<>(null, 0);
            Node<E> preHigh = new Node<>(null, 0);

            while (oldLen < newLen) {
                int i = 0;
                while (i < oldLen) {
                    Node<E> old = table[i];
                    if (old != null) {
                        preLow.next = null;
                        preHigh.next = null;
                        Node<E> lastLow = preLow;
                        Node<E> lastHigh = preHigh;
                        Node<E> n = old;
                        while (n != null) {
                            Node<E> next = n.next;
                            if ((n.hash & oldLen) == 0) { // keep low
                                lastLow.next = n;
                                lastLow = n;
                            } else { // move to high
                                lastHigh.next = n;
                                lastHigh = n;
                            }
                            n = next;
                        }
                        lastLow.next = null;
                        if (old != preLow.next) {
                            table[i] = preLow.next;
                        }
                        if (preHigh.next != null) {
                            table[i + oldLen] = preHigh.next;
                            lastHigh.next = null;
                        }
                    }
                    ++i;
                }
                oldLen *= 2;
            }
        }
    }

    //endregion

    //region MutableSet members

    private boolean add(E value, int hash) {
        int idx = indexOf(hash);
        Node<E> n = table[idx];
        if (n == null) {
            table[idx] = new Node<>(value, hash);
        } else {
            final Node<E> old = n;
            Node<E> prev = null;
            while ((n != null) && n.hash <= hash) {
                if (n.hash == hash && hasher.equals(value, n.key)) {
                    return false;
                }
                prev = n;
                n = n.next;
            }
            if (prev == null) {
                table[idx] = new Node<>(value, hash, old);
            } else {
                prev.next = new Node<>(value, hash, prev.next);
            }
        }
        contentSize++;
        return true;
    }

    @Override
    public boolean add(E value) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        return add(value, hasher.hash(value));
    }

    @Override
    public boolean remove(Object value) {
        return removeNode((E) value, hasher.hash((E) value)) != null;
    }

    //endregion

    //region MutableCollection members

    @Override
    public @NotNull String className() {
        return "MutableHashSet";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, MutableHashSet<U>> iterableFactory() {
        return factory();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableHashSet<E> clone() {
        return new MutableHashSet<>(this);
    }

    //endregion

    public int hashCode() {
        return Set.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Set<?> && Set.equals(this, ((Set<?>) obj));
    }


    @Override
    public String toString() {
        return joinToString(", ", className() + "[", "]");
    }

    //region Serialization

    @Serial
    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        final int size = in.readInt();
        final double loadFactor = in.readDouble();

        if (size < 0) {
            throw new InvalidObjectException("Illegal size: " + size);
        }

        if (loadFactor <= 0 || Double.isNaN(loadFactor)) {
            throw new InvalidObjectException("Illegal load factor: " + loadFactor);
        }

        this.contentSize = 0;
        this.loadFactor = loadFactor;
        this.table = createNodeArray(HashUtils.tableSizeFor(size));
        this.threshold = newThreshold(table.length);

        for (int i = 0; i < size; i++) {
            this.add((E) in.readObject());
        }
    }

    @Serial
    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeInt(contentSize);
        out.writeDouble(loadFactor);
        for (E e : this) {
            out.writeObject(e);
        }
    }

    //endregion

    private final class Itr implements Iterator<E> {
        private int i = 0;
        private Node<E> node = null;
        private final int len = table.length;

        @Override
        public boolean hasNext() {
            if (node != null) {
                return true;
            }

            while (i < len) {
                Node<E> n = table[i];
                ++i;
                if (n != null) {
                    node = n;
                    return true;
                }
            }
            return false;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E res = node.key;
            node = node.next;
            return res;
        }
    }

    protected static final class Node<E> extends HashNode<E, Node<E>> {

        private Node(E key, int hash) {
            super(key, hash);
        }

        private Node(E key, int hash, Node<E> next) {
            super(key, hash, next);
        }

        @Override
        public Node<E> deepClone() {
            final Node<E> head = new Node<>(key, hash, next);

            Node<E> node = head;
            Node<E> nextNode;
            while ((nextNode = node.next) != null) {
                nextNode = new Node<>(nextNode.key, nextNode.hash, nextNode.next);
                node.next = nextNode;
                node = nextNode;
            }

            return head;
        }
    }

    private static final class Factory<E> extends AbstractMutableSetFactory<E, MutableHashSet<E>> {
        @Override
        public MutableHashSet<E> newBuilder() {
            return new MutableHashSet<>();
        }

        @Override
        public void sizeHint(@NotNull MutableHashSet<E> es, int size) {
            es.sizeHint(size);
        }
    }
}
