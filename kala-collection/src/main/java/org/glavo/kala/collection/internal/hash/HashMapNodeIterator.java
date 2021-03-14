package org.glavo.kala.collection.internal.hash;

import org.glavo.kala.collection.base.AbstractIterator;
import org.glavo.kala.collection.internal.hash.HashMapNode;

import java.util.NoSuchElementException;

public final class HashMapNodeIterator<K, V> extends AbstractIterator<HashMapNode<K, V>> {
    private int i = 0;
    private HashMapNode<K, V> node = null;

    private final HashMapNode<K, V>[] table;
    private final int len;

    public HashMapNodeIterator(HashMapNode<K, V>[] table) {
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

    public final HashMapNode<K, V> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        HashMapNode<K, V> oldNode = this.node;
        this.node = oldNode.next;
        return oldNode;
    }
}
