package kala.collection.internal.hash;

import kala.function.Hasher;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import static kala.collection.internal.hash.HashUtils.tableSizeFor;

public abstract class HashBase<K, N extends HashNode<K, N>> implements Serializable {
    private static final long serialVersionUID = 5938151855937027660L;

    protected static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected static final double DEFAULT_LOAD_FACTOR = 0.75;

    protected static final int MAXIMUM_CAPACITY = 1 << 30;

    protected Hasher<? super K> hasher;
    protected double loadFactor;

    protected transient N[] table;
    protected int threshold;

    protected transient int contentSize = 0;

    protected HashBase(Hasher<? super K> hasher, int initialCapacity, double loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (loadFactor <= 0 || Double.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        this.hasher = Objects.requireNonNull(hasher);
        this.loadFactor = loadFactor;

        final int tableSize = tableSizeFor(initialCapacity);
        this.table = createNodeArray(tableSize);
        this.threshold = newThreshold(tableSize);
    }

    protected HashBase(HashBase<K, N> old) {
        this.hasher = old.hasher;
        this.loadFactor = old.loadFactor;
        this.threshold = old.threshold;
        this.contentSize = old.contentSize;

        N[] oldTable = old.table;
        N[] newTable = createNodeArray(oldTable.length);
        this.table = newTable;

        for (int i = 0; i < oldTable.length; i++) {
            N oldNode = oldTable[i];
            if (oldNode != null) {
                newTable[i] = oldNode.deepClone();
            }
        }
    }


    //region HashMap helpers

    protected abstract N[] createNodeArray(int length);

    protected abstract void growTable(int newLen);

    protected final int index(int hash) {
        return hash & (table.length - 1);
    }

    protected final int newThreshold(int size) {
        return (int) ((double) size * loadFactor);
    }

    protected final @Nullable N findNode(K key) {
        final int hash = hasher.hash(key);
        N fn = table[index(hash)];
        if (fn == null) {
            return null;
        }
        return fn.findNode(key, hash);
    }

    protected final N removeNode(K elem) {
        return removeNode(elem, hasher.hash(elem));
    }

    protected final N removeNode(K elem, int hash) {
        final N[] table = this.table;
        final int idx = index(hash);

        N nd = this.table[idx];
        if (nd == null) {
            return null;
        }

        if (nd.hash == hash && hasher.test(nd.key, elem)) {
            table[idx] = nd.next;
            contentSize -= 1;
            return nd;
        }

        // find an element that matches
        N prev = nd;
        N next = nd.next;

        while (next != null && next.hash <= hash) {
            if (next.hash == hash && hasher.test(next.key, elem)) {
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

    public Hasher<? super K> getHasher() {
        return hasher;
    }

    public final int size() {
        return contentSize;
    }

    public final int knownSize() {
        return contentSize;
    }

    public final void sizeHint(int size) {
        final int target = tableSizeFor((int) ((size + 1) / loadFactor));
        if (target > table.length) {
            growTable(target);
        }
    }

    public void clear() {
        contentSize = 0;
        Arrays.fill(table, null);
    }
}
