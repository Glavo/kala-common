package kala.collection.primitive;

import kala.Conditions;
import kala.collection.base.primitive.AbstractBooleanIterator;
import kala.collection.base.primitive.BooleanIterator;
import kala.collection.base.primitive.LongArrays;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class BitArray extends AbstractBooleanSeq implements IndexedBooleanSeq, Serializable {
    private static final long serialVersionUID = 4673372256434484L;

    //region Bit Array Utils

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
        return get(bitsArray[index / Long.SIZE], index % Long.SIZE);
    }

    public static void set(long[] bitsArray, int index, boolean newValue) {
        int i = index / Long.SIZE;
        bitsArray[i] = set(bitsArray[i], index % Long.SIZE, newValue);
    }

    public static BooleanIterator iterator(long[] bitsArray, int beginIndex, int endIndex) {
        Objects.requireNonNull(bitsArray);
        Conditions.checkPositionIndices(beginIndex, endIndex, bitsArray.length * Long.SIZE);
        return new Itr(bitsArray, beginIndex, endIndex);
    }

    //endregion

    private static final BitArray EMPTY = new BitArray(LongArrays.EMPTY, 0);

    //region Static Factories

    public static @NotNull BitArray wrap(long[] bitArray, int size) {
        if (bitArray.length * Long.SIZE < size) {
            throw new IllegalArgumentException();
        }

        return new BitArray(bitArray, size);
    }


    public static @NotNull BitArray emtpy() {
        return EMPTY;
    }

    //endregion

    protected final long[] bitArray;
    protected final int size;

    protected BitArray(long[] bitArray, int size) {
        this.bitArray = bitArray;
        this.size = size;
    }

    @Override
    public @NotNull String className() {
        return "BitArray";
    }

    @Override
    public @NotNull BooleanIterator iterator() {
        return new Itr(bitArray, 0, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean get(int index) {
        Conditions.checkElementIndex(index, size);
        return get(bitArray, index);
    }

    private static final class Itr extends AbstractBooleanIterator {
        private final int endIndex;
        private final long[] bitsArray;

        private long bits;
        private int index;

        Itr(long[] bitsArray, int index, int endIndex) {
            this.bitsArray = bitsArray;
            this.index = index;
            this.endIndex = endIndex;

            if (index % Long.SIZE != 0) {
                bits = bitsArray[index / Long.SIZE];
            }
        }

        @Override
        public boolean hasNext() {
            return index < endIndex;
        }

        @Override
        public boolean nextBoolean() {
            checkStatus();

            int position = index % Long.SIZE;

            if (position == 0)
                bits = bitsArray[index / Long.SIZE];

            index++;
            return get(bits, position);
        }
    }
}
