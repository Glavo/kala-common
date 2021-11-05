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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class DynamicArray<E> extends AbstractDynamicSeq<E>
        implements DynamicSeqOps<E, DynamicArray<?>, DynamicArray<E>>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 2545219250020890853L;

    private static final int DEFAULT_CAPACITY = 16;

    private static final DynamicArray.Factory<?> FACTORY = new Factory<>();

    //region Fields

    Object @NotNull [] elements;
    int size;

    //endregion

    //region Constructors

    private DynamicArray(Object @NotNull [] elements, int size) {
        this.elements = elements;
        this.size = size;
    }

    public DynamicArray() {
        this(ObjectArrays.EMPTY, 0);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("illegal initialCapacity: " + initialCapacity);
        }

        this.elements = initialCapacity == 0 ? GenericArrays.EMPTY_OBJECT_ARRAY : new Object[initialCapacity];
        this.size = 0;
    }

    //endregion

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, DynamicArray<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @Contract("-> new")
    public static <E> @NotNull DynamicArray<E> create() {
        return new DynamicArray<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicArray<E> create(int initialCapacity) {
        return new DynamicArray<>(initialCapacity);
    }

    @Contract("-> new")
    public static <E> @NotNull DynamicArray<E> of() {
        return new DynamicArray<>();
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicArray<E> of(E value1) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        return new DynamicArray<>(arr, 1);
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull DynamicArray<E> of(E value1, E value2) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        return new DynamicArray<>(arr, 2);
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull DynamicArray<E> of(E value1, E value2, E value3) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        return new DynamicArray<>(arr, 3);
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull DynamicArray<E> of(E value1, E value2, E value3, E value4) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        return new DynamicArray<>(arr, 4);
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull DynamicArray<E> of(E value1, E value2, E value3, E value4, E value5) {
        Object[] arr = new Object[DEFAULT_CAPACITY];
        arr[0] = value1;
        arr[1] = value2;
        arr[2] = value3;
        arr[3] = value4;
        arr[4] = value5;
        return new DynamicArray<>(arr, 5);
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicArray<E> of(E... values) {
        return from(values);
    }

    @Contract("_ -> new")
    public static <E> @NotNull DynamicArray<E> from(E @NotNull [] values) {
        int length = values.length; // implicit null check of values
        if (length == 0) {
            return new DynamicArray<>();
        }
        Object[] newValues = new Object[length];
        System.arraycopy(values, 0, newValues, 0, length);
        return new DynamicArray<>(newValues, length);
    }

    public static <E> @NotNull DynamicArray<E> from(@NotNull Iterable<? extends E> values) {
        DynamicArray<E> buffer = new DynamicArray<>();
        buffer.appendAll(values);
        return buffer;
    }

    public static <E> @NotNull DynamicArray<E> from(@NotNull Iterator<? extends E> it) {
        DynamicArray<E> buffer = new DynamicArray<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer;
    }

    public static <E> @NotNull DynamicArray<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull DynamicArray<E> fill(int n, E value) {
        if (n <= 0) {
            return new DynamicArray<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        if (value != null) {
            Arrays.fill(arr, 0, n, value);
        }
        return new DynamicArray<>(arr, n);
    }

    public static <E> @NotNull DynamicArray<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return new DynamicArray<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = supplier.get();
        }
        return new DynamicArray<>(arr, n);
    }

    public static <E> @NotNull DynamicArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return new DynamicArray<>();
        }

        Object[] arr = new Object[Integer.max(DEFAULT_CAPACITY, n)];
        for (int i = 0; i < n; i++) {
            arr[i] = init.apply(i);
        }
        return new DynamicArray<>(arr, n);
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

    Object @NotNull [] getArray() {
        return this.elements;
    }

    public void sizeHint(int s) {
        int len = elements.length;
        int size = this.size;

        if (s > 0 && s + size > len) {
            grow(size + s);
        }
    }

    //endregion

    //region Collection Operations

    @Override
    public @NotNull String className() {
        return "DynamicArray";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, DynamicArray<U>> iterableFactory() {
        return factory();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return (Iterator<E>) GenericArrays.iterator(elements, 0, size);
    }

    @Override
    public @NotNull Iterator<E> iterator(int beginIndex) {
        return (Iterator<E>) GenericArrays.iterator(elements, beginIndex, size);
    }

    @Override
    public @NotNull DynamicSeqIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, size);
        return new SeqItr<>(this, index);
    }

    @Override
    public @NotNull Spliterator<E> spliterator() {
        return (Spliterator<E>) Arrays.spliterator(elements, 0, size);
    }

    @Override
    public @NotNull DynamicSeqEditor<E, DynamicArray<E>> edit() {
        return new DynamicSeqEditor<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public DynamicArray<E> clone() {
        final Object[] elements = this.elements;
        final int size = this.size;
        return new DynamicArray<>(size == 0 ? GenericArrays.EMPTY_OBJECT_ARRAY : elements.clone(), size);
    }

    //endregion

    //region Size Info

    @Override
    public int size() {
        return size;
    }

    //endregion

    //region Positional Access Operations

    @Override
    public E get(int index) {
        Conditions.checkElementIndex(index, size);
        return (E) elements[index];
    }

    @Override
    public void set(int index, E newValue) {
        Conditions.checkElementIndex(index, size);
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
        if (values instanceof SeqLike<?> && values instanceof RandomAccess) {
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
    public int binarySearch(E value, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        return Arrays.binarySearch(elements, beginIndex, endIndex, value);
    }

    @Override
    public int binarySearch(E value, Comparator<? super E> comparator, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        return Arrays.binarySearch((E[]) elements, beginIndex, endIndex, value, comparator);
    }

    //endregion

    @Override
    public void sort() {
        Arrays.sort(elements, 0, size);
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        Arrays.sort(elements, 0, size, (Comparator<? super Object>) comparator);
    }

    @Override
    public void insert(int index, E value) {
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
        size += values.length;
    }

    @Override
    public void clear() {
        Arrays.fill(elements, null);
        size = 0;
    }

    @Override
    public E removeAt(int index) {
        Conditions.checkElementIndex(index, size);
        E v = (E) elements[index];
        if (index == size - 1) {
            elements[index] = null;
        } else {
            System.arraycopy(elements, index + 1, elements, index, size - index);
        }
        size--;
        return v;
    }

    @Override
    public void removeAt(int index, int count) {
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
    public void takeInPlace(int n) {
        if (n <= 0) {
            clear();
        } else if (n < size) {
            Arrays.fill(elements, n, elements.length, null);
            size = n;
        }
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

    //region Serialization

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeObject(elements[i]);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        final int size = in.readInt();
        final Object[] elements = size == 0 ? ObjectArrays.EMPTY : new Object[Integer.max(DEFAULT_CAPACITY, size)];

        for (int i = 0; i < size; i++) {
            elements[i] = in.readObject();
        }

        this.size = size;
        this.elements = elements;
    }

    //endregion

    private static final class Factory<E> extends AbstractDynamicSeqFactory<E, DynamicArray<E>> {
        @Override
        public DynamicArray<E> newBuilder() {
            return new DynamicArray<>();
        }

        @Override
        public void sizeHint(@NotNull DynamicArray<E> buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public DynamicArray<E> from(E @NotNull [] values) {
            return DynamicArray.from(values);
        }

        @Override
        public DynamicArray<E> from(@NotNull Iterable<? extends E> values) {
            return DynamicArray.from(values);
        }

        @Override
        public DynamicArray<E> from(@NotNull Iterator<? extends E> it) {
            return DynamicArray.from(it);
        }

        @Override
        public DynamicArray<E> fill(int n, E value) {
            return DynamicArray.fill(n, value);
        }

        @Override
        public DynamicArray<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return DynamicArray.fill(n, supplier);
        }

        @Override
        public DynamicArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return DynamicArray.fill(n, init);
        }
    }

    private static final class SeqItr<E> extends AbstractDynamicSeqIterator<E> {
        private final DynamicArray<E> seq;
        private int lastReturned = -1;

        SeqItr(DynamicArray<E> seq, int index) {
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
                throw new ConcurrentModificationException(ex);
            }
        }


    }
}
