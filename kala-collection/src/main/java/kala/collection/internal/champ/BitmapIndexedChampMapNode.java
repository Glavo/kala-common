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

import kala.collection.base.ObjectArrays;
import kala.collection.base.primitive.IntArrays;
import kala.collection.immutable.ImmutableVector;
import kala.collection.mutable.MutableQueue;
import kala.control.Option;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;

import static java.lang.Integer.bitCount;

@SuppressWarnings("unchecked")
public final class BitmapIndexedChampMapNode<K, V> extends ChampMapNode<K, V> {

    static final BitmapIndexedChampMapNode<?, ?> EmptyMapNode = new BitmapIndexedChampMapNode<>(0, 0, ObjectArrays.EMPTY, IntArrays.EMPTY, 0, 0);

    int dataMap;
    int nodeMap;
    Object[] content;
    int[] originalHashes;
    int size;
    int cachedJavaKeySetHashCode;

    public BitmapIndexedChampMapNode(int dataMap, int nodeMap, Object[] content, int[] originalHashes, int size, int cachedJavaKeySetHashCode) {
        this.dataMap = dataMap;
        this.nodeMap = nodeMap;
        this.content = content;
        this.originalHashes = originalHashes;
        this.size = size;
        this.cachedJavaKeySetHashCode = cachedJavaKeySetHashCode;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int cachedJavaKeySetHashCode() {
        return cachedJavaKeySetHashCode;
    }

    @Override
    public K getKey(int index) {
        return (K) content[TupleLength * index];
    }

    @Override
    public V getValue(int index) {
        return (V) content[TupleLength * index + 1];
    }

    @Override
    public Tuple2<K, V> getPayload(int index) {
        return Tuple.of((K) content[TupleLength * index], (V) content[TupleLength * index + 1]);
    }

    @Override
    public int getHash(int index) {
        return originalHashes[index];
    }

    @Override
    public ChampMapNode<K, V> getNode(int index) {
        return (ChampMapNode<K, V>) content[content.length - 1 - index];
    }

    @Override
    public V apply(K key, int originalHash, int keyHash, int shift) {
        int mask = maskFrom(keyHash, shift);
        int bitpos = bitposFrom(mask);

        if ((dataMap & bitpos) != 0) {
            int index = indexFrom(dataMap, mask, bitpos);
            if (Objects.equals(key, getKey(index))) {
                return getValue(index);
            } else {
                throw new NoSuchElementException("key not found: " + key);
            }
        } else if ((nodeMap & bitpos) != 0) {
            return getNode(indexFrom(nodeMap, mask, bitpos)).apply(key, originalHash, keyHash, shift + BitPartitionSize);
        } else {
            throw new NoSuchElementException("key not found: " + key);
        }
    }

    @Override
    public Option<V> getOption(K key, int originalHash, int keyHash, int shift) {
        int mask = maskFrom(keyHash, shift);
        int bitpos = bitposFrom(mask);

        if ((dataMap & bitpos) != 0) {
            int index = indexFrom(dataMap, mask, bitpos);
            K key0 = this.getKey(index);
            return Objects.equals(key, key0) ? Option.some(this.getValue(index)) : Option.none();
        } else if ((nodeMap & bitpos) != 0) {
            int index = indexFrom(nodeMap, mask, bitpos);
            return this.getNode(index).getOption(key, originalHash, keyHash, shift + BitPartitionSize);
        } else {
            return Option.none();
        }
    }

    @Override
    public Tuple2<K, V> getTuple(K key, int originalHash, int hash, int shift) {
        int mask = maskFrom(hash, shift);
        int bitpos = bitposFrom(mask);

        if ((dataMap & bitpos) != 0) {
            int index = indexFrom(dataMap, mask, bitpos);
            Tuple2<K, V> payload = getPayload(index);
            if (key == payload.getKey()) {
                return payload;
            } else {
                throw new NoSuchElementException("key not found: " + key);
            }
        } else if ((nodeMap & bitpos) != 0) {
            int index = indexFrom(nodeMap, mask, bitpos);
            return getNode(index).getTuple(key, originalHash, hash, shift + BitPartitionSize);
        } else {
            throw new NoSuchElementException("key not found: " + key);
        }
    }

    @Override
    public boolean containsKey(K key, int originalHash, int keyHash, int shift) {
        int mask = maskFrom(keyHash, shift);
        int bitpos = bitposFrom(mask);

        if ((dataMap & bitpos) != 0) {
            int index = indexFrom(dataMap, mask, bitpos);
            // assert(hashes(index) == computeHash(this.getKey(index)), (hashes.toSeq, content.toSeq, index, key, keyHash, shift))
            return (originalHashes[index] == originalHash) && Objects.equals(key, getKey(index));
        } else if ((nodeMap & bitpos) != 0) {
            return getNode(indexFrom(nodeMap, mask, bitpos)).containsKey(key, originalHash, keyHash, shift + BitPartitionSize);
        } else {
            return false;
        }
    }

    @Override
    public BitmapIndexedChampMapNode<K, V> updated(K key, V value, int originalHash, int keyHash, int shift, boolean replaceValue) {
        int mask = maskFrom(keyHash, shift);
        int bitpos = bitposFrom(mask);

        if ((dataMap & bitpos) != 0) {
            int index = indexFrom(dataMap, mask, bitpos);
            K key0 = getKey(index);
            int key0UnimprovedHash = getHash(index);
            if (key0UnimprovedHash == originalHash && Objects.equals(key0, key)) {
                if (replaceValue) {
                    V value0 = this.getValue(index);
                    if ((key0 == key) && (value0 == value))
                        return this;
                    else
                        return copyAndSetValue(bitpos, key, value);
                } else {
                    return this;
                }
            } else {
                V value0 = this.getValue(index);
                int key0Hash = improve(key0UnimprovedHash);
                var subNodeNew = mergeTwoKeyValPairs(key0, value0, key0UnimprovedHash, key0Hash, key, value, originalHash, keyHash, shift + BitPartitionSize);

                return copyAndMigrateFromInlineToNode(bitpos, key0Hash, subNodeNew);
            }
        } else if ((nodeMap & bitpos) != 0) {
            int index = indexFrom(nodeMap, mask, bitpos);
            ChampMapNode<K, V> subNode = this.getNode(index);
            ChampMapNode<K, V> subNodeNew = subNode.updated(key, value, originalHash, keyHash, shift + BitPartitionSize, replaceValue);

            if (subNodeNew == subNode) {
                return this;
            } else {
                return copyAndSetNode(bitpos, subNode, subNodeNew);
            }
        } else {
            return copyAndInsertValue(bitpos, key, originalHash, keyHash, value);
        }
    }

    /**
     * A variant of `updated` which performs shallow mutations on the root (`this`), and if possible, on immediately
     * descendant child nodes (only one level beneath `this`)
     * <p>
     * The caller should pass a bitmap of child nodes of this node, which this method may mutate.
     * If this method may mutate a child node, then if the updated key-value belongs in that child node, it will
     * be shallowly mutated (its children will not be mutated).
     * <p>
     * If instead this method may not mutate the child node in which the to-be-updated key-value pair belongs, then
     * that child will be updated immutably, but the result will be mutably re-inserted as a child of this node.
     *
     * @param key                     the key to update
     * @param value                   the value to set `key` to
     * @param originalHash            key.##
     * @param keyHash                 the improved hash
     * @param shallowlyMutableNodeMap bitmap of child nodes of this node, which can be shallowly mutated
     *                                during the call to this method
     * @return Int which is the bitwise OR of shallowlyMutableNodeMap and any freshly created nodes, which will be
     * available for mutations in subsequent calls.
     */
    public int updateWithShallowMutations(K key, V value, int originalHash, int keyHash, int shift, int shallowlyMutableNodeMap) {
        int mask = maskFrom(keyHash, shift);
        int bitpos = bitposFrom(mask);

        if ((dataMap & bitpos) != 0) {
            int index = indexFrom(dataMap, mask, bitpos);
            K key0 = getKey(index);
            int key0UnimprovedHash = getHash(index);
            if (key0UnimprovedHash == originalHash && Objects.equals(key0, key)) {
                V value0 = this.getValue(index);
                if (!((key0 == key) && (value0 == value))) {
                    int dataIx = dataIndex(bitpos);
                    int idx = TupleLength * dataIx;
                    content[idx + 1] = value;
                }
                return shallowlyMutableNodeMap;
            } else {
                V value0 = this.getValue(index);
                int key0Hash = improve(key0UnimprovedHash);

                var subNodeNew = mergeTwoKeyValPairs(key0, value0, key0UnimprovedHash, key0Hash, key, value, originalHash, keyHash, shift + BitPartitionSize);
                migrateFromInlineToNodeInPlace(bitpos, key0Hash, subNodeNew);
                return shallowlyMutableNodeMap | bitpos;
            }
        } else if ((nodeMap & bitpos) != 0) {
            int index = indexFrom(nodeMap, mask, bitpos);
            ChampMapNode<K, V> subNode = this.getNode(index);
            int subNodeSize = subNode.size();
            int subNodeHashCode = subNode.cachedJavaKeySetHashCode();

            var returnMutableNodeMap = shallowlyMutableNodeMap;

            final ChampMapNode<K, V> subNodeNew;
            if (subNode instanceof BitmapIndexedChampMapNode<K, V> subNodeBm && (bitpos & shallowlyMutableNodeMap) != 0) {
                subNodeBm.updateWithShallowMutations(key, value, originalHash, keyHash, shift + BitPartitionSize, 0);
                subNodeNew = subNodeBm;
            } else {
                var result = subNode.updated(key, value, originalHash, keyHash, shift + BitPartitionSize, true);
                if (result != subNode) {
                    returnMutableNodeMap |= bitpos;
                }
                subNodeNew = result;
            }

            this.content[this.content.length - 1 - this.nodeIndex(bitpos)] = subNodeNew;
            this.size = this.size - subNodeSize + subNodeNew.size();
            this.cachedJavaKeySetHashCode = this.cachedJavaKeySetHashCode - subNodeHashCode + subNodeNew.cachedJavaKeySetHashCode();
            return returnMutableNodeMap;
        } else {
            int dataIx = dataIndex(bitpos);
            int idx = TupleLength * dataIx;

            Object[] src = this.content;
            Object[] dst = new Object[src.length + TupleLength];

            // copy 'src' and insert 2 element(s) at position 'idx'
            System.arraycopy(src, 0, dst, 0, idx);
            dst[idx] = key;
            dst[idx + 1] = value;
            System.arraycopy(src, idx, dst, idx + TupleLength, src.length - idx);

            this.dataMap |= bitpos;
            this.content = dst;
            this.originalHashes = insertElement(originalHashes, dataIx, originalHash);
            this.size += 1;
            this.cachedJavaKeySetHashCode += keyHash;
            return shallowlyMutableNodeMap;
        }
    }

    @Override
    public BitmapIndexedChampMapNode<K, V> removed(K key, int originalHash, int keyHash, int shift) {
        int mask = maskFrom(keyHash, shift);
        int bitpos = bitposFrom(mask);

        if ((dataMap & bitpos) != 0) {
            int index = indexFrom(dataMap, mask, bitpos);
            K key0 = this.getKey(index);

            if (Objects.equals(key0, key)) {
                if (this.payloadArity() == 2 && this.nodeArity() == 0) {
                    /*
                     * Create new node with remaining pair. The new node will a) either become the new root
                     * returned, or b) unwrapped and inlined during returning.
                     */
                    var newDataMap = shift == 0 ? (dataMap ^ bitpos) : bitposFrom(maskFrom(keyHash, 0));
                    if (index == 0)
                        return new BitmapIndexedChampMapNode<>(newDataMap, 0, new Object[]{getKey(1), getValue(1)}, new int[]{originalHashes[1]}, 1, improve(getHash(1)));
                    else
                        return new BitmapIndexedChampMapNode<>(newDataMap, 0, new Object[]{getKey(0), getValue(0)}, new int[]{originalHashes[0]}, 1, improve(getHash(0)));
                } else
                    return copyAndRemoveValue(bitpos, keyHash);
            } else {
                return this;
            }
        } else if ((nodeMap & bitpos) != 0) {
            int index = indexFrom(nodeMap, mask, bitpos);
            ChampMapNode<K, V> subNode = this.getNode(index);

            ChampMapNode<K, V> subNodeNew = subNode.removed(key, originalHash, keyHash, shift + BitPartitionSize);
            // assert(subNodeNew.size != 0, "Sub-node must have at least one element.")

            if (subNodeNew == subNode) {
                return this;
            }

            // cache just in case subNodeNew is a hashCollision node, in which in which case a little arithmetic is avoided
            // in Vector#length
            int subNodeNewSize = subNodeNew.size();

            if (subNodeNewSize == 1) {
                if (this.size == subNode.size()) {
                    // subNode is the only child (no other data or node children of `this` exist)
                    // escalate (singleton or empty) result
                    return (BitmapIndexedChampMapNode<K, V>) subNodeNew;
                } else {
                    // inline value (move to front)
                    return copyAndMigrateFromNodeToInline(bitpos, subNode, subNodeNew);
                }
            } else if (subNodeNewSize > 1) {
                // modify current node (set replacement node)
                return copyAndSetNode(bitpos, subNode, subNodeNew);
            } else {
                return this;
            }
        } else {
            return this;
        }
    }

    public ChampMapNode<K, V> mergeTwoKeyValPairs(K key0, V value0, int originalHash0, int keyHash0, K key1, V value1, int originalHash1, int keyHash1, int shift) {
        // assert(key0 != key1)

        if (shift >= HashCodeLength) {
            return new HashCollisionChampMapNode<>(originalHash0, keyHash0, ImmutableVector.of(Tuple.of(key0, value0), Tuple.of(key1, value1)));
        } else {
            int mask0 = maskFrom(keyHash0, shift);
            int mask1 = maskFrom(keyHash1, shift);
            var newCachedHash = keyHash0 + keyHash1;

            if (mask0 != mask1) {
                // unique prefixes, payload fits on same level
                int dataMap = bitposFrom(mask0) | bitposFrom(mask1);

                if (mask0 < mask1) {
                    return new BitmapIndexedChampMapNode<>(dataMap, 0, new Object[]{key0, value0, key1, value1}, new int[]{originalHash0, originalHash1}, 2, newCachedHash);
                } else {
                    return new BitmapIndexedChampMapNode<>(dataMap, 0, new Object[]{key1, value1, key0, value0}, new int[]{originalHash1, originalHash0}, 2, newCachedHash);
                }
            } else {
                // identical prefixes, payload must be disambiguated deeper in the trie
                var nodeMap = bitposFrom(mask0);
                ChampMapNode<K, V> node = mergeTwoKeyValPairs(key0, value0, originalHash0, keyHash0, key1, value1, originalHash1, keyHash1, shift + BitPartitionSize);
                return new BitmapIndexedChampMapNode<>(0, nodeMap, new Object[]{node}, IntArrays.EMPTY, node.size(), node.cachedJavaKeySetHashCode());
            }
        }
    }

    public boolean hasNodes() {
        return nodeMap != 0;
    }

    public int nodeArity() {
        return bitCount(nodeMap);
    }

    public boolean hasPayload() {
        return dataMap != 0;
    }

    public int payloadArity() {
        return bitCount(dataMap);
    }

    public int dataIndex(int bitpos) {
        return bitCount(dataMap & (bitpos - 1));
    }

    public int nodeIndex(int bitpos) {
        return bitCount(nodeMap & (bitpos - 1));
    }

    public BitmapIndexedChampMapNode<K, V> copyAndSetValue(int bitpos, K newKey, V newValue) {
        int dataIx = dataIndex(bitpos);
        int idx = TupleLength * dataIx;

        Object[] src = this.content;
        Object[] dst = new Object[src.length];

        // copy 'src' and set 1 element(s) at position 'idx'
        System.arraycopy(src, 0, dst, 0, src.length);
        //dst[idx] = newKey
        dst[idx + 1] = newValue;
        return new BitmapIndexedChampMapNode<>(dataMap, nodeMap, dst, originalHashes, size, cachedJavaKeySetHashCode);
    }

    public BitmapIndexedChampMapNode<K, V> copyAndSetNode(int bitpos, ChampMapNode<K, V> oldNode, ChampMapNode<K, V> newNode) {
        int idx = this.content.length - 1 - this.nodeIndex(bitpos);

        Object[] src = this.content;
        Object[] dst = new Object[src.length];

        // copy 'src' and set 1 element(s) at position 'idx'
        System.arraycopy(src, 0, dst, 0, src.length);
        dst[idx] = newNode;
        return new BitmapIndexedChampMapNode<>(
                dataMap,
                nodeMap,
                dst,
                originalHashes,
                size - oldNode.size() + newNode.size(),
                cachedJavaKeySetHashCode - oldNode.cachedJavaKeySetHashCode() + newNode.cachedJavaKeySetHashCode()
        );
    }

    public BitmapIndexedChampMapNode<K, V> copyAndInsertValue(int bitpos, K key, int originalHash, int keyHash, V value) {
        int dataIx = dataIndex(bitpos);
        int idx = TupleLength * dataIx;

        Object[] src = this.content;
        Object[] dst = new Object[src.length + TupleLength];

        // copy 'src' and insert 2 element(s) at position 'idx'
        System.arraycopy(src, 0, dst, 0, idx);
        dst[idx] = key;
        dst[idx + 1] = value;
        System.arraycopy(src, idx, dst, idx + TupleLength, src.length - idx);

        var dstHashes = insertElement(originalHashes, dataIx, originalHash);

        return new BitmapIndexedChampMapNode<>(
                dataMap | bitpos, nodeMap, dst, dstHashes, size + 1,
                cachedJavaKeySetHashCode + keyHash);
    }

    public BitmapIndexedChampMapNode<K, V> copyAndRemoveValue(int bitpos, int keyHash) {
        int dataIx = dataIndex(bitpos);
        int idx = TupleLength * dataIx;

        var src = this.content;
        var dst = new Object[src.length - TupleLength];

        // copy 'src' and remove 2 element(s) at position 'idx'
        System.arraycopy(src, 0, dst, 0, idx);
        System.arraycopy(src, idx + TupleLength, dst, idx, src.length - idx - TupleLength);

        int[] dstHashes = removeElement(originalHashes, dataIx);

        return new BitmapIndexedChampMapNode<>(
                dataMap ^ bitpos, nodeMap, dst, dstHashes, size - 1,
                cachedJavaKeySetHashCode - keyHash);
    }

    public BitmapIndexedChampMapNode<K, V> migrateFromInlineToNodeInPlace(int bitpos, int keyHash, ChampMapNode<K, V> node) {
        int dataIx = dataIndex(bitpos);
        int idxOld = TupleLength * dataIx;
        int idxNew = this.content.length - TupleLength - nodeIndex(bitpos);

        var src = this.content;
        var dst = new Object[src.length - TupleLength + 1];

        // copy 'src' and remove 2 element(s) at position 'idxOld' and
        // insert 1 element(s) at position 'idxNew'
        // assert(idxOld <= idxNew)
        System.arraycopy(src, 0, dst, 0, idxOld);
        System.arraycopy(src, idxOld + TupleLength, dst, idxOld, idxNew - idxOld);
        dst[idxNew] = node;
        System.arraycopy(src, idxNew + TupleLength, dst, idxNew + 1, src.length - idxNew - TupleLength);

        var dstHashes = removeElement(originalHashes, dataIx);

        this.dataMap = dataMap ^ bitpos;
        this.nodeMap = nodeMap | bitpos;
        this.content = dst;
        this.originalHashes = dstHashes;
        this.size = size - 1 + node.size();
        this.cachedJavaKeySetHashCode = cachedJavaKeySetHashCode - keyHash + node.cachedJavaKeySetHashCode();
        return this;
    }

    public BitmapIndexedChampMapNode<K, V> copyAndMigrateFromInlineToNode(int bitpos, int keyHash, ChampMapNode<K, V> node) {
        int dataIx = dataIndex(bitpos);
        int idxOld = TupleLength * dataIx;
        int idxNew = this.content.length - TupleLength - nodeIndex(bitpos);

        var src = this.content;
        var dst = new Object[src.length - TupleLength + 1];

        // copy 'src' and remove 2 element(s) at position 'idxOld' and
        // insert 1 element(s) at position 'idxNew'
        // assert(idxOld <= idxNew)
        System.arraycopy(src, 0, dst, 0, idxOld);
        System.arraycopy(src, idxOld + TupleLength, dst, idxOld, idxNew - idxOld);
        dst[idxNew] = node;
        System.arraycopy(src, idxNew + TupleLength, dst, idxNew + 1, src.length - idxNew - TupleLength);

        var dstHashes = removeElement(originalHashes, dataIx);

        return new BitmapIndexedChampMapNode<>(
                dataMap = dataMap ^ bitpos,
                nodeMap = nodeMap | bitpos,
                content = dst,
                originalHashes = dstHashes,
                size = size - 1 + node.size(),
                cachedJavaKeySetHashCode = cachedJavaKeySetHashCode - keyHash + node.cachedJavaKeySetHashCode()
        );
    }

    public BitmapIndexedChampMapNode<K, V> copyAndMigrateFromNodeToInline(int bitpos, ChampMapNode<K, V> oldNode, ChampMapNode<K, V> node) {
        int idxOld = this.content.length - 1 - nodeIndex(bitpos);
        int dataIxNew = dataIndex(bitpos);
        int idxNew = TupleLength * dataIxNew;

        K key = node.getKey(0);
        V value = node.getValue(0);
        var src = this.content;
        var dst = new Object[src.length - 1 + TupleLength];

        // copy 'src' and remove 1 element(s) at position 'idxOld' and
        // insert 2 element(s) at position 'idxNew'
        // assert(idxOld >= idxNew)
        System.arraycopy(src, 0, dst, 0, idxNew);
        dst[idxNew] = key;
        dst[idxNew + 1] = value;
        System.arraycopy(src, idxNew, dst, idxNew + TupleLength, idxOld - idxNew);
        System.arraycopy(src, idxOld + 1, dst, idxOld + TupleLength, src.length - idxOld - 1);
        int hash = node.getHash(0);
        var dstHashes = insertElement(originalHashes, dataIxNew, hash);
        return new BitmapIndexedChampMapNode<>(
                dataMap = dataMap | bitpos,
                nodeMap = nodeMap ^ bitpos,
                content = dst,
                originalHashes = dstHashes,
                size = size - oldNode.size() + 1,
                cachedJavaKeySetHashCode = cachedJavaKeySetHashCode - oldNode.cachedJavaKeySetHashCode() + node.cachedJavaKeySetHashCode()
        );
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> consumer) {
        var iN = payloadArity(); // arity doesn't change during this operation
        var i = 0;
        while (i < iN) {
            consumer.accept(getKey(i), getValue(i));
            i += 1;
        }

        var jN = nodeArity(); // arity doesn't change during this operation
        var j = 0;
        while (j < jN) {
            getNode(j).forEach(consumer);
            j += 1;
        }
    }

    @Override
    public void buildTo(ChampMapBuilder<K, V> builder) {
        int i = 0;
        int iN = payloadArity();
        int jN = nodeArity();
        while (i < iN) {
            builder.add(getKey(i), getValue(i), getHash(i));
            i += 1;
        }

        var j = 0;
        while (j < jN) {
            getNode(j).buildTo(builder);
            j += 1;
        }
    }

    @Override
    public <U> BitmapIndexedChampMapNode<K, U> transform(BiFunction<K, V, U> function) {
        Object[] newContent = null;
        final int iN = payloadArity(); // arity doesn't change during this operation
        final int jN = nodeArity(); // arity doesn't change during this operation
        int newContentLength = content.length;
        var i = 0;
        while (i < iN) {
            K key = getKey(i);
            V value = getValue(i);
            U newValue = function.apply(key, value);
            if (newContent == null) {
                if (newValue != value) {
                    newContent = content.clone();
                    newContent[TupleLength * i + 1] = newValue;
                }
            } else {
                newContent[TupleLength * i + 1] = newValue;
            }
            i += 1;
        }

        var j = 0;
        while (j < jN) {
            var node = getNode(j);
            var newNode = node.transform(function);
            if (newContent == null) {
                if (newNode != node) {
                    newContent = content.clone();
                    newContent[newContentLength - j - 1] = newNode;
                }
            } else {
                newContent[newContentLength - j - 1] = newNode;
            }
            j += 1;
        }
        return newContent == null ? (BitmapIndexedChampMapNode<K, U>) this : new BitmapIndexedChampMapNode<>(dataMap, nodeMap, newContent, originalHashes, size, cachedJavaKeySetHashCode);
    }

    @Override
    public void mergeInto(ChampMapNode<K, V> that, ChampMapBuilder<K, V> builder, int shift, BinaryOperator<Tuple2<K, V>> mergef) {
        if (!(that instanceof BitmapIndexedChampMapNode<K, V> bm)) {
            throw new RuntimeException("Cannot merge BitmapIndexedMapNode with HashCollisionMapNode");
        }

        if (size == 0) {
            that.buildTo(builder);
            return;
        } else if (bm.size == 0) {
            buildTo(builder);
            return;
        }

        int allMap = dataMap | bm.dataMap | nodeMap | bm.nodeMap;

        int minIndex = Integer.numberOfTrailingZeros(allMap);
        int maxIndex = ChampNode.BranchingFactor - Integer.numberOfLeadingZeros(allMap);

        {
            int index = minIndex;
            int leftIdx = 0;
            int rightIdx = 0;

            while (index < maxIndex) {
                int bitpos = bitposFrom(index);

                if ((bitpos & dataMap) != 0) {
                    K leftKey = getKey(leftIdx);
                    V leftValue = getValue(leftIdx);
                    int leftOriginalHash = getHash(leftIdx);
                    if ((bitpos & bm.dataMap) != 0) {
                        // left data and right data
                        K rightKey = bm.getKey(rightIdx);
                        V rightValue = bm.getValue(rightIdx);
                        int rightOriginalHash = bm.getHash(rightIdx);
                        if (leftOriginalHash == rightOriginalHash && Objects.equals(leftKey, rightKey)) {
                            builder.add(mergef.apply(Tuple.of(leftKey, leftValue), Tuple.of(rightKey, rightValue)));
                        } else {
                            builder.add(leftKey, leftValue, leftOriginalHash);
                            builder.add(rightKey, rightValue, rightOriginalHash);
                        }
                        rightIdx += 1;
                    } else if ((bitpos & bm.nodeMap) != 0) {
                        // left data and right node
                        var subNode = bm.getNode(bm.nodeIndex(bitpos));
                        var leftImprovedHash = improve(leftOriginalHash);
                        var removed = subNode.removed(leftKey, leftOriginalHash, leftImprovedHash, shift + BitPartitionSize);
                        if (removed == subNode) {
                            // no overlap in leftData and rightNode, just build both children to builder
                            subNode.buildTo(builder);
                            builder.add(leftKey, leftValue, leftOriginalHash, leftImprovedHash);
                        } else {
                            // there is collision, so special treatment for that key
                            removed.buildTo(builder);
                            builder.add(mergef.apply(Tuple.of(leftKey, leftValue), subNode.getTuple(leftKey, leftOriginalHash, leftImprovedHash, shift + BitPartitionSize)));
                        }
                    } else {
                        // left data and nothing on right
                        builder.add(leftKey, leftValue, leftOriginalHash);
                    }
                    leftIdx += 1;
                } else if ((bitpos & nodeMap) != 0) {
                    if ((bitpos & bm.dataMap) != 0) {
                        // left node and right data
                        K rightKey = bm.getKey(rightIdx);
                        V rightValue = bm.getValue(rightIdx);
                        int rightOriginalHash = bm.getHash(rightIdx);
                        int rightImprovedHash = improve(rightOriginalHash);

                        ChampMapNode<K, V> subNode = getNode(nodeIndex(bitpos));
                        ChampMapNode<K, V> removed = subNode.removed(rightKey, rightOriginalHash, rightImprovedHash, shift + BitPartitionSize);
                        if (removed == subNode) {
                            // no overlap in leftNode and rightData, just build both children to builder
                            subNode.buildTo(builder);
                            builder.add(rightKey, rightValue, rightOriginalHash, rightImprovedHash);
                        } else {
                            // there is collision, so special treatment for that key
                            removed.buildTo(builder);
                            builder.add(mergef.apply(subNode.getTuple(rightKey, rightOriginalHash, rightImprovedHash, shift + BitPartitionSize), Tuple.of(rightKey, rightValue)));
                        }
                        rightIdx += 1;

                    } else if ((bitpos & bm.nodeMap) != 0) {
                        // left node and right node
                        getNode(nodeIndex(bitpos)).mergeInto(bm.getNode(bm.nodeIndex(bitpos)), builder, shift + BitPartitionSize, mergef);
                    } else {
                        // left node and nothing on right
                        getNode(nodeIndex(bitpos)).buildTo(builder);
                    }
                } else if ((bitpos & bm.dataMap) != 0) {
                    // nothing on left, right data
                    int dataIndex = bm.dataIndex(bitpos);
                    builder.add(bm.getKey(dataIndex), bm.getValue(dataIndex), bm.getHash(dataIndex));
                    rightIdx += 1;

                } else if ((bitpos & bm.nodeMap) != 0) {
                    // nothing on left, right node
                    bm.getNode(bm.nodeIndex(bitpos)).buildTo(builder);
                }

                index += 1;
            }
        }
    }

    private boolean deepContentEquality(Object[] a1, Object[] a2, int length) {
        if (a1 == a2) {
            return true;
        } else {
            var isEqual = true;
            var i = 0;

            while (isEqual && i < length) {
                isEqual = Objects.equals(a1[i], a2[i]);
                i += 1;
            }

            return isEqual;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        return obj instanceof BitmapIndexedChampMapNode<?, ?> node
               && (this.cachedJavaKeySetHashCode == node.cachedJavaKeySetHashCode)
               && (this.nodeMap == node.nodeMap)
               && (this.dataMap == node.dataMap)
               && (this.size == node.size)
               && Arrays.equals(this.originalHashes, node.originalHashes)
               && deepContentEquality(this.content, node.content, content.length);

    }

    @Override
    public ChampMapNode<K, V> concat(ChampMapNode<K, V> that, int shift) {
        if (!(that instanceof BitmapIndexedChampMapNode<K, V> bm)) {
            // should never happen -- hash collisions are never at the same level as bitmapIndexedMapNodes
            throw new UnsupportedOperationException("Cannot concatenate a HashCollisionMapNode with a BitmapIndexedMapNode");
        }

        if (size == 0) return bm;
        else if (bm.size == 0 || (bm == this)) return this;
        else if (bm.size == 1) {
            int originalHash = bm.getHash(0);
            return this.updated(bm.getKey(0), bm.getValue(0), originalHash, improve(originalHash), shift, true);
        }
        // if we go through the merge and the result does not differ from `bm`, we can just return `bm`, to improve sharing
        // So, `anyChangesMadeSoFar` will be set to `true` as soon as we encounter a difference between the
        // currently-being-computed result, and `bm`
        var anyChangesMadeSoFar = false;

        int allMap = dataMap | bm.dataMap | nodeMap | bm.nodeMap;

        // minimumIndex is inclusive -- it is the first index for which there is data or nodes
        int minimumBitPos = ChampNode.bitposFrom(Integer.numberOfTrailingZeros(allMap));
        // maximumIndex is inclusive -- it is the last index for which there is data or nodes
        // it could not be exclusive, because then upper bound in worst case (Node.BranchingFactor) would be out-of-bound
        // of int bitposition representation
        int maximumBitPos = ChampNode.bitposFrom(ChampNode.BranchingFactor - Integer.numberOfLeadingZeros(allMap) - 1);

        int leftNodeRightNode = 0;
        int leftDataRightNode = 0;
        int leftNodeRightData = 0;
        int leftDataOnly = 0;
        int rightDataOnly = 0;
        int leftNodeOnly = 0;
        int rightNodeOnly = 0;
        int leftDataRightDataMigrateToNode = 0;
        int leftDataRightDataRightOverwrites = 0;

        int dataToNodeMigrationTargets = 0;

        {
            int bitpos = minimumBitPos;
            int leftIdx = 0;
            int rightIdx = 0;
            boolean finished = false;

            while (!finished) {

                if ((bitpos & dataMap) != 0) {
                    if ((bitpos & bm.dataMap) != 0) {
                        int leftOriginalHash = getHash(leftIdx);
                        if (leftOriginalHash == bm.getHash(rightIdx) && Objects.equals(getKey(leftIdx), bm.getKey(rightIdx))) {
                            leftDataRightDataRightOverwrites |= bitpos;
                        } else {
                            leftDataRightDataMigrateToNode |= bitpos;
                            dataToNodeMigrationTargets |= ChampNode.bitposFrom(ChampNode.maskFrom(improve(leftOriginalHash), shift));
                        }
                        rightIdx += 1;
                    } else if ((bitpos & bm.nodeMap) != 0) {
                        leftDataRightNode |= bitpos;
                    } else {
                        leftDataOnly |= bitpos;
                    }
                    leftIdx += 1;
                } else if ((bitpos & nodeMap) != 0) {
                    if ((bitpos & bm.dataMap) != 0) {
                        leftNodeRightData |= bitpos;
                        rightIdx += 1;
                    } else if ((bitpos & bm.nodeMap) != 0) {
                        leftNodeRightNode |= bitpos;
                    } else {
                        leftNodeOnly |= bitpos;
                    }
                } else if ((bitpos & bm.dataMap) != 0) {
                    rightDataOnly |= bitpos;
                    rightIdx += 1;
                } else if ((bitpos & bm.nodeMap) != 0) {
                    rightNodeOnly |= bitpos;
                }

                if (bitpos == maximumBitPos) {
                    finished = true;
                } else {
                    bitpos = bitpos << 1;
                }
            }
        }

        int newDataMap = leftDataOnly | rightDataOnly | leftDataRightDataRightOverwrites;

        int newNodeMap =
                leftNodeRightNode |
                leftDataRightNode |
                leftNodeRightData |
                leftNodeOnly |
                rightNodeOnly |
                dataToNodeMigrationTargets;


        if ((newDataMap == (rightDataOnly | leftDataRightDataRightOverwrites)) && (newNodeMap == rightNodeOnly)) {
            // nothing from `this` will make it into the result -- return early
            return bm;
        }

        int newDataSize = bitCount(newDataMap);
        int newContentSize = (ChampMapNode.TupleLength * newDataSize) + bitCount(newNodeMap);

        var newContent = new Object[newContentSize];
        var newOriginalHashes = new int[newDataSize];
        int newSize = 0;
        int newCachedHashCode = 0;

        {
            int leftDataIdx = 0;
            int rightDataIdx = 0;
            int leftNodeIdx = 0;
            int rightNodeIdx = 0;

            int nextShift = shift + ChampNode.BitPartitionSize;

            int compressedDataIdx = 0;
            int compressedNodeIdx = 0;

            int bitpos = minimumBitPos;
            boolean finished = false;

            while (!finished) {

                if ((bitpos & leftNodeRightNode) != 0) {
                    var rightNode = bm.getNode(rightNodeIdx);
                    var newNode = getNode(leftNodeIdx).concat(rightNode, nextShift);
                    if (rightNode != newNode) {
                        anyChangesMadeSoFar = true;
                    }
                    newContent[newContentSize - compressedNodeIdx - 1] = newNode;
                    compressedNodeIdx += 1;
                    rightNodeIdx += 1;
                    leftNodeIdx += 1;
                    newSize += newNode.size();
                    newCachedHashCode += newNode.cachedJavaKeySetHashCode();

                } else if ((bitpos & leftDataRightNode) != 0) {
                    ChampMapNode<K, V> newNode;
                    {
                        ChampMapNode<K, V> n = bm.getNode(rightNodeIdx);
                        K leftKey = getKey(leftDataIdx);
                        V leftValue = getValue(leftDataIdx);
                        int leftOriginalHash = getHash(leftDataIdx);
                        int leftImproved = improve(leftOriginalHash);

                        var updated = n.updated(leftKey, leftValue, leftOriginalHash, leftImproved, nextShift, false);

                        if (updated != n) {
                            anyChangesMadeSoFar = true;
                        }

                        newNode = updated;
                    }

                    newContent[newContentSize - compressedNodeIdx - 1] = newNode;
                    compressedNodeIdx += 1;
                    rightNodeIdx += 1;
                    leftDataIdx += 1;
                    newSize += newNode.size();
                    newCachedHashCode += newNode.cachedJavaKeySetHashCode();
                } else if ((bitpos & leftNodeRightData) != 0) {
                    anyChangesMadeSoFar = true;
                    ChampMapNode<K, V> newNode;
                    {
                        int rightOriginalHash = bm.getHash(rightDataIdx);
                        newNode = getNode(leftNodeIdx).updated(
                                bm.getKey(rightDataIdx),
                                bm.getValue(rightDataIdx),
                                bm.getHash(rightDataIdx),
                                improve(rightOriginalHash),
                                shift = nextShift,
                                true
                        );
                    }

                    newContent[newContentSize - compressedNodeIdx - 1] = newNode;
                    compressedNodeIdx += 1;
                    leftNodeIdx += 1;
                    rightDataIdx += 1;
                    newSize += newNode.size();
                    newCachedHashCode += newNode.cachedJavaKeySetHashCode();

                } else if ((bitpos & leftDataOnly) != 0) {
                    anyChangesMadeSoFar = true;
                    int originalHash = originalHashes[leftDataIdx];
                    newContent[ChampMapNode.TupleLength * compressedDataIdx] = getKey(leftDataIdx);
                    newContent[ChampMapNode.TupleLength * compressedDataIdx + 1] = getValue(leftDataIdx);
                    newOriginalHashes[compressedDataIdx] = originalHash;

                    compressedDataIdx += 1;
                    leftDataIdx += 1;
                    newSize += 1;
                    newCachedHashCode += improve(originalHash);
                } else if ((bitpos & rightDataOnly) != 0) {
                    int originalHash = bm.originalHashes[rightDataIdx];
                    newContent[ChampMapNode.TupleLength * compressedDataIdx] = bm.getKey(rightDataIdx);
                    newContent[ChampMapNode.TupleLength * compressedDataIdx + 1] = bm.getValue(rightDataIdx);
                    newOriginalHashes[compressedDataIdx] = originalHash;

                    compressedDataIdx += 1;
                    rightDataIdx += 1;
                    newSize += 1;
                    newCachedHashCode += improve(originalHash);
                } else if ((bitpos & leftNodeOnly) != 0) {
                    anyChangesMadeSoFar = true;
                    var newNode = getNode(leftNodeIdx);
                    newContent[newContentSize - compressedNodeIdx - 1] = newNode;
                    compressedNodeIdx += 1;
                    leftNodeIdx += 1;
                    newSize += newNode.size();
                    newCachedHashCode += newNode.cachedJavaKeySetHashCode();
                } else if ((bitpos & rightNodeOnly) != 0) {
                    var newNode = bm.getNode(rightNodeIdx);
                    newContent[newContentSize - compressedNodeIdx - 1] = newNode;
                    compressedNodeIdx += 1;
                    rightNodeIdx += 1;
                    newSize += newNode.size();
                    newCachedHashCode += newNode.cachedJavaKeySetHashCode();
                } else if ((bitpos & leftDataRightDataMigrateToNode) != 0) {
                    anyChangesMadeSoFar = true;
                    ChampMapNode<K, V> newNode;
                    {
                        int leftOriginalHash = getHash(leftDataIdx);
                        int rightOriginalHash = bm.getHash(rightDataIdx);

                        newNode = bm.mergeTwoKeyValPairs(
                                getKey(leftDataIdx), getValue(leftDataIdx), leftOriginalHash, improve(leftOriginalHash),
                                bm.getKey(rightDataIdx), bm.getValue(rightDataIdx), rightOriginalHash, improve(rightOriginalHash),
                                nextShift
                        );
                    }

                    newContent[newContentSize - compressedNodeIdx - 1] = newNode;
                    compressedNodeIdx += 1;
                    leftDataIdx += 1;
                    rightDataIdx += 1;
                    newSize += newNode.size();
                    newCachedHashCode += newNode.cachedJavaKeySetHashCode();
                } else if ((bitpos & leftDataRightDataRightOverwrites) != 0) {
                    int originalHash = bm.originalHashes[rightDataIdx];
                    newContent[ChampMapNode.TupleLength * compressedDataIdx] = bm.getKey(rightDataIdx);
                    newContent[ChampMapNode.TupleLength * compressedDataIdx + 1] = bm.getValue(rightDataIdx);
                    newOriginalHashes[compressedDataIdx] = originalHash;

                    compressedDataIdx += 1;
                    rightDataIdx += 1;
                    newSize += 1;
                    newCachedHashCode += improve(originalHash);
                    leftDataIdx += 1;
                }

                if (bitpos == maximumBitPos) {
                    finished = true;
                } else {
                    bitpos = bitpos << 1;
                }
            }
        }

        return anyChangesMadeSoFar ? new BitmapIndexedChampMapNode<K, V>(
                dataMap = newDataMap,
                nodeMap = newNodeMap,
                content = newContent,
                originalHashes = newOriginalHashes,
                size = newSize,
                cachedJavaKeySetHashCode = newCachedHashCode
        ) : bm;
    }

    @Override
    public BitmapIndexedChampMapNode<K, V> copy() {
        Object[] contentClone = content.clone();
        int contentLength = contentClone.length;
        int i = bitCount(dataMap) * TupleLength;
        while (i < contentLength) {
            contentClone[i] = ((ChampMapNode<K, V>) contentClone[i]).copy();
            i += 1;
        }
        return new BitmapIndexedChampMapNode<>(dataMap, nodeMap, contentClone, originalHashes.clone(), size, cachedJavaKeySetHashCode);
    }

    @Override
    public BitmapIndexedChampMapNode<K, V> filter(BiPredicate<K, V> predicate) {
        if (size == 0) return this;
        else if (size == 1) {
            Tuple2<K, V> payload = getPayload(0);
            if (predicate.test(payload.getKey(), payload.getValue()))
                return this;
            else return ChampMapNode.empty();
        } else if (nodeMap == 0) {
            // Performance optimization for nodes of depth 1:
            //
            // this node has no "node" children, all children are inlined data elems, therefor logic is significantly simpler
            // approach:
            //   * traverse the content array, accumulating in `newDataMap: Int` any bit positions of keys which pass the filter
            //   * (bitCount(newDataMap) * TupleLength) tells us the new content array and originalHashes array size, so now perform allocations
            //   * traverse the content array once more, placing each passing element (according to `newDatamap`) in the new content and originalHashes arrays
            //
            // note:
            //   * this optimization significantly improves performance of not only small trees, but also larger trees, since
            //     even non-root nodes are affected by this improvement, and large trees will consist of many nodes as
            //     descendants
            //
            int minimumIndex = Integer.numberOfTrailingZeros(dataMap);
            int maximumIndex = ChampNode.BranchingFactor - Integer.numberOfLeadingZeros(dataMap);

            int newDataMap = 0;
            int newCachedHashCode = 0;
            int dataIndex = 0;

            int i = minimumIndex;

            while(i < maximumIndex) {
                int bitpos = bitposFrom(i);

                if ((bitpos & dataMap) != 0) {
                    Tuple2<K, V> payload = getPayload(dataIndex);
                    boolean passed = predicate.test(payload.getKey(), payload.getValue());

                    if (passed) {
                        newDataMap |= bitpos;
                        newCachedHashCode += improve(getHash(dataIndex));
                    }

                    dataIndex += 1;
                }

                i += 1;
            }

            if (newDataMap == 0) {
                return ChampMapNode.empty();
            } else if (newDataMap == dataMap) {
                return this;
            } else {
                int newSize = Integer.bitCount(newDataMap);
                Object[] newContent = new Object[newSize * TupleLength];
                int[] newOriginalHashCodes = new int[newSize];
                        int newMaximumIndex = ChampNode.BranchingFactor - Integer.numberOfLeadingZeros(newDataMap);

                var j = Integer.numberOfTrailingZeros(newDataMap);

                var newDataIndex = 0;


                while (j < newMaximumIndex) {
                    int bitpos = bitposFrom(j);
                    if ((bitpos & newDataMap) != 0) {
                        int oldIndex = indexFrom(dataMap, bitpos);
                        newContent[newDataIndex * TupleLength] = content[oldIndex * TupleLength];
                        newContent[newDataIndex * TupleLength + 1] = content[oldIndex * TupleLength + 1];
                        newOriginalHashCodes[newDataIndex] = originalHashes[oldIndex];
                        newDataIndex += 1;
                    }
                    j += 1;
                }

                return new BitmapIndexedChampMapNode<>(newDataMap, 0, newContent, newOriginalHashCodes, newSize, newCachedHashCode);
            }
        } else {
            int allMap = dataMap | nodeMap;
            int minimumIndex = Integer.numberOfTrailingZeros(allMap);
            int maximumIndex = ChampNode.BranchingFactor - Integer.numberOfLeadingZeros(allMap);

            int oldDataPassThrough = 0;

            // bitmap of nodes which, when filtered, returned a single-element node. These must be migrated to data
            int nodeMigrateToDataTargetMap = 0;
            // the queue of single-element, post-filter nodes
            MutableQueue<ChampMapNode<K, V>> nodesToMigrateToData = null;

            // bitmap of all nodes which, when filtered, returned themselves. They are passed forward to the returned node
            int nodesToPassThroughMap = 0;

            // bitmap of any nodes which, after being filtered, returned a node that is not empty, but also not `eq` itself
            // These are stored for later inclusion into the final `content` array
            // not named `newNodesMap` (plural) to avoid confusion with `newNodeMap` (singular)
            int mapOfNewNodes = 0;
            // each bit in `mapOfNewNodes` corresponds to one element in this queue
            MutableQueue<ChampMapNode<K, V>> newNodes = null;

            int newDataMap = 0;
            int newNodeMap = 0;
            int newSize = 0;
            int newCachedHashCode = 0;

            int dataIndex = 0;
            int nodeIndex = 0;

            for (int i = minimumIndex; i < maximumIndex; i++) {
                int bitpos = bitposFrom(i);

                if ((bitpos & dataMap) != 0) {
                    var payload = getPayload(dataIndex);
                    boolean passed = predicate.test(payload.getKey(), payload.getValue());

                    if (passed) {
                        newDataMap |= bitpos;
                        oldDataPassThrough |= bitpos;
                        newSize += 1;
                        newCachedHashCode += improve(getHash(dataIndex));
                    }

                    dataIndex += 1;
                } else if ((bitpos & nodeMap) != 0) {
                    var oldSubNode = getNode(nodeIndex);
                    var newSubNode = oldSubNode.filter(predicate);

                    newSize += newSubNode.size();
                    newCachedHashCode += newSubNode.cachedJavaKeySetHashCode();

                    // if (newSubNode.size == 0) do nothing (drop it)
                    if (newSubNode.size() > 1) {
                        newNodeMap |= bitpos;
                        if (oldSubNode == newSubNode) {
                            nodesToPassThroughMap |= bitpos;
                        } else {
                            mapOfNewNodes |= bitpos;
                            if (newNodes == null) {
                                newNodes = MutableQueue.create();
                            }
                            newNodes.enqueue(newSubNode);
                        }
                    } else if (newSubNode.size() == 1) {
                        newDataMap |= bitpos;
                        nodeMigrateToDataTargetMap |= bitpos;
                        if (nodesToMigrateToData == null) {
                            nodesToMigrateToData = MutableQueue.create();
                        }
                        nodesToMigrateToData.enqueue(newSubNode);
                    }

                    nodeIndex += 1;
                }
            }

            if (newSize == 0) {
                return ChampMapNode.empty();
            } else if (newSize == size) {
                return this;
            } else {
                int newDataSize = bitCount(newDataMap);
                int newContentSize = (ChampMapNode.TupleLength * newDataSize) + bitCount(newNodeMap);
                var newContent = new Object[newContentSize];
                        var newOriginalHashes = new int[newDataSize];

                        int newAllMap = newDataMap | newNodeMap;
                int maxIndex = ChampNode.BranchingFactor - Integer.numberOfLeadingZeros(newAllMap);

                // note: We MUST start from the minimum index in the old (`this`) node, otherwise `old{Node,Data}Index` will
                // not be incremented properly. Otherwise we could have started at Integer.numberOfTrailingZeroes(newAllMap)
                int i = minimumIndex;

                int oldDataIndex = 0;
                int oldNodeIndex = 0;

                int newDataIndex = 0;
                int newNodeIndex = 0;

                while (i < maxIndex) {
                    int bitpos = bitposFrom(i);

                    if ((bitpos & oldDataPassThrough) != 0) {
                        newContent[newDataIndex * TupleLength] = getKey(oldDataIndex);
                        newContent[newDataIndex * TupleLength + 1] = getValue(oldDataIndex);
                        newOriginalHashes[newDataIndex] = getHash(oldDataIndex);
                        newDataIndex += 1;
                        oldDataIndex += 1;
                    } else if ((bitpos & nodesToPassThroughMap) != 0) {
                        newContent[newContentSize - newNodeIndex - 1] = getNode(oldNodeIndex);
                        newNodeIndex += 1;
                        oldNodeIndex += 1;
                    } else if ((bitpos & nodeMigrateToDataTargetMap) != 0) {
                        // we need not check for null here. If nodeMigrateToDataTargetMap != 0, then nodesMigrateToData must not be null
                        var node = nodesToMigrateToData.dequeue();
                        newContent[TupleLength * newDataIndex] = node.getKey(0);
                        newContent[TupleLength * newDataIndex + 1] = node.getValue(0);
                        newOriginalHashes[newDataIndex] = node.getHash(0);
                        newDataIndex += 1;
                        oldNodeIndex += 1;
                    } else if ((bitpos & mapOfNewNodes) != 0) {
                        newContent[newContentSize - newNodeIndex - 1] = newNodes.dequeue();
                        newNodeIndex += 1;
                        oldNodeIndex += 1;
                    } else if ((bitpos & dataMap) != 0) {
                        oldDataIndex += 1;
                    } else if ((bitpos & nodeMap) != 0) {
                        oldNodeIndex += 1;
                    }

                    i += 1;
                }

                return new BitmapIndexedChampMapNode<>(newDataMap, newNodeMap, newContent, newOriginalHashes, newSize, newCachedHashCode);
            }
        }
    }
}
