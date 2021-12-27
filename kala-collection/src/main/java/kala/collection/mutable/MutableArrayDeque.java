package kala.collection.mutable;

import kala.Conditions;
import kala.collection.IndexedSeq;
import kala.collection.base.Iterators;
import kala.collection.base.ObjectArrays;
import kala.collection.factory.CollectionFactory;
import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public final class MutableArrayDeque<E> extends AbstractMutableSeq<E> implements MutableIndexedSeqDeque<E>, Serializable {
    private static final long serialVersionUID = -4166302067142375121L;

    private static final Factory<?> FACTORY = new Factory<>();

    static final int DEFAULT_CAPACITY = 16;

    public Object[] elements; // TODO: private
    private int begin = -1;
    private int end = 0;

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
            System.arraycopy(elements, begin, newElements, 0, oldCapacity - begin);
            System.arraycopy(elements, 0, newElements, oldCapacity - begin, end);

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
        return (Factory<E>) FACTORY;
    }

    public static <E> MutableArrayDeque<E> create() {
        return new MutableArrayDeque<>();
    }

    public static <E> MutableArrayDeque<E> create(int initialCapacity) {
        return new MutableArrayDeque<>(initialCapacity);
    }


    //endregion

    //region Collection Operations

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

    //endregion

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

    private static final class Factory<E> implements CollectionFactory<E, MutableArrayDeque<E>, MutableArrayDeque<E>> {

        @Override
        public MutableArrayDeque<E> newBuilder() {
            return new MutableArrayDeque<>();
        }

        @Override
        public MutableArrayDeque<E> build(MutableArrayDeque<E> es) {
            return es;
        }

        @Override
        public void addToBuilder(@NotNull MutableArrayDeque<E> es, E value) {
            es.append(value);
        }

        @Override
        public MutableArrayDeque<E> mergeBuilder(@NotNull MutableArrayDeque<E> builder1, @NotNull MutableArrayDeque<E> builder2) {
            Object[] tmp = builder2.toArray(); // TODO
            for (Object e : tmp) {
                ((MutableArrayDeque<Object>) builder1).append(e);
            }
            return builder1;
        }
    }
}
