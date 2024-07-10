package kala.collection.mutable;

import kala.Conditions;
import kala.collection.IndexedSeq;
import kala.collection.base.GenericArrays;
import kala.collection.base.Iterators;
import kala.function.IndexedFunction;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class MutableSmartArrayList<E> extends AbstractMutableList<E> implements IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 85150510977824651L;

    private static final int DEFAULT_CAPACITY = 16;

    private static final MutableListFactory<Object, MutableSmartArrayList<Object>> FACTORY = MutableSmartArrayList::new;

    private Object elem;

    private int size = 0;

    private MutableSmartArrayList(int size, Object[] elements) {
        this.size = size;
        this.elem = elements;
    }

    public MutableSmartArrayList() {
    }

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, MutableSmartArrayList<E>> factory() {
        return MutableListFactory.cast(FACTORY);
    }

    @Contract("-> new")
    public static <E> @NotNull MutableSmartArrayList<E> create() {
        return new MutableSmartArrayList<>();
    }

    @Contract("-> new")
    public static <E> @NotNull MutableSmartArrayList<E> of() {
        return new MutableSmartArrayList<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableSmartArrayList<E> of(E value1) {
        MutableSmartArrayList<E> buffer = new MutableSmartArrayList<>();
        buffer.size = 1;
        buffer.elem = value1;
        return buffer;
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull MutableSmartArrayList<E> of(E value1, E value2) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;

        return new MutableSmartArrayList<>(2, arr);
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull MutableSmartArrayList<E> of(E value1, E value2, E value3) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;

        return new MutableSmartArrayList<>(3, arr);
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull MutableSmartArrayList<E> of(E value1, E value2, E value3, E value4) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;

        return new MutableSmartArrayList<>(4, arr);
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull MutableSmartArrayList<E> of(E value1, E value2, E value3, E value4, E value5) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        arr[4] = value5;

        return new MutableSmartArrayList<>(5, arr);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableSmartArrayList<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableSmartArrayList<E> from(E @NotNull [] values) {
        final int length = values.length; // implicit null check of values
        switch (length) {
            case 0:
                return new MutableSmartArrayList<>();
            case 1:
                return MutableSmartArrayList.of(values[0]);
            default:
                return new MutableSmartArrayList<>(
                        length,
                        Arrays.copyOf(values, Math.max(length, DEFAULT_CAPACITY), Object[].class)
                );
        }
    }

    public static <E> @NotNull MutableSmartArrayList<E> from(@NotNull Iterable<? extends E> values) {
        MutableSmartArrayList<E> buffer = new MutableSmartArrayList<>();
        buffer.appendAll(values);
        return buffer;
    }

    public static <E> @NotNull MutableSmartArrayList<E> from(@NotNull Iterator<? extends E> it) {
        MutableSmartArrayList<E> buffer = new MutableSmartArrayList<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer;
    }

    public static <E> @NotNull MutableSmartArrayList<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull MutableSmartArrayList<E> fill(int n, E value) {
        if (n <= 0) {
            return new MutableSmartArrayList<>();
        }
        if (n == 1) {
            return MutableSmartArrayList.of(value);
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        if (value != null) {
            Arrays.fill(arr, 0, n, value);
        }

        return new MutableSmartArrayList<>(n, arr);
    }

    public static <E> @NotNull MutableSmartArrayList<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return new MutableSmartArrayList<>();
        }
        if (n == 1) {
            return MutableSmartArrayList.of(init.apply(0));
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = init.apply(i);
        }
        return new MutableSmartArrayList<>(n, arr);
    }

    public static <E> @NotNull MutableSmartArrayList<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        MutableSmartArrayList<E> res = new MutableSmartArrayList<>();
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;
            res.append(value);
        }
        return res;
    }

    public static <E> @NotNull MutableSmartArrayList<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        MutableSmartArrayList<E> res = new MutableSmartArrayList<>();
        while (true) {
            E value = supplier.get();
            if (value == null)
                break;
            res.append(value);
        }
        return res;
    }

    //endregion

    //region SmartArrayBuffer helpers

    private static Object[] growArray(int oldCapacity) {
        assert oldCapacity > 0;
        assert oldCapacity < Integer.MAX_VALUE;
        return growArray(oldCapacity, oldCapacity + 1);
    }

    private static Object[] growArray(int oldCapacity, int minCapacity) {
        int newCapacity = Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
        return new Object[newCapacity];
    }

    private static int growSize(int oldCapacity) {
        assert oldCapacity > 0;
        assert oldCapacity < Integer.MAX_VALUE;
        return growSize(oldCapacity, oldCapacity + 1);
    }

    private static int growSize(int oldCapacity, int minCapacity) {
        return Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "MutableSmartArrayList";
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
        } else if (oldSize == 1) {
            Object[] arr = new Object[DEFAULT_CAPACITY];
            if (index == 0) {
                arr[0] = value;
                arr[1] = this.elem;
            } else {
                arr[0] = this.elem;
                arr[1] = value;
            }
            this.elem = arr;
            this.size = 2;
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
            if (oldSize == 2)
                this.elem = arr[index == 0 ? 1 : 0];
            else
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
                for (int i = 0; i < size; i++) {
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
                for (int i = 0; i < size; i++) {
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
}
