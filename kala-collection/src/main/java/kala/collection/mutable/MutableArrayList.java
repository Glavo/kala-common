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
package kala.collection.mutable;

import kala.Conditions;
import kala.collection.Collection;
import kala.collection.IndexedSeq;
import kala.collection.SeqLike;
import kala.collection.base.AnyTraversable;
import kala.collection.base.GenericArrays;
import kala.collection.base.ObjectArrays;
import kala.collection.immutable.ImmutableArray;
import kala.collection.internal.CollectionHelper;
import kala.collection.factory.CollectionFactory;
import kala.index.Index;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class MutableArrayList<E> extends AbstractMutableList<E> implements MutableStack<E>, IndexedSeq<E>, Serializable {

    private static final MutableArrayList.Factory<?> FACTORY = new Factory<>();

    static final int DEFAULT_CAPACITY = 10;
    static final Object[] DEFAULT_EMPTY_ARRAY = new Object[0];

    //region Fields

    Object @NotNull [] elements;
    int size;

    //endregion

    //region Constructors

    private MutableArrayList(Object @NotNull [] elements, int size) {
        this.elements = elements;
        this.size = size;
    }

    public MutableArrayList() {
        this(DEFAULT_EMPTY_ARRAY, 0);
    }

    public MutableArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("illegal initialCapacity: " + initialCapacity);
        }

        this.elements = initialCapacity == 0 ? ObjectArrays.EMPTY : new Object[initialCapacity];
        this.size = 0;
    }

    //endregion

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, MutableArrayList<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @Contract("-> new")
    public static <E> @NotNull MutableArrayList<E> create() {
        return new MutableArrayList<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArrayList<E> create(int initialCapacity) {
        return new MutableArrayList<>(initialCapacity);
    }

    @Contract("-> new")
    public static <E> @NotNull MutableArrayList<E> of() {
        return new MutableArrayList<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArrayList<E> of(E value1) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        return new MutableArrayList<>(arr, 1);
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull MutableArrayList<E> of(E value1, E value2) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        return new MutableArrayList<>(arr, 2);
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull MutableArrayList<E> of(E value1, E value2, E value3) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        return new MutableArrayList<>(arr, 3);
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull MutableArrayList<E> of(E value1, E value2, E value3, E value4) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        return new MutableArrayList<>(arr, 4);
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull MutableArrayList<E> of(E value1, E value2, E value3, E value4, E value5) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        arr[4] = value5;
        return new MutableArrayList<>(arr, 5);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArrayList<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArrayList<E> from(E @NotNull [] values) {
        int length = values.length; // implicit null check of values
        if (length == 0) {
            return new MutableArrayList<>();
        }
        Object[] newValues = new Object[length];
        System.arraycopy(values, 0, newValues, 0, length);
        return new MutableArrayList<>(newValues, length);
    }

    public static <E> @NotNull MutableArrayList<E> from(@NotNull Iterable<? extends E> values) {
        MutableArrayList<E> buffer = new MutableArrayList<>();
        buffer.appendAll(values);
        return buffer;
    }

    public static <E> @NotNull MutableArrayList<E> from(@NotNull Iterator<? extends E> it) {
        MutableArrayList<E> buffer = new MutableArrayList<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer;
    }

    public static <E> @NotNull MutableArrayList<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull MutableArrayList<E> fill(int n, E value) {
        if (n <= 0) {
            return new MutableArrayList<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        if (value != null) {
            Arrays.fill(arr, 0, n, value);
        }
        return new MutableArrayList<>(arr, n);
    }

    public static <E> @NotNull MutableArrayList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return new MutableArrayList<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = init.apply(i);
        }
        return new MutableArrayList<>(arr, n);
    }

    public static <E> @NotNull MutableArrayList<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        MutableArrayList<E> res = new MutableArrayList<>();
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull MutableArrayList<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        MutableArrayList<E> res = new MutableArrayList<>();
        while (true) {
            E value = supplier.get();
            if (value == null)
                break;
            res.append(value);
        }
        return res;
    }

    //endregion

    //region Internal

    private void grow() {
        grow(size + 1);
    }

    private void grow(int minCapacity) {
        Object[] newArray = growArray(minCapacity);
        if (elements.length != 0) {
            System.arraycopy(elements, 0, newArray, 0, size);
        }
        elements = newArray;
    }

    private Object[] growArray(int minCapacity) {
        int oldCapacity = elements.length;
        if (elements == DEFAULT_EMPTY_ARRAY && oldCapacity == 0) {
            return new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }

        int newCapacity = Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
        return new Object[newCapacity];
    }

    private void reduceToSize(int newSize) {
        Arrays.fill(elements, newSize, size, null);
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
        return "MutableArrayList";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, MutableArrayList<U>> iterableFactory() {
        return factory();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return (Iterator<E>) GenericArrays.iterator(elements, 0, size);
    }

    @Override
    public @NotNull Iterator<E> iterator(@Index int beginIndex) {
        return (Iterator<E>) GenericArrays.iterator(elements, beginIndex, size);
    }

    @Override
    public @NotNull MutableListIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, size);
        return new SeqItr<>(this, index);
    }

    @Override
    public @NotNull Spliterator<E> spliterator() {
        return (Spliterator<E>) Arrays.spliterator(elements, 0, size);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableArrayList<E> clone() {
        return new MutableArrayList<>(elements.length == 0 ? elements : elements.clone(), size);
    }

    //region Size Info

    @Override
    public int size() {
        return size;
    }

    //endregion

    //region Positional Access Operations

    @Override
    public E get(int index) {
        Objects.checkIndex(index, size);
        return (E) elements[index];
    }

    @Override
    public void set(int index, E newValue) {
        Objects.checkIndex(index, size);
        elements[index] = newValue;
    }

    //endregion

    //region Modification Operations

    @Override
    public void prepend(E value) {
        Object[] values = elements;
        if (size == values.length) {
            values = growArray(size + 1);
        }
        System.arraycopy(elements, 0, values, 1, size);
        values[0] = value;
        this.elements = values;
        ++size;
    }

    @Override
    public void prependAll(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        if (values == this) {
            appendThis();
            return;
        }

        final int size = this.size;
        if (values instanceof SeqLike<?> && ((SeqLike<? extends E>) values).supportsFastRandomAccess()) {
            SeqLike<?> seq = (SeqLike<?>) values;
            int s = seq.size();
            if (s == 0) {
                return;
            }
            Object[] arr = this.elements;
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

        Object[] cv = CollectionHelper.asArray(values);
        if (cv.length == 0) {
            return;
        }

        Object[] elements = this.elements;
        if (elements.length < size + cv.length) {
            elements = growArray(size + cv.length);
        }

        System.arraycopy(this.elements, 0, elements, cv.length, size);
        System.arraycopy(cv, 0, elements, 0, cv.length);
        this.elements = elements;
        this.size += cv.length;
    }

    @Override
    public void append(E value) {
        if (size == elements.length) {
            grow();
        }
        elements[size++] = value;
    }

    @Override
    public void appendAll(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values == this) {
            appendThis();
            return;
        }

        int knowSize = AnyTraversable.knownSize(values);
        if (knowSize > 0 && size + knowSize > elements.length) {
            grow(size + knowSize);
        }

        for (E e : values) {
            this.append(e);
        }
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

        final Object[] elements = this.elements;
        System.arraycopy(elements, 0, elements, size, size);
        this.size = newSize;
    }

    //endregion

    //region Search Operations

    @Override
    public int binarySearch(int beginIndex, int endIndex, E value) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        return Arrays.binarySearch(elements, beginIndex, endIndex, value);
    }

    @Override
    public int binarySearch(int beginIndex, int endIndex, E value, Comparator<? super E> comparator) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        return Arrays.binarySearch((E[]) elements, beginIndex, endIndex, value, comparator);
    }

    //endregion

    @Override
    public void sort(Comparator<? super E> comparator) {
        Arrays.sort(elements, 0, size, (Comparator<? super Object>) comparator);
    }

    @Override
    public void insert(int index, E value) {
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
        this.size++;
    }

    @Override
    public void insertAll(int index, @NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        Conditions.checkPositionIndex(index, size);

        final Collection<Object> other = CollectionHelper.asSizedCollection(values);
        final int otherSize = other.size();

        Object[] elements = this.elements;
        if (elements.length < size + otherSize || values == this) {
            elements = growArray(size + otherSize);
        }
        System.arraycopy(this.elements, 0, elements, 0, index);
        System.arraycopy(this.elements, index, elements, index + otherSize, size - index);

        final Iterator<Object> it = other.iterator();
        for (int i = 0; i < otherSize; i++) {
            elements[i + index] = it.next();
        }
        assert !it.hasNext();

        this.elements = elements;
        size += otherSize;
    }

    @Override
    public void insertAll(int index, E @NotNull [] values) {
        Objects.requireNonNull(values);
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
        if (values.length == 0) {
            return;
        }

        Object[] elements = this.elements;
        if (elements.length < size + values.length) {
            elements = growArray(size + values.length);
        }
        System.arraycopy(this.elements, 0, elements, 0, index);
        System.arraycopy(values, 0, elements, index, values.length);
        System.arraycopy(this.elements, index, elements, index + values.length, size - index);

        this.elements = elements;
        this.size += values.length;
    }

    public void trimToSize() {
        if (size < elements.length) {
            elements = size == 0 ? DEFAULT_EMPTY_ARRAY : Arrays.copyOf(elements, size);
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        this.size = 0;
    }

    @Override
    public E removeAt(int index) {
        Objects.checkIndex(index, size);
        E oldValue = (E) elements[index];
        int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(elements, index + 1, elements, index, newSize - index);
        }
        elements[newSize] = null;
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
        if (tailElementsCount < rangeLength) {
            Arrays.fill(elements, beginIndex + tailElementsCount, beginIndex + rangeLength, null);
        }

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
        Arrays.fill(elements, n, elements.length, null);
        size = n;
    }

    @Override
    public int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
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
    public Object @NotNull [] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public <U> U @NotNull [] toArray(@NotNull Class<U> type) {
        return Arrays.copyOf(elements, size, GenericArrays.arrayType(type));
    }

    @Override
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        final int size = this.size;
        U[] arr = generator.apply(size);
        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(elements, 0, arr, 0, size);
        return arr;
    }

    @Override
    public @NotNull ImmutableArray<E> toImmutableArray() {
        return size == 0 ? ImmutableArray.empty() : (ImmutableArray<E>) ImmutableArray.Unsafe.wrap(Arrays.copyOf(elements, size));
    }

    //region Stack

    @Override
    public void push(E value) {
        this.append(value);
    }

    @Override
    public E pop() {
        return removeLast();
    }

    @Override
    public E peek() {
        return getLast();
    }

    //endregion

    //region Serialization

    @Serial
    private Object writeReplace() {
        return new SerializationWrapper<>(factory(), this);
    }

    //endregion

    private static final class Factory<E> implements MutableListFactory<E, MutableArrayList<E>>, Serializable {

        @Serial
        private static final long serialVersionUID = 0L;

        @Override
        public MutableArrayList<E> newBuilder() {
            return new MutableArrayList<>();
        }

        @Override
        public void sizeHint(@NotNull MutableArrayList<E> buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public MutableArrayList<E> from(E @NotNull [] values) {
            return MutableArrayList.from(values);
        }

        @Override
        public MutableArrayList<E> from(@NotNull Iterable<? extends E> values) {
            return MutableArrayList.from(values);
        }

        @Override
        public MutableArrayList<E> from(@NotNull Iterator<? extends E> it) {
            return MutableArrayList.from(it);
        }

        @Override
        public MutableArrayList<E> fill(int n, E value) {
            return MutableArrayList.fill(n, value);
        }

        @Override
        public MutableArrayList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return MutableArrayList.fill(n, init);
        }

        @Serial
        private Object readResolve() {
            return factory();
        }
    }

    private static final class SeqItr<E> extends AbstractMutableListIterator<E> {
        private final MutableArrayList<E> seq;
        private int lastReturned = -1;

        SeqItr(MutableArrayList<E> seq, int index) {
            super(index);
            this.seq = seq;
        }

        @Override
        public boolean hasNext() {
            return cursor < seq.size;
        }

        @Override
        public E next() {
            final int idx = cursor;
            if (idx >= seq.size) {
                throw new NoSuchElementException();
            }
            try {
                E res = (E) seq.elements[idx];
                lastReturned = idx;
                cursor = idx + 1;
                return res;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        @Override
        public E previous() {
            final int idx = cursor - 1;
            if (idx < 0) {
                throw new NoSuchElementException();
            }
            try {
                E res = (E) seq.elements[idx];
                lastReturned = idx;
                cursor = idx;
                return res;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ConcurrentModificationException(e);
            }
        }

        @Override
        public void set(E e) {
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
        public void add(E e) {
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
