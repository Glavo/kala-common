/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection;

import kala.collection.base.GenericArrays;
import kala.collection.base.ObjectArrays;
import kala.collection.base.Traversable;
import kala.Conditions;
import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.internal.CollectionHelper;
import kala.collection.internal.view.SeqViews;
import kala.collection.mutable.MutableArrayList;
import kala.collection.factory.CollectionFactory;
import kala.control.Option;
import kala.function.CheckedPredicate;
import kala.function.IndexedBiConsumer;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.*;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static kala.Conditions.checkPositionIndices;

@SuppressWarnings("unchecked")
@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "elements")
public class ArraySeq<E> extends AbstractSeq<E> implements Seq<E>, IndexedSeq<E>, Serializable {
    @Serial
    private static final long serialVersionUID = 4981379062449237945L;

    private static final ArraySeq<?> EMPTY = new ArraySeq<>(GenericArrays.EMPTY_OBJECT_ARRAY);

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
        return values.isEmpty() ? empty() : new ArraySeq<>(values.toArray());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        if (values instanceof Traversable<?>) {
            return from((Traversable<E>) values);
        }
        if (values instanceof java.util.Collection<?>) {
            return from(((java.util.Collection<E>) values));
        }

        return new ArraySeq<>(MutableArrayList.<E>from(values).toArray());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) { // implicit null check of it
            return empty();
        }
        return new ArraySeq<>(MutableArrayList.<E>from(it).toArray());
    }

    public static <E> @NotNull ArraySeq<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(ArraySeq.<E>factory());
    }

    public static <E> @NotNull ArraySeq<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }

        return new ArraySeq<>(ObjectArrays.fill(n, value));
    }

    public static <E> @NotNull ArraySeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }

        return new ArraySeq<>(ObjectArrays.fill(n, init));
    }

    public static <E> @NotNull ArraySeq<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        MutableArrayList<E> builder = new MutableArrayList<>();
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;
            builder.append(value);
        }
        return new ArraySeq<>(builder.toArray());
    }

    public static <E> @NotNull ArraySeq<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        MutableArrayList<E> builder = new MutableArrayList<>();
        while (true) {
            E value = supplier.get();
            if (value == null)
                break;
            builder.append(value);
        }
        return new ArraySeq<>(builder.toArray());
    }


    //endregion

    @Override
    public @NotNull String className() {
        return "ArraySeq";
    }

    @Override
    public final @NotNull Iterator<E> iterator() {
        return (Iterator<E>) GenericArrays.iterator(elements);
    }

    @Override
    public @NotNull Iterator<E> iterator(@Index int beginIndex) {
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

    public final E get(@Index int index) {
        return (E) elements[Indexes.checkElementIndex(index, elements.length)];
    }

    //endregion

    //region Addition Operations

    @Override
    public @NotNull ImmutableSeq<E> prepended(E value) {
        Object[] newValues = new Object[elements.length + 1];
        newValues[0] = value;
        System.arraycopy(elements, 0, newValues, 1, elements.length);

        return ImmutableArray.Unsafe.wrap(newValues);
    }

    @Override
    public @NotNull ImmutableSeq<E> prependedAll(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of prefix
            return ImmutableArray.empty();
        }

        Object[] newValues = Arrays.copyOf(values, values.length + elements.length, Object[].class);
        System.arraycopy(elements, 0, newValues, values.length, elements.length);

        return ImmutableArray.Unsafe.wrap(newValues);
    }

    @Override
    public @NotNull ImmutableSeq<E> prependedAll(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        Object[] data = values instanceof ImmutableArray<?> ?
                ((ImmutableArray<?>) values).elements : CollectionHelper.asArray(values);
        Object[] newValues = new Object[data.length + elements.length];

        System.arraycopy(data, 0, newValues, 0, data.length);
        System.arraycopy(elements, 0, newValues, data.length, elements.length);

        return ImmutableArray.Unsafe.wrap(newValues);
    }

    @Override
    public @NotNull ImmutableSeq<E> appended(E value) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        Object[] newValues = Arrays.copyOf(elements, size + 1);
        newValues[size] = value;

        return ImmutableArray.Unsafe.wrap(newValues);
    }

    @Override
    public @NotNull ImmutableSeq<E> appendedAll(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of values
            return ImmutableArray.empty();
        }

        final Object[] elements = this.elements;
        final int size = elements.length;

        Object[] newValues = Arrays.copyOf(elements, values.length + size);
        System.arraycopy(values, 0, newValues, size, values.length);

        return ImmutableArray.Unsafe.wrap(newValues);
    }

    @Override
    public @NotNull ImmutableSeq<E> appendedAll(@NotNull Iterable<? extends E> values) {
        Objects.requireNonNull(values);

        Object[] data = values instanceof ImmutableArray<?>
                ? ((ImmutableArray<?>) values).elements
                : CollectionHelper.asArray(values);
        Object[] newValues = Arrays.copyOf(elements, elements.length + data.length);
        System.arraycopy(data, 0, newValues, elements.length, data.length);

        return ImmutableArray.Unsafe.wrap(newValues);
    }

    //endregion

    @Override
    public @NotNull ImmutableSeq<E> slice(int beginIndex, int endIndex) {
        final Object[] elements = this.elements;
        final int size = elements.length;
        checkPositionIndices(beginIndex, endIndex, size);

        final int ns = endIndex - beginIndex;
        if (ns == 0) {
            return ImmutableArray.empty();
        }
        if (ns == size) {
            return this.toImmutableArray();
        }

        return ImmutableArray.Unsafe.wrap(Arrays.copyOfRange(elements, beginIndex, endIndex));
    }

    @Override
    public @NotNull ImmutableSeq<E> drop(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this.toImmutableArray();
        }

        final Object[] elements = this.elements;
        final int size = elements.length;

        if (n >= size) {
            return ImmutableArray.empty();
        }

        return ImmutableArray.Unsafe.wrap(Arrays.copyOfRange(elements, n, size));
    }

    @Override
    public @NotNull ImmutableSeq<E> dropLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this.toImmutableArray();
        }
        return take(Integer.max(0, size() - n)); // TODO
    }

    @Override
    public @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        int count = 0;
        while (count < size && predicate.test((E) elements[count])) {
            ++count;
        }

        return drop(count);
    }

    @Override
    public @NotNull ImmutableSeq<E> take(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        final int size = elements.length;
        if (n == 0) {
            return ImmutableSeq.empty();
        }
        if (n >= size) {
            return this.toImmutableArray();
        }

        return ImmutableArray.Unsafe.wrap(Arrays.copyOf(elements, n));
    }

    @Override
    public @NotNull ImmutableSeq<E> takeLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return ImmutableArray.empty();
        }
        return drop(Integer.max(0, size() - n)); // TODO
    }

    @Override
    public @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        int count = 0;
        while (count < size && predicate.test((E) elements[count])) { // implicit null check of predicate
            ++count;
        }

        return take(count);
    }

    @Override
    public @NotNull ImmutableSeq<E> updated(int index, E newValue) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        Objects.checkIndex(index, size);

        Object[] newValues = elements.clone();
        newValues[index] = newValue;
        return ImmutableArray.Unsafe.wrap(newValues);
    }

    @Override
    public @NotNull ImmutableSeq<E> inserted(int index, E value) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        Conditions.checkPositionIndex(index, size);
        return ImmutableArray.Unsafe.wrap(ObjectArrays.inserted(elements, index, value));
    }

    @Override
    public @NotNull ImmutableSeq<E> removedAt(int index) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        Objects.checkIndex(index, size);

        if (size == 1) {
            return ImmutableArray.empty();
        }

        return ImmutableArray.Unsafe.wrap(ObjectArrays.removedAt(elements, index));
    }

    @Override
    public @NotNull ImmutableSeq<E> concat(@NotNull SeqLike<? extends E> other) {
        return appendedAll(other);
    }

    @Override
    public @NotNull ImmutableSeq<E> concat(@NotNull List<? extends E> other) {
        return appendedAll(other);
    }


    @Override
    public @NotNull ImmutableSeq<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            E v = (E) value;
            if (predicate.test(v)) {
                tmp[c++] = v;
            }
        }

        if (c == 0) {
            return ImmutableArray.empty();
        }
        if (c == size) {
            return this.toImmutableArray();
        }

        return ImmutableArray.Unsafe.wrap(Arrays.copyOf(tmp, c));
    }

    @Override
    public @NotNull <Ex extends Throwable> ImmutableSeq<E> filterChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) {
        return filter(predicate);
    }

    @Override
    public @NotNull ImmutableSeq<E> filterUnchecked(@NotNull CheckedPredicate<? super E, ?> predicate) {
        return filter(predicate);
    }

    @Override
    public @NotNull ImmutableSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            E v = (E) value;
            if (!predicate.test(v)) {
                tmp[c++] = v;
            }
        }

        if (c == 0) {
            return ImmutableArray.empty();
        }
        if (c == size) {
            return this.toImmutableArray();
        }

        return ImmutableArray.Unsafe.wrap(Arrays.copyOf(tmp, c));
    }

    @Override
    public @NotNull ImmutableSeq<@NotNull E> filterNotNull() {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            E v = (E) value;
            if (v != null) {
                tmp[c++] = v;
            }
        }

        if (c == 0) {
            return ImmutableArray.empty();
        }
        if (c == size) {
            return this.toImmutableArray();
        }

        return ImmutableArray.Unsafe.wrap(Arrays.copyOf(tmp, c));
    }

    @Override
    public @NotNull <U> ImmutableSeq<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            E v = (E) value;
            if (clazz.isInstance(v)) {
                tmp[c++] = v;
            }
        }

        if (c == 0) {
            return ImmutableArray.empty();
        }
        if (c == size) {
            return (ImmutableSeq<U>) this.toImmutableArray();
        }

        return ImmutableArray.Unsafe.wrap(Arrays.copyOf(tmp, c));
    }

    @Override
    public <U> @NotNull ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        return ImmutableArray.Unsafe.wrap(ObjectArrays.map(elements, mapper));
    }

    @Override
    public <U> @NotNull ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        return ImmutableArray.Unsafe.wrap(ObjectArrays.mapIndexed(elements, mapper));
    }

    @Override
    public <U> @NotNull ImmutableSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (Object value : elements) {
            final U u = mapper.apply((E) value);
            if (u != null) {
                tmp[c++] = u;
            }
        }

        if (c == 0) {
            return ImmutableArray.empty();
        }
        if (c == size) {
            return ImmutableArray.Unsafe.wrap(tmp);
        }

        return ImmutableArray.Unsafe.wrap(Arrays.copyOf(tmp, c));
    }

    @Override
    public @NotNull <U> ImmutableSeq<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        final int size = elements.length;

        if (size == 0) {
            return ImmutableArray.empty();
        }

        Object[] tmp = new Object[size];
        int c = 0;

        for (int i = 0; i < size; i++) {
            final U u = mapper.apply(i, (E) elements[i]);
            if (u != null) {
                tmp[c++] = u;
            }
        }

        if (c == 0) {
            return ImmutableArray.empty();
        }
        if (c == size) {
            return ImmutableArray.Unsafe.wrap(tmp);
        }

        return ImmutableArray.Unsafe.wrap(Arrays.copyOf(tmp, c));
    }

    @Override
    public <U> @NotNull ImmutableSeq<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        Object[] arr = ObjectArrays.mapMulti(elements, mapper);
        return arr.length != 0 ? ImmutableArray.Unsafe.wrap(arr) : ImmutableArray.empty();
    }

    @Override
    public <U> @NotNull ImmutableSeq<U> mapIndexedMulti(@NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        Object[] arr = ObjectArrays.mapIndexedMulti(elements, mapper);
        return arr.length != 0 ? ImmutableArray.Unsafe.wrap(arr) : ImmutableArray.empty();
    }

    @Override
    public <U> @NotNull ImmutableSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        final int size = elements.length;
        if (size == 0) {
            return ImmutableArray.empty();
        }

        MutableArrayList<U> builder = new MutableArrayList<>();
        for (Object value : elements) {
            builder.appendAll(mapper.apply((E) value));
        }
        return builder.toImmutableArray();
    }

    @Override
    public @NotNull ImmutableSeq<E> sorted() {
        final Object[] elements = this.elements;
        if (elements.length == 0 || elements.length == 1) {
            return this.toImmutableArray();
        }

        Object[] newValues = elements.clone();
        Arrays.sort(newValues);
        return ImmutableArray.Unsafe.wrap(newValues);
    }

    @Override
    public @NotNull ImmutableSeq<E> sorted(Comparator<? super E> comparator) {
        final Object[] elements = this.elements;
        if (elements.length == 0 || elements.length == 1) {
            return this.toImmutableArray();
        }

        Object[] newValues = elements.clone();
        Arrays.sort(newValues, (Comparator<Object>) comparator);
        return ImmutableArray.Unsafe.wrap(newValues);
    }

    @Override
    public @NotNull ImmutableSeq<E> reversed() {
        final int size = elements.length;
        if (size == 0) {
            return ImmutableArray.empty();
        }
        return ImmutableArray.Unsafe.wrap(ObjectArrays.reversed(elements));
    }

    @Override
    public <U, R> @NotNull ImmutableSeq<R> zip(@NotNull Iterable<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
        Iterator<? extends U> it = other.iterator(); // implicit null check of other
        if (!it.hasNext()) {
            return ImmutableArray.empty();
        }

        final Object[] elements = this.elements;
        final int size = elements.length;
        Object[] tmp = new Object[size];

        int i = 0;
        while (it.hasNext() && i < size) {
            tmp[i] = mapper.apply((E) elements[i], it.next());
            ++i;
        }

        if (i < size) {
            tmp = Arrays.copyOf(tmp, i);
        }
        return ImmutableArray.Unsafe.wrap(tmp);
    }

    //region Search Operations

    @Override
    public final int binarySearch(@Index int beginIndex, @Index int endIndex, E value) {
        beginIndex = Indexes.checkBeginIndex(beginIndex, elements.length);
        endIndex = Indexes.checkEndIndex(beginIndex, endIndex, elements.length);
        return Arrays.binarySearch(elements, beginIndex, endIndex, value);
    }

    @Override
    public int binarySearch(@Index int beginIndex, @Index int endIndex, E value, Comparator<? super E> comparator) {
        beginIndex = Indexes.checkBeginIndex(beginIndex, elements.length);
        endIndex = Indexes.checkEndIndex(beginIndex, endIndex, elements.length);
        return Arrays.binarySearch((E[]) elements, beginIndex, endIndex, value, comparator);
    }

    //endregion

    //region Element Retrieval Operations

    @Override
    public final E getFirst() {
        final int size = elements.length;
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return (E) elements[0];
    }

    @Override
    public final E getLast() {
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
        if (elements.length == 0) {
            return false;
        }

        if (value != null) {
            for (Object e : elements) {
                if (value.equals(e)) {
                    return true;
                }
            }
        } else {
            for (Object e : elements) {
                if (null == e) {
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
        return ObjectArrays.anyMatch(elements, predicate);
    }

    @Override
    public final boolean allMatch(@NotNull Predicate<? super E> predicate) {
        return ObjectArrays.allMatch(elements, predicate);
    }

    @Override
    public final boolean noneMatch(@NotNull Predicate<? super E> predicate) {
        return ObjectArrays.noneMatch(elements, predicate);
    }

    //endregion

    //region Search Operations

    @Override
    public final int indexOf(Object value) {
        return ObjectArrays.indexOf(elements, value);
    }

    @Override
    public final int indexOf(Object value, int from) {
        return ObjectArrays.indexOf(elements, value, from);
    }

    @Override
    public final int indexWhere(@NotNull Predicate<? super E> predicate) {
        return ObjectArrays.indexWhere(elements, (Predicate<Object>) predicate);
    }

    @Override
    public final int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
        return ObjectArrays.indexWhere(elements, (Predicate<Object>) predicate, from);
    }

    @Override
    public final int lastIndexOf(Object value) {
        return ObjectArrays.lastIndexOf(elements, value);
    }

    @Override
    public final int lastIndexOf(Object value, int end) {
        return ObjectArrays.lastIndexOf(elements, value, end);
    }

    @Override
    public final int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        return ObjectArrays.lastIndexWhere(elements, predicate);
    }

    @Override
    public final int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
        return ObjectArrays.lastIndexWhere(elements, predicate, end);
    }

    //endregion

    @Override
    public @NotNull SeqView<E> sliceView(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, elements.length);
        final int ns = endIndex - beginIndex;
        if (ns == 0) {
            return SeqView.empty();
        } else if (ns == 1) {
            return SeqView.of((E) elements[beginIndex]);
        } else {
            return new SeqViews.OfArraySlice<>(elements, beginIndex, endIndex);
        }
    }

    //region Aggregate Operations

    @Override
    public final int count(@NotNull Predicate<? super E> predicate) {
        return ObjectArrays.count(elements, predicate);
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
    public final E min() {
        return (E) ObjectArrays.min(elements);
    }

    @Override
    public final E min(@NotNull Comparator<? super E> comparator) {
        return (E) GenericArrays.min(elements, (Comparator<Object>) comparator);
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
        return Arrays.copyOf(elements, elements.length, Object[].class);
    }

    @Override
    public final <U> U @NotNull [] toArray(@NotNull Class<U> type) {
        return Arrays.copyOf(elements, elements.length, GenericArrays.arrayType(type));
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

    private static final class Factory<E> implements CollectionFactory<E, MutableArrayList<E>, ArraySeq<E>> {

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
        public ArraySeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ArraySeq.fill(n, init);
        }

        @Override
        public MutableArrayList<E> newBuilder() {
            return new MutableArrayList<>();
        }

        @Override
        public void addToBuilder(@NotNull MutableArrayList<E> builder, E value) {
            builder.append(value);
        }

        @Override
        public MutableArrayList<E> mergeBuilder(@NotNull MutableArrayList<E> builder1, @NotNull MutableArrayList<E> builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public void sizeHint(@NotNull MutableArrayList<E> builder, int size) {
            builder.sizeHint(size);
        }

        @Override
        public ArraySeq<E> build(@NotNull MutableArrayList<E> buffer) {
            return new ArraySeq<>(buffer.toArray());
        }
    }
}
