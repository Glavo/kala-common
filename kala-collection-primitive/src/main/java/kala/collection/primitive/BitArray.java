package kala.collection.primitive;

import kala.Conditions;
import kala.collection.base.primitive.BitArrays;
import kala.collection.base.primitive.BooleanIterator;
import kala.collection.base.primitive.LongArrays;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

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
        if (size <= Long.SIZE) {
            return BitArrays.get(bits, index);
        } else {
            return BitArrays.get(bitsArray, index);
        }
    }
}
