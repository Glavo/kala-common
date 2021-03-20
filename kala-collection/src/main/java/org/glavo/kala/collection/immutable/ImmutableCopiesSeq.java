package org.glavo.kala.collection.immutable;

import org.glavo.kala.Conditions;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public final class ImmutableCopiesSeq<E> extends AbstractImmutableSeq<E> implements IndexedSeq<E> {
    private static final long serialVersionUID = 6615175156982747837L;

    private static final ImmutableCopiesSeq<?> EMPTY = new ImmutableCopiesSeq<>(0, null);

    private final int size;
    private final E value;

    ImmutableCopiesSeq(int size, E value) {
        this.size = size;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static <E> @NotNull ImmutableCopiesSeq<E> empty() {
        return (ImmutableCopiesSeq<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableCopiesSeq<E> fill(int n, E value) {
        if (n <= 0) {
            return ImmutableCopiesSeq.empty();
        }
        return new ImmutableCopiesSeq<>(n, value);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return Iterators.fill(size, value);
    }

    @Override
    public final int size() {
        return size;
    }

    @Override
    public final E get(int index) {
        Conditions.checkElementIndex(index, size);
        return value;
    }

    @Override
    public final @Nullable E getOrNull(int index) {
        return index >= 0 && index < size ? value : null;
    }

    @Override
    public final @NotNull Option<E> getOption(int index) {
        return index >= 0 && index < size ? Option.some(value) : Option.none();
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> reversed() {
        return this;
    }

    @Override
    public final @NotNull Iterator<E> reverseIterator() {
        return iterator();
    }

    @Override
    public final @NotNull ImmutableSeq<E> prepended(E value) {
        final int size = this.size;
        if (value == this.value) {
            return new ImmutableCopiesSeq<>(size + 1, value);
        }
        final E oldValue = this.value;
        if (size < ImmutableVectors.WIDTH) {
            final Object[] arr = new Object[size + 1];
            Arrays.fill(arr, oldValue);
            arr[0] = value;
            return new ImmutableVectors.Vector1<>(arr);
        }
        final ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        builder.add(value);
        for (int i = 0; i < size; i++) {
            builder.add(oldValue);
        }
        return builder.build();
    }

    @Override
    public final @NotNull ImmutableSeq<E> appended(E value) {
        final int size = this.size;
        if (value == this.value) {
            assert size != Integer.MAX_VALUE;
            return new ImmutableCopiesSeq<>(size + 1, value);
        }
        final E oldValue = this.value;
        if (size < ImmutableVectors.WIDTH) {
            final Object[] arr = new Object[size + 1];
            Arrays.fill(arr, oldValue);
            arr[size] = value;
            return new ImmutableVectors.Vector1<>(arr);
        }
        final ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        for (int i = 0; i < size; i++) {
            builder.add(oldValue);
        }
        builder.add(value);
        return builder.build();
    }

    @Override
    public final E first() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public final E first(@NotNull Predicate<? super E> predicate) {
        if (size == 0 || !predicate.test(value)) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public final @Nullable E firstOrNull() {
        return size == 0 ? null : value;
    }

    @Override
    public final @Nullable E firstOrNull(@NotNull Predicate<? super E> predicate) {
        return (size == 0 || !predicate.test(value)) ? null : value;
    }

    @Override
    public final @NotNull Option<E> firstOption() {
        return size == 0 ? Option.none() : Option.some(value);
    }

    @Override
    public final @NotNull Option<E> firstOption(@NotNull Predicate<? super E> predicate) {
        return (size == 0 || !predicate.test(value)) ? Option.none() : Option.some(value);
    }

    @Override
    public final E last() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public final E last(@NotNull Predicate<? super E> predicate) {
        if (size == 0 || !predicate.test(value)) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public final @Nullable E lastOrNull() {
        return size == 0 ? null : value;
    }

    @Override
    public final @Nullable E lastOrNull(@NotNull Predicate<? super E> predicate) {
        return (size == 0 || !predicate.test(value)) ? null : value;
    }

    @Override
    public final @NotNull Option<E> lastOption() {
        return size == 0 ? Option.none() : Option.some(value);
    }

    @Override
    public final @NotNull Option<E> lastOption(@NotNull Predicate<? super E> predicate) {
        return (size == 0 || !predicate.test(value)) ? Option.none() : Option.some(value);
    }

    //region Element Conditions

    @Override
    public final boolean contains(Object value) {
        return size != 0 && Objects.equals(value, this.size);
    }

    @Override
    public final boolean containsAll(Object @NotNull [] values) {
        if (size == 0) {
            return values.length == 0;
        }

        final E value = this.value;
        if (value == null) {
            for (Object v : values) {
                if (null != v) {
                    return false;
                }
            }
        } else {
            for (Object v : values) {
                if (!value.equals(v)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public final boolean anyMatch(@NotNull Predicate<? super E> predicate) {
        return size != 0 && predicate.test(value);
    }

    @Override
    public final boolean allMatch(@NotNull Predicate<? super E> predicate) {
        return size == 0 || predicate.test(value);
    }

    @Override
    public final boolean noneMatch(@NotNull Predicate<? super E> predicate) {
        return size == 0 || !predicate.test(value);
    }

    //endregion

    //region Search Operations

    @Override
    public final int indexOf(Object value) {
        if (size == 0) {
            return -1;
        } else {
            return Objects.equals(value, this.value) ? 0 : -1;
        }
    }

    @Override
    public final int indexOf(Object value, int from) {
        if (size == 0 || from >= size) {
            return -1;
        } else {
            return Objects.equals(value, this.value) ? Integer.max(from, 0) : -1;
        }
    }

    @Override
    public final int lastIndexOf(Object value) {
        if (size == 0) {
            return -1;
        } else {
            return Objects.equals(value, this.value) ? size - 1 : -1;
        }
    }

    @Override
    public final int lastIndexOf(Object value, int end) {
        if (size == 0 || end < 0) {
            return -1;
        } else {
            return Objects.equals(value, this.value) ? Integer.min(size - 1, end) : -1;
        }
    }

    //endregion
}
