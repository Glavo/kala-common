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
import kala.collection.immutable.ImmutableChampMap;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Objects;

import static kala.collection.internal.champ.ChampMapNode.TupleLength;
import static kala.collection.internal.champ.ChampNode.BitPartitionSize;
import static kala.collection.internal.champ.ChampNode.bitposFrom;
import static kala.collection.internal.champ.ChampNode.improve;
import static kala.collection.internal.champ.ChampNode.indexFrom;
import static kala.collection.internal.champ.ChampNode.maskFrom;

@SuppressWarnings("unchecked")
public final class ChampMapBuilder<K, V> {

    private ImmutableChampMap<K, V> aliased;
    private BitmapIndexedChampMapNode<K, V> rootNode = newEmptyRootNode();

    private BitmapIndexedChampMapNode<K, V> newEmptyRootNode() {
        return new BitmapIndexedChampMapNode<>(0, 0, ObjectArrays.EMPTY, IntArrays.EMPTY, 0, 0);
    }

    private boolean isAliased() {
        return aliased != null;
    }

    private int[] insertElement(int[] as, int ix, int elem) {
        if (ix < 0 || ix > as.length) {
            throw new ArrayIndexOutOfBoundsException(ix);
        }

        int[] result = new int[as.length + 1];
        System.arraycopy(as, 0, result, 0, ix);
        result[ix] = elem;
        System.arraycopy(as, ix, result, ix + 1, as.length - ix);
        return result;
    }


    private void insertValue(BitmapIndexedChampMapNode<K, V> bm, int bitpos, K key, int originalHash, int keyHash, V value) {
        int dataIx = bm.dataIndex(bitpos);
        int idx = TupleLength * dataIx;

        var src = bm.content;
        var dst = new Object[src.length + TupleLength];

        // copy 'src' and insert 2 element(s) at position 'idx'
        System.arraycopy(src, 0, dst, 0, idx);
        dst[idx] = key;
        dst[idx + 1] = value;
        System.arraycopy(src, idx, dst, idx + TupleLength, src.length - idx);

        var dstHashes = insertElement(bm.originalHashes, dataIx, originalHash);

        bm.dataMap |= bitpos;
        bm.content = dst;
        bm.originalHashes = dstHashes;
        bm.size += 1;
        bm.cachedJavaKeySetHashCode += keyHash;
    }

    private void update(ChampMapNode<K, V> mapNode, K key, V value, int originalHash, int keyHash, int shift) {
        if (mapNode instanceof BitmapIndexedChampMapNode<K, V> bm) {
            int mask = maskFrom(keyHash, shift);
            int bitpos = bitposFrom(mask);
            if ((bm.dataMap & bitpos) != 0) {
                int index = indexFrom(bm.dataMap, mask, bitpos);
                K key0 = bm.getKey(index);
                int key0UnimprovedHash = bm.getHash(index);

                if (key0UnimprovedHash == originalHash && Objects.equals(key0, key)) {
                    bm.content[TupleLength * index + 1] = value;
                } else {
                    V value0 = bm.getValue(index);
                    int key0Hash = improve(key0UnimprovedHash);

                    ChampMapNode<K, V> subNodeNew =
                            bm.mergeTwoKeyValPairs(key0, value0, key0UnimprovedHash, key0Hash, key, value, originalHash, keyHash, shift + BitPartitionSize);

                    bm.migrateFromInlineToNodeInPlace(bitpos, key0Hash, subNodeNew);
                }

            } else if ((bm.nodeMap & bitpos) != 0) {
                int index = indexFrom(bm.nodeMap, mask, bitpos);
                var subNode = bm.getNode(index);
                int beforeSize = subNode.size();
                int beforeHash = subNode.cachedJavaKeySetHashCode();
                update(subNode, key, value, originalHash, keyHash, shift + BitPartitionSize);
                bm.size += subNode.size() - beforeSize;
                bm.cachedJavaKeySetHashCode += subNode.cachedJavaKeySetHashCode() - beforeHash;
            } else {
                insertValue(bm, bitpos, key, originalHash, keyHash, value);
            }
        } else if (mapNode instanceof HashCollisionChampMapNode<K, V> hc) {
            int index = hc.indexOf(key);
            if (index < 0) {
                hc.content = hc.content.appended(Tuple.of(key, value));
            } else {
                hc.content = hc.content.updated(index, Tuple.of(key, value));
            }
        }
    }

    private void ensureUnaliased() {
        if (isAliased()) {
            rootNode = rootNode.copy();
            aliased = null;
        }
    }

    private static final MethodHandle createImmutableChampHashMap;

    static {
        try {
            createImmutableChampHashMap = MethodHandles.privateLookupIn(ImmutableChampMap.class, MethodHandles.lookup())
                    .findConstructor(ImmutableChampMap.class, MethodType.methodType(void.class, BitmapIndexedChampMapNode.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public ImmutableChampMap<K, V> build() {
        if (rootNode.size == 0) {
            return ImmutableChampMap.empty();
        } else if (aliased != null) {
            return aliased;
        } else {
            try {
                aliased = (ImmutableChampMap<K, V>) createImmutableChampHashMap.invokeExact(rootNode);
            } catch (Throwable e) {
                throw new InternalError(e);
            }
            VarHandle.releaseFence();
            return aliased;
        }
    }

    public void add(final Tuple2<K, V> elem) {
        ensureUnaliased();
        int h = Objects.hashCode(elem.getKey());
        int im = improve(h);
        update(rootNode, elem.getKey(), elem.getValue(), h, im, 0);
    }

    public void add(K key, V value) {
        ensureUnaliased();
        int originalHash = Objects.hashCode(key);
        update(rootNode, key, value, originalHash, improve(originalHash), 0);
    }

    public void add(final K key, final V value, final int originalHash) {
        ensureUnaliased();
        update(rootNode, key, value, originalHash, improve(originalHash), 0);
    }

    public void add(final K key, final V value, final int originalHash, final int hash) {
        ensureUnaliased();
        update(rootNode, key, value, originalHash, hash, 0);
    }

    public void addAll(@NotNull ChampMapBuilder<K, V> other) {
        new ChampBaseIterator<Tuple2<K, V>, ChampMapNode<K, V>>(other.rootNode) {
            {
                while (hasNext()) {
                    int originalHash = currentValueNode.getHash(currentValueCursor);
                    ChampMapBuilder.this.update(
                            rootNode,
                            currentValueNode.getKey(currentValueCursor),
                            currentValueNode.getValue(currentValueCursor),
                            originalHash,
                            improve(originalHash),
                            0
                    );
                    currentValueCursor += 1;
                }
            }

            @Override
            public Tuple2<K, V> next() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
