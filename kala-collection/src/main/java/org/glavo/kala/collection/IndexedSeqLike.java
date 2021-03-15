package org.glavo.kala.collection;

import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.immutable.ImmutableList;
import org.glavo.kala.collection.internal.view.IndexedSeqViews;
import org.glavo.kala.collection.base.Growable;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.IndexedBiFunction;
import org.glavo.kala.function.IndexedConsumer;
import org.glavo.kala.function.IndexedFunction;
import org.glavo.kala.tuple.primitive.IntObjTuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.*;

public interface IndexedSeqLike<E> extends SeqLike<E>, RandomAccess {

    //region Collection Operations

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
    default @NotNull IndexedSeqView<E> view() {
        return new IndexedSeqViews.Of<>(this);
    }

    //endregion

    //region Size Info

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default int knownSize() {
        return size();
    }

    //endregion

    //region Positional Access Operations

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

    //endregion

    //region Reversal Operations

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

    //endregion

    //region Element Retrieval Operations

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
    default E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return get(0);
    }

    @Override
    default E first(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = get(i);
            if (predicate.test(e)) {
                return e;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    default @Nullable E firstOrNull() {
        return isEmpty() ? null : get(0);
    }

    @Override
    default @Nullable E firstOrNull(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = get(i);
            if (predicate.test(e)) {
                return e;
            }
        }
        return null;
    }

    @Override
    default @NotNull Option<E> firstOption() {
        return isEmpty() ? Option.none() : Option.some(get(0));
    }

    @Override
    default @NotNull Option<E> firstOption(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = get(i);
            if (predicate.test(e)) {
                return Option.some(e);
            }
        }
        return Option.none();
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
    default E last(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            E e = get(i);
            if (predicate.test(e)) {
                return e;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    default @Nullable E lastOrNull() {
        final int size = size();
        return size == 0 ? null : get(size - 1);
    }

    @Override
    default @Nullable E lastOrNull(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            E e = get(i);
            if (predicate.test(e)) {
                return e;
            }
        }
        return null;
    }

    @Override
    default @NotNull Option<E> lastOption() {
        final int size = size();
        return size == 0 ? Option.none() : Option.some(get(size - 1));
    }

    @Override
    default @NotNull Option<E> lastOption(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            E e = get(i);
            if (predicate.test(e)) {
                return Option.some(e);
            }
        }
        return Option.none();
    }

    //endregion

    //region Element Conditions

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

    //endregion

    //region Search Operations

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

    //endregion


    @Override
    default <G extends Growable<? super E>> @NotNull G filterTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        for (int i = 0; i < this.size(); i++) {
            E e = this.get(i);
            if (predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Override
    default <G extends Growable<? super E>> @NotNull G filterNotTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        for (int i = 0; i < this.size(); i++) {
            E e = this.get(i);
            if (!predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Override
    default <G extends Growable<? super E>> @NotNull G filterNotNullTo(@NotNull G destination) {
        for (int i = 0; i < this.size(); i++) {
            E e = this.get(i);
            if (e != null) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<? super U>> @NotNull G mapTo(@NotNull G destination, @NotNull Function<? super E, ? extends U> mapper) {
        for (int i = 0; i < this.size(); i++) {
            destination.plusAssign(mapper.apply(this.get(i)));
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<@NotNull ? super U>> @NotNull G mapNotNullTo(
            @NotNull G destination,
            @NotNull Function<? super E, ? extends U> mapper) {
        for (int i = 0; i < this.size(); i++) {
            U u = mapper.apply(this.get(i));
            if (u != null) {
                destination.plusAssign(u);
            }
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<? super U>> @NotNull G mapIndexedTo(@NotNull G destination, @NotNull IndexedFunction<? super E, ? extends U> mapper) {
        for (int i = 0; i < this.size(); i++) {
            destination.plusAssign(mapper.apply(i, this.get(i)));
        }
        return destination;
    }

    @Override
    default @NotNull IndexedSeqView<IntObjTuple2<E>> withIndex() {
        return view().withIndex();
    }

    //region Aggregate Operations

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
    default <U> U foldLeftIndexed(U zero, @NotNull IndexedBiFunction<? super U, ? super E, ? extends U> op) {
        final int size = size();

        for (int i = 0; i < size; i++) {
            zero = op.apply(i, zero, get(i));
        }
        return zero;
    }

    @Override
    default <U> U foldRightIndexed(U zero, @NotNull IndexedBiFunction<? super E, ? super U, ? extends U> op) {
        final int size = size();

        for (int i = size - 1; i >= 0; i--) {
            zero = op.apply(i, get(i), zero);
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

    //endregion

    @Override
    default int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        final int dl = dest.length;
        final int size = size();

        if (destPos >= dl || srcPos >= size) {
            return 0;
        }

        final int n = Math.min(Math.min(size - srcPos, dl - destPos), limit);

        for (int i = 0; i < n; i++) {
            dest[i + destPos] = get(i + srcPos);
        }

        return n;
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

}
