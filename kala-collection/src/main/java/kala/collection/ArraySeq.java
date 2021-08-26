package kala.collection;

import kala.collection.base.GenericArrays;
import kala.collection.base.ObjectArrays;
import kala.collection.base.Traversable;
import kala.Conditions;
import kala.collection.immutable.ImmutableArray;
import kala.collection.internal.view.IndexedSeqViews;
import kala.collection.mutable.DynamicArray;
import kala.collection.factory.CollectionFactory;
import kala.control.Option;
import kala.function.IndexedConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Debug;
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
@Debug.Renderer(hasChildren = "!isEmpty()", childrenArray = "elements")
public class ArraySeq<E> extends AbstractSeq<E> implements Seq<E>, IndexedSeq<E>, Serializable {
    private static final long serialVersionUID = 4981379062449237945L;

    public static final ArraySeq<?> EMPTY = new ArraySeq<>(GenericArrays.EMPTY_OBJECT_ARRAY);

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

        return new ArraySeq<>(DynamicArray.<E>from(values).toArray());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new ArraySeq<>(DynamicArray.<E>from(it).toArray());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Stream<? extends E> stream) {
        return (ArraySeq<E>) stream.collect(factory());
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
    public @NotNull String className() {
        return "ArraySeq";
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        return (Iterator<E>) GenericArrays.iterator(elements);
    }

    @Override
    public @NotNull Iterator<E> iterator(int beginIndex) {
        return (Iterator<E>) GenericArrays.iterator(elements, beginIndex);
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

    @Override
    public final @NotNull Iterator<E> reverseIterator() {
        return (Iterator<E>) GenericArrays.reverseIterator(elements);
    }

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

    //region Element Retrieval Operations

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

    //endregion

    //region Element Conditions

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

    //endregion

    //region Search Operations

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

    //endregion

    @Override
    public @NotNull IndexedSeqView<E> sliceView(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, elements.length);
        final int ns = endIndex - beginIndex;
        switch (ns) {
            case 0:
                return IndexedSeqView.empty();
            case 1:
                return new IndexedSeqViews.Single<>((E) elements[beginIndex]);
        }
        return new IndexedSeqViews.OfArraySlice<>(elements, beginIndex, endIndex);
    }

    //region Aggregate Operations

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
    public final E reduceLeft(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        return (E) GenericArrays.reduceLeft(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        return (E) GenericArrays.reduceRight(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final @NotNull Option<E> reduceLeftOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return (Option<E>) GenericArrays.reduceLeftOption(elements, (BiFunction<Object, Object, ?>) op);
    }

    @Override
    public final @NotNull Option<E> reduceRightOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        return (Option<E>) GenericArrays.reduceRightOption(elements, (BiFunction<Object, Object, ?>) op);
    }

    //endregion


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

    //region Conversion Operations

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

    //endregion

    //region Traverse Operations

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

    //endregion

    @Override
    public final int hashCode() {
        int ans = 0;
        for (Object o : elements) {
            ans = ans * 31 + Objects.hashCode(o);
        }
        return ans + SEQ_HASH_MAGIC;
    }

    //region String Representation

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

    //endregion

    private static final class Factory<E> implements CollectionFactory<E, DynamicArray<E>, ArraySeq<E>> {

        @Override
        public ArraySeq<E> empty() {
            return ArraySeq.empty();
        }

        @Override
        public ArraySeq<E> from(E @NotNull [] values) {
            return ArraySeq.from(values);
        }

        @Override
        public ArraySeq<E> from(@NotNull Iterable<? extends E> values) {
            return ArraySeq.from(values);
        }

        @Override
        public ArraySeq<E> from(@NotNull Iterator<? extends E> it) {
            return ArraySeq.from(it);
        }

        @Override
        public ArraySeq<E> fill(int n, E value) {
            return ArraySeq.fill(n, value);
        }

        @Override
        public ArraySeq<E> fill(int n, @NotNull Supplier<? extends E> supplier) {
            return ArraySeq.fill(n, supplier);
        }

        @Override
        public ArraySeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ArraySeq.fill(n, init);
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
        public DynamicArray<E> mergeBuilder(@NotNull DynamicArray<E> builder1, @NotNull DynamicArray<E> builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public void sizeHint(@NotNull DynamicArray<E> buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public ArraySeq<E> build(@NotNull DynamicArray<E> buffer) {
            return new ArraySeq<>(buffer.toArray());
        }
    }
}
