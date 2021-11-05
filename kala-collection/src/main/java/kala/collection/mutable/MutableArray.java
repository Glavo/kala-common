package kala.collection.mutable;

import kala.Conditions;
import kala.collection.ArraySeq;
import kala.collection.IndexedSeq;
import kala.collection.base.GenericArrays;
import kala.collection.base.Traversable;
import kala.function.IndexedFunction;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class MutableArray<E> extends ArraySeq<E> implements MutableSeq<E>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 8060307722127719792L;

    public static final MutableArray<?> EMPTY = new MutableArray<>(GenericArrays.EMPTY_OBJECT_ARRAY);

    private static final MutableArray.Factory<?> FACTORY = new Factory<>();

    //region Constructors

    MutableArray(Object @NotNull [] array) {
        super(array);
    }

    //endregion

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, MutableArray<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArray<E> create(int size) {
        return new MutableArray<>(new Object[size]);
    }

    public static <E> @NotNull MutableArray<E> empty() {
        return (MutableArray<E>) EMPTY;
    }

    public static <E> @NotNull MutableArray<E> of() {
        return (MutableArray<E>) EMPTY;
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1) {
        return new MutableArray<>(new Object[]{value1});
    }

    @Contract("_, _ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1, E value2) {
        return new MutableArray<>(new Object[]{value1, value2});
    }

    @Contract("_, _, _ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1, E value2, E value3) {
        return new MutableArray<>(new Object[]{value1, value2, value3});
    }

    @Contract("_, _, _, _ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1, E value2, E value3, E value4) {
        return new MutableArray<>(new Object[]{value1, value2, value3, value4});
    }

    @Contract("_, _, _, _, _ -> new")
    public static <E> @NotNull MutableArray<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new MutableArray<>(new Object[]{value1, value2, value3, value4, value5});
    }

    public static <E> @NotNull MutableArray<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull MutableArray<E> from(E @NotNull [] values) {
        final int length = values.length; // implicit null check of values+
        if (length == 0) {
            return empty();
        }

        Object[] newValues = new Object[length];
        System.arraycopy(values, 0, newValues, 0, length);
        return new MutableArray<>(newValues);
    }

    public static <E> @NotNull MutableArray<E> from(@NotNull Traversable<? extends E> values) {
        return values.knownSize() == 0 // implicit null check of values
                ? empty()
                : new MutableArray<>(values.toArray());

    }

    public static <E> @NotNull MutableArray<E> from(@NotNull java.util.Collection<? extends E> values) {
        return values.size() == 0 // implicit null check of values
                ? empty()
                : new MutableArray<>(values.toArray());

    }

    public static <E> @NotNull MutableArray<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values instanceof Traversable<?>) {
            return from((Traversable<E>) values);
        }

        if (values instanceof java.util.Collection<?>) {
            return from(((java.util.Collection<E>) values));
        }

        return from(values.iterator());
    }

    public static <E> @NotNull MutableArray<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        DynamicArray<E> buffer = new DynamicArray<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return new MutableArray<>(buffer.toArray());
    }

    public static <E> @NotNull MutableArray<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    public static <E> @NotNull MutableArray<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        if (value != null) {
            Arrays.fill(ans, value);
        }
        return new MutableArray<>(ans);
    }

    public static <E> @NotNull MutableArray<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.get();
        }
        return new MutableArray<>(ans);
    }

    public static <E> @NotNull MutableArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = init.apply(i);
        }
        return new MutableArray<>(ans);
    }

    public static <E> @NotNull MutableArray<E> wrap(E @NotNull [] array) {
        Objects.requireNonNull(array);
        return new Checked<>(array);
    }

    //endregion

    //region Collection Operations

    @Override
    public final @NotNull String className() {
        return "MutableArray";
    }

    @Override
    public final <U> @NotNull CollectionFactory<U, ?, MutableArray<U>> iterableFactory() {
        return factory();
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableArray<E> clone() {
        return new MutableArray<>(this.elements.clone());
    }

    //endregion

    public final Object @NotNull [] getArray() {
        return elements;
    }

    public boolean isChecked() {
        return false;
    }

    @Override
    public final void set(int index, E newValue) {
        try {
            elements[index] = newValue;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(e.getMessage());
        }
    }

    @Override
    public final @NotNull MutableArraySliceView<E> sliceView(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, elements.length);
        return new MutableArraySliceView<>(elements, beginIndex, endIndex);
    }

    @Override
    public final void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
        final Object[] elements = this.elements;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = operator.apply((E) elements[i]);
        }
    }

    @Override
    public final void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
        final Object[] elements = this.elements;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = operator.apply(i, (E) elements[i]);
        }
    }

    @Override
    public final void sort(@NotNull Comparator<? super E> comparator) {
        Arrays.sort(elements, (Comparator<? super Object>) comparator);
    }

    private static final class Checked<E> extends MutableArray<E> {
        private static final long serialVersionUID = 3903230112786321463L;

        Checked(Object @NotNull [] array) {
            super(array);
        }

        @Override
        public boolean isChecked() {
            return true;
        }

        @Override
        public @NotNull MutableArray<E> clone() {
            return new Checked<>(this.elements.clone());
        }
    }

    private static final class Factory<E> implements CollectionFactory<E, DynamicArray<E>, MutableArray<E>> {
        Factory() {
        }

        @Override
        public MutableArray<E> from(E @NotNull [] values) {
            return MutableArray.from(values);
        }

        @Override
        public MutableArray<E> from(@NotNull Iterable<? extends E> values) {
            return MutableArray.from(values);
        }

        @Override
        public @NotNull MutableArray<E> from(@NotNull Iterator<? extends E> it) {
            return MutableArray.from(it);
        }

        @Override
        public MutableArray<E> fill(int n, E value) {
            return MutableArray.fill(n, value);
        }

        @Override
        public MutableArray<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return MutableArray.fill(n, supplier);
        }

        @Override
        public MutableArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return MutableArray.fill(n, init);
        }

        @Override
        public DynamicArray<E> newBuilder() {
            return new DynamicArray<>();
        }

        @Override
        public void addToBuilder(@NotNull DynamicArray<E> buffer, E value) {
            buffer.append(value);
        }

        @Override
        public void sizeHint(@NotNull DynamicArray<E> buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public DynamicArray<E> mergeBuilder(@NotNull DynamicArray<E> builder1, @NotNull DynamicArray<E> builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public MutableArray<E> build(@NotNull DynamicArray<E> buffer) {
            return new MutableArray<>(buffer.toArray());
        }
    }
}
