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

import kala.control.Option;
import kala.tuple.Tuple2;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;

@SuppressWarnings("unchecked")
public sealed abstract class ChampMapNode<K, V> extends ChampNode<ChampMapNode<K, V>>
        permits BitmapIndexedChampMapNode, HashCollisionChampMapNode {

    public static final int TupleLength = 2;

    public static <K, V> BitmapIndexedChampMapNode<K, V> empty() {
        return (BitmapIndexedChampMapNode<K, V>) BitmapIndexedChampMapNode.EmptyMapNode;
    }

    public abstract V apply(K key, int originalHash, int hash, int shift);

    public abstract Option<V> getOption(K key, int originalHash, int hash, int shift);

    public abstract boolean containsKey(K key, int originalHash, int hash, int shift);

    /**
     * Returns a MapNode with the passed key-value assignment added
     *
     * @param key          the key to add to the MapNode
     * @param value        the value to associate with `key`
     * @param originalHash the original hash of `key`
     * @param hash         the improved hash of `key`
     * @param shift        the shift of the node (distanceFromRoot * BitPartitionSize)
     * @param replaceValue if true, then the value currently associated to `key` will be replaced with the passed value
     *                     argument.
     *                     if false, then the key will be inserted if not already present, however if the key is present
     *                     then the passed value will not replace the current value. That is, if `false`, then this
     *                     method has `update if not exists` semantics.
     */
    public abstract ChampMapNode<K, V> updated(K key, V value, int originalHash, int hash, int shift, boolean replaceValue);

    public abstract ChampMapNode<K, V> removed(K key, int originalHash, int hash, int shift);

    public abstract boolean hasNodes();

    public abstract int nodeArity();

    public abstract ChampMapNode<K, V> getNode(int index);

    public abstract boolean hasPayload();

    public abstract int payloadArity();

    public abstract K getKey(int index);

    public abstract V getValue(int index);

    public abstract Tuple2<K, V> getPayload(int index);

    public abstract int size();

    public abstract void forEach(BiConsumer<K, V> consumer);

    public abstract <U> ChampMapNode<K, U> transform(BiFunction<K, V, U> function);

    public abstract ChampMapNode<K, V> copy();

    public abstract ChampMapNode<K, V> concat(ChampMapNode<K, V> that, int shift);

    public abstract ChampMapNode<K, V> filter(BiPredicate<K, V> predicate);

    /**
     * Merges this node with that node, adding each resulting tuple to `builder`
     * <p>
     * `this` should be a node from `left` hashmap in `left.merged(right)(mergef)`
     *
     * @param that node from the "right" HashMap. Must also be at the same "path" or "position" within the right tree,
     *             as this is, within the left tree
     */
    public abstract void mergeInto(ChampMapNode<K, V> that, ChampMapBuilder<K, V> builder, int shift, BinaryOperator<Tuple2<K, V>> mergef);

    /**
     * Returns the exact (equal by reference) key, and value, associated to a given key.
     * If the key is not bound to a value, then an exception is thrown
     */
    public abstract Tuple2<K, V> getTuple(K key, int originalHash, int hash, int shift);

    /**
     * Adds all key-value pairs to a builder
     */
    public abstract void buildTo(ChampMapBuilder<K, V> builder);
}
