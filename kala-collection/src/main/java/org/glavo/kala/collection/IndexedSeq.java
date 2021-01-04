package org.glavo.kala.collection;

import org.glavo.kala.control.Option;
import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.immutable.ImmutableList;
import org.glavo.kala.collection.immutable.ImmutableVector;
import org.glavo.kala.factory.CollectionFactory;
import org.glavo.kala.function.IndexedConsumer;
import org.glavo.kala.iterator.Iterators;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;

public interface IndexedSeq<@Covariant E> extends Seq<E>, RandomAccess {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> IndexedSeq<E> narrow(IndexedSeq<? extends E> seq) {
        return (IndexedSeq<E>) seq;
    }

    //endregion

    //region Static Factories

    @NotNull
    @Contract(pure = true)
    static <E> CollectionFactory<E, ?, ? extends IndexedSeq<E>> factory() {
        return ImmutableVector.factory();
    }

    @NotNull
    @SafeVarargs
    static <E> IndexedSeq<E> of(E... values) {
        return IndexedSeq.<E>factory().from(values);
    }

    @NotNull
    static <E> IndexedSeq<E> from(E @NotNull [] values) {
        return IndexedSeq.<E>factory().from(values);
    }

    @NotNull
    static <E> IndexedSeq<E> from(@NotNull Iterable<? extends E> values) {
        return IndexedSeq.<E>factory().from(values);
    }

    //endregion

    E get(int index);

    int size();

    //region Optimized Collection Operations

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default int knownSize() {
        return size();
    }

    @Override
    default boolean isDefinedAt(int index) {
        return index >= 0 && index < size();
    }

    @Override
    default @Nullable E getOrNull(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        return get(index);
    }

    @Override
    default @NotNull Option<E> getOption(int index) {
        if (index < 0 || index >= size()) {
            return Option.none();
        }
        return Option.some(get(index));
    }

    @Override
    default E first() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        return get(0);
    }

    @Override
    default E last() {
        final int size = size();
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return get(size - 1);
    }

    @Override
    default int indexOf(Object value) {
        final int size = size();

        if (value == null) {
            for (int i = 0; i < size; i++) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    default int indexOf(Object value, int from) {
        final int size = size();

        if (from >= size) {
            return -1;
        }

        if (value == null) {
            for (int i = Math.max(from, 0); i < size; i++) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = Math.max(from, 0); i < size; i++) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    default int indexWhere(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
        final int size = size();

        if (from >= size) {
            return -1;
        }

        for (int i = Math.max(from, 0); i < size; i++) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default int lastIndexOf(Object value) {
        if (value == null) {
            for (int i = size() - 1; i >= 0; i--) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = size() - 1; i >= 0; i--) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    default int lastIndexOf(Object value, int end) {
        if (end < 0) {
            return -1;
        }
        if (value == null) {
            for (int i = end; i >= 0; i--) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = end; i >= 0; i--) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        for (int i = size() - 1; i >= 0; i--) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
        if (end < 0) {
            return -1;
        }
        for (int i = end; i >= 0; i--) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default E max(@NotNull Comparator<? super E> comparator) {
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E res = get(0);

        for (int i = 1; i < size; i++) {
            E e = get(i);
            if (comparator.compare(res, e) < 0) {
                res = e;
            }
        }

        return res;
    }

    @Override
    default @NotNull Option<E> maxOption(@NotNull Comparator<? super E> comparator) {
        if (isEmpty()) {
            return Option.none();
        }
        return Option.some(max(comparator));
    }

    @Override
    default E min(@NotNull Comparator<? super E> comparator) {
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E res = get(0);

        for (int i = 1; i < size; i++) {
            E e = get(i);
            if (comparator.compare(res, e) > 0) {
                res = e;
            }
        }

        return res;
    }

    @Override
    default @NotNull Option<E> minOption(@NotNull Comparator<? super E> comparator) {
        if (isEmpty()) {
            return Option.none();
        }
        return Option.some(min(comparator));
    }

    @Override
    default int copyToArray(Object @NotNull [] array, int start) {
        if (start < 0) {
            throw new IllegalArgumentException("start: " + start);
        }

        final int size = size();
        final int arrayLength = array.length;

        if (start > arrayLength || size == 0) {
            return 0;
        }

        final int n = Math.min(size, arrayLength - start);

        for (int i = 0; i < n; i++) {
            array[i + start] = get(i);
        }

        return n;
    }

    @NotNull
    @Override
    default <A extends Appendable> A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        final int size = size();

        try {
            buffer.append(prefix);
            if (size > 0) {
                buffer.append(Objects.toString(get(0)));
                for (int i = 1; i < size; i++) {
                    buffer.append(separator).append(Objects.toString(get(i)));
                }
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return buffer;
    }

    @Override
    default <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op) {
        final int size = size();

        for (int i = 0; i < size; i++) {
            zero = op.apply(zero, get(i));
        }
        return zero;
    }

    @Override
    default <U> U foldRight(U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op) {
        final int size = size();

        for (int i = size - 1; i >= 0; i--) {
            zero = op.apply(get(i), zero);
        }
        return zero;
    }

    @Override
    default E reduceLeft(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E e = get(0);
        for (int i = 1; i < size; i++) {
            e = op.apply(e, get(i));
        }
        return e;
    }

    @Override
    default E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E e = get(size - 1);
        for (int i = size - 2; i >= 0; i--) {
            e = op.apply(get(i), e);
        }
        return e;
    }

    @Override
    default @NotNull Option<E> reduceLeftOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int size = size();

        if (size == 0) {
            return Option.none();
        }

        E e = get(0);
        for (int i = 1; i < size; i++) {
            e = op.apply(e, get(i));
        }
        return Option.some(e);
    }

    @Override
    default @NotNull Option<E> reduceRightOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int size = size();

        if (size == 0) {
            return Option.none();
        }

        E e = get(size - 1);
        for (int i = size - 2; i >= 0; i--) {
            e = op.apply(get(i), e);
        }
        return Option.some(e);
    }

    @Override
    default boolean anyMatch(@NotNull Predicate<? super E> predicate) {
        final int size = size();

        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean allMatch(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (!predicate.test(get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean noneMatch(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean contains(Object value) {
        final int size = size();

        if (size == 0) {
            return false;
        }

        if (value == null) {
            for (int i = 0; i < size; i++) {
                if (null == get(i)) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (value.equals(get(i))) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    default int count(@NotNull Predicate<? super E> predicate) {
        final int size = size();

        int c = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                ++c;
            }
        }
        return c;
    }

    @Override
    default @NotNull Option<E> find(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();

        for (int i = 0; i < size; i++) {
            E element = get(i);
            if (predicate.test(element)) {
                return Option.some(element);
            }
        }
        return Option.none();
    }

    @Override
    default @NotNull Iterator<E> iterator() {
        final int size = size();

        if (size == 0) {
            return Iterators.empty();
        }

        return new Iterator<E>() {
            private int idx = 0;

            @Override
            public final boolean hasNext() {
                return idx < size;
            }

            @Override
            public final E next() {
                if (idx >= size) {
                    throw new NoSuchElementException();
                }
                return get(idx++);
            }
        };
    }

    @Override
    default @NotNull Iterator<E> reverseIterator() {
        return new Iterator<E>() {
            private int idx = size() - 1;

            @Override
            public final boolean hasNext() {
                return idx >= 0;
            }

            @Override
            public final E next() {
                if (idx < 0) {
                    throw new NoSuchElementException();
                }
                return get(idx--);
            }
        };
    }

    @Override
    default void forEach(@NotNull Consumer<? super E> action) {
        final int size = this.size();

        for (int i = 0; i < size; i++) {
            action.accept(get(i));
        }
    }

    @Override
    default void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        final int size = this.size();

        for (int i = 0; i < size; i++) {
            action.accept(i, get(i));
        }
    }

    @Override
    default @NotNull IndexedSeqView<E> view() {
        return new IndexedSeqViews.Of<>(this);
    }

    @Override
    default Object @NotNull [] toArray() {
        final int size = size();
        Object[] arr = new Object[size];

        for (int i = 0; i < size; i++) {
            arr[i] = get(i);
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    default <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        final int size = size();
        U[] arr = generator.apply(size); // implicit null check of generator

        for (int i = 0; i < size; i++) {
            arr[i] = (U) get(i);
        }
        return arr;
    }

    @Override
    default @NotNull ImmutableList<E> toImmutableList() {
        final int size = size();

        ImmutableList<E> list = ImmutableList.nil();
        for (int i = size - 1; i >= 0; i--) {
            list = list.cons(get(i));
        }
        return list;
    }

    //endregion
}
