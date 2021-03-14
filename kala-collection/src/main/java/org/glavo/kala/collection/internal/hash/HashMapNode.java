package org.glavo.kala.collection.internal.hash;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;

public final class HashMapNode<K, V> implements java.util.Map.Entry<K, V> {
    public final K key;
    public final int hash;
    public V value;

    public HashMapNode<K, V> next;

    public HashMapNode(K key, int hash, V value) {
        this(key, hash, value, null);
    }

    public HashMapNode(K key, int hash, V value, HashMapNode<K, V> next) {
        this.key = key;
        this.hash = hash;
        this.value = value;
        this.next = next;
    }


    public final HashMapNode<K, V> findNode(K k, int h) {
        HashMapNode<K, V> node = this;

        while (true) {
            final int nodeHash = node.hash;
            if (h == nodeHash && Objects.equals(k, node.key)) {
                return node;
            }

            final HashMapNode<K, V> nextNode = node.next;
            if (nextNode == null || nodeHash > h) {
                return null;
            }
            node = nextNode;
        }
    }

    public final void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        HashMapNode<K, V> node = this;
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

    public String toString(boolean debug) {
        if (!debug) {
            return toString();
        }

        StringBuilder builder = new StringBuilder();
        HashMapNode<K, V> node = this;
        while (true) {
            final HashMapNode<K, V> nextNode = node.next;
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
        return "HashMapNode[key=" + key + ", value=" + value + ", hash=" + hash + "]";
    }

    public final HashMapNode<K, V> deepClone() {
        final HashMapNode<K, V> head = new HashMapNode<>(key, hash, value, next);

        HashMapNode<K, V> node = head;
        HashMapNode<K, V> nextNode;
        while ((nextNode = node.next) != null) {
            nextNode = new HashMapNode<>(nextNode.key, nextNode.hash, nextNode.value, nextNode.next);
            node.next = nextNode;
            node = nextNode;
        }

        return head;
    }
}
