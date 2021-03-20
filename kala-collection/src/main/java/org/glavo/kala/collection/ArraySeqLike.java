package org.glavo.kala.collection;

import org.glavo.kala.Conditions;
import org.glavo.kala.collection.base.GenericArrays;
import org.glavo.kala.collection.base.ObjectArrays;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.IndexedConsumer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("ALL")
public class ArraySeqLike<E> implements SeqLike<E>, IndexedSeqLike<E> {

    protected final Object @NotNull [] elements;

    protected ArraySeqLike(Object @NotNull [] elements) {
        this.elements = elements;
    }

    //region Collection Operations

    @Override
    public @NotNull String className() {
        return "ArraySeq";
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        return (Iterator<E>) GenericArrays.iterator(elements);
    }

    @Override
    public @NotNull Spliterator<E> spliterator() {
        return Spliterators.spliterator(elements, 0);
    }

    @Override
    public final @NotNull Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public final @NotNull Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    //endregion

    //region Size Info

    @Override
    public final boolean isEmpty() {
        return elements.length == 0;
    }

    @Override
    public final int size() {
        return elements.length;
    }

    @Override
    public final int knownSize() {
        return elements.length;
    }

    //endregion

    //region Positional Access Operations

    public final E get(int index) {
        return (E) elements[index];
    }

    //endregion

    //region Search Operations

    @Override
    public final int binarySearch(E value, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, elements.length);
        return Arrays.binarySearch(elements, beginIndex, endIndex, value);
    }

    @Override
    public int binarySearch(E value, Comparator<? super E> comparator, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, elements.length);
        return Arrays.binarySearch((E[]) elements, beginIndex, endIndex, value, comparator);
    }

    //endregion

    @Override
    public final E first() {
        try {
            return (E) elements[0];
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public final E last() {
        final Object[] elements = this.elements;
        final int size = elements.length;
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) elements[size - 1];
    }

    @Override
    public final int indexOf(Object value) {
        return GenericArrays.indexOf(elements, value);
    }

    @Override
    public final int indexOf(Object value, int from) {
        return GenericArrays.indexOf(elements, value, from);
    }

    @Override
    public final int indexWhere(@NotNull Predicate<? super E> predicate) {
        return GenericArrays.indexWhere(elements, (Predicate<Object>) predicate);
    }

    @Override
    public final int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
        return GenericArrays.indexWhere(elements, (Predicate<Object>) predicate, from);
    }

    @Override
    public final int lastIndexOf(Object value) {
        return GenericArrays.lastIndexOf(elements, value);
    }

    @Override
    public final int lastIndexOf(Object value, int end) {
        return GenericArrays.lastIndexOf(elements, value, end);
    }

    @Override
    public final int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        return GenericArrays.lastIndexWhere(elements, (Predicate<Object>) predicate);
    }

    @Override
    public final int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
        return GenericArrays.lastIndexWhere(elements, (Predicate<Object>) predicate, end);
    }

    @Override
    public @NotNull ArraySliceView<E> sliceView(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, elements.length);
        return new ArraySliceView<>(elements, beginIndex, endIndex);
    }

    @Override
    public final E max() {
        return (E) ObjectArrays.max(elements);
    }

    @Override
    public final E max(@NotNull Comparator<? super E> comparator) {
        return (E) GenericArrays.max(elements, (Comparator<Object>) comparator);
    }

    @Override
    public final @NotNull Option<E> maxOption() {
        final Object[] elements = this.elements;
        if (elements.length == 0) {
            return Option.none();
        }
        return Option.some((E) ObjectArrays.max(elements));
    }

    @Override
    public final @NotNull Option<E> maxOption(@NotNull Comparator<? super E> comparator) {
        if (elements.length == 0) {
            return Option.none();
        }
        return Option.some(max(comparator));
    }

    @Override
    public final E min() {
        return (E) ObjectArrays.min(elements);
    }

    @Override
    public final E min(@NotNull Comparator<? super E> comparator) {
        return (E) GenericArrays.min(elements, (Comparator<Object>) comparator);
    }

    @Override
    public final @NotNull Option<E> minOption() {
        final Object[] elements = this.elements;
        if (elements.length == 0) {
            return Option.none();
        }
        return Option.some((E) ObjectArrays.min(elements));
    }

    @Override
    public final @NotNull Option<E> minOption(@NotNull Comparator<? super E> comparator) {
        if (elements.length == 0) {
            return Option.none();
        }
        return Option.some(min(comparator));
    }

    @Override
    public final E fold(E zero, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return (E) GenericArrays.fold(elements, zero, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op) {
        return GenericArrays.foldLeft(elements, zero, (BiFunction<U, Object, U>) op);
    }

    @Override
    public final <U> U foldRight(U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op) {
        return GenericArrays.foldRight(elements, zero, (BiFunction<Object, U, U>) op);
    }

    @Override
    public final E reduce(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        return (E) GenericArrays.reduce(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final E reduceLeft(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        return (E) GenericArrays.reduceLeft(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        return (E) GenericArrays.reduceRight(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final @NotNull Option<E> reduceOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return (Option<E>) GenericArrays.reduceOption(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final @NotNull Option<E> reduceLeftOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return (Option<E>) GenericArrays.reduceLeftOption(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final @NotNull Option<E> reduceRightOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return (Option<E>) GenericArrays.reduceRightOption(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final boolean anyMatch(@NotNull Predicate<? super E> predicate) {
        for (Object e : elements) {
            if (predicate.test((E) e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final boolean allMatch(@NotNull Predicate<? super E> predicate) {
        for (Object e : elements) {
            if (!predicate.test((E) e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean noneMatch(@NotNull Predicate<? super E> predicate) {
        for (Object e : elements) {
            if (predicate.test((E) e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final boolean contains(Object value) {
        final Object[] elements = this.elements;

        if (elements.length == 0) {
            return false;
        }
        if (value == null) {
            for (Object e : elements) {
                if (null == e) {
                    return true;
                }
            }
        } else {
            for (Object e : elements) {
                if (value.equals(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public final boolean containsAll(@NotNull Iterable<?> values) {
        for (Object v : values) {
            if (!contains(v)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public final int count(@NotNull Predicate<? super E> predicate) {
        int c = 0;
        for (Object e : this.elements) {
            if (predicate.test(((E) e))) {
                ++c;
            }
        }
        return c;
    }

    @Override
    public final @NotNull Option<E> find(@NotNull Predicate<? super E> predicate) {
        for (Object e : elements) {
            if (predicate.test((E) e)) {
                return Option.some((E) e);
            }
        }
        return Option.none();
    }

    @Override
    public final <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        try {
            buffer.append(prefix);
            if (size > 0) {
                buffer.append(Objects.toString(elements[0]));
                for (int i = 1; i < size; i++) {
                    buffer.append(separator);
                    buffer.append(Objects.toString(elements[i]));
                }
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return buffer;
    }

    @Override
    public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix, @NotNull Function<? super E, ? extends CharSequence> transform) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        try {
            buffer.append(prefix);
            if (size > 0) {
                buffer.append(transform.apply((E) elements[0]));
                for (int i = 1; i < size; i++) {
                    buffer.append(separator);
                    buffer.append(transform.apply((E) elements[i]));
                }
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return buffer;
    }

    @Override
    public final <R, Builder> R collect(@NotNull Collector<? super E, Builder, ? extends R> collector) {
        if (collector instanceof CollectionFactory<?, ?, ?>) {
            return collect((CollectionFactory<? super E, Builder, ? extends R>) collector);
        }

        final Object[] elements = this.elements;

        Builder builder = collector.supplier().get();
        if (elements.length == 0) {
            return collector.finisher().apply(builder);
        }
        final BiConsumer<Builder, ? super E> accumulator = collector.accumulator();
        for (Object o : elements) {
            accumulator.accept(builder, (E) o);
        }
        return collector.finisher().apply(builder);
    }

    @Override
    public final <R, Builder> R collect(@NotNull CollectionFactory<? super E, Builder, ? extends R> factory) {
        final Object[] elements = this.elements;
        final int length = elements.length;

        if (length == 0) {
            return factory.empty();
        }

        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, length);
        for (Object e : elements) {
            factory.addToBuilder(builder, (E) e);
        }
        return factory.build(builder);
    }

    @Override
    public final Object @NotNull [] toArray() {
        final Object[] elements = this.elements;
        return Arrays.copyOf(elements, elements.length, Object[].class);
    }

    @Override
    @SuppressWarnings("SuspiciousSystemArraycopy")
    public final <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        U[] res = generator.apply(size);
        if (size != 0) {
            System.arraycopy(elements, 0, res, 0, size);
        }
        return res;
    }

    @Override
    public final int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        final Object[] elements = this.elements;

        final int dl = dest.length;
        final int size = elements.length;

        if (destPos >= dl || srcPos >= size) {
            return 0;
        }

        final int n = Math.min(Math.min(size - srcPos, dl - destPos), limit);
        System.arraycopy(elements, srcPos, dest, destPos, n);
        return n;
    }

    @Override
    public final @NotNull Iterator<E> reverseIterator() {
        return (Iterator<E>) GenericArrays.reverseIterator(elements);
    }

    @Override
    public final void forEach(@NotNull Consumer<? super E> action) {
        for (Object e : this.elements) {
            action.accept((E) e);
        }
    }

    @Override
    public final void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {

        final Object[] elements = this.elements;
        final int length = elements.length;
        for (int i = 0; i < length; i++) {
            action.accept(i, (E) elements[i]);
        }
    }

}
