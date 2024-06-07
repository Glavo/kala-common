/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
