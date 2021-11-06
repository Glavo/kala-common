package kala.collection.mutable;

import kala.Conditions;
import kala.collection.IndexedSeq;
import kala.collection.base.GenericArrays;
import kala.collection.base.Iterators;
import kala.control.Option;
import kala.function.IndexedFunction;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class DynamicSmartArraySeq<E> extends AbstractDynamicSeq<E> implements IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 85150510977824651L;

    private static final int DEFAULT_CAPACITY = 16;

    private static final Factory<?> FACTORY = new Factory<>();

    private Object elem;

    private int size = 0;

    private DynamicSmartArraySeq(int size, Object[] elements) {
        this.size = size;
        this.elem = elements;
    }

    public DynamicSmartArraySeq() {
    }

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, DynamicSmartArraySeq<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @Contract("-> new")
    public static <E> @NotNull DynamicSmartArraySeq<E> create() {
        return new DynamicSmartArraySeq<>();
    }

    @Contract("-> new")
    public static <E> @NotNull DynamicSmartArraySeq<E> of() {
        return new DynamicSmartArraySeq<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicSmartArraySeq<E> of(E value1) {
        DynamicSmartArraySeq<E> buffer = new DynamicSmartArraySeq<>();
        buffer.size = 1;
        buffer.elem = value1;
        return buffer;
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull DynamicSmartArraySeq<E> of(E value1, E value2) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;

        return new DynamicSmartArraySeq<>(2, arr);
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull DynamicSmartArraySeq<E> of(E value1, E value2, E value3) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;

        return new DynamicSmartArraySeq<>(3, arr);
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull DynamicSmartArraySeq<E> of(E value1, E value2, E value3, E value4) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;

        return new DynamicSmartArraySeq<>(4, arr);
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull DynamicSmartArraySeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        arr[4] = value5;

        return new DynamicSmartArraySeq<>(5, arr);
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicSmartArraySeq<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicSmartArraySeq<E> from(E @NotNull [] values) {
        final int length = values.length; // implicit null check of values
        switch (length) {
            case 0:
                return new DynamicSmartArraySeq<>();
            case 1:
                return DynamicSmartArraySeq.of(values[0]);
            default:
                return new DynamicSmartArraySeq<>(
                        length,
                        Arrays.copyOf(values, Math.max(length, DEFAULT_CAPACITY), Object[].class)
                );
        }
    }

    public static <E> @NotNull DynamicSmartArraySeq<E> from(@NotNull Iterable<? extends E> values) {
        DynamicSmartArraySeq<E> buffer = new DynamicSmartArraySeq<>();
        buffer.appendAll(values);
        return buffer;
    }

    public static <E> @NotNull DynamicSmartArraySeq<E> from(@NotNull Iterator<? extends E> it) {
        DynamicSmartArraySeq<E> buffer = new DynamicSmartArraySeq<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer;
    }

    public static <E> @NotNull DynamicSmartArraySeq<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull DynamicSmartArraySeq<E> fill(int n, E value) {
        if (n <= 0) {
            return new DynamicSmartArraySeq<>();
        }
        if (n == 1) {
            return DynamicSmartArraySeq.of(value);
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        if (value != null) {
            Arrays.fill(arr, 0, n, value);
        }

        return new DynamicSmartArraySeq<>(n, arr);
    }

    public static <E> @NotNull DynamicSmartArraySeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return new DynamicSmartArraySeq<>();
        }
        if (n == 1) {
            return DynamicSmartArraySeq.of(supplier.get());
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = supplier.get();
        }
        return new DynamicSmartArraySeq<>(n, arr);
    }

    public static <E> @NotNull DynamicSmartArraySeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return new DynamicSmartArraySeq<>();
        }
        if (n == 1) {
            return DynamicSmartArraySeq.of(init.apply(0));
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = init.apply(i);
        }
        return new DynamicSmartArraySeq<>(n, arr);
    }

    //endregion

    //region SmartArrayBuffer helpers

    private static Object[] growArray(int oldCapacity) {
        assert oldCapacity < Integer.MAX_VALUE;
        return growArray(oldCapacity, oldCapacity + 1);
    }

    private static Object[] growArray(int oldCapacity, int minCapacity) {
        if (oldCapacity == 0) {
            return new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }

        int newCapacity = Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
        return new Object[newCapacity];
    }

    private static int growSize(int oldCapacity) {
        assert oldCapacity < Integer.MAX_VALUE;
        return growSize(oldCapacity, oldCapacity + 1);
    }

    private static int growSize(int oldCapacity, int minCapacity) {
        return oldCapacity == 0
                ? Math.max(DEFAULT_CAPACITY, minCapacity)
                : Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
    }

    //endregion

    //region Collection Operations

    @Override
    public @NotNull String className() {
        return "DynamicSmartArraySeq";
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        final int size = this.size;
        switch (size) {
            case 0:
                return Iterators.empty();
            case 1:
                return Iterators.of((E) elem);
            default:
                return (Iterator<E>) GenericArrays.iterator((Object[]) elem, 0, size);
        }
    }

    @Override
    public @NotNull Spliterator<E> spliterator() {
        final int size = this.size;
        switch (size) {
            case 0:
                return Spliterators.emptySpliterator();
            case 1:
                return (Spliterator<E>) Spliterators.spliteratorUnknownSize(Iterators.of(elem), 0);
            default:
                return Spliterators.spliterator(((Object[]) elem), 0, size, 0);
        }

    }

    //endregion

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    //region Positional Access Operations

    @Override
    public E get(int index) {
        final int size = this.size;
        Conditions.checkElementIndex(index, size);
        if (size == 1) {
            return (E) elem;
        } else {
            return (E) ((Object[]) elem)[index];
        }
    }

    @Override
    public E getOrNull(int index) {
        final int size = this.size;
        if (index < 0 || index >= size) {
            return null;
        }
        if (size == 1) {
            return (E) elem;
        } else {
            return (E) ((Object[]) elem)[index];
        }
    }

    @Override
    public @NotNull Option<E> getOption(int index) {
        final int size = this.size;
        if (index < 0 || index >= size) {
            return Option.none();
        }
        if (size == 1) {
            return Option.some((E) elem);
        } else {
            return Option.some((E) ((Object[]) elem)[index]);
        }
    }

    @Override
    public void set(int index, E newValue) {
        final int size = this.size;
        Conditions.checkElementIndex(index, size);
        if (size == 1) {
            elem = newValue;
        } else {
            ((Object[]) elem)[index] = newValue;
        }
    }

    @Override
    public void insert(int index, E value) {
        final int oldSize = this.size;
        Conditions.checkPositionIndex(index, oldSize);

        if (oldSize == 0) {
            elem = value;
            this.size = 1;
        } else {
            Object[] arr = (Object[]) this.elem;
            if (arr.length == oldSize) {
                Object[] newArr = new Object[growSize(oldSize)];
                System.arraycopy(arr, 0, newArr, 0, index);
                System.arraycopy(arr, index, newArr, index + 1, oldSize - index);
                newArr[index] = value;
                elem = newArr;
            } else {
                System.arraycopy(arr, index, arr, index + 1, oldSize - index);
                arr[index] = value;
            }
            this.size = oldSize + 1;
        }
    }

    @Override
    public E removeAt(int index) {
        final int oldSize = this.size;
        Conditions.checkElementIndex(index, oldSize);
        E res;

        if (oldSize == 1) {
            res = (E) elem;
            elem = null;
            this.size = 0;
        } else {
            Object[] arr = (Object[]) elem;
            res = (E) arr[index];
            System.arraycopy(arr, index + 1, arr, index, size - index);
            this.size = oldSize - 1;
        }
        return res;
    }

    @Override
    public void clear() {
        size = 0;
        elem = null;
    }

    //endregion

    //region Modification Operations

    @Override
    public void prepend(E value) {
        final int oldSize = this.size;
        if (oldSize == 0) {
            elem = value;
            this.size = 1;
        } else if (oldSize == 1) {
            final Object[] arr = new Object[DEFAULT_CAPACITY];
            arr[0] = value;
            arr[1] = elem;
            this.elem = arr;
            this.size = 2;
        } else {
            Object[] arr = (Object[]) this.elem;
            if (arr.length == oldSize) {
                Object[] newArr = growArray(oldSize);
                System.arraycopy(arr, 0, newArr, 1, oldSize);
                arr = newArr;
            } else {
                System.arraycopy(arr, 0, arr, 1, oldSize);
            }
            arr[0] = value;
            elem = arr;
            this.size = oldSize + 1;
        }

    }

    @Override
    public void append(E value) {
        final int oldSize = this.size;
        if (oldSize == 0) {
            elem = value;
            this.size = 1;
        } else if (oldSize == 1) {
            final Object[] arr = new Object[DEFAULT_CAPACITY];
            arr[0] = elem;
            arr[1] = value;
            this.elem = arr;
            this.size = 2;
        } else {
            Object[] arr = (Object[]) this.elem;
            if (arr.length == oldSize) {
                arr = Arrays.copyOf(arr, growSize(oldSize));
            }
            arr[oldSize] = value;
            elem = arr;
            this.size = oldSize + 1;
        }
    }

    private void appendThis() {
        final int oldSize = this.size;
        switch (oldSize) {
            case 0:
                return;
            case 1: {
                Object[] arr = new Object[DEFAULT_CAPACITY];
                arr[0] = arr[1] = elem;
                this.elem = arr;
                this.size = 2;
                return;
            }
            default: {
                Object[] arr = (Object[]) this.elem;
                final int newSize = oldSize * 2;
                if (newSize < 0) {
                    throw new AssertionError();
                }
                if (arr.length < newSize) {
                    arr = Arrays.copyOf(arr, growSize(arr.length, newSize));
                    this.elem = arr;
                }
                System.arraycopy(arr, 0, arr, oldSize, oldSize);
            }
        }
    }

    //endregion

    @Override
    public void sort(Comparator<? super E> comparator) {
        final int size = this.size;
        if (size == 0 || size == 1) {
            return;
        }
        Arrays.sort((Object[]) elem, 0, size, (Comparator<Object>) comparator);
    }

    @Override
    public void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
        switch (size) {
            case 0:
                return;
            case 1:
                elem = operator.apply((E) elem);
                return;
            default:
                Object[] arr = (Object[]) elem;
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = operator.apply(((E) arr[i]));
                }
        }
    }

    @Override
    public void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
        switch (size) {
            case 0:
                return;
            case 1:
                elem = operator.apply(0, (E) elem);
                return;
            default:
                Object[] arr = (Object[]) elem;
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = operator.apply(i, ((E) arr[i]));
                }
        }
    }

    @Override
    public Object @NotNull [] toArray() {
        final int size = this.size;
        switch (size) {
            case 0:
                return new Object[0];
            case 1:
                return new Object[]{elem};
            default:
                return Arrays.copyOf((Object[]) elem, size);
        }
    }

    @Override
    public <U> U @NotNull [] toArray(@NotNull Class<U> type) {
        final int size = this.size;
        U[] arr = (U[]) Array.newInstance(type, size);
        switch (size) {
            case 0:
                return arr;
            case 1:
                arr[0] = (U) elem;
                return arr;
            default:
                //noinspection SuspiciousSystemArraycopy
                System.arraycopy(elem, 0, arr, 0, size);
                return arr;
        }
    }

    @Override
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        final int size = this.size;
        U[] arr = generator.apply(size);
        switch (size) {
            case 0:
                return arr;
            case 1:
                arr[0] = (U) elem;
                return arr;
            default:
                //noinspection SuspiciousSystemArraycopy
                System.arraycopy(elem, 0, arr, 0, size);
                return arr;
        }
    }

    private static final class Factory<E> extends AbstractDynamicSeqFactory<E, DynamicSmartArraySeq<E>> {
        @Override
        public DynamicSmartArraySeq<E> newBuilder() {
            return new DynamicSmartArraySeq<>();
        }
    }
}
