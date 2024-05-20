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
package kala.collection.primitive;

import kala.Conditions;
import kala.collection.base.primitive.BitArrays;
import kala.collection.base.primitive.BooleanIterator;
import kala.collection.base.primitive.LongArrays;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import static kala.collection.base.primitive.BitArrays.BITS_PRE_VALUE;

public class BitArray extends AbstractBooleanSeq implements IndexedBooleanSeq, Serializable {
    private static final long serialVersionUID = 4673372256434484L;

    private static final BitArray EMPTY = new BitArray(LongArrays.EMPTY, 0);

    //region Static Factories

    public static @NotNull BitArray wrap(long bits, int size) {
        if (size < 0 || size > Long.SIZE) {
            throw new IllegalArgumentException();
        }

        return new BitArray(bits, size);
    }

    public static @NotNull BitArray wrap(long[] bitArray, int size) {
        if (bitArray.length * Long.SIZE < size) {
            throw new IllegalArgumentException();
        }

        if (size <= Long.SIZE) {
            return new BitArray(bitArray[0], size);
        } else {
            return new BitArray(bitArray, size);
        }
    }


    public static @NotNull BitArray emtpy() {
        return EMPTY;
    }

    //endregion

    protected final long[] bitsArray;
    protected final long bits;
    protected final int size;

    protected BitArray(long[] bitsArray, int size) {
        this.bitsArray = bitsArray;
        this.bits = 0L;
        this.size = size;
    }

    protected BitArray(long bits, int size) {
        this.bitsArray = null;
        this.bits = bits;
        this.size = size;
    }

    @Override
    public @NotNull String className() {
        return "BitArray";
    }

    @Override
    public @NotNull BooleanIterator iterator() {
        return BitArrays.iterator(bitsArray, 0, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean get(int index) {
        Conditions.checkElementIndex(index, size);
        if (size <= BITS_PRE_VALUE) {
            return BitArrays.get(bits, index);
        } else {
            return BitArrays.get(bitsArray, index);
        }
    }

    @Override
    public boolean @NotNull [] toArray() {
        boolean[] res = new boolean[size];
        if (size <= BITS_PRE_VALUE) {
            for (int i = 0; i < size; i++) {
                res[i] = BitArrays.get(bits, i);
            }
        } else {
            final int fullChunkCount = size / Long.SIZE;
            final int notFullChunkLength = size % Long.SIZE;

            for (int i = 0; i < fullChunkCount; i++) {
                final long currentBits = bitsArray[i];
                final int baseIndex = i * BITS_PRE_VALUE;

                for (int j = 0; j < BITS_PRE_VALUE; j++) {
                    res[baseIndex + j] = BitArrays.get(currentBits, j);
                }
            }

            if (notFullChunkLength != 0) {
                final long currentBits = bitsArray[bitsArray.length - 1];
                final int baseIndex = (bitsArray.length - 1) * BITS_PRE_VALUE;
                for (int i = 0; i < notFullChunkLength; i++) {
                    res[baseIndex + i] = BitArrays.get(currentBits, i);
                }
            }
        }
        return res;
    }
}
