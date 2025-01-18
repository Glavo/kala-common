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

public abstract class ChampNode<T extends ChampNode<T>> {

    public static final int HashCodeLength = 32;
    public static final int BitPartitionSize = 5;
    public static final int BitPartitionMask = (1 << BitPartitionSize) - 1;
    public static final int MaxDepth = (int) Math.ceil((double) HashCodeLength / BitPartitionSize);
    public static final int BranchingFactor = 1 << BitPartitionSize;

    public static int improve(int hcode) {
        int h = hcode + ~(hcode << 9);
        h = h ^ (h >>> 14);
        h = h + (h << 4);
        return h ^ (h >>> 10);
    }

    public static int maskFrom(int hash, int shift) {
        return (hash >>> shift) & BitPartitionMask;
    }

    public static int bitposFrom(int mask) {
        return 1 << mask;
    }

    public static int indexFrom(int bitmap, int bitpos) {
        return Integer.bitCount(bitmap & (bitpos - 1));
    }

    public static int indexFrom(int bitmap, int mask, int bitpos) {
        return bitmap == -1 ? mask : indexFrom(bitmap, bitpos);
    }

    private static ArrayIndexOutOfBoundsException arrayIndexOutOfBounds(int index, int size) {
        return new ArrayIndexOutOfBoundsException("Index %d out of bounds for length %d".formatted(index, size));
    }

    protected static int[] removeElement(int[] as, int ix) {
        if (ix < 0 || ix >= as.length) {
            throw arrayIndexOutOfBounds(ix, as.length);
        }

        var result = new int[as.length - 1];
        System.arraycopy(as, 0, result, 0, ix);
        System.arraycopy(as, ix + 1, result, ix, as.length - ix - 1);
        return result;
    }

    protected static int[] insertElement(int[] as, int ix, int elem) {
        if (ix < 0 || ix > as.length) {
            throw arrayIndexOutOfBounds(ix, as.length);
        }
        final var result = new int[as.length + 1];
        System.arraycopy(as, 0, result, 0, ix);
        result[ix] = elem;
        System.arraycopy(as, ix, result, ix + 1, as.length - ix);
        return result;
    }

    protected static Object[] insertAnyElement(Object[] as, int ix, int elem) {
        if (ix < 0 || ix > as.length) {
            throw arrayIndexOutOfBounds(ix, as.length);
        }

        var result = new Object[as.length + 1];
        System.arraycopy(as, 0, result, 0, ix);
        result[ix] = elem;
        System.arraycopy(as, ix, result, ix + 1, as.length - ix);
        return result;
    }

    public abstract boolean hasNodes();

    public abstract int nodeArity();

    public abstract T getNode(int index);

    public abstract boolean hasPayload();

    public abstract int payloadArity();

    public abstract int getHash(int index);

    public abstract int cachedJavaKeySetHashCode();


}
