package kala.collection.mutable.primitive;

import kala.Conditions;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.immutable.primitive.Immutable${Type}Array;
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

public final class Mutable${Type}ArrayList extends AbstractMutable${Type}List implements Indexed${Type}Seq, Serializable {
    private static final long serialVersionUID = 2545219250020890853L;

    private static final Factory FACTORY = new Factory();

    static final int DEFAULT_CAPACITY = 10;
    static final ${PrimitiveType}[] DEFAULT_EMPTY_ARRAY = new ${PrimitiveType}[0];

    //region Fields

    ${PrimitiveType} @NotNull [] elements;
    int size;

    //endregion

    //region Constructors

    private Mutable${Type}ArrayList(${PrimitiveType} @NotNull [] elements, int size) {
        this.elements = elements;
        this.size = size;
    }

    public Mutable${Type}ArrayList() {
        this(DEFAULT_EMPTY_ARRAY, 0);
    }

    public Mutable${Type}ArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("illegal initialCapacity: " + initialCapacity);
        }

        this.elements = initialCapacity == 0 ? ${Type}Arrays.EMPTY : new ${PrimitiveType}[initialCapacity];
        this.size = 0;
    }

    //endregion

    //region Static Factories

    public static @NotNull ${Type}CollectionFactory<?, Mutable${Type}ArrayList> factory() {
        return FACTORY;
    }

    @Contract("-> new")
    public static @NotNull Mutable${Type}ArrayList create() {
        return new Mutable${Type}ArrayList();
    }

    @Contract("_ -> new")
    public static @NotNull Mutable${Type}ArrayList create(int initialCapacity) {
        return new Mutable${Type}ArrayList(initialCapacity);
    }

    @Contract("-> new")
    public static @NotNull Mutable${Type}ArrayList of() {
        return new Mutable${Type}ArrayList();
    }

    @Contract("_ -> new")
    public static @NotNull Mutable${Type}ArrayList of(${PrimitiveType} value1) {
        ${PrimitiveType}[] arr = new ${PrimitiveType}[DEFAULT_CAPACITY];
        arr[0] = value1;
        return new Mutable${Type}ArrayList(arr, 1);
    }

    @Contract("_, _ -> new")
    public static @NotNull Mutable${Type}ArrayList of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        ${PrimitiveType}[] arr = new ${PrimitiveType}[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        return new Mutable${Type}ArrayList(arr, 2);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Mutable${Type}ArrayList of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        ${PrimitiveType}[] arr = new ${PrimitiveType}[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        return new Mutable${Type}ArrayList(arr, 3);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Mutable${Type}ArrayList of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        ${PrimitiveType}[] arr = new ${PrimitiveType}[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        return new Mutable${Type}ArrayList(arr, 4);
    }

    @Contract("_, _, _, _, _ -> new")
    public static @NotNull Mutable${Type}ArrayList of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        ${PrimitiveType}[] arr = new ${PrimitiveType}[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        arr[4] = value5;
        return new Mutable${Type}ArrayList(arr, 5);
    }

    @Contract("_ -> new")
    public static @NotNull Mutable${Type}ArrayList of(${PrimitiveType}... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static @NotNull Mutable${Type}ArrayList from(${PrimitiveType} @NotNull [] values) {
        int length = values.length; // implicit null check of values
        return length != 0
                ? new Mutable${Type}ArrayList(Arrays.copyOf(values, Integer.max(DEFAULT_CAPACITY, values.length)), length)
                : new Mutable${Type}ArrayList();
    }

    public static @NotNull Mutable${Type}ArrayList from(@NotNull ${Type}Traversable values) {
        Mutable${Type}ArrayList buffer = new Mutable${Type}ArrayList();
        buffer.appendAll(values);
        return buffer;
    }

    public static @NotNull Mutable${Type}ArrayList from(@NotNull ${Type}Iterator it) {
        Mutable${Type}ArrayList buffer = new Mutable${Type}ArrayList();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer;
    }

    public static @NotNull Mutable${Type}ArrayList fill(int n, ${PrimitiveType} value) {
        if (n <= 0) {
            return new Mutable${Type}ArrayList();
        }

        ${PrimitiveType}[] arr = new ${PrimitiveType}[Integer.max(DEFAULT_CAPACITY, n)];
        if (value != ${Values.Default}) {
            Arrays.fill(arr, 0, n, value);
        }
        return new Mutable${Type}ArrayList(arr, n);
    }

    public static @NotNull Mutable${Type}ArrayList fill(int n, @NotNull ${Type}Supplier supplier) {
        if (n <= 0) {
            return new Mutable${Type}ArrayList();
        }

        ${PrimitiveType}[] arr = new ${PrimitiveType}[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = supplier.getAs${Type}();
        }
        return new Mutable${Type}ArrayList(arr, n);
    }

    //endregion

    //region Internal

    private void grow() {
        grow(size + 1);
    }

    private void grow(int minCapacity) {
        ${PrimitiveType}[] newArray = growArray(minCapacity);
        if (elements.length != 0) {
            System.arraycopy(elements, 0, newArray, 0, size);
        }
        elements = newArray;
    }

    private ${PrimitiveType}[] growArray(int minCapacity) {
        int oldCapacity = elements.length;
        if (elements == DEFAULT_EMPTY_ARRAY && oldCapacity == 0) {
            return new ${PrimitiveType}[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }

        int newCapacity = Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
        return new ${PrimitiveType}[newCapacity];
    }

    private void checkInBound(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }

    ${PrimitiveType} @NotNull [] getArray() {
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
        return "Mutable${Type}ArrayList" ;
    }

    @Override
    public @NotNull ${Type}CollectionFactory<?, Mutable${Type}ArrayList> iterableFactory() {
        return factory();
    }

    @Override
    public @NotNull ${Type}Iterator iterator() {
        return ${Type}Arrays.iterator(elements, 0, size);
    }

    @Override
    public @NotNull ${Type}Iterator iterator(int beginIndex) {
        return ${Type}Arrays.iterator(elements, beginIndex, size);
    }

    // TODO: @Override
    public @NotNull Mutable${Type}ListIterator seqIterator(int index) {
        Conditions.checkPositionIndex(index, size);
        return new SeqItr(this, index);
    }
<#if IsSpecialized>

    @Override
    public @NotNull Spliterator.Of${Type} spliterator() {
        return Arrays.spliterator(elements, 0, size);
    }
</#if>

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Mutable${Type}ArrayList clone() {
        return new Mutable${Type}ArrayList(elements.length == 0 ? elements : elements.clone(), size);
    }

    //region Size Info

    @Override
    public int size() {
        return size;
    }

    //endregion

    //region Positional Access Operations

    @Override
    public ${PrimitiveType} get(int index) {
        Conditions.checkElementIndex(index, size);
        return elements[index];
    }

    @Override
    public void set(int index, ${PrimitiveType} newValue) {
        Conditions.checkElementIndex(index, size);
        elements[index] = newValue;
    }

    //endregion

    //region Modification Operations

    @Override
    public void prepend(${PrimitiveType} value) {
        ${PrimitiveType}[] values = elements;
        if (size == values.length) {
            values = growArray(size + 1);
        }
        System.arraycopy(elements, 0, values, 1, size);
        values[0] = value;
        this.elements = values;
        size++;
    }

    @Override
    public void prependAll(@NotNull ${Type}Traversable values) {
        Objects.requireNonNull(values);
        if (values == this) {
            appendThis();
            return;
        }

        final int size = this.size;
        if (values instanceof ${Type}SeqLike && ((${Type}SeqLike) values).supportsFastRandomAccess()) {
            ${Type}SeqLike seq = (${Type}SeqLike) values;
            int s = seq.size();
            if (s == 0) {
                return;
            }
            ${PrimitiveType}[] arr = this.elements;
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

        ${PrimitiveType}[] cv = values.toArray();
        if (cv.length == 0) {
            return;
        }

        ${PrimitiveType}[] elements = this.elements;
        if (elements.length < size + cv.length) {
            elements = growArray(size + cv.length);
        }

        System.arraycopy(this.elements, 0, elements, cv.length, size);
        System.arraycopy(cv, 0, elements, 0, cv.length);
        this.elements = elements;
        this.size += cv.length;
    }

    @Override
    public void append(${PrimitiveType} value) {
        if (size == elements.length) {
            grow();
        }
        elements[size++] = value;
    }

    @Override
    public void appendAll(@NotNull ${Type}Traversable values) {
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

        final ${PrimitiveType}[] elements = this.elements;
        System.arraycopy(elements, 0, elements, size, size);
        this.size = newSize;
    }

    //endregion
<#if Type != "Boolean">

    //region Search Operations

    @Override
    public int binarySearch(${PrimitiveType} value, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        return Arrays.binarySearch(elements, beginIndex, endIndex, value);
    }

    //endregion
</#if>

    @Override
    public void sort() {
        ${Type}Arrays.sort(elements, 0, size);
    }

    @Override
    public void insert(int index, ${PrimitiveType} value) {
        int size = this.size;
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
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
    public void insertAll(int index, @NotNull ${Type}Traversable values) {
        Objects.requireNonNull(values);
        Conditions.checkPositionIndex(index, size);

        final int otherSize = values.size();

        ${PrimitiveType}[] elements = this.elements;
        if (elements.length < size + otherSize || values == this) {
            elements = growArray(size + otherSize);
        }
        System.arraycopy(this.elements, 0, elements, 0, index);
        System.arraycopy(this.elements, index, elements, index + otherSize, size - index);

        final ${Type}Iterator it = values.iterator();
        for (int i = 0; i < otherSize; i++) {
            elements[i + index] = it.next${Type}();
        }
        assert !it.hasNext();

        this.elements = elements;
        size += otherSize;
    }

    @Override
    public void insertAll(int index, ${PrimitiveType} @NotNull [] values) {
        Objects.requireNonNull(values);
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
        if (values.length == 0) {
            return;
        }

        ${PrimitiveType}[] elements = this.elements;
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
    public ${PrimitiveType} removeAt(int index) {
        Conditions.checkElementIndex(index, size);
        ${PrimitiveType} oldValue = elements[index];
        int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(elements, index + 1, elements, index, newSize - index);
        }
        elements[newSize] = ${Values.Default};
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
    public int copyToArray(int srcPos, ${PrimitiveType} @NotNull [] dest, int destPos, int limit) {
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
    public ${PrimitiveType} @NotNull [] toArray() {
        return Arrays.copyOf(elements, size);
    }

    // TODO: @Override
    public @NotNull Immutable${Type}Array toImmutableArray() {
        return size == 0 ? Immutable${Type}Array.empty() : Immutable${Type}Array.Unsafe.wrap(Arrays.copyOf(elements, size));
    }

    //region Serialization

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.write${Type}(elements[i]);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        final int size = in.readInt();
        final ${PrimitiveType}[] elements = size == 0 ? DEFAULT_EMPTY_ARRAY : new ${PrimitiveType}[Integer.max(DEFAULT_CAPACITY, size)];

        for (int i = 0; i < size; i++) {
            elements[i] = in.read${Type}();
        }

        this.size = size;
        this.elements = elements;
    }

    //endregion

    private static final class Factory extends AbstractMutable${Type}ListFactory<Mutable${Type}ArrayList> {
        @Override
        public Mutable${Type}ArrayList newBuilder() {
            return new Mutable${Type}ArrayList();
        }

        @Override
        public void sizeHint(@NotNull Mutable${Type}ArrayList buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public Mutable${Type}ArrayList from(${PrimitiveType} @NotNull [] values) {
            return Mutable${Type}ArrayList.from(values);
        }

        @Override
        public Mutable${Type}ArrayList from(@NotNull ${Type}Traversable values) {
            return Mutable${Type}ArrayList.from(values);
        }

        @Override
        public Mutable${Type}ArrayList from(@NotNull ${Type}Iterator it) {
            return Mutable${Type}ArrayList.from(it);
        }

        @Override
        public Mutable${Type}ArrayList fill(int n, ${PrimitiveType} value) {
            return Mutable${Type}ArrayList.fill(n, value);
        }

        @Override
        public Mutable${Type}ArrayList fill(int n, @NotNull ${Type}Supplier supplier) {
            return Mutable${Type}ArrayList.fill(n, supplier);
        }

    }

    private static final class SeqItr extends Abstract${Type}SeqIterator implements Mutable${Type}ListIterator {
        private final Mutable${Type}ArrayList seq;
        private int lastReturned = -1;

        SeqItr(Mutable${Type}ArrayList seq, int index) {
            super(index);
            this.seq = seq;
        }

        @Override
        public boolean hasNext() {
            return cursor < seq.size;
        }

        @Override
        public ${PrimitiveType} next${Type}() {
            final int idx = cursor;
            if (idx >= seq.size) {
                throw new NoSuchElementException();
            }
            try {
                ${PrimitiveType} res = seq.elements[idx];
                lastReturned = idx;
                cursor = idx + 1;
                return res;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        @Override
        public ${PrimitiveType} previous${Type}() {
            final int idx = cursor - 1;
            if (idx < 0) {
                throw new NoSuchElementException();
            }
            try {
                ${PrimitiveType} res = seq.elements[idx];
                lastReturned = idx;
                cursor = idx;
                return res;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        @Override
        public void set(${PrimitiveType} e) {
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
        public void add(${PrimitiveType} e) {
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
