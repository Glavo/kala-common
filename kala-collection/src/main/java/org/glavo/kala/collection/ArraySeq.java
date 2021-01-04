package org.glavo.kala.collection;

import org.glavo.kala.traversable.Traversable;
import org.glavo.kala.control.Option;
import org.glavo.kala.collection.immutable.ImmutableArray;
import org.glavo.kala.collection.mutable.ArrayBuffer;
import org.glavo.kala.factory.CollectionFactory;
import org.glavo.kala.function.IndexedConsumer;
import org.glavo.kala.traversable.JavaArray;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("unchecked")
public class ArraySeq<E> extends AbstractSeq<E> implements Seq<E>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 4981379062449237945L;

    public static final ArraySeq<?> EMPTY = new ArraySeq<>(JavaArray.EMPTY_OBJECT_ARRAY);

    private static final ArraySeq.Factory<?> FACTORY = new ArraySeq.Factory<>();

    protected final Object @NotNull [] elements;

    protected ArraySeq(Object @NotNull [] elements) {
        this.elements = elements;
    }

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    public static <E> ArraySeq<E> narrow(ArraySeq<? extends E> seq) {
        return (ArraySeq<E>) seq;
    }

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, ? extends ArraySeq<E>> factory() {
        return (Factory<E>) FACTORY;
    }

    @Contract("_ -> new")
    public static <E> @NotNull ArraySeq<E> wrap(@NotNull E[] array) {
        Objects.requireNonNull(array);
        return new ArraySeq<>(array);
    }

    public static <E> @NotNull ArraySeq<E> empty() {
        return (ArraySeq<E>) EMPTY;
    }

    public static <E> @NotNull ArraySeq<E> of() {
        return empty();
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1) {
        return new ArraySeq<>(new Object[]{value1});
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1, E value2) {
        return new ArraySeq<>(new Object[]{value1, value2});
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1, E value2, E value3) {
        return new ArraySeq<>(new Object[]{value1, value2, value3});
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1, E value2, E value3, E value4) {
        return new ArraySeq<>(new Object[]{value1, value2, value3, value4});
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E> @NotNull ArraySeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ArraySeq<>(new Object[]{value1, value2, value3, value4, value5});
    }

    @SafeVarargs
    @Contract(pure = true)
    public static <E> @NotNull ArraySeq<E> of(E... values) {
        return from(values);
    }

    @Contract(pure = true)
    public static <E> @NotNull ArraySeq<E> from(E @NotNull [] values) {
        return values.length == 0
                ? empty()
                : new ArraySeq<>(values.clone());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Traversable<? extends E> values) {
        if (values instanceof ImmutableArray<?>) {
            return (ArraySeq<E>) values;
        }

        if (values.knownSize() == 0) { // implicit null check of values
            return empty();
        }

        Object[] arr = values.toArray();
        if (arr.length == 0) {
            return empty();
        }
        return new ArraySeq<>(arr);
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull java.util.Collection<? extends E> values) {
        return values.size() == 0
                ? empty()
                : new ArraySeq<>(values.toArray());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values instanceof Traversable<?>) {
            return from((Traversable<E>) values);
        }
        if (values instanceof java.util.Collection<?>) {
            return from(((java.util.Collection<E>) values));
        }

        return new ArraySeq<>(ArrayBuffer.<E>from(values).toArray());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new ArraySeq<>(ArrayBuffer.<E>from(it).toArray());
    }

    public static <E> @NotNull ArraySeq<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        if (value != null) {
            Arrays.fill(ans, value);
        }
        return new ArraySeq<>(ans);
    }

    public static <E> @NotNull ArraySeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = supplier.get();
        }
        return new ArraySeq<>(ans);
    }

    public static <E> @NotNull ArraySeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = init.apply(i);
        }
        return new ArraySeq<>(ans);
    }

    //endregion

    //region Collection Operations

    @Override
    public String className() {
        return "ArraySeq";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, ? extends ArraySeq<U>> iterableFactory() {
        return factory();
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        return (Iterator<E>) JavaArray.iterator(elements);
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
        return JavaArray.indexOf(elements, value);
    }

    @Override
    public final int indexOf(Object value, int from) {
        return JavaArray.indexOf(elements, value, from);
    }

    @Override
    public final int indexWhere(@NotNull Predicate<? super E> predicate) {
        return JavaArray.indexWhere(elements, (Predicate<Object>) predicate);
    }

    @Override
    public final int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
        return JavaArray.indexWhere(elements, (Predicate<Object>) predicate, from);
    }

    @Override
    public final int lastIndexOf(Object value) {
        return JavaArray.lastIndexOf(elements, value);
    }

    @Override
    public final int lastIndexOf(Object value, int end) {
        return JavaArray.lastIndexOf(elements, value, end);
    }

    @Override
    public final int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        return JavaArray.lastIndexWhere(elements, (Predicate<Object>) predicate);
    }

    @Override
    public final int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
        return JavaArray.lastIndexWhere(elements, (Predicate<Object>) predicate, end);
    }

    @Override
    public final E max() {
        return (E) JavaArray.Unsafe.max(elements);
    }

    @Override
    public final E max(@NotNull Comparator<? super E> comparator) {
        return (E) JavaArray.max(elements, (Comparator<Object>) comparator);
    }

    @Override
    public final @NotNull Option<E> maxOption() {
        final Object[] elements = this.elements;
        if (elements.length == 0) {
            return Option.none();
        }
        return Option.some((E) JavaArray.Unsafe.max(elements));
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
        return (E) JavaArray.Unsafe.min(elements);
    }

    @Override
    public final E min(@NotNull Comparator<? super E> comparator) {
        return (E) JavaArray.min(elements, (Comparator<Object>) comparator);
    }

    @Override
    public final @NotNull Option<E> minOption() {
        final Object[] elements = this.elements;
        if (elements.length == 0) {
            return Option.none();
        }
        return Option.some((E) JavaArray.Unsafe.min(elements));
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
        return (E) JavaArray.fold(elements, zero, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op) {
        return JavaArray.foldLeft(elements, zero, (BiFunction<U, Object, U>) op);
    }

    @Override
    public final <U> U foldRight(U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op) {
        return JavaArray.foldRight(elements, zero, (BiFunction<Object, U, U>) op);
    }

    @Override
    public final E reduce(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        return (E) JavaArray.reduce(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final E reduceLeft(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        return (E) JavaArray.reduceLeft(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        return (E) JavaArray.reduceRight(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final @NotNull Option<E> reduceOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return (Option<E>) JavaArray.reduceOption(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final @NotNull Option<E> reduceLeftOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return (Option<E>) JavaArray.reduceLeftOption(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final @NotNull Option<E> reduceRightOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return (Option<E>) JavaArray.reduceRightOption(elements, (BiFunction<Object, Object, ?>) op);
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
        final int size = elements.length;

        Object[] res = new Object[size];
        if (size != 0) {
            System.arraycopy(elements, 0, res, 0, size);
        }
        return res;
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
    public final @NotNull Iterator<E> reverseIterator() {
        return (Iterator<E>) JavaArray.reverseIterator(elements);
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

    @Override
    public final int hashCode() {
        int ans = 0;
        for (Object o : elements) {
            ans = ans * 31 + Objects.hashCode(o);
        }
        return ans + Collection.SEQ_HASH_MAGIC;
    }

    private static final class Factory<E> implements CollectionFactory<E, ArrayBuffer<E>, ArraySeq<E>> {

        @Override
        public final ArraySeq<E> empty() {
            return ArraySeq.empty();
        }

        @Override
        public final ArraySeq<E> from(E @NotNull [] values) {
            return ArraySeq.from(values);
        }

        @Override
        public final ArraySeq<E> from(@NotNull Iterable<? extends E> values) {
            return ArraySeq.from(values);
        }

        @Override
        public final ArraySeq<E> from(@NotNull Iterator<? extends E> it) {
            return ArraySeq.from(it);
        }

        @Override
        public final ArraySeq<E> fill(int n, E value) {
            return ArraySeq.fill(n, value);
        }

        @Override
        public final ArraySeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return ArraySeq.fill(n, supplier);
        }

        @Override
        public final ArraySeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ArraySeq.fill(n, init);
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
        public final ArrayBuffer<E> mergeBuilder(@NotNull ArrayBuffer<E> builder1, @NotNull ArrayBuffer<E> builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public final void sizeHint(@NotNull ArrayBuffer<E> buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public final ArraySeq<E> build(@NotNull ArrayBuffer<E> buffer) {
            return new ArraySeq<>(buffer.toArray());
        }
    }
}
