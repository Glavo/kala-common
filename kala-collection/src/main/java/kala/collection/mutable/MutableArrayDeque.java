/*
 * Copyright 2025 Glavo
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
import kala.collection.IndexedSeq;
import kala.collection.base.Iterators;
import kala.collection.base.ObjectArrays;
import kala.collection.factory.CollectionFactory;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class MutableArrayDeque<E> extends AbstractMutableList<E> implements MutableDeque<E>, IndexedSeq<E>, Serializable {
    @Serial
    private static final long serialVersionUID = -4166302067142375121L;

    private static final MutableListFactory<Object, MutableArrayDeque<Object>> FACTORY = MutableArrayDeque::new;

    static final int DEFAULT_CAPACITY = 16;

    Object[] elements;
    int begin = -1;
    int end = 0;

    private MutableArrayDeque(Object[] elements, int begin, int end) {
        this.elements = elements;
        this.begin = begin;
        this.end = end;
    }

    public MutableArrayDeque() {
        this.elements = ObjectArrays.EMPTY;
    }

    public MutableArrayDeque(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("illegal initialCapacity: " + initialCapacity);
        }

        this.elements = initialCapacity == 0
                ? ObjectArrays.EMPTY
                : new Object[initialCapacity];
    }

    //region Internal

    private static int inc(int i, int capacity) {
        return i + 1 >= capacity ? 0 : i + 1;
    }

    private static int inc(int i, int distance, int capacity) {
        if ((i += distance) - capacity >= 0) {
            i -= capacity;
        }

        return i;
    }

    private static int dec(int i, int capacity) {
        return i - 1 < 0 ? capacity - 1 : i - 1;
    }

    private static int sub(int i, int distance, int capacity) {
        if ((i -= distance) < 0) {
            i += capacity;
        }
        return i;
    }

    private void grow() {
        grow(elements.length + 1);
    }

    private void grow(int minCapacity) {
        final int oldCapacity = elements.length;
        final int size = size();
        final int newCapacity = newCapacity(oldCapacity, minCapacity);

        final Object[] newElements;
        if (size == 0) {
            newElements = new Object[newCapacity];
        } else if (begin < end) {
            newElements = Arrays.copyOf(elements, newCapacity);
        } else {
            newElements = new Object[newCapacity];
            System.arraycopy(elements, begin, newElements, 0, elements.length - begin);
            System.arraycopy(elements, 0, newElements, elements.length - begin, end);
            begin = 0;
            end = size;
        }
        this.elements = newElements;
    }

    private int newCapacity(int oldCapacity, int minCapacity) {
        return oldCapacity == 0
                ? Integer.max(DEFAULT_CAPACITY, minCapacity)
                : Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
    }

    //endregion

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, MutableArrayDeque<E>> factory() {
        return MutableListFactory.cast(FACTORY);
    }

    public static <E> MutableArrayDeque<E> create() {
        return new MutableArrayDeque<>();
    }

    public static <E> MutableArrayDeque<E> create(int initialCapacity) {
        return new MutableArrayDeque<>(initialCapacity);
    }

    @Contract("-> new")
    public static <E> @NotNull MutableArrayDeque<E> of() {
        return new MutableArrayDeque<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArrayDeque<E> of(E value1) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        return new MutableArrayDeque<>(arr, 0, 1);
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull MutableArrayDeque<E> of(E value1, E value2) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        return new MutableArrayDeque<>(arr, 0, 2);
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull MutableArrayDeque<E> of(E value1, E value2, E value3) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        return new MutableArrayDeque<>(arr, 0, 3);
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull MutableArrayDeque<E> of(E value1, E value2, E value3, E value4) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        return new MutableArrayDeque<>(arr, 0, 4);
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull MutableArrayDeque<E> of(E value1, E value2, E value3, E value4, E value5) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        arr[4] = value5;
        return new MutableArrayDeque<>(arr, 0, 5);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArrayDeque<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArrayDeque<E> from(E @NotNull [] values) {
        int length = values.length; // implicit null check of values
        if (length == 0) {
            return new MutableArrayDeque<>();
        }

        Object[] newValues = Arrays.copyOf(values, length, Object[].class);
        return new MutableArrayDeque<>(newValues, 0, length);
    }

    public static <E> @NotNull MutableArrayDeque<E> from(@NotNull Iterable<? extends E> values) {
        MutableArrayDeque<E> buffer = new MutableArrayDeque<>();
        buffer.appendAll(values);
        return buffer;
    }

    public static <E> @NotNull MutableArrayDeque<E> from(@NotNull Iterator<? extends E> it) {
        MutableArrayDeque<E> buffer = new MutableArrayDeque<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer;
    }

    public static <E> @NotNull MutableArrayDeque<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull MutableArrayDeque<E> fill(int n, E value) {
        if (n <= 0) {
            return new MutableArrayDeque<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        if (value != null) {
            Arrays.fill(arr, 0, n, value);
        }
        return new MutableArrayDeque<>(arr, 0, n);
    }

    public static <E> @NotNull MutableArrayDeque<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return new MutableArrayDeque<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = init.apply(i);
        }
        return new MutableArrayDeque<>(arr, 0, n);
    }

    public static <E> @NotNull MutableArrayDeque<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        MutableArrayDeque<E> res = new MutableArrayDeque<>();
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull MutableArrayDeque<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        MutableArrayDeque<E> res = new MutableArrayDeque<>();
        while (true) {
            E value = supplier.get();
            if (value == null)
                break;
            res.append(value);
        }
        return res;
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "MutableArrayDeque";
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        if (isEmpty()) {
            return Iterators.empty();
        } else if (begin < end) {
            return (Iterator<E>) ObjectArrays.iterator(elements, begin, end);
        } else {
            return (Iterator<E>) Iterators.concat(
                    ObjectArrays.iterator(elements, begin, elements.length),
                    ObjectArrays.iterator(elements, 0, end)
            );
        }
    }

    //region Size Info

    @Override
    public boolean isEmpty() {
        return begin == -1;
    }

    @Override
    public int size() {
        if (isEmpty()) {
            return 0;
        } else if (begin < end) {
            return end - begin;
        } else {
            return elements.length - begin + end;
        }
    }

    //endregion

    //region Positional Access Operations

    @Override
    public E get(@Index int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        } else if (begin < end) {
            index = Indexes.checkIndex(index, end - begin);
            return (E) elements[begin + index];
        } else {
            index = Indexes.checkIndex(index, elements.length - begin + end);
            return (E) elements[inc(begin, index, elements.length)];
        }
    }

    @Override
    public void set(@Index int index, E newValue) {
        if (isEmpty()) {
            throw Indexes.outOfBounds(index, 0);
        }

        if (begin < end) {
            index = Indexes.checkIndex(index, end - begin);
            elements[begin + index] = newValue;
        } else {
            final int size = elements.length - begin + end;
            index = Indexes.checkIndex(index, size);
            elements[inc(begin, index, elements.length)] = newValue;
        }
    }

    @Override
    public void insert(@Index int index, E value) {
        final int oldSize = size();
        index = Indexes.checkPositionIndex(index, oldSize);

        if (index == 0) {
            prepend(value);
            return;
        }

        if (index == oldSize) {
            append(value);
            return;
        }

        if (oldSize == elements.length) {
            grow();
        }

        if (begin < end) {
            final int targetIndex = begin + index;
            if (end < elements.length) {
                System.arraycopy(elements, targetIndex, elements, targetIndex + 1, end - targetIndex);
                end++;
            } else {
                System.arraycopy(elements, begin, elements, begin - 1, targetIndex - begin + 1);
                begin--;
            }
            elements[targetIndex] = value;
        } else {
            int targetIndex = inc(begin, index, elements.length);
            if (targetIndex <= end) {
                System.arraycopy(elements, targetIndex, elements, targetIndex + 1, end - targetIndex);
                elements[targetIndex] = value;
                end++;
            } else {
                System.arraycopy(elements, begin, elements, begin - 1, targetIndex - begin);
                elements[targetIndex - 1] = value;
                begin--;
            }
        }
    }

    @Override
    public void insertAll(int index, E @NotNull [] values) {
        final int oldSize = size();
        Conditions.checkPositionIndex(index, oldSize);

        final int valuesLength = values.length;
        if (valuesLength == 0) {
            return;
        }

        final int newSize = oldSize + valuesLength;
        if (newSize < 0) {
            throw new OutOfMemoryError();
        }

        if (newSize > elements.length) {
            grow(newSize);
        }

        if (oldSize == 0) {
            System.arraycopy(values, 0, elements, 0, valuesLength);
            begin = 0;
            end = valuesLength;
        } else if (begin < end) {
            if (newSize <= elements.length - begin) {
                System.arraycopy(elements, begin + index, elements, begin + index + valuesLength, valuesLength);
                System.arraycopy(values, 0, elements, begin + index, valuesLength);
                end = begin + newSize;
            } else {
                System.arraycopy(elements, begin, elements, 0, index);
                System.arraycopy(elements, begin + index, elements, begin + index + valuesLength, oldSize - index);
                System.arraycopy(values, 0, elements, index, valuesLength);
                begin = 0;
                end = newSize;
            }
        } else {
            int targetIndex = inc(begin, index, elements.length);
            if (targetIndex <= end) {
                System.arraycopy(elements, targetIndex, elements, targetIndex + valuesLength, end - targetIndex);
                System.arraycopy(values, 0, elements, targetIndex, valuesLength);
                end += valuesLength;
            } else {
                System.arraycopy(elements, begin, elements, begin - valuesLength, targetIndex - begin);
                System.arraycopy(values, 0, elements, targetIndex - valuesLength, valuesLength);
                begin -= valuesLength;
            }
        }
    }

    @Override
    public E removeAt(int index) {
        final int oldSize = size();
        Objects.checkIndex(index, oldSize);

        if (index == 0) {
            return removeFirst();
        }

        if (index == oldSize - 1) {
            return removeLast();
        }

        final Object res;

        if (begin < end) {
            final int targetIndex = begin + index;
            res = elements[targetIndex];
            System.arraycopy(elements, targetIndex + 1, elements, targetIndex, end - targetIndex - 1);
            end--;
        } else {
            final int targetIndex = inc(begin, index, elements.length);
            res = elements[targetIndex];
            if (targetIndex < end) {
                System.arraycopy(elements, targetIndex + 1, elements, targetIndex, end - targetIndex - 1);
                end--;
            } else {
                System.arraycopy(elements, begin, elements, begin + 1, targetIndex - begin);
                begin = inc(begin, elements.length);
            }
        }

        return (E) res;
    }

    //endregion

    //region Modification Operations

    @Override
    public void prepend(E value) {
        final int oldSize = size();
        if (oldSize == elements.length) {
            grow();
        }

        if (oldSize == 0) {
            begin = elements.length - 1;
        } else {
            begin = dec(begin, elements.length);
        }
        elements[begin] = value;
    }

    @Override
    public void prependAll(E @NotNull [] values) {
        final int valuesLength = values.length;
        final int oldSize = size();

        final int newSize = oldSize + valuesLength;
        if (newSize < 0) {
            throw new OutOfMemoryError();
        }

        if (newSize > elements.length) {
            grow(newSize);
        }

        if (oldSize == 0) {
            System.arraycopy(values, 0, elements, 0, valuesLength);
            begin = 0;
            end = valuesLength;
        } else if (begin < end) {
            if (valuesLength <= begin) {
                System.arraycopy(values, 0, elements, begin - valuesLength, valuesLength);
                begin -= valuesLength;
            } else {
                System.arraycopy(elements, begin, elements, valuesLength, oldSize);
                System.arraycopy(values, 0, elements, 0, valuesLength);
                begin = 0;
                end = newSize;
            }
        } else {
            System.arraycopy(values, 0, elements, begin - valuesLength, valuesLength);
            begin -= valuesLength;
        }
    }

    @Override
    public void append(E value) {
        final int oldSize = size();
        if (oldSize == elements.length) {
            grow();
        }
        elements[end] = value;
        end = inc(end, elements.length);

        if (oldSize == 0) {
            begin = 0;
        }
    }

    @Override
    public void appendAll(E @NotNull [] values) {
        final int valuesLength = values.length;
        final int oldSize = size();

        if (valuesLength == 0) {
            return;
        }

        final int newSize = oldSize + valuesLength;
        if (newSize < 0) {
            throw new OutOfMemoryError();
        }

        if (newSize > elements.length) {
            grow(newSize);
        }

        if (oldSize == 0) {
            System.arraycopy(values, 0, elements, 0, valuesLength);
            begin = 0;
            end = valuesLength;
        } else if (begin < end) {
            if (newSize <= elements.length - begin) {
                System.arraycopy(values, 0, elements, end, valuesLength);
                end += valuesLength;
            } else {
                System.arraycopy(elements, begin, elements, 0, oldSize);
                System.arraycopy(values, 0, elements, oldSize, valuesLength);
                begin = 0;
                end = newSize;
            }
        } else {
            System.arraycopy(values, 0, elements, end, valuesLength);
            end += valuesLength;
        }
    }

    @Override
    public E removeFirst() {
        final int oldSize = size();
        if (oldSize == 0) {
            throw new NoSuchElementException();
        }

        Object res = elements[begin];
        elements[begin] = null;

        if (oldSize == 1) {
            begin = -1;
            end = 0;
        } else {
            begin = inc(begin, elements.length);
        }
        return (E) res;
    }

    @Override
    public @NotNull E removeLast() {
        final int oldSize = size();
        if (oldSize == 0) {
            throw new NoSuchElementException();
        }
        final int lastIdx = dec(end, elements.length);
        Object res = elements[lastIdx];
        elements[lastIdx] = null;

        if (oldSize == 1) {
            begin = -1;
            end = 0;
        } else {
            end = lastIdx;
        }
        return (E) res;
    }

    @Override
    public void clear() {
        if (isEmpty()) {
            return;
        }

        if (begin < end) {
            Arrays.fill(elements, begin, end, null);
        } else {
            Arrays.fill(elements, 0, end, null);
            Arrays.fill(elements, begin, elements.length, null);
        }

        begin = -1;
        end = 0;
    }

    //endregion

    //region Element Retrieval Operations

    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) elements[begin];
    }

    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) elements[dec(end, elements.length)];
    }

    //endregion
}
