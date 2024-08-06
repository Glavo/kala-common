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
package kala.collection.mutable;

import kala.Conditions;
import kala.collection.ArraySeq;
import kala.collection.base.GenericArrays;
import kala.collection.base.Traversable;
import kala.function.IndexedFunction;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class MutableArray<E> extends ArraySeq<E> implements MutableSeq<E>, Serializable {
    @Serial
    private static final long serialVersionUID = 8060307722127719792L;

    public static final MutableArray<?> EMPTY = new MutableArray<>(GenericArrays.EMPTY_OBJECT_ARRAY);

    private static final MutableArray.Factory<?> FACTORY = new Factory<>();

    //region Constructors

    private MutableArray(Object @NotNull [] array) {
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
        final int length = values.length; // implicit null check of values
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
        return values.isEmpty() // implicit null check of values
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
        MutableArrayList<E> buffer = new MutableArrayList<>();
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

    public static <E> @NotNull MutableArray<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        MutableArrayList<E> builder = new MutableArrayList<>();
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;
            builder.append(value);
        }
        return new MutableArray<>(builder.toArray());
    }

    public static <E> @NotNull MutableArray<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        MutableArrayList<E> builder = new MutableArrayList<>();
        while (true) {
            E value = supplier.get();
            if (value == null)
                break;
            builder.append(value);
        }
        return new MutableArray<>(builder.toArray());
    }

    public static <E> @NotNull MutableArray<E> wrap(E @NotNull [] array) {
        Objects.requireNonNull(array);
        return new MutableArray<>(array);
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "MutableArray";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, MutableArray<U>> iterableFactory() {
        return factory();
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableArray<E> clone() {
        return new MutableArray<>(this.elements.clone());
    }

    public Object @NotNull [] getArray() {
        return elements;
    }

    @Override
    public void set(int index, E newValue) {
        elements[index] = newValue;
    }

    @Override
    public @NotNull MutableArraySliceView<E> sliceView(int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, elements.length);
        return new MutableArraySliceView<>(elements, beginIndex, endIndex);
    }

    @Override
    public void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
        final Object[] elements = this.elements;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = operator.apply((E) elements[i]);
        }
    }

    @Override
    public void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
        final Object[] elements = this.elements;
        for (int i = 0; i < elements.length; i++) {
            elements[i] = operator.apply(i, (E) elements[i]);
        }
    }

    @Override
    public void sort(@NotNull Comparator<? super E> comparator) {
        Arrays.sort(elements, (Comparator<? super Object>) comparator);
    }

    private static final class Factory<E> implements CollectionFactory<E, MutableArrayList<E>, MutableArray<E>> {
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
        public MutableArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return MutableArray.fill(n, init);
        }

        @Override
        public MutableArrayList<E> newBuilder() {
            return new MutableArrayList<>();
        }

        @Override
        public void addToBuilder(@NotNull MutableArrayList<E> buffer, E value) {
            buffer.append(value);
        }

        @Override
        public void sizeHint(@NotNull MutableArrayList<E> buffer, int size) {
            buffer.sizeHint(size);
        }

        @Override
        public MutableArrayList<E> mergeBuilder(@NotNull MutableArrayList<E> builder1, @NotNull MutableArrayList<E> builder2) {
            builder1.appendAll(builder2);
            return builder1;
        }

        @Override
        public MutableArray<E> build(@NotNull MutableArrayList<E> buffer) {
            return new MutableArray<>(buffer.toArray());
        }
    }
}
