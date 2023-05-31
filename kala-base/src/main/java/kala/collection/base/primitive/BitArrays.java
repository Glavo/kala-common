package kala.collection.base.primitive;

import kala.Conditions;
import kala.annotations.StaticClass;

import java.util.Objects;

@StaticClass
public final class BitArrays {
    private BitArrays() {
    }

    public static final int BITS_PRE_VALUE = Long.SIZE;
    public static final long FULL_BITS = 0xffff_ffff_ffff_ffffL;

    public static boolean get(long bits, int position) {
        return ((bits >> position) & 1) != 0;
    }

    public static long set(long bits, int position, boolean newValue) {
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

    public static BooleanIterator iterator(long[] bitsArray, int beginIndex, int endIndex) {
        Objects.requireNonNull(bitsArray);
        Conditions.checkPositionIndices(beginIndex, endIndex, bitsArray.length * BITS_PRE_VALUE);
        return new BitsArrayIterator(bitsArray, beginIndex, endIndex);
    }

    private static final class BitsArrayIterator extends AbstractBooleanIterator {
        private final int endIndex;
        private final long[] bitsArray;

        private long bits;
        private int index;

        BitsArrayIterator(long[] bitsArray, int index, int endIndex) {
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
