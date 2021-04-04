package org.glavo.kala.collection.internal.hash;

import org.glavo.kala.collection.base.AbstractMapIterator;

import java.util.NoSuchElementException;

public final class HashMapIterator<K, V> extends AbstractMapIterator<K, V> {
    private int i = 0;
    private HashMapNode<K, V> node = null;

    private final HashMapNode<K, V>[] table;
    private final int len;

    private V value;

    public HashMapIterator(HashMapNode<K, V>[] table) {
        this.table = table;
        this.len = table.length;
    }

    @Override
    public final boolean hasNext() {
        if (node != null) {
            return true;
        }
        while (i < len) {
            HashMapNode<K, V> n = table[i];
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
