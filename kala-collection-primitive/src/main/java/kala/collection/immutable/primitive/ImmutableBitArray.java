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
package kala.collection.immutable.primitive;

import kala.NotImplementedError;
import kala.collection.base.primitive.BitArrays;
import kala.collection.base.primitive.BooleanIterator;
import kala.collection.base.primitive.BooleanTraversable;
import kala.collection.factory.primitive.BooleanCollectionFactory;
import kala.collection.primitive.BitArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

import static kala.collection.base.primitive.BitArrays.BITS_PRE_VALUE;
import static kala.collection.base.primitive.BitArrays.FULL_BITS;

final class ImmutableBitArray extends BitArray implements ImmutableBooleanSeq {

    private static final ImmutableBitArray EMPTY = new ImmutableBitArray(0, 0);

    private ImmutableBitArray(long[] bitsArray, int size) {
        super(bitsArray, size);
    }

    private ImmutableBitArray(long bits, int size) {
        super(bits, size);
    }

    //region Static Factories

    public static @NotNull BooleanCollectionFactory<?, ? extends ImmutableBitArray> factory() {
        throw new NotImplementedError();
    }

    public static @NotNull ImmutableBitArray empty() {
        return EMPTY;
    }

    public static @NotNull ImmutableBitArray of() {
        return empty();
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ImmutableBitArray of(boolean value1) {
        long bits = 0L;
        bits = BitArrays.set(bits, 0, value1);
        return new ImmutableBitArray(bits, 1);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull ImmutableBitArray of(boolean value1, boolean value2) {
        long bits = 0L;
        bits = BitArrays.set(bits, 0, value1);
        bits = BitArrays.set(bits, 1, value2);
        return new ImmutableBitArray(bits, 2);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull ImmutableBitArray of(boolean value1, boolean value2, boolean value3) {
        long bits = 0L;
        bits = BitArrays.set(bits, 0, value1);
        bits = BitArrays.set(bits, 1, value2);
        bits = BitArrays.set(bits, 2, value3);
        return new ImmutableBitArray(bits, 3);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull ImmutableBitArray of(boolean value1, boolean value2, boolean value3, boolean value4) {
        long bits = 0L;
        bits = BitArrays.set(bits, 0, value1);
        bits = BitArrays.set(bits, 1, value2);
        bits = BitArrays.set(bits, 2, value3);
        bits = BitArrays.set(bits, 3, value4);
        return new ImmutableBitArray(bits, 4);
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static @NotNull ImmutableBitArray of(boolean value1, boolean value2, boolean value3, boolean value4, boolean value5) {
        long bits = 0L;
        bits = BitArrays.set(bits, 0, value1);
        bits = BitArrays.set(bits, 1, value2);
        bits = BitArrays.set(bits, 2, value3);
        bits = BitArrays.set(bits, 3, value4);
        bits = BitArrays.set(bits, 4, value5);
        return new ImmutableBitArray(bits, 5);
    }

    @Contract(pure = true)
    public static @NotNull ImmutableBitArray of(boolean... values) {
        return from(values);
    }

    @Contract(pure = true)
    public static @NotNull ImmutableBitArray from(boolean @NotNull [] values) {
        final int size = values.length; // implicit null check of values

        if (size == 0) {
            return empty();
        }

        if (size <= BITS_PRE_VALUE) {
            long bits = 0L;
            for (int i = 0; i < size; i++) {
                bits = BitArrays.set(bits, i, values[i]);
            }
            return new ImmutableBitArray(bits, size);
        }

        final int fullChunkCount = size / BITS_PRE_VALUE;
        final int notFullChunkLength = size % BITS_PRE_VALUE;
        final long[] bitsArray = new long[notFullChunkLength > 0 ? fullChunkCount + 1 : fullChunkCount];

        for (int i = 0; i < fullChunkCount; i++) {
            final int baseIndex = i * BITS_PRE_VALUE;
            long bits = 0L;
            for (int j = 0; j < BITS_PRE_VALUE; j++) {
                bits = BitArrays.set(bits, j, values[baseIndex + j]);
            }
            bitsArray[i] = bits;
        }

        if (notFullChunkLength > 0) {
            final int baseIndex = fullChunkCount * BITS_PRE_VALUE;
            long bits = 0L;
            for (int i = 0; i < notFullChunkLength; i++) {
                bits = BitArrays.set(bits, i, values[baseIndex + i]);
            }
            bitsArray[fullChunkCount] = bits;
        }

        return new ImmutableBitArray(bitsArray, size);
    }

    public static @NotNull ImmutableBitArray from(@NotNull BooleanTraversable values) {
        if (values instanceof ImmutableBitArray ba) {
            return ba;
        }

        if (values.isEmpty()) { // implicit null check of values
            return empty();
        }

        throw new NotImplementedError();
    }

    public static @NotNull ImmutableBitArray from(@NotNull BooleanIterator it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        throw new NotImplementedError();
    }

    public static @NotNull ImmutableBitArray fill(int n, boolean value) {
        if (n <= 0) {
            return empty();
        }

        if (n <= BITS_PRE_VALUE) {
            if (value) {
                long bits = 0L;
                for (int i = 0; i < n; i++) {
                    bits = BitArrays.set(bits, i, true);
                }
                return new ImmutableBitArray(bits, n);
            } else {
                return new ImmutableBitArray(0L, n);
            }
        }

        final int fullChunkCount = n / BITS_PRE_VALUE;
        final int notFullChunkLength = n % BITS_PRE_VALUE;
        final long[] bitsArray = new long[notFullChunkLength > 0 ? fullChunkCount + 1 : fullChunkCount];

        if (value) {
            Arrays.fill(bitsArray, FULL_BITS);
        }

        return new ImmutableBitArray(bitsArray, n);
    }

    public static @NotNull ImmutableBitArray fill(int n, @NotNull BooleanSupplier supplier) {
        if (n <= 0) {
            return empty();
        }

        throw new NotImplementedError();
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "ImmutableBitArray";
    }
}
