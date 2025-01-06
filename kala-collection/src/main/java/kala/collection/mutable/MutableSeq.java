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
import kala.annotations.DelegateBy;
import kala.collection.Seq;
import kala.collection.base.ObjectArrays;
import kala.collection.internal.SeqIterators;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.function.IndexedFunction;
import kala.collection.factory.CollectionFactory;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public interface MutableSeq<E> extends MutableCollection<E>, Seq<E>, MutableAnySeq<E> {

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, MutableSeq<E>> factory() {
        return CollectionFactory.narrow(MutableArray.factory());
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableSeq<E> create(int size) {
        return MutableArray.create(size);
    }

    static <E> @NotNull MutableSeq<E> of() {
        return MutableArray.of();
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1) {
        return MutableArray.of(value1);
    }

    @Contract("_, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2) {
        return MutableArray.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2, E value3) {
        return MutableArray.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2, E value3, E value4) {
        return MutableArray.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static <E> @NotNull MutableSeq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableArray.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull MutableSeq<E> of(E... values) {
        return from(values);
    }

    static <E> @NotNull MutableSeq<E> from(E @NotNull [] values) {
        return MutableArray.from(values);
    }

    static <E> @NotNull MutableSeq<E> from(@NotNull Iterable<? extends E> values) {
        return MutableArray.from(values);
    }

    static <E> @NotNull MutableSeq<E> from(@NotNull Iterator<? extends E> it) {
        return MutableArray.from(it);
    }

    static <E> @NotNull MutableSeq<E> from(@NotNull Stream<? extends E> stream) {
        return MutableArray.from(stream);
    }

    static <E> @NotNull MutableSeq<E> fill(int n, E value) {
        return MutableArray.fill(n, value);
    }

    static <E> @NotNull MutableSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        return MutableArray.fill(n, init);
    }

    static <E> @NotNull MutableSeq<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        return MutableArray.generateUntil(supplier, predicate);
    }

    static <E> @NotNull MutableSeq<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        return MutableArray.generateUntilNull(supplier);
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableSeq<E> wrapJava(java.util.@NotNull List<E> list) {
        Objects.requireNonNull(list);
        return new FromJavaConvert.MutableSeqFromJava<>(list);
    }

    static <E, C extends MutableSeq<E>> @NotNull MutableSeqEditor<E, C> edit(@NotNull C seq) {
        return new MutableSeqEditor<>(seq);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "MutableSeq";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends MutableSeq<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull MutableSeqIterator<E> seqIterator() {
        return seqIterator(0);
    }

    @Override
    default @NotNull MutableSeqIterator<E> seqIterator(int index) {
        Conditions.checkPositionIndex(index, size());
        return new SeqIterators.DefaultMutableSeqIterator<>(this, index);
    }

    @Override
    default @NotNull java.util.List<E> asJava() {
        return this.supportsFastRandomAccess() ? new AsJavaConvert.MutableIndexedSeqAsJava<>(this) : new AsJavaConvert.MutableSeqAsJava<>(this);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    default @NotNull MutableSeq<E> clone() {
        return this.<E>iterableFactory().from(this);
    }

    @Contract(mutates = "this")
    void set(@Index int index, E newValue);

    @DelegateBy("replaceAllIndexed(IndexedFunction<E, E>)")
    default void setAll(E... values) {
        final int size = size();
        if (size != values.length) {
            throw new IllegalArgumentException();
        }

        replaceAllIndexed((idx, ignored) -> values[idx]);
    }

    default void swap(@Index int index1, @Index int index2) {
        final int size = size();
        index1 = Indexes.checkIndex(index1, size);
        index2 = Indexes.checkIndex(index2, size);

        if (index1 != index2) {
            final E old1 = this.get(index1);
            final E old2 = this.get(index2);

            this.set(index1, old2);
            this.set(index2, old1);
        }
    }

    @Contract(mutates = "this")
    default void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
        int size = size();
        if (supportsFastRandomAccess()) {
            for (int i = 0; i < size; i++) {
                this.set(i, operator.apply(this.get(i)));
            }
        } else {
            MutableSeqIterator<E> it = seqIterator();
            while (it.hasNext()) {
                it.set(operator.apply(it.next()));
            }
        }
    }

    @Contract(mutates = "this")
    default void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
        int size = size();
        if (supportsFastRandomAccess()) {
            for (int i = 0; i < size; i++) {
                this.set(i, operator.apply(i, this.get(i)));
            }
        } else {
            int idx = 0;
            MutableSeqIterator<E> it = seqIterator();
            while (it.hasNext()) {
                it.set(operator.apply(idx++, it.next()));
            }
        }
    }

    @Contract(mutates = "this")
    @DelegateBy("sort(Comparator<E>)")
    default void sort() {
        sort(null);
    }

    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    default void sort(Comparator<? super E> comparator) {
        Object[] values = toArray();
        if (values.length <= 1) return;

        Arrays.sort(values, (Comparator<? super Object>) comparator);

        if (supportsFastRandomAccess()) {
            for (int i = 0; i < values.length; i++) {
                this.set(i, (E) values[i]);
            }
        } else {
            MutableSeqIterator<E> it = seqIterator();
            for (Object value : values) {
                it.next();
                it.set((E) value);
            }
        }
    }

    @Contract(mutates = "this")
    default void reverse() {
        final int size = this.size();
        if (size <= 1) return;

        for (int i = 0; i < size / 2; i++) {
            swap(i, size - i - 1);
        }
    }

    @DelegateBy("shuffle(RandomGenerator)")
    default void shuffle() {
        shuffle(ThreadLocalRandom.current());
    }

    default void shuffle(@NotNull RandomGenerator random) {
        int ks = this.knownSize();
        if (ks == 0 || ks == 1) {
            return;
        }
        if (ks > 0 && (supportsFastRandomAccess() || ks <= AbstractMutableSeq.SHUFFLE_THRESHOLD)) {
            for (int i = ks; i > 1; i--) {
                swap(i - 1, random.nextInt(i));
            }
        } else {
            @SuppressWarnings("unchecked") final E[] arr = (E[]) this.toArray();
            ObjectArrays.shuffle(arr, random);
            this.setAll(arr);
        }
    }
}
