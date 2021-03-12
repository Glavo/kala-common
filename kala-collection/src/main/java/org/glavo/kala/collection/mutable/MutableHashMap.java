package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.MapLike;
import org.glavo.kala.collection.base.AbstractIterator;
import org.glavo.kala.collection.base.AbstractMapIterator;
import org.glavo.kala.collection.base.MapIterator;
import org.glavo.kala.collection.factory.MapFactory;
import org.glavo.kala.collection.immutable.AbstractImmutableMap;
import org.glavo.kala.collection.immutable.ImmutableHashMap;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.control.Option;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class MutableHashMap<K, V> extends AbstractMutableMap<K, V>
        implements MutableMapOps<K, V, MutableHashMap<?, ?>, MutableHashMap<K, V>>, Cloneable {
    private static final Factory<?, ?> FACTORY = new Factory<>();

    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;

    private final double loadFactor;

    private Node<K, V>[] table;
    private int threshold;

    private int contentSize = 0;

    public MutableHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashMap(int initialCapacity, double loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Double.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;

        final int tableSize = tableSizeFor(initialCapacity);
        this.table = (Node<K, V>[]) new Node<?, ?>[tableSize];
        this.threshold = newThreshold(tableSize);
    }

    /**
     * @see #clone()
     */
    private MutableHashMap(@NotNull MutableHashMap<K, V> old) {
        this.loadFactor = old.loadFactor;
        this.threshold = old.threshold;
        this.contentSize = old.contentSize;

        Node<K, V>[] oldTable = old.table;
        Node<K, V>[] newTable = (Node<K, V>[]) new Node<?, ?>[oldTable.length];
        this.table = newTable;

        for (int i = 0; i < oldTable.length; i++) {
            Node<K, V> oldNode = oldTable[i];
            if (oldNode != null) {
                newTable[i] = oldNode.deepClone();
            }
        }
    }

    //region Static Factories

    public static <K, V> @NotNull MapFactory<K, V, ?, MutableHashMap<K, V>> factory() {
        return (Factory<K, V>) FACTORY;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of() {
        return new MutableHashMap<>();
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(K k1, V v1) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2
    ) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3
    ) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4
    ) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> of(
            K k1, V v1,
            K k2, V v2,
            K k3, V v3,
            K k4, V v4,
            K k5, V v5
    ) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.set(k1, v1);
        m.set(k2, v2);
        m.set(k3, v3);
        m.set(k4, v4);
        m.set(k5, v5);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries() {
        return new MutableHashMap<>();
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        res.set(entry2);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(
            @NotNull Tuple2<? extends K, ? extends V> entry1,
            @NotNull Tuple2<? extends K, ? extends V> entry2,
            @NotNull Tuple2<? extends K, ? extends V> entry3,
            @NotNull Tuple2<? extends K, ? extends V> entry4,
            @NotNull Tuple2<? extends K, ? extends V> entry5
    ) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        res.set(entry1);
        res.set(entry2);
        res.set(entry3);
        res.set(entry4);
        res.set(entry5);
        return res;
    }

    @SafeVarargs
    public static <K, V> @NotNull MutableHashMap<K, V> ofEntries(Tuple2<? extends K, ? extends V> @NotNull ... entries) {
        MutableHashMap<K, V> res = new MutableHashMap<>();
        for (Tuple2<? extends K, ? extends V> entry : entries) {
            res.set(entry);
        }
        return res;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(java.util.@NotNull Map<? extends K, ? extends V> values) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.putAll(values);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(@NotNull MapLike<? extends K, ? extends V> values) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        m.putAll(values);
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(java.util.Map.Entry<? extends K, ? extends V> @NotNull [] values) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    public static <K, V> @NotNull MutableHashMap<K, V> from(@NotNull Iterable<? extends java.util.Map.Entry<? extends K, ? extends V>> values) {
        MutableHashMap<K, V> m = new MutableHashMap<>();
        for (java.util.Map.Entry<? extends K, ? extends V> value : values) {
            m.set(value.getKey(), value.getValue());
        }
        return m;
    }

    //endregion

    //region HashMap helpers

    private static int improveHash(int originalHash) {
        return originalHash ^ (originalHash >>> 16);
    }

    private static int unimproveHash(int improvedHash) {
        return improveHash(improvedHash);
    }

    private static int computeHash(Object o) {
        return improveHash(Objects.hashCode(o));
    }

    private int index(int hash) {
        return hash & (table.length - 1);
    }

    private static int tableSizeFor(int capacity) {
        return Integer.min(Integer.highestOneBit(Integer.max(capacity - 1, 4)) * 2, 1 << 30);
    }

    private int newThreshold(int size) {
        return (int) ((double) size * loadFactor);
    }

    private void growTable(int newLen) {
        if (newLen < 0) {
            throw new IllegalStateException("The new HashMap table size " + newLen + " exceeds maximum");
        }
        final Node<K, V>[] oldTable = this.table;
        int oldLen = oldTable.length;
        this.threshold = newThreshold(newLen);
        if (contentSize == 0) {
            this.table = (Node<K, V>[]) new Node<?, ?>[newLen];
        } else {
            final Node<K, V>[] newTable = Arrays.copyOf(oldTable, newLen);
            this.table = newTable;
            Node<K, V> preLow = new Node<>(null, 0, null, null);
            Node<K, V> preHigh = new Node<>(null, 0, null, null);

            /*
             * Split buckets until the new length has been reached. This could be done more
             * efficiently when growing an already filled table to more than double the size.
             */
            while (oldLen < newLen) {
                int i = 0;
                while (i < oldLen) {
                    Node<K, V> old = newTable[i];
                    if (old != null) {
                        preLow.next = null;
                        preHigh.next = null;

                        Node<K, V> lastLow = preLow;
                        Node<K, V> lastHigh = preHigh;

                        Node<K, V> n = old;
                        while (n != null) {
                            final Node<K, V> next = n.next;
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
                            newTable[i] = preLow.next;
                        }
                        if (preHigh.next != null) {
                            newTable[i + oldLen] = preHigh.next;
                            lastHigh.next = null;
                        }
                    }
                    i += 1;
                }
                oldLen *= 2;
            }
        }
    }

    private @Nullable Node<K, V> findNode(K key) {
        final int hash = computeHash(key);
        Node<K, V> fn = table[index(hash)];
        if (fn == null) {
            return null;
        }
        return fn.findNode(key, hash);
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "MutableHashMap";
    }

    @Override
    public final @NotNull <NK, NV> MapFactory<NK, NV, ?, MutableHashMap<NK, NV>> mapFactory() {
        return MutableHashMap.factory();
    }

    @Override
    public final @NotNull MapIterator<K, V> iterator() {
        return new Itr<>(table);
    }

    final @NotNull NodeItr<K, V> nodeIterator() {
        return new NodeItr<>(table);
    }

    @Override
    public final @NotNull java.util.Map<K, V> asJava() {
        return new AsJava<>(this);
    }

    final @NotNull MutableHashMap<K, V> shallowClone() {
        try {
            return (MutableHashMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final @NotNull MutableHashMap<K, V> clone() {
        return new MutableHashMap<>(this);
    }

    //endregion

    //region Size Info

    @Override
    public final int size() {
        return contentSize;
    }

    @Override
    public final int knownSize() {
        return contentSize;
    }

    //endregion

    //region Internal put and remove helper

    private void put0(K key, V value) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int hash = computeHash(key);
        final int idx = index(hash);
        put0(key, value, hash, idx);
    }

    private void put0(K key, V value, int hash) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int idx = index(hash);
        put0(key, value, hash, idx);
    }

    private void put0(K key, V value, int hash, int idx) {
        final Node<K, V>[] table = this.table;
        final Node<K, V> old = table[idx];
        if (old == null) {
            table[idx] = new Node<>(key, hash, value);
        } else {
            Node<K, V> prev = null;
            Node<K, V> n = old;

            while (n != null && n.hash <= hash) {
                if (n.hash == hash && Objects.equals(key, n.key)) {
                    n.value = value;
                    return;
                }
                prev = n;
                n = n.next;
            }
            if (prev == null) {
                table[idx] = new Node<>(key, hash, value, old);
            } else {
                prev.next = new Node<>(key, hash, value, prev.next);
            }
        }
        contentSize += 1;
    }

    private Option<V> putAndGetOld0(K key, V value) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int hash = computeHash(key);
        final int idx = index(hash);
        return putAndGetOld0(key, value, hash, idx);
    }

    private Option<V> putAndGetOld0(K key, V value, int hash) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int idx = index(hash);
        return putAndGetOld0(key, value, hash, idx);
    }

    private Option<V> putAndGetOld0(K key, V value, int hash, int idx) {
        final Node<K, V>[] table = this.table;
        final Node<K, V> old = table[idx];
        if (old == null) {
            table[idx] = new Node<>(key, hash, value);
        } else {
            Node<K, V> prev = null;
            Node<K, V> n = old;

            while (n != null && n.hash <= hash) {
                if (n.hash == hash && Objects.equals(key, n.key)) {
                    V oldValue = n.value;
                    n.value = value;
                    return Option.some(oldValue);
                }
                prev = n;
                n = n.next;
            }
            if (prev == null) {
                table[idx] = new Node<>(key, hash, value, old);
            } else {
                prev.next = new Node<>(key, hash, value, prev.next);
            }
        }
        contentSize += 1;
        return Option.none();
    }

    private Node<K, V> remove0(K elem) {
        return remove0(elem, computeHash(elem));
    }

    private Node<K, V> remove0(K elem, int hash) {
        final Node<K, V>[] table = this.table;
        final int idx = index(hash);

        Node<K, V> nd = this.table[idx];
        if (nd == null) {
            return null;
        }

        if (nd.hash == hash && Objects.equals(nd.key, elem)) {
            table[idx] = nd.next;
            contentSize -= 1;
            return nd;
        }

        // find an element that matches
        Node<K, V> prev = nd;
        Node<K, V> next = nd.next;

        while (next != null && next.hash <= hash) {
            if (next.hash == hash && Objects.equals(next.key, elem)) {
                prev.next = next.next;
                contentSize -= 1;
                return next;
            }
            prev = next;
            next = next.next;
        }
        return null;
    }

    //endregion

    //region Map Operations

    @Override
    public final V get(K key) {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.value;
    }

    @Override
    public final @Nullable V getOrNull(K key) {
        final Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public final @NotNull Option<V> getOption(K key) {
        final Node<K, V> node = findNode(key);
        return node == null ? Option.none() : Option.some(node.value);
    }

    @Override
    public final V getOrDefault(K key, V defaultValue) {
        final Node<K, V> node = findNode(key);
        return node == null ? defaultValue : node.value;
    }

    @Override
    public final V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
        final Node<K, V> node = findNode(key);
        return node == null ? supplier.get() : node.value;
    }

    @Override
    public final V getOrPut(K key, @NotNull Supplier<? extends V> defaultValue) {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            V value = defaultValue.get();
            put0(key, value);
            return value;
        }
        return node.value;
    }

    @Override
    public final <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            throw supplier.get();
        }
        return node.value;
    }

    @Override
    public final <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            throw exception;
        }
        return node.value;
    }

    @Override
    public final void set(K key, V value) {
        put0(key, value);
    }

    @Override
    public final @NotNull Option<V> put(K key, V value) {
        return putAndGetOld0(key, value);
    }

    @Override
    public final void putAll(java.util.@NotNull Map<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);
        if (m instanceof AsJavaConvert.MapAsJava<?, ?, ?>) {
            putAll(((AsJavaConvert.MapAsJava<K, V, ?>) m).source);
            return;
        }
        m.forEach(this::set);
    }

    @Override
    public final void putAll(@NotNull MapLike<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);

        if (m == this) {
            return;
        }
        if (m instanceof ImmutableHashMap<?, ?>) {
            m = ((Frozen<K, V>) m).source;
        }
        if (m instanceof MutableHashMap<?, ?>) {
            MutableHashMap<K, V> mhm = (MutableHashMap<K, V>) m;
            NodeItr<K, V> itr = mhm.nodeIterator();
            while (itr.hasNext()) {
                Node<K, V> next = itr.next();
                put0(next.key, next.value, next.hash);
            }
            return;
        }
        m.forEach(this::set);
    }

    public final void putAll(@NotNull MutableHashMap<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);

        if (m == this) {
            return;
        }
        NodeItr<? extends K, ? extends V> itr = m.nodeIterator();
        while (itr.hasNext()) {
            Node<? extends K, ? extends V> next = itr.next();
            put0(next.key, next.value, next.hash);
        }
    }

    @Override
    public final @NotNull Option<V> remove(K key) {
        Node<K, V> node = remove0(key);
        return node == null ? Option.none() : Option.some(node.value);
    }

    @Override
    public void clear() {
        Arrays.fill(table, null);
        contentSize = 0;
    }

    @Override
    public void replaceAll(@NotNull BiFunction<? super K, ? super V, ? extends V> function) {
        for (Node<K, V> fn : this.table) {
            Node<K, V> node = fn;
            while (node != null) {
                node.value = function.apply(node.key, node.value);
                node = node.next;
            }
        }
    }

    //endregion

    @Override
    public final boolean containsKey(K key) {
        return findNode(key) != null;
    }

    @Override
    public final void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        for (Node<K, V> fn : this.table) {
            Node<K, V> node = fn;
            while (node != null) {
                consumer.accept(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static final class Factory<K, V> extends AbstractMutableMapFactory<K, V, MutableHashMap<K, V>> {
        @Override
        public final MutableHashMap<K, V> newBuilder() {
            return new MutableHashMap<>();
        }
    }

    static final class Node<K, V> implements java.util.Map.Entry<K, V> {
        final K key;
        final int hash;
        V value;

        Node<K, V> next;

        Node(K key, int hash, V value) {
            this(key, hash, value, null);
        }

        Node(K key, int hash, V value, Node<K, V> next) {
            this.key = key;
            this.hash = hash;
            this.value = value;
            this.next = next;
        }


        Node<K, V> findNode(K k, int h) {
            Node<K, V> node = this;

            while (true) {
                final int nodeHash = node.hash;
                if (h == nodeHash && Objects.equals(k, node.key)) {
                    return node;
                }

                final Node<K, V> nextNode = node.next;
                if (nextNode == null || nodeHash > h) {
                    return null;
                }
                node = nextNode;
            }
        }

        final void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
            Node<K, V> node = this;
            while (node != null) {
                consumer.accept(node.key, node.value);
                node = node.next;
            }
        }

        @Override
        public final K getKey() {
            return key;
        }

        @Override
        public final V getValue() {
            return value;
        }

        @Override
        public final V setValue(V value) {
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        String toString(boolean debug) {
            if (!debug) {
                return toString();
            }

            StringBuilder builder = new StringBuilder();
            Node<K, V> node = this;
            while (true) {
                final Node<K, V> nextNode = node.next;
                builder.append(node.toString());

                if (nextNode == null) {
                    break;
                }

                builder.append(" -> ");
                node = nextNode;
            }
            return builder.toString();
        }

        @Override
        public String toString() {
            return "MutableHashMap.Node[key=" + key + ", value=" + value + ", hash=" + hash + "]";
        }

        final Node<K, V> deepClone() {
            final Node<K, V> head = new Node<>(key, hash, value, next);

            Node<K, V> node = head;
            Node<K, V> nextNode;
            while ((nextNode = node.next) != null) {
                nextNode = new Node<>(nextNode.key, nextNode.hash, nextNode.value, nextNode.next);
                node.next = nextNode;
                node = nextNode;
            }

            return head;
        }
    }

    static final class Itr<K, V> extends AbstractMapIterator<K, V> {
        private int i = 0;
        private Node<K, V> node = null;

        private final Node<K, V>[] table;
        private final int len;

        Itr(Node<K, V>[] table) {
            this.table = table;
            this.len = table.length;
        }

        V value;

        @Override
        public final boolean hasNext() {
            if (node != null) {
                return true;
            }
            while (i < len) {
                Node<K, V> n = table[i];
                i += 1;
                if (n != null) {
                    node = n;
                    return true;
                }
            }
            return false;
        }

        @Override
        public final K nextKey() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            K key = node.key;
            this.value = node.value;
            node = node.next;
            return key;
        }

        @Override
        public final V getValue() {
            return value;
        }
    }

    static final class NodeItr<K, V> extends AbstractIterator<Node<K, V>> {
        private int i = 0;
        private Node<K, V> node = null;

        private final Node<K, V>[] table;
        private final int len;

        NodeItr(Node<K, V>[] table) {
            this.table = table;
            this.len = table.length;
        }

        @Override
        public final boolean hasNext() {
            if (node != null) {
                return true;
            }
            while (i < len) {
                Node<K, V> n = table[i];
                i += 1;
                if (n != null) {
                    node = n;
                    return true;
                }
            }
            return false;
        }

        public final Node<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node<K, V> oldNode = this.node;
            this.node = oldNode.next;
            return oldNode;
        }
    }

    static final class AsJava<K, V> extends AsJavaConvert.MutableMapAsJava<K, V, MutableHashMap<K, V>> {

        public AsJava(@NotNull MutableHashMap<K, V> source) {
            super(source);
        }

        @Override
        public @NotNull Set<Entry<K, V>> entrySet() {
            return new EntrySet<>(source);
        }

        static final class EntrySet<K, V> extends AsJavaConvert.MapAsJava.EntrySet<K, V, MutableHashMap<K, V>> {
            EntrySet(MutableHashMap<K, V> source) {
                super(source);
            }

            @Override
            @SuppressWarnings("rawtypes")
            public final @NotNull Iterator<java.util.Map.Entry<K, V>> iterator() {
                return (Iterator) source.nodeIterator();
            }
        }
    }

    @ApiStatus.Internal
    public static class Frozen<K, V> extends AbstractImmutableMap<K, V> {
        protected final MutableHashMap<K, V> source;

        protected Frozen(MutableHashMap<K, V> source) {
            this.source = source;
        }

        @Override
        public final @NotNull MapIterator<K, V> iterator() {
            return source.iterator();
        }

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final int knownSize() {
            return source.knownSize();
        }

        //endregion

        @Override
        public final V get(K key) {
            return source.get(key);
        }

        @Override
        public final @Nullable V getOrNull(K key) {
            return source.getOrNull(key);
        }

        @Override
        public final @NotNull Option<V> getOption(K key) {
            return source.getOption(key);
        }

        @Override
        public final V getOrDefault(K key, V defaultValue) {
            return source.getOrDefault(key, defaultValue);
        }

        @Override
        public final V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
            return source.getOrElse(key, supplier);
        }

        @Override
        public final <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
            return source.getOrThrow(key, supplier);
        }

        @Override
        public final <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
            return source.getOrThrowException(key, exception);
        }

        //region Element Conditions

        @Override
        public final boolean containsKey(K key) {
            return source.containsKey(key);
        }

        @Override
        public final boolean containsValue(Object value) {
            return source.containsValue(value);
        }

        @Override
        public final boolean anyMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
            return source.anyMatch(predicate);
        }

        @Override
        public final boolean allMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
            return source.allMatch(predicate);
        }

        @Override
        public final boolean noneMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
            return source.noneMatch(predicate);
        }

        //endregion

        @Override
        public final void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
            source.forEach(consumer);
        }
    }
}
