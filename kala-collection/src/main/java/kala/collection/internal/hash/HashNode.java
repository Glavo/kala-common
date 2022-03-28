package kala.collection.internal.hash;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class HashNode<K, N extends HashNode<K, N>> {
    public final K key;
    public final int hash;

    public N next;

    public HashNode(K key, int hash) {
        this.key = key;
        this.hash = hash;
    }

    public HashNode(K key, int hash, N next) {
        this.key = key;
        this.hash = hash;
        this.next = next;
    }

    @SuppressWarnings("unchecked")
    private N self() {
        return (N) this;
    }

    public final N findNode(K k, int h) {
        N node = self();

        while (true) {
            final int nodeHash = node.hash;
            if (h == nodeHash && Objects.equals(k, node.key)) {
                return node;
            }

            final N nextNode = node.next;
            if (nextNode == null || nodeHash > h) {
                return null;
            }
            node = nextNode;
        }
    }

    public abstract N deepClone();

    public void forEachKey(@NotNull Consumer<? super K> consumer) {
        N node = self();
        while (node != null) {
            consumer.accept(node.key);
            node = node.next;
        }
    }
}
