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
package kala.collection.mutable.primitive;

import kala.Conditions;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.BooleanCollectionFactory;
import kala.collection.immutable.primitive.ImmutableBooleanArray;
import kala.collection.primitive.*;
import kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;

public final class MutableBooleanArrayList extends AbstractMutableBooleanList implements IndexedBooleanSeq, Serializable {
    private static final long serialVersionUID = 2545219250020890853L;

    private static final Factory FACTORY = new Factory();

    static final int DEFAULT_CAPACITY = 10;
    static final boolean[] DEFAULT_EMPTY_ARRAY = new boolean[0];

    //region Fields

    boolean @NotNull [] elements;
    int size;

    //endregion

    //region Constructors

    private MutableBooleanArrayList(boolean @NotNull [] elements, int size) {
        this.elements = elements;
        this.size = size;
    }

    public MutableBooleanArrayList() {
        this(DEFAULT_EMPTY_ARRAY, 0);
    }

    public MutableBooleanArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("illegal initialCapacity: " + initialCapacity);
        }

        this.elements = initialCapacity == 0 ? BooleanArrays.EMPTY : new boolean[initialCapacity];
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
        boolean[] arr = new boolean[DEFAULT_CAPACITY];
        arr[0] = value1;
        return new MutableBooleanArrayList(arr, 1);
    }

    @Contract("_, _ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean value1, boolean value2) {
        boolean[] arr = new boolean[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        return new MutableBooleanArrayList(arr, 2);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean value1, boolean value2, boolean value3) {
        boolean[] arr = new boolean[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        return new MutableBooleanArrayList(arr, 3);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean value1, boolean value2, boolean value3, boolean value4) {
        boolean[] arr = new boolean[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        return new MutableBooleanArrayList(arr, 4);
    }

    @Contract("_, _, _, _, _ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean value1, boolean value2, boolean value3, boolean value4, boolean value5) {
        boolean[] arr = new boolean[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        arr[4] = value5;
        return new MutableBooleanArrayList(arr, 5);
    }

    @Contract("_ -> new")
    public static @NotNull MutableBooleanArrayList of(boolean... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static @NotNull MutableBooleanArrayList from(boolean @NotNull [] values) {
        int length = values.length; // implicit null check of values
        return length != 0
                ? new MutableBooleanArrayList(Arrays.copyOf(values, Integer.max(DEFAULT_CAPACITY, values.length)), length)
                : new MutableBooleanArrayList();
    }

    public static @NotNull MutableBooleanArrayList from(@NotNull BooleanTraversable values) {
        MutableBooleanArrayList buffer = new MutableBooleanArrayList();
        buffer.appendAll(values);
        return buffer;
    }

    public static @NotNull MutableBooleanArrayList from(@NotNull BooleanIterator it) {
        MutableBooleanArrayList buffer = new MutableBooleanArrayList();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer;
    }

    public static @NotNull MutableBooleanArrayList fill(int n, boolean value) {
        if (n <= 0) {
            return new MutableBooleanArrayList();
        }

        boolean[] arr = new boolean[Integer.max(DEFAULT_CAPACITY, n)];
        if (value != false) {
            Arrays.fill(arr, 0, n, value);
        }
        return new MutableBooleanArrayList(arr, n);
    }

    public static @NotNull MutableBooleanArrayList fill(int n, @NotNull BooleanSupplier supplier) {
        if (n <= 0) {
            return new MutableBooleanArrayList();
        }

        boolean[] arr = new boolean[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = supplier.getAsBoolean();
        }
        return new MutableBooleanArrayList(arr, n);
    }

    //endregion

    //region Internal

    private void grow() {
        grow(size + 1);
    }

    private void grow(int minCapacity) {
        boolean[] newArray = growArray(minCapacity);
        if (elements.length != 0) {
            System.arraycopy(elements, 0, newArray, 0, size);
        }
        elements = newArray;
    }

    private boolean[] growArray(int minCapacity) {
        int oldCapacity = elements.length;
        if (elements == DEFAULT_EMPTY_ARRAY && oldCapacity == 0) {
            return new boolean[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }

        int newCapacity = Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
        return new boolean[newCapacity];
    }

    private void checkInBound(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }

    boolean @NotNull [] getArray() {
        return this.elements;
    }

    private void reduceToSize(int newSize) {
        this.size = newSize;
    }

    public void sizeHint(int s) {
        int len = elements.length;
        int size = this.size;

        if (s > 0 && s + size > len) {
            grow(size + s);
        }
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "MutableBooleanArrayList" ;
    }

    @Override
    public @NotNull BooleanCollectionFactory<?, MutableBooleanArrayList> iterableFactory() {
        return factory();
    }

    @Override
    public @NotNull BooleanIterator iterator() {
        return BooleanArrays.iterator(elements, 0, size);
    }

    @Override
    public @NotNull BooleanIterator iterator(int beginIndex) {
        return BooleanArrays.iterator(elements, beginIndex, size);
    }

    @Override
    public @NotNull MutableBooleanListIterator seqIterator(int index) {
        Conditions.checkPositionIndex(index, size);
        return new SeqItr(this, index);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public MutableBooleanArrayList clone() {
        return new MutableBooleanArrayList(elements.length == 0 ? elements : elements.clone(), size);
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
        return elements[index];
    }

    @Override
    public void set(int index, boolean newValue) {
        Conditions.checkElementIndex(index, size);
        elements[index] = newValue;
    }

    //endregion

    //region Modification Operations

    @Override
    public void prepend(boolean value) {
        boolean[] values = elements;
        if (size == values.length) {
            values = growArray(size + 1);
        }
        System.arraycopy(elements, 0, values, 1, size);
        values[0] = value;
        this.elements = values;
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
            boolean[] arr = this.elements;
            if (arr.length < size + s) {
                arr = growArray(size + s);
            }
            System.arraycopy(this.elements, 0, arr, s, size);
            for (int i = 0; i < s; i++) {
                arr[i] = seq.get(i);
            }
            this.elements = arr;
            this.size += s;
            return;
        }

        boolean[] cv = values.toArray();
        if (cv.length == 0) {
            return;
        }

        boolean[] elements = this.elements;
        if (elements.length < size + cv.length) {
            elements = growArray(size + cv.length);
        }

        System.arraycopy(this.elements, 0, elements, cv.length, size);
        System.arraycopy(cv, 0, elements, 0, cv.length);
        this.elements = elements;
        this.size += cv.length;
    }

    @Override
    public void append(boolean value) {
        if (size == elements.length) {
            grow();
        }
        elements[size++] = value;
    }

    @Override
    public void appendAll(@NotNull BooleanTraversable values) {
        Objects.requireNonNull(values);

        if (values == this) {
            appendThis();
            return;
        }

        int knowSize = values.knownSize();
        if (knowSize > 0 && size + knowSize > elements.length) {
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

        if (elements.length < newSize) {
            grow(newSize);
        }

        final boolean[] elements = this.elements;
        System.arraycopy(elements, 0, elements, size, size);
        this.size = newSize;
    }

    //endregion

    @Override
    public void sort() {
        BooleanArrays.sort(elements, 0, size);
    }

    @Override
    public void insert(int index, boolean value) {
        Conditions.checkPositionIndex(index, size);

        if (index == size) {
            append(value);
            return;
        }
        if (elements.length == size) {
            grow();
        }

        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = value;
        ++this.size;
    }

    @Override
    public void insertAll(int index, @NotNull BooleanTraversable values) {
        Objects.requireNonNull(values);
        Conditions.checkPositionIndex(index, size);

        final int otherSize = values.size();

        boolean[] elements = this.elements;
        if (elements.length < size + otherSize || values == this) {
            elements = growArray(size + otherSize);
        }
        System.arraycopy(this.elements, 0, elements, 0, index);
        System.arraycopy(this.elements, index, elements, index + otherSize, size - index);

        final BooleanIterator it = values.iterator();
        for (int i = 0; i < otherSize; i++) {
            elements[i + index] = it.nextBoolean();
        }
        assert !it.hasNext();

        this.elements = elements;
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

        boolean[] elements = this.elements;
        if (elements.length < size + values.length) {
            elements = growArray(size + values.length);
        }
        System.arraycopy(this.elements, 0, elements, 0, index);
        System.arraycopy(values, 0, elements, index, values.length);
        System.arraycopy(this.elements, index, elements, index + values.length, size - index);

        this.elements = elements;
        size += values.length;
    }

    public void trimToSize() {
        if (size < elements.length) {
            elements = size == 0 ? DEFAULT_EMPTY_ARRAY : Arrays.copyOf(elements, size);
        }
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public boolean removeAt(int index) {
        Conditions.checkElementIndex(index, size);
        boolean oldValue = elements[index];
        int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(elements, index + 1, elements, index, newSize - index);
        }
        elements[newSize] = false;
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
        System.arraycopy(elements, endIndex, elements, beginIndex, tailElementsCount);
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
        System.arraycopy(elements, n, elements, 0, newSize);
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
        System.arraycopy(elements, srcPos, dest, destPos, n);
        return n;
    }

    @Override
    public boolean @NotNull [] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public @NotNull ImmutableBooleanArray toImmutableArray() {
        return size == 0 ? ImmutableBooleanArray.empty() : ImmutableBooleanArray.Unsafe.wrap(Arrays.copyOf(elements, size));
    }

    //region Serialization

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeBoolean(elements[i]);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        final int size = in.readInt();
        final boolean[] elements = size == 0 ? DEFAULT_EMPTY_ARRAY : new boolean[Integer.max(DEFAULT_CAPACITY, size)];

        for (int i = 0; i < size; i++) {
            elements[i] = in.readBoolean();
        }

        this.size = size;
        this.elements = elements;
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

    private static final class SeqItr extends AbstractBooleanSeqIterator implements MutableBooleanListIterator {
        private final MutableBooleanArrayList seq;
        private int lastReturned = -1;

        SeqItr(MutableBooleanArrayList seq, int index) {
            super(index);
            this.seq = seq;
        }

        @Override
        public boolean hasNext() {
            return cursor < seq.size;
        }

        @Override
        public boolean nextBoolean() {
            final int idx = cursor;
            if (idx >= seq.size) {
                throw new NoSuchElementException();
            }
            try {
                boolean res = seq.elements[idx];
                lastReturned = idx;
                cursor = idx + 1;
                return res;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        @Override
        public boolean previousBoolean() {
            final int idx = cursor - 1;
            if (idx < 0) {
                throw new NoSuchElementException();
            }
            try {
                boolean res = seq.elements[idx];
                lastReturned = idx;
                cursor = idx;
                return res;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        @Override
        public void set(boolean e) {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }
            try {
                seq.set(lastReturned, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException(ex);
            }
        }

        @Override
        public void add(boolean e) {
            final int idx = this.cursor;
            try {
                seq.insert(idx, e);
                cursor = idx + 1;
                lastReturned = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException(ex);
            }
        }


        @Override
        public void remove() {
            if (lastReturned < 0) {
                throw new IllegalStateException();
            }

            try {
                seq.removeAt(lastReturned);
                cursor = lastReturned;
                lastReturned = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException(String.format("lastReturned=%d,size=%d,array=%s", lastReturned, seq.size, Arrays.toString(seq.elements)), ex);
            }
        }


    }
}
