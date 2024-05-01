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
package kala.collection.base.primitive;

import kala.Conditions;
import kala.annotations.StaticClass;
import org.jetbrains.annotations.Range;

import java.util.Objects;

@StaticClass
public final class BitArrays {
    private BitArrays() {
    }

    public static final int BITS_PRE_VALUE = Long.SIZE;
    public static final long FULL_BITS = 0xffff_ffff_ffff_ffffL;

    public static boolean get(long bits, @Range(from = 0, to = BITS_PRE_VALUE - 1) int position) {
        return ((bits >> position) & 1) != 0;
    }

    public static long set(long bits, @Range(from = 0, to = BITS_PRE_VALUE - 1) int position, boolean newValue) {
        if (newValue) {
            return bits | (1L << position);
        } else {
            return bits & -(1L << position);
        }
    }

    public static boolean get(long[] bitsArray, int index) {
        return get(bitsArray[index / BITS_PRE_VALUE], index % BITS_PRE_VALUE);
    }

    public static void set(long[] bitsArray, int index, boolean newValue) {
        int i = index / BITS_PRE_VALUE;
        bitsArray[i] = set(bitsArray[i], index % BITS_PRE_VALUE, newValue);
    }

    public static BooleanIterator iterator(long bits, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, BITS_PRE_VALUE);
        return new Itr(bits, beginIndex, endIndex);
    }

    public static BooleanIterator iterator(long[] bitsArray, int beginIndex, int endIndex) {
        Objects.requireNonNull(bitsArray);
        Conditions.checkPositionIndices(beginIndex, endIndex, bitsArray.length * BITS_PRE_VALUE);
        return new LargeItr(bitsArray, beginIndex, endIndex);
    }

    private static final class Itr extends AbstractBooleanIterator {
        private final long bits;
        private final int endIndex;

        private int index;

        Itr(long bits, int index, int endIndex) {
            this.bits = bits;
            this.endIndex = endIndex;
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index < endIndex;
        }

        @Override
        public boolean nextBoolean() {
            checkStatus();
            return get(bits, index++);
        }
    }

    private static final class LargeItr extends AbstractBooleanIterator {
        private final int endIndex;
        private final long[] bitsArray;

        private long bits;
        private int index;

        LargeItr(long[] bitsArray, int index, int endIndex) {
            this.bitsArray = bitsArray;
            this.index = index;
            this.endIndex = endIndex;

            if (index % BITS_PRE_VALUE != 0)
                bits = bitsArray[index / BITS_PRE_VALUE];
        }

        @Override
        public boolean hasNext() {
            return index < endIndex;
        }

        @Override
        public boolean nextBoolean() {
            checkStatus();

            int position = index % BITS_PRE_VALUE;

            if (position == 0)
                bits = bitsArray[index / BITS_PRE_VALUE];

            index++;
            return get(bits, position);
        }
    }

}
