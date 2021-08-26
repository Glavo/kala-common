package kala.collection.internal.hash;

import kala.collection.base.AbstractIterator;

import java.util.NoSuchElementException;

public final class HashMapNodeIterator<K, V> extends AbstractIterator<HashMapNode<K, V>> {
    private int i = 0;
    private HashMapNode<K, V> node = null;

    private final HashMapNode<K, V>[] table;

    public HashMapNodeIterator(HashMapNode<K, V>[] table) {
        this.table = table;
    }

    @Override
    public boolean hasNext() {
        if (node != null) {
            return true;
        }
        while (i < table.length) {
            HashMapNode<K, V> n = table[i++];
            if (n != null) {
                node = n;
                return true;
            }
        }
        return false;
    }

    public HashMapNode<K, V> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final HashMapNode<K, V> oldNode = this.node;
        this.node = oldNode.next;
        return oldNode;
    }
}
