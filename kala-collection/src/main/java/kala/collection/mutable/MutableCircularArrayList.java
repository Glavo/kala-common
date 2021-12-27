package kala.collection.mutable;

import kala.Conditions;
import kala.collection.base.Iterators;
import kala.collection.base.ObjectArrays;
import kala.collection.factory.CollectionFactory;
import kala.control.Option;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class MutableCircularArrayList<E> extends AbstractMutableList<E> implements MutableIndexedListDeque<E>, Serializable {
    private static final long serialVersionUID = -4166302067142375121L;

    private static final Factory<?> FACTORY = new Factory<>();

    static final int DEFAULT_CAPACITY = 16;

    private Object[] elements;
    private int begin = -1;
    private int end = 0;

    private MutableCircularArrayList(Object[] elements, int begin, int end) {
        this.elements = elements;
        this.begin = begin;
        this.end = end;
    }

    public MutableCircularArrayList() {
        this.elements = ObjectArrays.EMPTY;
    }

    public MutableCircularArrayList(int initialCapacity) {
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
            System.arraycopy(elements, 0, newElements, 0, end);
            System.arraycopy(elements, begin, newElements, begin + (newCapacity - oldCapacity), oldCapacity - begin);
            begin += newCapacity - oldCapacity;
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

    public static <E> @NotNull CollectionFactory<E, ?, MutableCircularArrayList<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    public static <E> MutableCircularArrayList<E> create() {
        return new MutableCircularArrayList<>();
    }

    public static <E> MutableCircularArrayList<E> create(int initialCapacity) {
        return new MutableCircularArrayList<>(initialCapacity);
    }

    @Contract("-> new")
    public static <E> @NotNull MutableCircularArrayList<E> of() {
        return new MutableCircularArrayList<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableCircularArrayList<E> of(E value1) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        return new MutableCircularArrayList<>(arr, 0, 1);
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull MutableCircularArrayList<E> of(E value1, E value2) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        return new MutableCircularArrayList<>(arr, 0, 2);
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull MutableCircularArrayList<E> of(E value1, E value2, E value3) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        return new MutableCircularArrayList<>(arr, 0, 3);
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull MutableCircularArrayList<E> of(E value1, E value2, E value3, E value4) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        return new MutableCircularArrayList<>(arr, 0, 4);
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull MutableCircularArrayList<E> of(E value1, E value2, E value3, E value4, E value5) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        arr[4] = value5;
        return new MutableCircularArrayList<>(arr, 0, 5);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableCircularArrayList<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableCircularArrayList<E> from(E @NotNull [] values) {
        int length = values.length; // implicit null check of values
        if (length == 0) {
            return new MutableCircularArrayList<>();
        }

        Object[] newValues = Arrays.copyOf(values, length, Object[].class);
        return new MutableCircularArrayList<>(newValues, 0, length);
    }

    public static <E> @NotNull MutableCircularArrayList<E> from(@NotNull Iterable<? extends E> values) {
        MutableCircularArrayList<E> buffer = new MutableCircularArrayList<>();
        buffer.appendAll(values);
        return buffer;
    }

    public static <E> @NotNull MutableCircularArrayList<E> from(@NotNull Iterator<? extends E> it) {
        MutableCircularArrayList<E> buffer = new MutableCircularArrayList<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer;
    }

    public static <E> @NotNull MutableCircularArrayList<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull MutableCircularArrayList<E> fill(int n, E value) {
        if (n <= 0) {
            return new MutableCircularArrayList<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        if (value != null) {
            Arrays.fill(arr, 0, n, value);
        }
        return new MutableCircularArrayList<>(arr, 0, n);
    }

    public static <E> @NotNull MutableCircularArrayList<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return new MutableCircularArrayList<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = supplier.get();
        }
        return new MutableCircularArrayList<>(arr, 0, n);
    }

    public static <E> @NotNull MutableCircularArrayList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return new MutableCircularArrayList<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = init.apply(i);
        }
        return new MutableCircularArrayList<>(arr, 0, n);
    }

    //endregion

    //region Collection Operations

    @Override
    public @NotNull String className() {
        return "MutableCircularArrayList";
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

    //endregion

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
    public E get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        } else if (begin < end) {
            Conditions.checkElementIndex(index, end - begin);
            return (E) elements[begin + index];
        } else {
            Conditions.checkElementIndex(index, elements.length - begin + end);
            return (E) elements[inc(begin, index, elements.length)];
        }
    }

    @Override
    public void set(int index, E newValue) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException();
        } else if (begin < end) {
            Conditions.checkElementIndex(index, end - begin);
            elements[begin + index] = newValue;
        } else {
            final int size = elements.length - begin + end;
            Conditions.checkElementIndex(index, size);
            elements[inc(begin, index, elements.length)] = newValue;
        }
    }

    @Override
    public void insert(int index, E value) {
        final int oldSize = size();
        Conditions.checkPositionIndex(index, oldSize);

        if (oldSize == elements.length) {
            grow();
        }

        if (oldSize == 0) {
            elements[0] = value;
            begin = 0;
            end = 1;
        } else if (begin < end) {
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
            final int targetIndex = inc(begin, index, elements.length);
            if (targetIndex <= end) {
                System.arraycopy(elements, targetIndex, elements, targetIndex + 1, end - targetIndex);
                elements[targetIndex] = value;
                end++;
            } else {
                System.arraycopy(elements, begin, elements, begin - 1, targetIndex - begin + 1);
                elements[targetIndex] = value;
                begin--;
            }
        }
    }

    @Override
    public E removeAt(int index) {
        final int oldSize = size();
        Conditions.checkElementIndex(index, oldSize);

        final Object res;

        if (oldSize == 1) {
            res = elements[begin];
            elements[begin] = null;
            begin = -1;
            end = 0;
        } else if (begin < end) {
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
                System.arraycopy(elements, begin, elements, begin + 1, targetIndex - begin + 1);
                begin++;
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
    public void append(E value) {
        final int oldSize = size();
        if (oldSize == elements.length) {
            grow();
        }
        if (oldSize == 0) {
            begin = 0;
        }

        elements[end] = value;
        end = inc(end, elements.length);
    }

    @Override
    public @NotNull Option<E> removeFirstOption() {
        final int oldSize = size();
        if (oldSize == 0) {
            return Option.none();
        }

        Object res = elements[begin];
        elements[begin] = null;

        if (oldSize == 1) {
            begin = -1;
            end = 0;
        } else {
            begin = inc(begin, elements.length);
        }
        return Option.some((E) res);
    }

    @Override
    public @NotNull Option<E> removeLastOption() {
        final int oldSize = size();
        if (oldSize == 0) {
            return Option.none();
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
        return Option.some((E) res);
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
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) elements[begin];
    }

    @Override
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) elements[dec(end, elements.length)];
    }

    //endregion

    private static final class Factory<E> extends AbstractMutableListFactory<E, MutableCircularArrayList<E>> {
        @Override
        public MutableCircularArrayList<E> newBuilder() {
            return new MutableCircularArrayList<>();
        }
    }
}
