package asia.kala.collection.mutable;

import asia.kala.collection.internal.CollectionHelper;
import asia.kala.traversable.Traversable;
import asia.kala.collection.*;
import asia.kala.factory.CollectionFactory;
import asia.kala.traversable.JavaArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public final class MutableArray<E> extends ArraySeq<E> implements MutableSeq<E>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 6278999671163491762L;

    public static final MutableArray<?> EMPTY = new MutableArray<>(JavaArray.EMPTY_OBJECT_ARRAY);

    private static final MutableArray.Factory<?> FACTORY = new Factory<>();

    private final boolean isChecked;

    //region Constructors

    MutableArray(@NotNull Object[] array) {
        this(array, false);
    }

    MutableArray(@NotNull Object[] array, boolean isChecked) {
        super(array);
        this.isChecked = isChecked;
    }

    public MutableArray(int size) {
        this(new Object[size]);
    }

    //endregion

    //region Factory methods

    @NotNull
    public static <E> CollectionFactory<E, ?, MutableArray<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @NotNull
    public static <E> MutableArray<E> empty() {
        return (MutableArray<E>) EMPTY;
    }

    @NotNull
    public static <E> MutableArray<E> of() {
        return (MutableArray<E>) EMPTY;
    }

    @NotNull
    @Contract("_ -> new")
    public static <E> MutableArray<E> of(E value1) {
        return new MutableArray<>(new Object[]{value1});
    }

    @NotNull
    @Contract("_, _ -> new")
    public static <E> MutableArray<E> of(E value1, E value2) {
        return new MutableArray<>(new Object[]{value1, value2});
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static <E> MutableArray<E> of(E value1, E value2, E value3) {
        return new MutableArray<>(new Object[]{value1, value2, value3});
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static <E> MutableArray<E> of(E value1, E value2, E value3, E value4) {
        return new MutableArray<>(new Object[]{value1, value2, value3, value4});
    }

    @NotNull
    @Contract("_, _, _, _, _ -> new")
    public static <E> MutableArray<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new MutableArray<>(new Object[]{value1, value2, value3, value4, value5});
    }

    @NotNull
    public static <E> MutableArray<E> of(@NotNull E... values) {
        return from(values);
    }

    @NotNull
    public static <E> MutableArray<E> from(E @NotNull [] values) {
        final int length = values.length;
        if (length == 0) {
            return empty();
        }

        Object[] newValues = new Object[length];
        System.arraycopy(values, 0, newValues, 0, length);
        return new MutableArray<>(newValues);
    }

    @NotNull
    public static <E> MutableArray<E> from(@NotNull Traversable<? extends E> values) {
        Objects.requireNonNull(values);

        if (CollectionHelper.knowSize(values) == 0) {
            return empty();
        }

        return new MutableArray<>(values.toArray());
    }

    @NotNull
    public static <E> MutableArray<E> from(@NotNull java.util.Collection<? extends E> values) {
        Objects.requireNonNull(values);

        if (values.size() == 0) {
            return empty();
        }

        return new MutableArray<>(values.toArray());
    }

    @NotNull
    public static <E> MutableArray<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values instanceof Traversable<?>) {
            return from((Traversable<E>) values);
        }

        if (values instanceof java.util.Collection<?>) {
            return from(((java.util.Collection<E>) values));
        }

        ArrayBuffer<E> buffer = new ArrayBuffer<>();
        buffer.appendAll(values);
        if (buffer.size() == 0) {
            return empty();
        }
        return new MutableArray<>(buffer.toArray());
    }

    @NotNull
    public static <E> MutableArray<E> wrap(E @NotNull [] array) {
        Objects.requireNonNull(array);
        return new MutableArray<>(array, true);
    }

    //endregion

    //region MutableArray members

    public final Object[] getArray() {
        return array;
    }

    public final boolean isChecked() {
        return isChecked;
    }

    //endregion

    //region MutableSeq members

    @Override
    public final void set(int index, E newValue) {
        array[index] = newValue;
    }

    @Override
    public final void mapInPlace(@NotNull Function<? super E, ? extends E> mapper) {
        final Object[] array = this.array;
        for (int i = 0; i < array.length; i++) {
            array[i] = mapper.apply((E) array[i]);
        }
    }

    @Override
    public final void sort(@NotNull Comparator<? super E> comparator) {
        Arrays.sort(array, (Comparator<? super Object>) comparator);
    }

    //endregion

    //region MutableCollection members

    @Override
    public final String className() {
        return "MutableArray";
    }

    @NotNull
    @Override
    public final <U> CollectionFactory<U, ?, MutableArray<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public final MutableArray<E> clone() {
        if (this == EMPTY) {
            return this;
        }
        return new MutableArray<>(this.array.clone(), isChecked);
    }

    //endregion

    private static final class Factory<E> implements CollectionFactory<E, ArrayBuffer<E>, MutableArray<E>> {
        Factory() {
        }

        @Override
        public final MutableArray<E> from(E @NotNull [] values) {
            return MutableArray.from(values);
        }

        @Override
        public final MutableArray<E> from(@NotNull Iterable<? extends E> values) {
            return MutableArray.from(values);
        }

        @Override
        public final ArrayBuffer<E> newBuilder() {
            return new ArrayBuffer<>();
        }

        @Override
        public final void addToBuilder(@NotNull ArrayBuffer<E> buffer, E value) {
            buffer.append(value);
        }

        @Override
        public final void sizeHint(@NotNull ArrayBuffer<E> buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public final ArrayBuffer<E> mergeBuilder(@NotNull ArrayBuffer<E> builder1, @NotNull ArrayBuffer<E> builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public final MutableArray<E> build(@NotNull ArrayBuffer<E> buffer) {
            return new MutableArray<>(buffer.toArray(Object[]::new));
        }
    }
}
