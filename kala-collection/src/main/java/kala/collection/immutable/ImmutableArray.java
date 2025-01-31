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
package kala.collection.immutable;

import kala.collection.*;
import kala.collection.base.GenericArrays;
import kala.collection.base.Traversable;
import kala.collection.factory.CollectionBuilder;
import kala.collection.mutable.MutableArrayList;
import kala.tuple.Tuple2;
import kala.annotations.Covariant;
import kala.annotations.StaticClass;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class ImmutableArray<@Covariant E> extends ArraySeq<E> implements ImmutableSeq<E>, Serializable {
    @Serial
    private static final long serialVersionUID = 1845940935381169058L;

    public static final ImmutableArray<?> EMPTY = new ImmutableArray<>(GenericArrays.EMPTY_OBJECT_ARRAY);

    private static final ImmutableArray.Factory<?> FACTORY = new Factory<>();

    private ImmutableArray(Object[] array) {
        super(array);
    }

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    public static <E> ImmutableArray<E> narrow(ImmutableArray<? extends E> array) {
        return (ImmutableArray<E>) array;
    }

    //endregion

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, ImmutableArray<E>> factory() {
        return (ImmutableArray.Factory<E>) FACTORY;
    }

    @Contract("-> new")
    public static <E> @NotNull CollectionBuilder<E, ImmutableArray<E>> newBuilder() {
        return ImmutableArray.<E>factory().newCollectionBuilder();
    }

    public static <E> @NotNull ImmutableArray<E> empty() {
        return (ImmutableArray<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableArray<E> of() {
        return (ImmutableArray<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1) {
        return new ImmutableArray<>(new Object[]{value1});
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1, E value2) {
        return new ImmutableArray<>(new Object[]{value1, value2});
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1, E value2, E value3) {
        return new ImmutableArray<>(new Object[]{value1, value2, value3});
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1, E value2, E value3, E value4) {
        return new ImmutableArray<>(new Object[]{value1, value2, value3, value4});
    }

    public static <E> @NotNull ImmutableArray<E> of(E value1, E value2, E value3, E value4, E value5) {
        return new ImmutableArray<>(new Object[]{value1, value2, value3, value4, value5});
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static <E> @NotNull ImmutableArray<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableArray<E> from(E @NotNull [] values) {
        if (values.length == 0) { // implicit null check of values
            return empty();
        }
        return new ImmutableArray<>(values.clone());
    }

    public static <E> @NotNull ImmutableArray<E> from(@NotNull Traversable<? extends E> values) {
        if (values instanceof ImmutableArray<?>) {
            return (ImmutableArray<E>) values;
        }

        if (values.knownSize() == 0) {
            return empty();
        }

        Object[] arr = values.toArray();
        if (arr.length == 0) {
            return empty();
        }
        return new ImmutableArray<>(arr);
    }

    public static <E> @NotNull ImmutableArray<E> from(@NotNull java.util.Collection<? extends E> values) {
        if (values.isEmpty()) { // implicit null check of values
            return empty();
        }
        return new ImmutableArray<>(values.toArray());
    }

    public static <E> @NotNull ImmutableArray<E> from(@NotNull Iterable<? extends E> values) {
        return switch (values) { // implicit null check of values
            case ImmutableArray<?> immutableArray -> ((ImmutableArray<E>) values);
            case Traversable<?> traversable -> from((Traversable<E>) values);
            case java.util.Collection<?> collection -> from(((java.util.Collection<E>) values));
            default -> MutableArrayList.<E>from(values).toImmutableArray();
        };

    }

    public static <E> @NotNull ImmutableArray<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return empty();
        }
        MutableArrayList<E> buffer = new MutableArrayList<>();
        while (it.hasNext()) {
            buffer.append(it.next());
        }
        return buffer.toImmutableArray();
    }

    public static <E> @NotNull ImmutableArray<E> from(@NotNull Stream<? extends E> stream) {
        final Object[] arr = stream.toArray();
        return arr.length == 0 ? ImmutableArray.empty() : new ImmutableArray<>(arr);
    }

    public static <E> @NotNull ImmutableArray<E> fill(int n, E value) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        if (value != null) {
            Arrays.fill(ans, value);
        }
        return new ImmutableArray<>(ans);
    }

    public static <E> @NotNull ImmutableArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        if (n <= 0) {
            return empty();
        }

        Object[] ans = new Object[n];
        for (int i = 0; i < n; i++) {
            ans[i] = init.apply(i);
        }
        return new ImmutableArray<>(ans);
    }

    public static <E> @NotNull ImmutableArray<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        MutableArrayList<E> builder = new MutableArrayList<>();
        while (true) {
            E value = supplier.get();
            if (predicate.test(value))
                break;
            builder.append(value);
        }
        return builder.toImmutableArray();
    }

    public static <E> @NotNull ImmutableArray<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        MutableArrayList<E> builder = new MutableArrayList<>();
        while (true) {
            E value = supplier.get();
            if (value == null)
                break;
            builder.append(value);
        }
        return builder.toImmutableArray();
    }

    @StaticClass
    public final static class Unsafe {
        private Unsafe() {
        }

        @Contract("_ -> new")
        public static <E> @NotNull ImmutableArray<E> wrap(Object @NotNull [] array) {
            Objects.requireNonNull(array);
            return new ImmutableArray<>(array);
        }
    }

    //endregion

    Object @NotNull [] getArray() {
        return elements;
    }

    @Override
    public @NotNull String className() {
        return "ImmutableArray";
    }

    @Override
    public <U> @NotNull CollectionFactory<U, ?, ImmutableArray<U>> iterableFactory() {
        return factory();
    }

    @Override
    public @NotNull Spliterator<E> spliterator() {
        return Spliterators.spliterator(elements, Spliterator.IMMUTABLE);
    }

    //@Override
    public @NotNull Tuple2<@NotNull ImmutableSeq<E>, @NotNull ImmutableSeq<E>> span(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);

        final Object[] elements = this.elements;
        final int size = elements.length;

        if (size == 0) {
            return new Tuple2<>(empty(), empty());
        }

        int idx = 0;
        while (idx < size) {
            if (!predicate.test((E) elements[idx])) {
                break;
            }
            ++idx;
        }

        if (idx == 0) {
            return new Tuple2<>(empty(), this);
        } else if (idx == size) {
            return new Tuple2<>(this, empty());
        } else {
            return new Tuple2<>(
                    new ImmutableArray<>(Arrays.copyOfRange(elements, 0, idx)),
                    new ImmutableArray<>(Arrays.copyOfRange(elements, idx, size))
            );
        }

    }

    @Override
    public @NotNull ImmutableArray<E> toImmutableArray() {
        return this;
    }

    private static final class Factory<E> implements CollectionFactory<E, MutableArrayList<E>, ImmutableArray<E>> {
        Factory() {
        }

        @Override
        public ImmutableArray<E> empty() {
            return ImmutableArray.empty();
        }

        @Override
        public ImmutableArray<E> from(E @NotNull [] values) {
            return ImmutableArray.from(values);
        }

        @Override
        public ImmutableArray<E> from(@NotNull Iterable<? extends E> values) {
            return ImmutableArray.from(values);
        }

        @Override
        public ImmutableArray<E> from(@NotNull Iterator<? extends E> it) {
            return ImmutableArray.from(it);
        }

        @Override
        public ImmutableArray<E> fill(int n, E value) {
            return ImmutableArray.fill(n, value);
        }

        @Override
        public ImmutableArray<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ImmutableArray.fill(n, init);
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
        public MutableArrayList<E> mergeBuilder(@NotNull MutableArrayList<E> buffer1, @NotNull MutableArrayList<E> buffer2) {
            buffer1.appendAll(buffer2);
            return buffer1;
        }

        @Override
        public ImmutableArray<E> build(@NotNull MutableArrayList<E> buffer) {
            return buffer.toImmutableArray();
        }

    }
}
