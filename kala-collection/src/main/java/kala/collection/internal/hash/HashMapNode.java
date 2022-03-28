package kala.collection.internal.hash;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;

public class HashMapNode<K, V> extends HashNode<K, HashMapNode<K, V>> implements java.util.Map.Entry<K, V> {
    public V value;

    public HashMapNode(K key, int hash, V value) {
        super(key, hash);
        this.value = value;
    }

    public HashMapNode(K key, int hash, V value, HashMapNode<K, V> next) {
        super(key, hash);
        this.value = value;
        this.next = next;
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

    public final void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        HashMapNode<K, V> node = this;
        while (node != null) {
            consumer.accept(node.key, node.value);
            node = node.next;
        }
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
