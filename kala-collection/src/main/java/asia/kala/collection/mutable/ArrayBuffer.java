package asia.kala.collection.mutable;

import asia.kala.collection.*;
import asia.kala.collection.immutable.ImmutableArray;
import asia.kala.collection.internal.CollectionHelper;
import asia.kala.factory.CollectionFactory;
import asia.kala.traversable.JavaArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.IntFunction;

@SuppressWarnings("unchecked")
public final class ArrayBuffer<E> extends AbstractBuffer<E>
        implements BufferOps<E, ArrayBuffer<?>, ArrayBuffer<E>>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 2545219250020890853L;

    private static final int DEFAULT_CAPACITY = 16;

    private static final ArrayBuffer.Factory<?> FACTORY = new Factory<>();

    //region Fields

    @NotNull
    private Object[] elements;
    private int size;

    //endregion

    //region Constructors

    ArrayBuffer(@NotNull Object[] elements, int size) {
        this.elements = elements;
        this.size = size;
    }

    public ArrayBuffer() {
        this(JavaArray.EMPTY_OBJECT_ARRAY, 0);
    }

    public ArrayBuffer(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("illegal initialCapacity: " + initialCapacity);
        }

        this.elements = initialCapacity == 0 ? JavaArray.EMPTY_OBJECT_ARRAY : new Object[initialCapacity];
        this.size = 0;
    }

    //endregion

    //region Factory methods

    @NotNull
    public static <E> CollectionFactory<E, ?, ArrayBuffer<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @NotNull
    @Contract("-> new")
    public static <E> ArrayBuffer<E> of() {
        return new ArrayBuffer<>();
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> ArrayBuffer<E> of(E value1) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        return new ArrayBuffer<>(arr, 1);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <E> ArrayBuffer<E> of(E value1, E value2) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        return new ArrayBuffer<>(arr, 2);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static <E> ArrayBuffer<E> of(E value1, E value2, E value3) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        return new ArrayBuffer<>(arr, 3);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static <E> ArrayBuffer<E> of(E value1, E value2, E value3, E value4) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        return new ArrayBuffer<>(arr, 4);
    }

    @NotNull
    @Contract("_, _, _, _, _ -> new")
    public static <E> ArrayBuffer<E> of(E value1, E value2, E value3, E value4, E value5) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        arr[4] = value5;
        return new ArrayBuffer<>(arr, 5);
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> ArrayBuffer<E> of(E... values) {
        return from(values);
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> ArrayBuffer<E> from(E @NotNull [] values) {
        int length = values.length;
        if (length == 0) {
            return new ArrayBuffer<>();
        }
        Object[] newValues = new Object[length];
        System.arraycopy(values, 0, newValues, 0, length);
        return new ArrayBuffer<>(newValues, length);
    }

    @NotNull
    public static <E> ArrayBuffer<E> from(@NotNull Iterable<? extends E> values) {
        ArrayBuffer<E> buffer = new ArrayBuffer<>();
        buffer.appendAll(values);
        return buffer;
    }

    @NotNull
    public static <E> ArrayBuffer<E> from(@NotNull Iterator<? extends E> it) {
        ArrayBuffer<E> buffer = new ArrayBuffer<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer;
    }

    //endregion

    //region ArrayBuffer helpers

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
        if (oldCapacity == 0) {
            return new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }

        int newCapacity = Math.max(Math.max(oldCapacity, minCapacity), oldCapacity + (oldCapacity >> 1));
        return new Object[newCapacity];
    }

    private void checkInBound(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
    }

    public void sizeHint(int s) {
        int len = elements.length;
        int size = this.size;

        if (s > 0 && s + size > len) {
            grow(size + s);
        }
    }

    //endregion

    //region Buffer members

    @Override
    public final void append(E value) {
        if (size == elements.length) {
            grow();
        }
        elements[size++] = value;
    }

    @Override
    public final void appendAll(@NotNull Iterable<? extends E> collection) {
        Objects.requireNonNull(collection);

        int knowSize = CollectionHelper.knowSize(collection);
        if (knowSize > 0 && size + knowSize > elements.length) {
            grow(size + knowSize);
        }

        for (E e : collection) {
            this.append(e);
        }
    }

    @Override
    public final void prepend(E value) {
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
    public final void prependAll(@NotNull Iterable<? extends E> collection) {
        Objects.requireNonNull(collection);
        if (collection instanceof IndexedSeq<?>) {
            IndexedSeq<?> seq = (IndexedSeq<?>) collection;
            int s = seq.size();
            Object[] values = elements;
            if (values.length < size + s) {
                values = growArray(size + s);
            }
            System.arraycopy(elements, 0, values, s, size);
            for (int i = 0; i < s; i++) {
                values[i] = seq.get(i);
            }
            elements = values;
            size += s;
            return;
        }


        Object[] cv = CollectionHelper.asArray(collection);
        if (cv.length == 0) {
            return;
        }

        Object[] values = elements;
        if (values.length < size + cv.length) {
            values = growArray(size + cv.length);
        }

        System.arraycopy(elements, 0, values, cv.length, size);
        System.arraycopy(cv, 0, values, 0, cv.length);
        elements = values;
        size += cv.length;
    }

    @Override
    public final void insert(int index, E value) {
        int size = this.size;
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size) {
            append(value);
        }
        if (elements.length == size) {
            grow();
        }

        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = value;
        ++this.size;
    }

    @Override
    public final void insertAll(int index, @NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }

        IndexedSeq<Object> seq = CollectionHelper.asIndexedSeq(values);
        int seqSize = seq.size();

        Object[] elements = this.elements;
        if (elements.length < size + seqSize) {
            elements = growArray(size + seqSize);
        }
        System.arraycopy(this.elements, 0, elements, 0, index);
        System.arraycopy(this.elements, index, elements, index + seqSize, size - index);

        for (int i = 0; i < seqSize; i++) {
            elements[i + index] = seq.get(i);
        }

        this.elements = elements;
        size += seqSize;
    }

    @Override
    public final void insertAll(int index, E @NotNull [] values) {
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
        size += values.length;
    }

    @Override
    public final E remove(int index) {
        checkInBound(index);
        E v = (E) elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index);
        --size;
        return v;
    }

    @Override
    public final void remove(int index, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count: " + count);
        }
        if (index < 0 || index > size - count) {
            throw new IndexOutOfBoundsException(String.format("index: %d, count: %d", index, count));
        }

        System.arraycopy(elements, index + count, elements, index, size - (index + count));
        Arrays.fill(elements, size - count, size, null);
        size -= count;
    }

    @Override
    public final void clear() {
        Arrays.fill(elements, null);
        size = 0;
    }

    @Override
    public final void takeInPlace(int n) {
        if (n <= 0) {
            clear();
        } else if (n < size) {
            Arrays.fill(elements, n, elements.length, null);
            size = n;
        }
    }

    //endregion

    //region MutableSeq members

    @Override
    public final E get(int index) {
        checkInBound(index);
        return (E) elements[index];
    }

    @Override
    public final void set(int index, E newValue) {
        checkInBound(index);
        elements[index] = newValue;
    }

    @Override
    public final void sort(Comparator<? super E> comparator) {
        Arrays.sort(elements, 0, size, (Comparator<? super Object>) comparator);
    }

    //endregion

    //region MutableCollection

    @Override
    public final String className() {
        return "ArrayBuffer";
    }

    @NotNull
    @Override
    public final <U> CollectionFactory<U, ?, ArrayBuffer<U>> iterableFactory() {
        return factory();
    }

    @Override
    public final int size() {
        return size;
    }

    @NotNull
    @Override
    public final BufferEditor<E, ArrayBuffer<E>> edit() {
        return new BufferEditor<>(this);
    }

    @NotNull
    @Override
    public final Iterator<E> iterator() {
        return (Iterator<E>) JavaArray.iterator(elements, 0, size);
    }

    @NotNull
    @Override
    public final Object[] toArray() {
        final int size = this.size;
        Object[] arr = new Object[size];
        System.arraycopy(elements, 0, arr, 0, size);
        return arr;
    }

    @NotNull
    @Override
    @SuppressWarnings("SuspiciousSystemArraycopy")
    public final <U> U[] toArray(@NotNull IntFunction<U[]> generator) {
        final int size = this.size;
        U[] arr = generator.apply(size);
        System.arraycopy(elements, 0, arr, 0, size);
        return arr;
    }

    @NotNull
    @Override
    public final ImmutableArray<E> toImmutableArray() {
        final int size = this.size;
        if (size == 0) {
            return ImmutableArray.empty();
        }
        Object[] arr = new Object[size];
        System.arraycopy(elements, 0, arr, 0, size);
        return (ImmutableArray<E>) ImmutableArray.Unsafe.wrap(arr);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final ArrayBuffer<E> clone() {
        return new ArrayBuffer<>(elements == JavaArray.EMPTY_OBJECT_ARRAY ? elements : elements.clone(), size);
    }

    //endregion

    //region Serialization

    private void writeObject(ObjectOutputStream out) throws IOException {
        final int size = this.size;
        out.writeInt(size);
        if (size != 0) {
            Object[] values = elements.length == size ? elements : Arrays.copyOf(elements, size);
            out.writeObject(values);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        final int size = this.size = in.readInt();
        if (size == 0) {
            elements = JavaArray.EMPTY_OBJECT_ARRAY;
        } else {
            elements = (Object[]) in.readObject();
        }
    }

    //endregion

    private static final class Factory<E> extends AbstractBufferFactory<E, ArrayBuffer<E>> {
        @Override
        public final ArrayBuffer<E> newBuilder() {
            return new ArrayBuffer<>();
        }

        @Override
        public void sizeHint(@NotNull ArrayBuffer<E> buffer, int size) {
            buffer.sizeHint(size);
        }
    }
}
