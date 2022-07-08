package kala.collection.mutable;

import kala.collection.MapLike;
import kala.collection.base.AbstractIterator;
import kala.collection.AbstractMapIterator;
import kala.collection.MapIterator;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.hash.*;
import kala.control.Option;
import kala.function.Hasher;
import kala.tuple.Tuple2;
import kala.collection.factory.MapFactory;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static kala.collection.internal.hash.HashUtils.computeHash;

@SuppressWarnings("unchecked")
@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public final class MutableHashMap<K, V> extends HashBase<K, MutableHashMap.Node<K, V>> implements MutableMap<K, V>, Cloneable, Serializable {
    private static final long serialVersionUID = 4445503260710443405L;

    private static final Factory<?, ?> FACTORY = new Factory<>();

    public static final int DEFAULT_INITIAL_CAPACITY = HashBase.DEFAULT_INITIAL_CAPACITY;
    public static final double DEFAULT_LOAD_FACTOR = HashBase.DEFAULT_LOAD_FACTOR;

    public MutableHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashMap(int initialCapacity, double loadFactor) {
        this(Hasher.optimizedHasher(), initialCapacity, loadFactor);
    }

    public MutableHashMap(Hasher<? super K> hasher) {
        this(hasher, DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashMap(Hasher<? super K> hasher, int initialCapacity) {
        this(hasher, initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MutableHashMap(Hasher<? super K> hasher, int initialCapacity, double loadFactor) {
        super(hasher, initialCapacity, loadFactor);
    }


    /**
     * @see #clone()
     */
    private MutableHashMap(@NotNull MutableHashMap<K, V> old) {
        super(old);
    }

    //region Static Factories

    public static <K, V> @NotNull MapFactory<K, V, ?, MutableHashMap<K, V>> factory() {
        return (Factory<K, V>) FACTORY;
    }

    static <T, K, V> @NotNull Collector<T, ?, MutableHashMap<K, V>> collector(
            @NotNull Function<? super T, ? extends K> keyMapper,
            @NotNull Function<? super T, ? extends V> valueMapper
    ) {
        return MapFactory.collector(factory(), keyMapper, valueMapper);
    }

    public static <K, V> @NotNull MutableHashMap<K, V> create() {
        return new MutableHashMap<>();
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

    public static <K, V> @NotNull MutableHashMap<K, V> of(Object... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException();
        }

        MutableHashMap<K, V> res = new MutableHashMap<>();

        for (int i = 0; i < values.length; i += 2) {
            res.set((K) values[i], (V) values[i + 1]);
        }

        return res;
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

    @Override
    protected Node<K, V>[] createNodeArray(int length) {
        return new Node[length];
    }

    protected void growTable(int newLen) {
        if (newLen < 0) {
            throw new IllegalStateException("The new HashMap table size " + newLen + " exceeds maximum");
        }
        final Node<K, V>[] oldTable = this.table;
        int oldLen = oldTable.length;
        this.threshold = newThreshold(newLen);
        if (contentSize == 0) {
            this.table = createNodeArray(newLen);
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

    //region Internal put helper

    private void set0(K key, V value, int hash) {
        if (contentSize >= threshold - 1) {
            growTable(table.length * 2);
        }
        final int idx = index(hash);
        set0(key, value, hash, idx);
    }

    private void set0(K key, V value, int hash, int idx) {
        final Node<K, V>[] table = this.table;
        final Node<K, V> old = table[idx];
        if (old == null) {
            table[idx] = new Node<>(key, hash, value);
        } else {
            Node<K, V> prev = null;
            Node<K, V> n = old;

            while (n != null && n.hash <= hash) {
                if (n.hash == hash && hasher.test(key, n.key)) {
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

    private Option<V> put0(K key, V value, int hash) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int idx = index(hash);
        return put0(key, value, hash, idx);
    }

    private Option<V> put0(K key, V value, int hash, int idx) {
        final Node<K, V>[] table = this.table;
        final Node<K, V> old = table[idx];
        if (old == null) {
            table[idx] = new Node<>(key, hash, value);
        } else {
            Node<K, V> prev = null;
            Node<K, V> n = old;

            while (n != null && n.hash <= hash) {
                if (n.hash == hash && hasher.test(key, n.key)) {
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

    //endregion

    @Override
    public @NotNull String className() {
        return "MutableHashMap";
    }

    @Override
    public @NotNull <NK, NV> MapFactory<NK, NV, ?, MutableHashMap<NK, NV>> mapFactory() {
        return MutableHashMap.factory();
    }

    @Override
    public @NotNull MapIterator<K, V> iterator() {
        return new Itr<>(table);
    }

    @NotNull MutableHashMap.NodeItr<K, V> nodeIterator() {
        return new NodeItr<>(table);
    }

    @Override
    public @NotNull MutableMapEditor<K, V, MutableHashMap<K, V>> edit() {
        return new MutableMapEditor<>(this);
    }

    @Override
    public @NotNull java.util.Map<K, V> asJava() {
        return new AsJava<>(this);
    }

    @NotNull MutableHashMap<K, V> shallowClone() {
        try {
            return (MutableHashMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableHashMap<K, V> clone() {
        return new MutableHashMap<>(this);
    }

    //region Map Operations

    @Override
    public V get(K key) {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.value;
    }

    @Override
    public @Nullable V getOrNull(K key) {
        final Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public @NotNull Option<V> getOption(K key) {
        final Node<K, V> node = findNode(key);
        return node == null ? Option.none() : Option.some(node.value);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        final Node<K, V> node = findNode(key);
        return node == null ? defaultValue : node.value;
    }

    @Override
    public V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
        final Node<K, V> node = findNode(key);
        return node == null ? supplier.get() : node.value;
    }

    @Override
    public V getOrPut(K key, @NotNull Supplier<? extends V> defaultValue) {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            V value = defaultValue.get();
            set(key, value);
            return value;
        }
        return node.value;
    }

    @Override
    public <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            throw supplier.get();
        }
        return node.value;
    }

    @Override
    public <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
        final Node<K, V> node = findNode(key);
        if (node == null) {
            throw exception;
        }
        return node.value;
    }

    @Override
    public void set(K key, V value) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int hash = computeHash(key);
        final int idx = index(hash);
        set0(key, value, hash, idx);
    }

    @Override
    public @NotNull Option<V> put(K key, V value) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int hash = computeHash(key);
        final int idx = index(hash);
        return put0(key, value, hash, idx);
    }

    @Override
    public void putAll(java.util.@NotNull Map<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);
        if (m instanceof AsJavaConvert.MapAsJava<?, ?, ?>) {
            putAll(((AsJavaConvert.MapAsJava<K, V, ?>) m).source);
            return;
        }
        m.forEach(this::set);
    }

    @Override
    public void putAll(@NotNull MapLike<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);

        if (m == this) {
            return;
        }
        sizeHint(m.knownSize());
        if (m instanceof MutableHashMap<?, ?>) {
            MutableHashMap<K, V> hashMap = (MutableHashMap<K, V>) m;
            NodeItr<K, V> itr = hashMap.nodeIterator();
            while (itr.hasNext()) {
                Node<K, V> next = itr.next();
                set0(next.key, next.value, next.hash);
            }
        } else {
            m.forEach(this::set);
        }
    }

    public void putAll(@NotNull MutableHashMap<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);

        if (m == this) {
            return;
        }
        NodeItr<? extends K, ? extends V> itr = m.nodeIterator();
        while (itr.hasNext()) {
            Node<? extends K, ? extends V> next = itr.next();
            set0(next.key, next.value, next.hash);
        }
    }

    @Override
    public @NotNull Option<V> remove(K key) {
        Node<K, V> node = removeNode(key);
        return node == null ? Option.none() : Option.some(node.value);
    }

    @Override
    public @NotNull Option<V> replace(K key, V value) {
        Node<K, V> node = findNode(key);
        if (node == null) {
            return Option.none();
        }
        V oldValue = node.value;
        node.value = value;
        return Option.some(oldValue);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Node<K, V> node = findNode(key);
        if (node == null || !Objects.equals(node.value, oldValue)) {
            return false;
        }
        node.value = newValue;
        return true;
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
    public boolean contains(K key, Object value) {
        Node<K, V> node = findNode(key);
        return node != null && Objects.equals(node.value, value);
    }

    @Override
    public boolean containsKey(K key) {
        return findNode(key) != null;
    }

    @Override
    public void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        for (Node<K, V> fn : this.table) {
            Node<K, V> node = fn;
            while (node != null) {
                consumer.accept(node.key, node.value);
                node = node.next;
            }
        }
    }

    @Override
    public int hashCode() {
        return kala.collection.Map.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof kala.collection.Map<?, ?> && kala.collection.Map.equals(this, ((kala.collection.Map<?, ?>) obj)));
    }

    @Override
    public String toString() {
        return className() + '{' + joinToString() + '}';
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        final int size = in.readInt();
        final double loadFactor = in.readDouble();
        final Hasher<K> hasher = (Hasher<K>) in.readObject();

        if (size < 0) {
            throw new InvalidObjectException("Illegal initial capacity: " + size);
        }

        if (loadFactor <= 0 || Double.isNaN(loadFactor)) {
            throw new InvalidObjectException("Illegal load factor: " + loadFactor);
        }

        Objects.requireNonNull(hasher);

        this.hasher = hasher;
        this.contentSize = 0;
        this.loadFactor = loadFactor;
        this.table = createNodeArray(HashUtils.tableSizeFor(size));
        this.threshold = newThreshold(table.length);


        for (int i = 0; i < size; i++) {
            Object key = in.readObject();
            Object value = in.readObject();

            this.set((K) key, (V) value);
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(contentSize);
        out.writeDouble(loadFactor);
        out.writeObject(hasher);
        this.forEachUnchecked((k, v) -> {
            out.writeObject(k);
            out.writeObject(v);
        });
    }

    protected static final class Node<K, V> extends HashNode<K, Node<K, V>> implements Map.Entry<K, V> {
        public V value;

        public Node(K key, int hash, V value) {
            super(key, hash);
            this.value = value;
        }

        public Node(K key, int hash, V value, Node<K, V> next) {
            super(key, hash);
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
            Node<K, V> node = this;
            while (node != null) {
                consumer.accept(node.key, node.value);
                node = node.next;
            }
        }

        @Override
        public String toString() {
            return "Node[key=" + key + ", value=" + value + ", hash=" + hash + "]";
        }

        public Node<K, V> deepClone() {
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

    private static final class Itr<K, V> extends AbstractMapIterator<K, V> {
        private int i = 0;
        private Node<K, V> node = null;

        private final Node<K, V>[] table;
        private final int len;

        private V value;

        public Itr(Node<K, V>[] table) {
            this.table = table;
            this.len = table.length;
        }

        @Override
        public boolean hasNext() {
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
        public K nextKey() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            K key = node.key;
            this.value = node.value;
            node = node.next;
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }
    }

    private static final class NodeItr<K, V> extends AbstractIterator<Node<K, V>> {
        private int i = 0;
        private Node<K, V> node = null;

        private final Node<K, V>[] table;

        public NodeItr(Node<K, V>[] table) {
            this.table = table;
        }

        @Override
        public boolean hasNext() {
            if (node != null) {
                return true;
            }
            while (i < table.length) {
                Node<K, V> n = table[i++];
                if (n != null) {
                    node = n;
                    return true;
                }
            }
            return false;
        }

        public Node<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final Node<K, V> oldNode = this.node;
            this.node = oldNode.next;
            return oldNode;
        }
    }

    private static final class Factory<K, V> extends AbstractMutableMapFactory<K, V, MutableHashMap<K, V>> {
        @Override
        public MutableHashMap<K, V> newBuilder() {
            return new MutableHashMap<>();
        }
    }

    private static final class AsJava<K, V> extends AsJavaConvert.MutableMapAsJava<K, V, MutableHashMap<K, V>> {

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
            public @NotNull Iterator<java.util.Map.Entry<K, V>> iterator() {
                return (Iterator) source.nodeIterator();
            }
        }
    }
}
