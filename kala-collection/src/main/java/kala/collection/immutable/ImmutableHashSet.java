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

import kala.collection.factory.CollectionFactory;
import kala.collection.internal.convert.FromJavaConvert;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public final class ImmutableHashSet<E> extends FromJavaConvert.SetFromJava<E> implements ImmutableSet<E>, Serializable {
    @Serial
    private static final long serialVersionUID = 9051623197618851317L;

    private static final Factory<?> FACTORY = new Factory<>();
    private static final ImmutableHashSet<?> EMPTY = new ImmutableHashSet<>();

    ImmutableHashSet(HashSet<E> source) {
        super(source);
    }

    public ImmutableHashSet() {
        this(new HashSet<>());
    }

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    public static <E> ImmutableHashSet<E> narrow(ImmutableHashSet<? extends E> set) {
        return (ImmutableHashSet<E>) set;
    }

    //endregion

    //region Static Factories

    public static <E> @NotNull CollectionFactory<E, ?, ImmutableHashSet<E>> factory() {
        return ((ImmutableHashSet.Factory<E>) FACTORY);
    }

    public static <E> @NotNull ImmutableHashSet<E> empty() {
        return (ImmutableHashSet<E>) EMPTY;
    }

    public static <E> @NotNull ImmutableHashSet<E> of() {
        return empty();
    }

    public static <E> @NotNull ImmutableHashSet<E> of(E value1) {
        HashSet<E> s = new HashSet<>();
        s.add(value1);
        return new ImmutableHashSet<>(s);
    }

    public static <E> @NotNull ImmutableHashSet<E> of(E value1, E value2) {
        HashSet<E> s = new HashSet<>();
        s.add(value1);
        s.add(value2);
        return new ImmutableHashSet<>(s);
    }

    public static <E> @NotNull ImmutableHashSet<E> of(E value1, E value2, E value3) {
        HashSet<E> s = new HashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        return new ImmutableHashSet<>(s);
    }

    public static <E> @NotNull ImmutableHashSet<E> of(E value1, E value2, E value3, E value4) {
        HashSet<E> s = new HashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        return new ImmutableHashSet<>(s);
    }

    public static <E> @NotNull ImmutableHashSet<E> of(E value1, E value2, E value3, E value4, E value5) {
        HashSet<E> s = new HashSet<>();
        s.add(value1);
        s.add(value2);
        s.add(value3);
        s.add(value4);
        s.add(value5);
        return new ImmutableHashSet<>(s);
    }

    public static <E> @NotNull ImmutableHashSet<E> of(E... values) {
        return from(values);
    }

    public static <E> @NotNull ImmutableHashSet<E> from(E @NotNull [] values) {
        if (values.length == 0) {
            return empty();
        }

        HashSet<E> set = new HashSet<>();
        Collections.addAll(set, values);
        return new ImmutableHashSet<>(set);
    }

    public static <E> @NotNull ImmutableHashSet<E> from(@NotNull Iterable<? extends E> values) {
        Iterator<? extends E> it = values.iterator();
        if (!it.hasNext()) {
            return empty();
        }

        HashSet<E> set = new HashSet<>();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return new ImmutableHashSet<>(set);
    }

    public static <E> @NotNull ImmutableHashSet<E> from(@NotNull Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return empty();
        }

        HashSet<E> set = new HashSet<>();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return new ImmutableHashSet<>(set);
    }

    public static <E> @NotNull ImmutableHashSet<E> from(@NotNull Stream<? extends E> stream) {
        return stream.collect(factory());
    }

    //endregion

    @Override
    public @NotNull String className() {
        return "ImmutableHashSet";
    }

    @Override
    public @NotNull <U> CollectionFactory<U, ?, ImmutableHashSet<U>> iterableFactory() {
        return factory();
    }

    @Serial
    private Object writeReplace() {
        return new SerializationWrapper<>(this);
    }

    private static final class Builder<E> {
        private boolean aliased = false;
        private HashSet<E> set = new HashSet<>();

        private void ensureUnaliased() {
            if (aliased) {
                set = new HashSet<>(set);
                aliased = false;
            }
        }

        void add(E value) {
            ensureUnaliased();
            set.add(value);
        }

        Builder<E> merge(Builder<E> other) {
            ensureUnaliased();
            this.set.addAll(other.set);
            return this;
        }

        ImmutableHashSet<E> build() {
            aliased = true;
            return new ImmutableHashSet<>(set);
        }
    }

    private static final class Factory<E> implements CollectionFactory<E, Builder<E>, ImmutableHashSet<E>> {

        @Override
        public Builder<E> newBuilder() {
            return new Builder<>();
        }

        @Override
        public ImmutableHashSet<E> build(Builder<E> builder) {
            return builder.build();
        }

        @Override
        public void addToBuilder(@NotNull Builder<E> builder, E value) {
            builder.add(value);
        }

        @Override
        public Builder<E> mergeBuilder(@NotNull Builder<E> builder1, @NotNull Builder<E> builder2) {
            return builder1.merge(builder2);
        }
    }

    private static final class SerializationWrapper<E> implements Externalizable {
        private ImmutableHashSet<E> value;

        public SerializationWrapper() {
        }

        SerializationWrapper(ImmutableHashSet<E> value) {
            this.value = value;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeInt(value.size());
            for (E e : value) {
                out.writeObject(e);
            }
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            assert value == null;

            HashSet<E> set = new HashSet<>();

            int len = in.readInt();
            for (int i = 0; i < len; i++) {
                set.add((E) in.readObject());
            }

            value = set.isEmpty() ? ImmutableHashSet.empty() : new ImmutableHashSet<>(set);
        }

        @Serial
        private Object readResolve() {
            return value;
        }
    }
}
