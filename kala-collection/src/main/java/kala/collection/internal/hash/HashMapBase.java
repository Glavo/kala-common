package kala.collection.internal.hash;

import kala.collection.base.MapBase;
import kala.collection.base.MapIterator;
import kala.collection.internal.convert.AsJavaConvert;
import kala.control.Option;
import kala.collection.mutable.AbstractMutableMap;
import kala.collection.mutable.MutableHashMap;
import kala.function.CheckedBiConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static kala.collection.internal.hash.HashUtils.*;

@SuppressWarnings("unchecked")
public class HashMapBase<K, V> extends AbstractMutableMap<K, V> implements Serializable {
    private static final long serialVersionUID = 5898767589365493454L;

    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final double DEFAULT_LOAD_FACTOR = 0.75;

    protected final double loadFactor;

    protected transient HashMapNode<K, V>[] table;
    protected int threshold;

    protected transient int contentSize = 0;

    protected HashMapBase(int initialCapacity, double loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Double.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.loadFactor = loadFactor;

        final int tableSize = tableSizeFor(initialCapacity);
        this.table = (HashMapNode<K, V>[]) new HashMapNode<?, ?>[tableSize];
        this.threshold = newThreshold(tableSize);
    }

    protected HashMapBase(@NotNull HashMapBase<K, V> old) {
        this.loadFactor = old.loadFactor;
        this.threshold = old.threshold;
        this.contentSize = old.contentSize;

        HashMapNode<K, V>[] oldTable = old.table;
        HashMapNode<K, V>[] newTable = (HashMapNode<K, V>[]) new HashMapNode<?, ?>[oldTable.length];
        this.table = newTable;

        for (int i = 0; i < oldTable.length; i++) {
            HashMapNode<K, V> oldNode = oldTable[i];
            if (oldNode != null) {
                newTable[i] = oldNode.deepClone();
            }
        }
    }

    //region HashMap helpers

    private void reinitialize(int initialCapacity) throws IOException {
        if (initialCapacity < 0) {
            throw new InvalidObjectException("Illegal initial capacity: " + initialCapacity);
        }

        if (loadFactor <= 0 || Double.isNaN(loadFactor)) {
            throw new InvalidObjectException("Illegal load factor: " + loadFactor);
        }

        contentSize = 0;

        final int tableSize = tableSizeFor(initialCapacity);
        this.table = (HashMapNode<K, V>[]) new HashMapNode<?, ?>[tableSize];
        this.threshold = newThreshold(tableSize);
    }

    protected final int index(int hash) {
        return hash & (table.length - 1);
    }

    protected final int newThreshold(int size) {
        return (int) ((double) size * loadFactor);
    }

    protected final void growTable(int newLen) {
        if (newLen < 0) {
            throw new IllegalStateException("The new HashMap table size " + newLen + " exceeds maximum");
        }
        final HashMapNode<K, V>[] oldTable = this.table;
        int oldLen = oldTable.length;
        this.threshold = newThreshold(newLen);
        if (contentSize == 0) {
            this.table = (HashMapNode<K, V>[]) new HashMapNode<?, ?>[newLen];
        } else {
            final HashMapNode<K, V>[] newTable = Arrays.copyOf(oldTable, newLen);
            this.table = newTable;
            HashMapNode<K, V> preLow = new HashMapNode<>(null, 0, null, null);
            HashMapNode<K, V> preHigh = new HashMapNode<>(null, 0, null, null);

            /*
             * Split buckets until the new length has been reached. This could be done more
             * efficiently when growing an already filled table to more than double the size.
             */
            while (oldLen < newLen) {
                int i = 0;
                while (i < oldLen) {
                    HashMapNode<K, V> old = newTable[i];
                    if (old != null) {
                        preLow.next = null;
                        preHigh.next = null;

                        HashMapNode<K, V> lastLow = preLow;
                        HashMapNode<K, V> lastHigh = preHigh;

                        HashMapNode<K, V> n = old;
                        while (n != null) {
                            final HashMapNode<K, V> next = n.next;
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

    protected final @Nullable HashMapNode<K, V> findNode(K key) {
        final int hash = computeHash(key);
        HashMapNode<K, V> fn = table[index(hash)];
        if (fn == null) {
            return null;
        }
        return fn.findNode(key, hash);
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull MapIterator<K, V> iterator() {
        return new HashMapIterator<>(table);
    }

    protected final @NotNull HashMapNodeIterator<K, V> nodeIterator() {
        return new HashMapNodeIterator<>(table);
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

    public final void sizeHint(int size) {
        final int target = tableSizeFor((int) ((size + 1) / loadFactor));
        if (target > table.length) {
            growTable(target);
        }
    }

    //endregion

    //region Internal put and remove helper

    protected final void set0(K key, V value, int hash) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int idx = index(hash);
        set0(key, value, hash, idx);
    }

    protected final void set0(K key, V value, int hash, int idx) {
        final HashMapNode<K, V>[] table = this.table;
        final HashMapNode<K, V> old = table[idx];
        if (old == null) {
            table[idx] = new HashMapNode<>(key, hash, value);
        } else {
            HashMapNode<K, V> prev = null;
            HashMapNode<K, V> n = old;

            while (n != null && n.hash <= hash) {
                if (n.hash == hash && Objects.equals(key, n.key)) {
                    n.value = value;
                    return;
                }
                prev = n;
                n = n.next;
            }
            if (prev == null) {
                table[idx] = new HashMapNode<>(key, hash, value, old);
            } else {
                prev.next = new HashMapNode<>(key, hash, value, prev.next);
            }
        }
        contentSize += 1;
    }

    protected final Option<V> put0(K key, V value, int hash) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int idx = index(hash);
        return put0(key, value, hash, idx);
    }

    protected final Option<V> put0(K key, V value, int hash, int idx) {
        final HashMapNode<K, V>[] table = this.table;
        final HashMapNode<K, V> old = table[idx];
        if (old == null) {
            table[idx] = new HashMapNode<>(key, hash, value);
        } else {
            HashMapNode<K, V> prev = null;
            HashMapNode<K, V> n = old;

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
                table[idx] = new HashMapNode<>(key, hash, value, old);
            } else {
                prev.next = new HashMapNode<>(key, hash, value, prev.next);
            }
        }
        contentSize += 1;
        return Option.none();
    }

    protected final HashMapNode<K, V> remove0(K elem) {
        return remove0(elem, computeHash(elem));
    }

    protected final HashMapNode<K, V> remove0(K elem, int hash) {
        final HashMapNode<K, V>[] table = this.table;
        final int idx = index(hash);

        HashMapNode<K, V> nd = this.table[idx];
        if (nd == null) {
            return null;
        }

        if (nd.hash == hash && Objects.equals(nd.key, elem)) {
            table[idx] = nd.next;
            contentSize -= 1;
            return nd;
        }

        // find an element that matches
        HashMapNode<K, V> prev = nd;
        HashMapNode<K, V> next = nd.next;

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
        final HashMapNode<K, V> node = findNode(key);
        if (node == null) {
            throw new NoSuchElementException();
        }
        return node.value;
    }

    @Override
    public final @Nullable V getOrNull(K key) {
        final HashMapNode<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    @Override
    public final @NotNull Option<V> getOption(K key) {
        final HashMapNode<K, V> node = findNode(key);
        return node == null ? Option.none() : Option.some(node.value);
    }

    @Override
    public final V getOrDefault(K key, V defaultValue) {
        final HashMapNode<K, V> node = findNode(key);
        return node == null ? defaultValue : node.value;
    }

    @Override
    public final V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
        final HashMapNode<K, V> node = findNode(key);
        return node == null ? supplier.get() : node.value;
    }

    @Override
    public final V getOrPut(K key, @NotNull Supplier<? extends V> defaultValue) {
        final HashMapNode<K, V> node = findNode(key);
        if (node == null) {
            V value = defaultValue.get();
            set(key, value);
            return value;
        }
        return node.value;
    }

    @Override
    public final <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
        final HashMapNode<K, V> node = findNode(key);
        if (node == null) {
            throw supplier.get();
        }
        return node.value;
    }

    @Override
    public final <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
        final HashMapNode<K, V> node = findNode(key);
        if (node == null) {
            throw exception;
        }
        return node.value;
    }

    @Override
    public final void set(K key, V value) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int hash = computeHash(key);
        final int idx = index(hash);
        set0(key, value, hash, idx);
    }

    @Override
    public final @NotNull Option<V> put(K key, V value) {
        if (contentSize + 1 >= threshold) {
            growTable(table.length * 2);
        }
        final int hash = computeHash(key);
        final int idx = index(hash);
        return put0(key, value, hash, idx);
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
    public final void putAll(@NotNull MapBase<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);

        if (m == this) {
            return;
        }
        sizeHint(m.knownSize());
        if (m instanceof MutableHashMap<?, ?>) {
            MutableHashMap<K, V> hashMap = (MutableHashMap<K, V>) m;
            HashMapNodeIterator<K, V> itr = hashMap.nodeIterator();
            while (itr.hasNext()) {
                HashMapNode<K, V> next = itr.next();
                set0(next.key, next.value, next.hash);
            }
        } else {
            m.forEach(this::set);
        }
    }

    public final void putAll(@NotNull MutableHashMap<? extends K, ? extends V> m) {
        Objects.requireNonNull(m);

        if (m == this) {
            return;
        }
        HashMapNodeIterator<? extends K, ? extends V> itr = m.nodeIterator();
        while (itr.hasNext()) {
            HashMapNode<? extends K, ? extends V> next = itr.next();
            set0(next.key, next.value, next.hash);
        }
    }

    @Override
    public final @NotNull Option<V> remove(K key) {
        HashMapNode<K, V> node = remove0(key);
        return node == null ? Option.none() : Option.some(node.value);
    }

    @Override
    public final void clear() {
        Arrays.fill(table, null);
        contentSize = 0;
    }

    @Override
    public final @NotNull Option<V> replace(K key, V value) {
        HashMapNode<K, V> node = findNode(key);
        if (node == null) {
            return Option.none();
        }
        V oldValue = node.value;
        node.value = value;
        return Option.some(oldValue);
    }

    @Override
    public final boolean replace(K key, V oldValue, V newValue) {
        HashMapNode<K, V> node = findNode(key);
        if (node == null || !Objects.equals(node.value, oldValue)) {
            return false;
        }
        node.value = newValue;
        return true;
    }

    @Override
    public final void replaceAll(@NotNull BiFunction<? super K, ? super V, ? extends V> function) {
        for (HashMapNode<K, V> fn : this.table) {
            HashMapNode<K, V> node = fn;
            while (node != null) {
                node.value = function.apply(node.key, node.value);
                node = node.next;
            }
        }
    }

    //endregion

    @Override
    public final boolean contains(K key, Object value) {
        HashMapNode<K, V> node = findNode(key);
        return node != null && Objects.equals(node.value, value);
    }

    @Override
    public final boolean containsKey(K key) {
        return findNode(key) != null;
    }

    @Override
    public final void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        for (HashMapNode<K, V> fn : this.table) {
            HashMapNode<K, V> node = fn;
            while (node != null) {
                consumer.accept(node.key, node.value);
                node = node.next;
            }
        }
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        final int capacity = table != null ? table.length : (threshold > 0 ? threshold : DEFAULT_INITIAL_CAPACITY);

        stream.defaultWriteObject();
        stream.writeInt(capacity);
        stream.writeInt(contentSize);
        this.forEachUnchecked((k, v) -> {
            stream.writeObject(k);
            stream.writeObject(v);
        });
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final int capacity = stream.readInt();
        reinitialize(capacity);

        final int size = stream.readInt();
        for (int i = 0; i < size; i++) {
            Object key = stream.readObject();
            Object value = stream.readObject();

            this.set((K) key, (V) value);
        }
    }
}
