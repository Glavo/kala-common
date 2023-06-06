package kala.collection.mutable.primitive;

import kala.Conditions;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.BooleanCollectionFactory;
import kala.collection.immutable.primitive.ImmutableBooleanArray;
import kala.collection.primitive.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.function.*;

public final class MutableBooleanArrayList extends AbstractMutableBooleanList implements IndexedBooleanSeq, Serializable {
    private static final long serialVersionUID = 2545219250020890853L;

    private static final Factory FACTORY = new Factory();

    private static final int DEFAULT_ARRAY_CAPACITY = 8;

    //region Fields

    long bits;
    long[] bitsArray;
    int size;

    //endregion

    //region Constructors

    private MutableBooleanArrayList(long bits, int size) {
        this.bits = bits;
        this.size = size;
    }

    private MutableBooleanArrayList(long[] bitsArray, int size) {
        this.bitsArray = bitsArray;
        this.size = size;
    }

    public MutableBooleanArrayList() {
        this.bits = 0L;
        this.bitsArray = null;
        this.size = 0;
    }

    public MutableBooleanArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("illegal initialCapacity: " + initialCapacity);
        }

        this.bitsArray = initialCapacity <= Long.SIZE ? null : new long[divRoundUp(initialCapacity, Long.SIZE)];
        this.size = 0;
    }

    //endregion

    //region Static Factories

    public static @NotNull BooleanCollectionFactory<?, MutableBooleanArrayList> factory() {
        return FACTORY;
    }

    @Contract("-> new")
    public static @NotNull MutableBooleanArrayList create() {
        return new MutableBooleanArrayList();
    }

    @Contract("_ -> new")
    public static @NotNull MutableBooleanArrayList create(int initialCapacity) {
        return new MutableBooleanArrayList(initialCapacity);
    }

    @Contract("-> new")
    public static @NotNull MutableBooleanArrayList of() {
        return new MutableBooleanArrayList();
    }

    @Contract("_ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean value1) {
        return new MutableBooleanArrayList(value1 ? 1L : 0L, 1);
    }

    @Contract("_, _ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean value1, boolean value2) {
        long bits = 0L;
        if (value1) bits |= (1L << 0);
        if (value2) bits |= (1L << 1);
        return new MutableBooleanArrayList(bits, 2);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean value1, boolean value2, boolean value3) {
        long bits = 0L;
        if (value1) bits |= (1L << 0);
        if (value2) bits |= (1L << 1);
        if (value3) bits |= (1L << 2);
        return new MutableBooleanArrayList(bits, 3);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean value1, boolean value2, boolean value3, boolean value4) {
        long bits = 0L;
        if (value1) bits |= (1L << 0);
        if (value2) bits |= (1L << 1);
        if (value3) bits |= (1L << 2);
        if (value4) bits |= (1L << 3);
        return new MutableBooleanArrayList(bits, 4);
    }

    @Contract("_, _, _, _, _ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean value1, boolean value2, boolean value3, boolean value4, boolean value5) {
        long bits = 0L;
        if (value1) bits |= (1L << 0);
        if (value2) bits |= (1L << 1);
        if (value3) bits |= (1L << 2);
        if (value4) bits |= (1L << 3);
        if (value5) bits |= (1L << 4);
        return new MutableBooleanArrayList(bits, 5);
    }

    @Contract("_ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static @NotNull MutableBooleanArrayList from(boolean @NotNull [] values) {
        final int size = values.length; // implicit null check of values
        if (size <= Long.SIZE) {
            long bits = 0L;
            for (int i = 0; i < size; i++) {
                if (values[i]) bits |= (1L << i);
            }
            return new MutableBooleanArrayList(bits, size);
        } else {
            final int fullChunkCount = size / Long.SIZE;
            final int notFullChunkLength = size % Long.SIZE;
            final boolean hasNotFullChunk = notFullChunkLength != 0;
            final int chunkCount = fullChunkCount + (hasNotFullChunk ? 1 : 0);

            long[] bitsArray = new long[chunkCount];

            int valueIndex = 0;
            for (int i = 0; i < fullChunkCount; i++) {
                long chunkBits = 0L;

                for (int j = 0; j < Long.SIZE; j++) {
                    if (values[valueIndex++]) chunkBits |= (1L << j);
                }

                bitsArray[i] = chunkBits;
            }

            if (hasNotFullChunk) {
                long chunkBits = 0L;
                for (int i = 0; i < notFullChunkLength; i++) {
                    if (values[valueIndex++]) chunkBits |= (1L << i);
                }
                bitsArray[fullChunkCount] = chunkBits;
            }

            return new MutableBooleanArrayList(bitsArray, size);
        }
    }

    public static @NotNull MutableBooleanArrayList from(@NotNull BooleanTraversable values) {
        MutableBooleanArrayList list = new MutableBooleanArrayList();
        list.appendAll(values);
        return list;
    }

    public static @NotNull MutableBooleanArrayList from(@NotNull BooleanIterator it) {
        MutableBooleanArrayList list = new MutableBooleanArrayList();
        while (it.hasNext()) {
            list.append(it.nextBoolean());
        }
        return list;
    }

    public static @NotNull MutableBooleanArrayList fill(int n, boolean value) {
        if (n <= 0) {
            return new MutableBooleanArrayList();
        }

        if (n <= Long.SIZE) {
            long bits = 0L;
            if (!value) {
                for (int i = 0; i < n; i++) {
                    bits |= (1L << i);
                }
            }
            return new MutableBooleanArrayList(bits, n);
        } else {

        }
    }

    public static @NotNull MutableBooleanArrayList fill(int n, @NotNull BooleanSupplier supplier) {
        if (n <= 0) {
            return new MutableBooleanArrayList();
        }

        boolean[] arr = new boolean[Integer.max(DEFAULT_ARRAY_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = supplier.getAsBoolean();
        }
        return new MutableBooleanArrayList(arr, n);
    }

    //endregion

    //region Internal

    private static int divRoundUp(int x, int y) {
        return (x + y - 1) / y;
    }

    private void grow() {
        grow(size + 1);
    }

    private void grow(int minCapacity) {
        boolean[] newArray = growArray(minCapacity);
        if (bitsArray.length != 0) {
            System.arraycopy(bitsArray, 0, newArray, 0, size);
        }
        bitsArray = newArray;
    }

    private boolean[] growArray(int minCapacity) {
        int oldCapacity = bitsArray.length;
        if (oldCapacity == 0) {
            return new boolean[Math.max(DEFAULT_ARRAY_CAPACITY, minCapacity)];
        }

        int newCapacity = Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
        return new boolean[newCapacity];
    }

    private void checkInBound(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }

    private void reduceToSize(int newSize) {
        this.size = newSize;
    }

    public void sizeHint(int s) {
        int len = bitsArray.length;
        int size = this.size;

        if (s > 0 && s + size > len) {
            grow(size + s);
        }
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "MutableBooleanArrayList";
    }

    @Override
    public @NotNull BooleanCollectionFactory<?, MutableBooleanArrayList> iterableFactory() {
        return factory();
    }

    @Override
    public @NotNull BooleanIterator iterator() {
        return BitArray.iterator(bitsArray, 0, size);
    }

    @Override
    public @NotNull BooleanIterator iterator(int beginIndex) {
        return BitArray.iterator(bitsArray, beginIndex, size);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public MutableBooleanArrayList clone() {
        return size <= Long.SIZE ? new MutableBooleanArrayList(bits, size) : new MutableBooleanArrayList(bitsArray.clone(), size);
    }

    //region Size Info

    @Override
    public int size() {
        return size;
    }

    //endregion

    //region Positional Access Operations

    @Override
    public boolean get(int index) {
        Conditions.checkElementIndex(index, size);
        if (size <= Long.SIZE) {
            return BitArray.get(bits, index);
        } else {
            return BitArray.get(bitsArray, index);
        }
    }

    @Override
    public void set(int index, boolean newValue) {
        Conditions.checkElementIndex(index, size);
        if (size <= Long.SIZE) {
            bits = BitArray.set(bits, index, newValue);
        } else {
            BitArray.set(bitsArray, index, newValue);
        }
    }

    //endregion

    //region Modification Operations

    @Override
    public void prepend(boolean value) {
        if (size < Long.SIZE) {
            bits <<= 1;
            if (value) bits |= (1L << 0);
        } else if (size == Long.SIZE) {
            long bits0 = (value ? 1L : 0L) | (bits << 1);
            long bits1 = ((bits >> (Long.SIZE - 1)) & 1) != 0 ? 1L : 0L;

            if (bitsArray == null || bitsArray.length < 2) {
                bitsArray = new long[DEFAULT_ARRAY_CAPACITY];
            }

            bitsArray[0] = bits0;
            bitsArray[1] = bits1;

            bits = 0L;
        } else { // size > Long.SIZE

        }
        size++;
    }

    @Override
    public void prependAll(@NotNull BooleanTraversable values) {
        Objects.requireNonNull(values);
        if (values == this) {
            appendThis();
            return;
        }

        final int size = this.size;
        if (values instanceof BooleanSeqLike && ((BooleanSeqLike) values).supportsFastRandomAccess()) {
            BooleanSeqLike seq = (BooleanSeqLike) values;
            int s = seq.size();
            if (s == 0) {
                return;
            }
            boolean[] arr = this.bitsArray;
            if (arr.length < size + s) {
                arr = growArray(size + s);
            }
            System.arraycopy(this.bitsArray, 0, arr, s, size);
            for (int i = 0; i < s; i++) {
                arr[i] = seq.get(i);
            }
            this.bitsArray = arr;
            this.size += s;
            return;
        }

        boolean[] cv = values.toArray();
        if (cv.length == 0) {
            return;
        }

        boolean[] elements = this.bitsArray;
        if (elements.length < size + cv.length) {
            elements = growArray(size + cv.length);
        }

        System.arraycopy(this.bitsArray, 0, elements, cv.length, size);
        System.arraycopy(cv, 0, elements, 0, cv.length);
        this.bitsArray = elements;
        this.size += cv.length;
    }

    @Override
    public void append(boolean value) {
        if (size == bitsArray.length) {
            grow();
        }
        bitsArray[size++] = value;
    }

    @Override
    public void appendAll(@NotNull BooleanTraversable values) {
        Objects.requireNonNull(values);

        if (values == this) {
            appendThis();
            return;
        }

        int knowSize = values.knownSize();
        if (knowSize > 0 && size + knowSize > bitsArray.length) {
            grow(size + knowSize);
        }

        values.forEach(this::append);
    }

    private void appendThis() {
        final int size = this.size;
        if (size == 0) {
            return;
        }

        if (size > Integer.MAX_VALUE / 2) {
            throw new OutOfMemoryError("Requested array size exceeds VM limit");
        }

        final int newSize = size * 2;

        if (bitsArray.length < newSize) {
            grow(newSize);
        }

        final boolean[] elements = this.bitsArray;
        System.arraycopy(elements, 0, elements, size, size);
        this.size = newSize;
    }

    //endregion

    @Override
    public void sort() {
        BooleanArrays.sort(bitsArray, 0, size);
    }

    @Override
    public void insert(int index, boolean value) {
        Conditions.checkPositionIndex(index, size);

        if (index == size) {
            append(value);
            return;
        }
        if (bitsArray.length == size) {
            grow();
        }

        System.arraycopy(bitsArray, index, bitsArray, index + 1, size - index);
        bitsArray[index] = value;
        ++this.size;
    }

    @Override
    public void insertAll(int index, @NotNull BooleanTraversable values) {
        Objects.requireNonNull(values);
        Conditions.checkPositionIndex(index, size);

        final int otherSize = values.size();

        boolean[] elements = this.bitsArray;
        if (elements.length < size + otherSize || values == this) {
            elements = growArray(size + otherSize);
        }
        System.arraycopy(this.bitsArray, 0, elements, 0, index);
        System.arraycopy(this.bitsArray, index, elements, index + otherSize, size - index);

        final BooleanIterator it = values.iterator();
        for (int i = 0; i < otherSize; i++) {
            elements[i + index] = it.nextBoolean();
        }
        assert !it.hasNext();

        this.bitsArray = elements;
        size += otherSize;
    }

    @Override
    public void insertAll(int index, boolean @NotNull [] values) {
        Objects.requireNonNull(values);
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
        if (values.length == 0) {
            return;
        }

        boolean[] elements = this.bitsArray;
        if (elements.length < size + values.length) {
            elements = growArray(size + values.length);
        }
        System.arraycopy(this.bitsArray, 0, elements, 0, index);
        System.arraycopy(values, 0, elements, index, values.length);
        System.arraycopy(this.bitsArray, index, elements, index + values.length, size - index);

        this.bitsArray = elements;
        size += values.length;
    }

    public void trimToSize() {
        if (size <= Long.SIZE) {
            bitsArray = null;
        } else {
            int newLength = divRoundUp(size, Long.SIZE);
            if (bitsArray.length > newLength) {
                bitsArray = Arrays.copyOf(bitsArray, newLength);
            }
        }
    }

    @Override
    public void clear() {
        bits = 0L;
        bitsArray = null;
        size = 0;
    }

    @Override
    public boolean removeAt(int index) {
        Conditions.checkElementIndex(index, size);
        boolean oldValue = bitsArray[index];
        int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(bitsArray, index + 1, bitsArray, index, newSize - index);
        }
        bitsArray[newSize] = false;
        size = newSize;
        return oldValue;
    }

    @Override
    public void removeInRange(int beginIndex, int endIndex) {
        int size = this.size();
        Conditions.checkPositionIndices(beginIndex, endIndex, size);

        int rangeLength = endIndex - beginIndex;

        if (rangeLength == 0) {
            return;
        }

        if (rangeLength == size) {
            clear();
            return;
        }

        if (rangeLength == 1) {
            removeAt(beginIndex);
            return;
        }

        int tailElementsCount = size - endIndex;
        System.arraycopy(bitsArray, endIndex, bitsArray, beginIndex, tailElementsCount);
        /*
        if (tailElementsCount < rangeLength) {
            Arrays.fill(elements, beginIndex + tailElementsCount, beginIndex + rangeLength, 0);
        }
         */

        this.size = size - rangeLength;
    }

    @Override
    public void dropInPlace(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return;
        }
        if (n >= size) {
            clear();
            return;
        }
        final int newSize = size - n;
        System.arraycopy(bitsArray, n, bitsArray, 0, newSize);
        reduceToSize(newSize);
    }

    @Override
    public void takeInPlace(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            clear();
            return;
        }
        if (n >= size) {
            return;
        }
        // Arrays.fill(elements, n, elements.length, null);
        size = n;
    }

    @Override
    public int copyToArray(int srcPos, boolean @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        final int destLength = dest.length;
        final int size = size();

        if (destPos >= destLength || srcPos >= size) {
            return 0;
        }

        final int n = Math.min(Math.min(size - srcPos, destLength - destPos), limit);
        System.arraycopy(bitsArray, srcPos, dest, destPos, n);
        return n;
    }

    @Override
    public boolean @NotNull [] toArray() {
        boolean[] res = new boolean[size];
        if (size <= Long.SIZE) {
            for (int i = 0; i < size; i++) {
                res[i] = BitArrays.get(bits, i);
            }
        } else {
            // TODO
        }
        return res;
    }

    @Override
    public @NotNull ImmutableBooleanArray toImmutableArray() {
        return size == 0 ? ImmutableBooleanArray.empty() : ImmutableBooleanArray.Unsafe.wrap(toArray());
    }

    //region Serialization

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(size);
        this.forEachChecked(out::writeBoolean);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        final int size = in.readInt();
        if (size < 0) {
            throw new InvalidObjectException("invalid size " + size);
        }

        long bits = 0L;
        long[] bitsArray = null;

        if (size <= Long.SIZE) {
            for (int i = 0; i < size; i++) {
                if (in.readBoolean()) bits |= (1L << i);
            }
        } else {
            final int fullChunkCount = size / Long.SIZE;
            final int notFullChunkLength = size % Long.SIZE;
            final boolean hasNotFullChunk = notFullChunkLength != 0;
            final int chunkCount = fullChunkCount + (hasNotFullChunk ? 1 : 0);

            bitsArray = new long[chunkCount];

            for (int i = 0; i < fullChunkCount; i++) {
                long chunkBits = 0L;

                for (int j = 0; j < Long.SIZE; j++) {
                    if (in.readBoolean()) chunkBits |= (1L << j);
                }

                bitsArray[i] = chunkBits;
            }

            if (hasNotFullChunk) {
                long chunkBits = 0L;
                for (int i = 0; i < notFullChunkLength; i++) {
                    if (in.readBoolean()) chunkBits |= (1L << i);
                }
                bitsArray[fullChunkCount] = chunkBits;
            }
        }

        this.size = size;
        this.bits = bits;
        this.bitsArray = bitsArray;
    }

    //endregion

    private static final class Factory extends AbstractMutableBooleanListFactory<MutableBooleanArrayList> {
        @Override
        public MutableBooleanArrayList newBuilder() {
            return new MutableBooleanArrayList();
        }

        @Override
        public void sizeHint(@NotNull MutableBooleanArrayList buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public MutableBooleanArrayList from(boolean @NotNull [] values) {
            return MutableBooleanArrayList.from(values);
        }

        @Override
        public MutableBooleanArrayList from(@NotNull BooleanTraversable values) {
            return MutableBooleanArrayList.from(values);
        }

        @Override
        public MutableBooleanArrayList from(@NotNull BooleanIterator it) {
            return MutableBooleanArrayList.from(it);
        }

        @Override
        public MutableBooleanArrayList fill(int n, boolean value) {
            return MutableBooleanArrayList.fill(n, value);
        }

        @Override
        public MutableBooleanArrayList fill(int n, @NotNull BooleanSupplier supplier) {
            return MutableBooleanArrayList.fill(n, supplier);
        }

    }
}
