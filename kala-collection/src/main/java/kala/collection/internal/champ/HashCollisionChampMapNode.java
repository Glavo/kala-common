/*
 * Copyright 2025 Glavo
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
package kala.collection.internal.champ;

import kala.collection.immutable.ImmutableSeq;
import kala.collection.mutable.MutableArrayList;
import kala.control.Option;
import kala.function.Consumers;
import kala.function.Predicates;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;

import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;

@SuppressWarnings("unchecked")
public final class HashCollisionChampMapNode<K, V> extends ChampMapNode<K, V> {
    private final int originalHash;
    private final int hash;
    private ImmutableSeq<Tuple2<K, V>> content;

    public HashCollisionChampMapNode(int originalHash, int hash, ImmutableSeq<Tuple2<K, V>> content) {
        if (content.size() < 2) {
            throw new IllegalArgumentException("HashCollisionChampMapNode must have at least two elements");
        }

        this.originalHash = originalHash;
        this.hash = hash;
        this.content = content;

    }

    int indexOf(Object key) {
        int i = 0;
        for (Tuple2<K, V> kvTuple2 : content) {
            if (kvTuple2.getKey() == key)
                return i;
            i += 1;
        }
        return -1;
    }

    @Override
    public int size() {
        return content.size();
    }

    @Override
    public V apply(K key, int originalHash, int hash, int shift) {
        return getOption(key, originalHash, hash, shift).get();
    }

    @Override
    public Option<V> getOption(K key, int originalHash, int hash, int shift) {
        if (this.hash == hash) {
            int index = indexOf(key);
            return index >= 0 ? Option.some(content.get(index).getValue()) : Option.none();
        } else {
            return Option.none();
        }
    }

    @Override
    public Tuple2<K, V> getTuple(K key, int originalHash, int hash, int shift) {
        final var index = indexOf(key);
        if (index >= 0) {
            return content.get(index);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public boolean containsKey(K key, int originalHash, int hash, int shift) {
        return this.hash == hash && indexOf(key) >= 0;
    }

    public boolean contains(K key, V value, int hash, int shift) {
        if (this.hash != hash) {
            return false;
        }
        int index = indexOf(key);
        return index >= 0 && content.get(index).getValue() == value;
    }

    @Override
    public ChampMapNode<K, V> updated(K key, V value, int originalHash, int hash, int shift, boolean replaceValue) {
        int index = indexOf(key);
        if (index >= 0) {
            if (replaceValue) {
                if (content.get(index).getValue() == value) {
                    return this;
                } else {
                    return new HashCollisionChampMapNode<>(originalHash, hash, content.updated(index, Tuple.of(key, value)));
                }
            } else {
                return this;
            }
        } else {
            return new HashCollisionChampMapNode<>(originalHash, hash, content.appended(Tuple.of(key, value)));
        }
    }

    @Override
    public ChampMapNode<K, V> removed(K key, int originalHash, int hash, int shift) {
        if (!this.containsKey(key, originalHash, hash, shift)) {
            return this;
        } else {
            var updatedContent = content.filter(keyValuePair -> keyValuePair.getKey() != key);
            // assert(updatedContent.size == content.size - 1)

            if (updatedContent.size() == 1) {
                var tuple = updatedContent.getFirst();
                return new BitmapIndexedChampMapNode<>(bitposFrom(maskFrom(hash, 0)), 0, new Object[]{tuple.getKey(), tuple.getValue()}, new int[]{originalHash}, 1, hash);
            } else {
                return new HashCollisionChampMapNode<>(originalHash, hash, updatedContent);
            }
        }
    }

    @Override
    public boolean hasNodes() {
        return false;
    }

    @Override
    public int nodeArity() {
        return 0;
    }

    @Override
    public ChampMapNode<K, V> getNode(int index) {
        throw new IndexOutOfBoundsException("No sub-nodes present in hash-collision leaf node.");
    }

    @Override
    public boolean hasPayload() {
        return true;
    }

    @Override
    public int payloadArity() {
        return content.size();
    }

    @Override
    public K getKey(int index) {
        return getPayload(index).getKey();
    }

    @Override
    public V getValue(int index) {
        return getPayload(index).getValue();
    }

    @Override
    public Tuple2<K, V> getPayload(int index) {
        return content.get(index);
    }

    @Override
    public int getHash(int index) {
        return originalHash;
    }

    @Override
    public void forEach(BiConsumer<K, V> consumer) {
        content.forEach(Consumers.tupled(consumer));
    }

    @Override
    public <U> HashCollisionChampMapNode<K, U> transform(BiFunction<K, V, U> function) {
        var newContent = new MutableArrayList<Tuple2<K, U>>();
        var contentIter = content.iterator();
        // true if any values have been transformed to a different value via `f`
        boolean anyChanges = false;
        while (contentIter.hasNext()) {
            var tuple = contentIter.next();
            var newValue = function.apply(tuple.getKey(), tuple.getValue());
            newContent.append(Tuple.of(tuple.getKey(), newValue));
            anyChanges |= (tuple.getValue() != newValue);
        }
        return anyChanges ? new HashCollisionChampMapNode<>(originalHash, hash, newContent.toImmutableArray()) : (HashCollisionChampMapNode<K, U>) this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HashCollisionChampMapNode<?, ?> node) || this.hash != node.hash || this.content.size() != node.content.size()) {
            return false;
        }

        for (var tuple : content) {
            int index = node.indexOf(tuple.getKey());
            if (index < 0 || tuple.getValue() != node.content.get(index).getValue()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ChampMapNode<K, V> concat(ChampMapNode<K, V> that, int shift) {
        if (!(that instanceof HashCollisionChampMapNode<K, V> hc)) {
            // should never happen -- hash collisions are never at the same level as bitmapIndexedMapNodes
            throw new UnsupportedOperationException("Cannot concatenate a HashCollisionChampMapNode with a BitmapIndexedChampMapNode");
        }

        if (hc == this) {
            return this;
        } else {
            MutableArrayList<Tuple2<K, V>> newContent = null;
            for (Tuple2<K, V> nextPayload : content) {
                if (hc.indexOf(nextPayload.getKey()) < 0) {
                    if (newContent == null) {
                        newContent = MutableArrayList.create();
                        newContent.appendAll(hc.content);
                    }
                    newContent.append(nextPayload);
                }
            }
            return newContent == null ? hc : new HashCollisionChampMapNode<>(originalHash, hash, newContent.toImmutableArray());
        }
    }

    private static <K> int rightIndexOf(Object[] rightArray, K key) {
        for (int i = 0; i < rightArray.length; i++) {
            var elem = rightArray[i];
            if ((elem != null) && (((Tuple2<K, ?>) elem).getKey() == key)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void mergeInto(ChampMapNode<K, V> that, ChampHashMapBuilder<K, V> builder, int shift, BinaryOperator<Tuple2<K, V>> mergef) {
        if (!(that instanceof HashCollisionChampMapNode<K, V> hc)) {
            throw new UnsupportedOperationException("Cannot merge HashCollisionChampMapNode with BitmapIndexedChampMapNode");
        }

        var rightArray = hc.content.toArray(); // really Array[(K, V1)]
        for (var nextPayload : content) {
            var index = rightIndexOf(rightArray, nextPayload.getKey());

            if (index == -1) {
                builder.addOne(nextPayload);
            } else {
                var rightPayload = (Tuple2<K, V>) rightArray[index];
                rightArray = null;

                builder.addOne(mergef.apply(nextPayload, rightPayload));
            }
        }


        var i = 0;
        while (i < rightArray.length) {
            var elem = rightArray[i];
            if (elem != null) {
                builder.addOne((Tuple2<K, V>) elem);
            }
            i += 1;
        }
    }

    @Override
    public void buildTo(ChampHashMapBuilder<K, V> builder) {
        for (var tuple : content) {
            builder.addOne(tuple.getKey(), tuple.getValue(), originalHash, hash);
        }
    }

    @Override
    public ChampMapNode<K, V> filter(BiPredicate<K, V> predicate) {
        var newContent = content.filter(Predicates.tupled(predicate));
        int newContentLength = newContent.size();
        if (newContentLength == 0) {
            return ChampMapNode.empty();
        } else if (newContentLength == 1) {
            var tuple = newContent.getFirst();
            return new BitmapIndexedChampMapNode<>(bitposFrom(maskFrom(hash, 0)), 0, new Object[]{tuple.getKey(), tuple.getValue()}, new int[]{originalHash}, 1, hash);
        } else if (newContentLength == content.size()) {
            return this;
        } else {
            return new HashCollisionChampMapNode<>(originalHash, hash, newContent);
        }
    }

    @Override
    public ChampMapNode<K, V> copy() {
        return new HashCollisionChampMapNode<>(originalHash, hash, content);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Trie nodes do not support hashing.");
    }

    @Override
    public int cachedJavaKeySetHashCode() {
        return size() * hash;
    }
}
