/*
 * Copyright 2025 Glavo
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

import kala.collection.factory.CollectionBuilder;
import kala.collection.factory.CollectionFactory;
import kala.collection.internal.convert.FromJavaConvert;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class MutableLinkedHashSet<E> extends FromJavaConvert.MutableSetFromJava<E> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Factory<?> FACTORY = new Factory<>();

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, MutableLinkedHashSet<E>> factory() {
        return ((MutableLinkedHashSet.Factory<E>) FACTORY);
    }

    @Contract("-> new")
    public static <E> @NotNull CollectionBuilder<E, MutableLinkedHashSet<E>> newBuilder() {
        return MutableLinkedHashSet.<E>factory().newCollectionBuilder();
    }

    @Contract(value = "-> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> create() {
        return new MutableLinkedHashSet<>();
    }

    @Contract(value = "-> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> of() {
        return new MutableLinkedHashSet<>();
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> of(E value1) {
        MutableLinkedHashSet<E> s = new MutableLinkedHashSet<>();
        s.add(value1);
        return s;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> of(E value1, E value2) {
        MutableLinkedHashSet<E> s = new MutableLinkedHashSet<>();
        s.add(value1);
        s.add(value2);
        return s;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> of(E value1, E value2, E value3) {
        MutableLinkedHashSet<E> s = new MutableLinkedHashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return s;
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> of(E value1, E value2, E value3, E value4) {
        MutableLinkedHashSet<E> s = new MutableLinkedHashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return s;
    }

    @Contract(value = "_, _, _, _, _ -> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        MutableLinkedHashSet<E> s = new MutableLinkedHashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> of(E... values) {
        return from(values);
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> from(E @NotNull [] values) {
        MutableLinkedHashSet<E> s = new MutableLinkedHashSet<>(values.length); // implicit null check of values
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> from(@NotNull Iterable<? extends E> values) {
        MutableLinkedHashSet<E> s = new MutableLinkedHashSet<>();
        s.addAll(values);
        return s;
    }

    @Contract(value = "_ -> new")
    public static <E> @NotNull MutableLinkedHashSet<E> from(@NotNull Iterator<? extends E> it) {
        MutableLinkedHashSet<E> s = new MutableLinkedHashSet<>();
        while (it.hasNext()) {
            s.add(it.next());
        }
        return s;
    }

    @Contract(value = "_ -> new", pure = true)
    public static <E> @NotNull MutableLinkedHashSet<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    //endregion

    public MutableLinkedHashSet() {
        this(new LinkedHashSet<>());
    }

    public MutableLinkedHashSet(int initialCapacity) {
        this(new java.util.LinkedHashSet<>(initialCapacity));
    }

    public MutableLinkedHashSet(int initialCapacity, double loadFactor) {
        this(new java.util.LinkedHashSet<>(initialCapacity, ((float) loadFactor)));
    }

    private MutableLinkedHashSet(@NotNull LinkedHashSet<E> set) {
        super(set);
    }

    @Override
    public @NotNull String className() {
        return "MutableLinkedHashSet";
    }

    private static final class Factory<E> extends AbstractMutableSetFactory<E, MutableLinkedHashSet<E>> {
        @Override
        public MutableLinkedHashSet<E> newBuilder() {
            return new MutableLinkedHashSet<>(new LinkedHashSet<>());
        }
    }
}
