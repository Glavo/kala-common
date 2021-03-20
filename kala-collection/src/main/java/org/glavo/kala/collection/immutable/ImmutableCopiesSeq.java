package org.glavo.kala.collection.immutable;

import org.glavo.kala.Conditions;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.SeqLike;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.mutable.MutableArray;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.IndexedConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public final class ImmutableCopiesSeq<E> extends AbstractImmutableSeq<E> implements IndexedSeq<E> {
    private static final long serialVersionUID = 6615175156982747837L;

    private static final ImmutableCopiesSeq<?> EMPTY = new ImmutableCopiesSeq<>(0, null);

    private final @Range(from = 0, to = Integer.MAX_VALUE) int size;
    private final E value;

    ImmutableCopiesSeq(@Range(from = 0, to = Integer.MAX_VALUE) int size, E value) {
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
        if (size == 0) {
            return new ImmutableCopiesSeq<>(1, value);
        }
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
        if (size == 0) {
            return new ImmutableCopiesSeq<>(1, value);
        }
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
    public final @Nullable E firstOrNull() {
        return size == 0 ? null : value;
    }

    @Override
    public final @NotNull Option<E> firstOption() {
        return size == 0 ? Option.none() : Option.some(value);
    }

    @Override
    public final E last() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public final @Nullable E lastOrNull() {
        return size == 0 ? null : value;
    }

    @Override
    public final @NotNull Option<E> lastOption() {
        return size == 0 ? Option.none() : Option.some(value);
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

    //region Misc Operations

    @Override
    public final @NotNull ImmutableCopiesSeq<E> slice(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size);
        final int ns = endIndex - beginIndex;
        return ns == 0 ? ImmutableCopiesSeq.empty() : new ImmutableCopiesSeq<>(ns, value);
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> drop(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= size) {
            return ImmutableCopiesSeq.empty();
        }
        return new ImmutableCopiesSeq<>(size - n, value);
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> dropLast(int n) {
        return drop(n);
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        final int size = this.size;
        final E value = this.value;
        for (int i = 0; i < size; i++) {
            if (!predicate.test(value)) {
                return drop(i);
            }
        }
        return ImmutableCopiesSeq.empty();
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> take(int n) {
        if (n <= 0) {
            return ImmutableCopiesSeq.empty();
        }
        if (n >= size) {
            return this;
        }
        return new ImmutableCopiesSeq<>(n, value);
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> takeLast(int n) {
        return take(n);
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        final int size = this.size;
        final E value = this.value;
        for (int i = 0; i < size; i++) {
            if (!predicate.test(value)) {
                return take(i);
            }
        }
        return this;
    }

    @Override
    public final @NotNull ImmutableSeq<E> concat(@NotNull SeqLike<? extends E> other) {
        if (other instanceof ImmutableCopiesSeq) {
            ImmutableCopiesSeq<E> ics = (ImmutableCopiesSeq<E>) other;
            if (size == 0) {
                return ics;
            }
            if (ics.size == 0) {
                return this;
            }
            if (ics.value == this.value) {
                return new ImmutableCopiesSeq<>(this.size + ics.size, value);
            }
        }
        if (size == 0) {
            return ImmutableVector.narrow(other.toImmutableVector());
        }
        Objects.requireNonNull(other);
        final ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
        for (int i = 0; i < size; i++) {
            builder.add(value);
        }
        builder.addAll(other);
        return builder.build();
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> filter(@NotNull Predicate<? super E> predicate) {
        final int size = this.size;
        final E value = this.value;

        int c = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(value)) {
                c++;
            }
        }

        if (c == 0) {
            return ImmutableCopiesSeq.empty();
        }
        if (c == size) {
            return this;
        }
        return new ImmutableCopiesSeq<>(c, value);
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        final int size = this.size;
        final E value = this.value;

        int c = 0;
        for (int i = 0; i < size; i++) {
            if (!predicate.test(value)) {
                c++;
            }
        }

        if (c == 0) {
            return ImmutableCopiesSeq.empty();
        }
        if (c == size) {
            return this;
        }
        return new ImmutableCopiesSeq<>(c, value);
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<@NotNull E> filterNotNull() {
        return value == null ? ImmutableCopiesSeq.empty() : this;
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> sorted() {
        return this;
    }

    @Override
    public final @NotNull ImmutableCopiesSeq<E> sorted(Comparator<? super E> comparator) {
        return this;
    }

    //endregion

    //region Aggregate Operations

    @Override
    public final E max() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E max(Comparator<? super E> comparator) {
        return max();
    }

    @Override
    public final @Nullable E maxOrNull() {
        return size == 0 ? null : value;
    }

    @Override
    public final @Nullable E maxOrNull(@NotNull Comparator<? super E> comparator) {
        return maxOrNull();
    }

    @Override
    public final @NotNull Option<E> maxOption() {
        return size == 0 ? Option.none() : Option.some(value);
    }

    @Override
    public final @NotNull Option<E> maxOption(Comparator<? super E> comparator) {
        return maxOption();
    }

    @Override
    public final E min() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Override
    public E min(Comparator<? super E> comparator) {
        return min();
    }

    @Override
    public final @Nullable E minOrNull() {
        return size == 0 ? null : value;
    }

    @Override
    public final @Nullable E minOrNull(@NotNull Comparator<? super E> comparator) {
        return minOrNull();
    }

    @Override
    public final @NotNull Option<E> minOption() {
        return size == 0 ? Option.none() : Option.some(value);
    }

    @Override
    public final @NotNull Option<E> minOption(Comparator<? super E> comparator) {
        return minOption();
    }

    //endregion

    //region Copy Operations

    @Override
    public int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        final int dl = dest.length;
        final int size = this.size;

        if (destPos >= dl || srcPos >= size) {
            return 0;
        }

        final int n = Math.min(Math.min(size - srcPos, dl - destPos), limit);
        final E value = this.value;

        final int end = n + destPos;
        for (int i = destPos; i < end; i++) {
            dest[i] = value;
        }

        return n;
    }


    //endregion

    //region Conversion Operations

    @Override
    public final Object @NotNull [] toArray() {
        final Object[] res = new Object[size];
        final E value = this.value;
        if (value != null) {
            Arrays.fill(res, value);
        }
        return res;
    }

    @Override
    public final <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        final U[] res = generator.apply(size);
        final E value = this.value;
        if (value != null) {
            Arrays.fill(res, value);
        }
        return res;
    }

    @Override
    public final @NotNull <K, V> ImmutableMap<K, V> toImmutableMap() {
        if (size == 0) {
            return ImmutableMap.empty();
        }
        java.util.Map.Entry<K, V> entry = (java.util.Map.Entry<K, V>) value;
        return ImmutableMap.of(entry.getKey(), entry.getValue());
    }

    //endregion

    //region Traverse Operations

    @Override
    public final void forEach(@NotNull Consumer<? super E> action) {
        for (int i = 0; i < size; i++) {
            action.accept(value);
        }
    }

    @Override
    public final void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        for (int i = 0; i < size; i++) {
            action.accept(i, value);
        }
    }

    //endregion
}
