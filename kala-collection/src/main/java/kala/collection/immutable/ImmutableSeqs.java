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

import kala.annotations.StaticClass;
import kala.collection.*;
import kala.collection.base.Iterators;
import kala.collection.internal.view.SeqViews;
import kala.control.Option;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.collection.factory.CollectionFactory;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Map;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@StaticClass
final class ImmutableSeqs {
    private ImmutableSeqs() {
    }

    static sealed abstract class SeqN<E> extends AbstractImmutableSeq<E> implements IndexedSeq<E> {
    }

    static final class Seq0<E> extends SeqN<E> implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        static final Seq0<?> INSTANCE = new Seq0<>();

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.empty();
        }

        @Override
        public @NotNull ImmutableSeq<E> reversed() {
            return this;
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return Iterators.empty();
        }

        @Override
        public @NotNull Spliterator<E> spliterator() {
            return Spliterators.emptySpliterator();
        }

        @Override
        public @NotNull SeqView<E> view() {
            return SeqView.empty();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public E get(int index) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        @Override
        public @NotNull ImmutableSeq<E> prepended(E value) {
            return ImmutableSeq.of(value);
        }

        @Override
        public @NotNull ImmutableSeq<E> appended(E value) {
            return ImmutableSeq.of(value);
        }

        @Override
        public @NotNull ImmutableSeq<E> take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }

            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> takeLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> dropLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> inserted(@Index int index, E value) {
            if (index == 0 || index == ~0) {
                return ImmutableSeq.of(value);
            }
            throw Indexes.outOfBounds(index, 0);
        }

        @Override
        public @NotNull ImmutableSeq<E> updated(@Index int index, E newValue) {
            throw Indexes.outOfBounds(index, 0);
        }

        @Override
        public @NotNull <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ImmutableSeq.empty();
        }

        @Override
        public @NotNull <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return ImmutableSeq.empty();
        }

        @Override
        public @NotNull <U> ImmutableSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
            return ImmutableSeq.empty();
        }

        @Override
        public @NotNull <U> ImmutableSeq<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
            return ImmutableSeq.empty();
        }

        @Serial
        private Object readResolve() {
            return INSTANCE;
        }
    }

    static final class Seq1<E> extends SeqN<E> implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        private final E value1;

        Seq1(E value1) {
            this.value1 = value1;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.of(value1);
        }

        @Override
        public @NotNull ImmutableSeq<E> reversed() {
            return this;
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return Iterators.of(value1);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public E get(@Index int index) {
            if (index != 0 && index != ~1) {
                throw Indexes.outOfBounds(index, size());
            }
            return value1;
        }

        @Override
        public @NotNull ImmutableSeq<E> prepended(E value) {
            return ImmutableSeq.of(value, value1);
        }

        @Override
        public @NotNull ImmutableSeq<E> appended(E value) {
            return ImmutableSeq.of(value1, value);
        }

        @Override
        public @NotNull ImmutableSeq<E> inserted(@Index int index, E value) {
            return switch (index) {
                case 0, ~1 -> ImmutableSeq.of(value, value1);
                case 1, ~0 -> ImmutableSeq.of(value1, value);
                default -> throw new IndexOutOfBoundsException(index);
            };
        }

        @Override
        public @NotNull ImmutableSeq<E> removedAt(@Index int index) {
            if (index == 0 || index == ~1) {
                return ImmutableSeq.empty();
            }

            throw Indexes.outOfBounds(index, 1);
        }

        @Override
        public @NotNull ImmutableSeq<E> take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return ImmutableSeq.empty();
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> takeLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return ImmutableSeq.empty();
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return ImmutableSeq.empty();
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }
            return ImmutableSeq.empty();
        }

        @Override
        public @NotNull ImmutableSeq<E> dropLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }
            return ImmutableSeq.empty();
        }

        @Override
        public @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            return predicate.test(value1) ? ImmutableSeq.empty() : this;
        }

        @Override
        public @NotNull ImmutableSeq<E> updated(@Index int index, E newValue) {
            if (index == 0 || index == ~1) {
                return ImmutableSeq.of(newValue);
            }

            throw Indexes.outOfBounds(index, size());
        }

        @Override
        public @NotNull <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(value1)
            );
        }

        @Override
        public @NotNull <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(0, value1)
            );
        }

        @Override
        public @NotNull <U> ImmutableSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
            final U u = mapper.apply(value1);
            return u == null ? ImmutableSeq.empty() : ImmutableSeq.of(u);
        }

        @Override
        public @NotNull <U> ImmutableSeq<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
            final U u = mapper.apply(0, value1);
            return u == null ? ImmutableSeq.empty() : ImmutableSeq.of(u);
        }
    }

    static final class Seq2<E> extends SeqN<E> implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        private final E value1;
        private final E value2;

        Seq2(E value1, E value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.of(value1, value2);
        }

        @Override
        public @NotNull ImmutableSeq<E> reversed() {
            return new Seq2<>(value2, value1);
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return Iterators.of(value2, value1);
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public E get(int index) {
            return switch (index) {
                case 0, ~2 -> value1;
                case 1, ~1 -> value2;
                default -> throw new IndexOutOfBoundsException(index);
            };
        }

        @Override
        public @NotNull ImmutableSeq<E> updated(@Index int index, E newValue) {
            return switch (index) {
                case 0, ~2 -> ImmutableSeq.of(newValue, value2);
                case 1, ~1 -> ImmutableSeq.of(value1, newValue);
                default -> throw Indexes.outOfBounds(index, size());
            };
        }

        @Override
        public @NotNull ImmutableSeq<E> prepended(E value) {
            return ImmutableSeq.of(value, value1, value2);
        }

        @Override
        public @NotNull ImmutableSeq<E> appended(E value) {
            return ImmutableSeq.of(value1, value2, value);
        }

        @Override
        public @NotNull ImmutableSeq<E> inserted(@Index int index, E value) {
            return switch (index) {
                case 0, ~2 -> ImmutableSeq.of(value, value1, value2);
                case 1, ~1 -> ImmutableSeq.of(value1, value, value2);
                case 2, ~0 -> ImmutableSeq.of(value1, value2, value);
                default -> throw Indexes.outOfBounds(index, 2);
            };
        }

        @Override
        public @NotNull ImmutableSeq<E> removedAt(@Index int index) {
            return switch (index) {
                case 0, ~2 -> ImmutableSeq.of(value2);
                case 1, ~1 -> ImmutableSeq.of(value1);
                default -> throw Indexes.outOfBounds(index, 2);
            };
        }

        @Override
        public @NotNull ImmutableSeq<E> take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return ImmutableSeq.empty();
            }
            if (n == 1) {
                return ImmutableSeq.of(value1);
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> takeLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return ImmutableSeq.empty();
            }
            if (n == 1) {
                return ImmutableSeq.of(value2);
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return ImmutableSeq.empty();
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value1);
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            return switch (n) {
                case 0 -> this;
                case 1 -> ImmutableSeq.of(value2);
                default -> ImmutableSeq.empty();
            };
        }

        @Override
        public @NotNull ImmutableSeq<E> dropLast(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }
            if (n == 1) {
                return ImmutableSeq.of(value1);
            }
            return ImmutableSeq.empty();
        }

        @Override
        public @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            if (!predicate.test(value1)) {
                return this;
            }
            if (!predicate.test(value2)) {
                return ImmutableSeq.of(value2);
            }
            return ImmutableSeq.empty();
        }

        @Override
        public @NotNull <U> ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(value1),
                    mapper.apply(value2)
            );
        }

        @Override
        public @NotNull <U> ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
            return ImmutableSeq.of(
                    mapper.apply(0, value1),
                    mapper.apply(1, value2)
            );
        }
    }

    @SuppressWarnings("unchecked")
    static sealed abstract class CopiesSeqBase<E> implements IndexedSeqLike<E>, Serializable {
        protected final @Range(from = 1, to = Integer.MAX_VALUE) int size;
        protected final E value;

        protected CopiesSeqBase(@Range(from = 1, to = Integer.MAX_VALUE) int size, E value) {
            this.size = size;
            this.value = value;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.fill(size, value);
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return stream().spliterator();
        }

        @Override
        public final @NotNull Stream<E> stream() {
            return size == 1 ? Stream.of(value) : IntStream.range(0, size).mapToObj(i -> value);
        }

        @Override
        public final @NotNull Stream<E> parallelStream() {
            return size == 1 ? Stream.of(value).parallel() : IntStream.range(0, size).parallel().mapToObj(i -> value);
        }

        @Override
        public final int size() {
            return size;
        }

        //region Positional Access Operations

        @Override
        public final E get(@Index int index) {
            Indexes.checkIndex(index, size);
            return value;
        }

        //endregion

        //region Reversal Operations

        @Override
        public final @NotNull Iterator<E> reverseIterator() {
            return iterator();
        }

        //endregion

        //region Element Retrieval Operations

        @Override
        public final E getFirst() {
            return value;
        }

        @Override
        public final @Nullable E getFirstOrNull() {
            return value;
        }

        @Override
        public final @NotNull Option<E> getFirstOption() {
            return Option.some(value);
        }

        @Override
        public final E getLast() {
            return value;
        }

        @Override
        public final @Nullable E getLastOrNull() {
            return value;
        }

        @Override
        public final @NotNull Option<E> getLastOption() {
            return Option.some(value);
        }

        //endregion

        //region Element Conditions

        @Override
        public final boolean contains(Object value) {
            return Objects.equals(value, this.value);
        }

        @Override
        public final boolean containsAll(Object @NotNull [] values) {
            final E value = this.value;
            if (value == null) {
                for (Object v : values) {
                    if (null != v) {
                        return false;
                    }
                }
            } else {
                for (Object v : values) {
                    if (!value.equals(v)) {
                        return false;
                    }
                }
            }
            return true;
        }

        //endregion

        //region Search Operations

        @Override
        public final int indexOf(Object value) {
            return Objects.equals(value, this.value) ? 0 : -1;
        }

        @Override
        public final int indexOf(Object value, int from) {
            if (from >= size) {
                return -1;
            } else {
                return Objects.equals(value, this.value) ? Integer.max(from, 0) : -1;
            }
        }

        @Override
        public final int lastIndexOf(Object value) {
            return Objects.equals(value, this.value) ? size - 1 : -1;
        }

        @Override
        public final int lastIndexOf(Object value, int end) {
            if (end < 0) {
                return -1;
            } else {
                return Objects.equals(value, this.value) ? Integer.min(size - 1, end) : -1;
            }
        }

        //endregion

        //region Aggregate Operations

        @Override
        public final E max() {
            return value;
        }

        @Override
        public E max(Comparator<? super E> comparator) {
            return max();
        }

        @Override
        public final @Nullable E maxOrNull() {
            return value;
        }

        @Override
        public final @Nullable E maxOrNull(@NotNull Comparator<? super E> comparator) {
            return maxOrNull();
        }

        @Override
        public final @NotNull Option<E> maxOption() {
            return Option.some(value);
        }

        @Override
        public final @NotNull Option<E> maxOption(Comparator<? super E> comparator) {
            return maxOption();
        }

        @Override
        public final E min() {
            return value;
        }

        @Override
        public E min(Comparator<? super E> comparator) {
            return min();
        }

        @Override
        public final @Nullable E minOrNull() {
            return value;
        }

        @Override
        public final @Nullable E minOrNull(@NotNull Comparator<? super E> comparator) {
            return minOrNull();
        }

        @Override
        public final @NotNull Option<E> minOption() {
            return Option.some(value);
        }

        @Override
        public final @NotNull Option<E> minOption(Comparator<? super E> comparator) {
            return minOption();
        }

        //endregion

        //region Copy Operations

        @Override
        public int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
            if (srcPos < 0) {
                throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
            }
            if (destPos < 0) {
                throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
            }

            final int dl = dest.length;
            final int size = this.size;

            if (destPos >= dl || srcPos >= size) {
                return 0;
            }

            final int n = Math.min(Math.min(size - srcPos, dl - destPos), limit);
            final E value = this.value;

            final int end = n + destPos;
            for (int i = destPos; i < end; i++) {
                dest[i] = value;
            }

            return n;
        }

        //endregion

        //region Conversion Operations

        @Override
        public final Object @NotNull [] toArray() {
            final Object[] res = new Object[size];
            final E value = this.value;
            if (value != null) {
                Arrays.fill(res, value);
            }
            return res;
        }

        @Override
        public final <U> U @NotNull [] toArray(@NotNull Class<U> type) {
            final U[] res = (U[]) Array.newInstance(type, size);
            final E value = this.value;
            if (value != null) {
                Arrays.fill(res, value);
            }
            return res;
        }

        @Override
        public final <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
            final U[] res = generator.apply(size);
            final E value = this.value;
            if (value != null) {
                Arrays.fill(res, value);
            }
            return res;
        }

        @Override
        public final @NotNull <K, V> ImmutableMap<K, V> toImmutableMap() {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) value;
            return ImmutableMap.of(entry.getKey(), entry.getValue());
        }

        //endregion

        //region Traverse Operations

        @Override
        public final void forEach(@NotNull Consumer<? super E> action) {
            for (int i = 0; i < size; i++) {
                action.accept(value);
            }
        }

        @Override
        public final void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
            for (int i = 0; i < size; i++) {
                action.accept(i, value);
            }
        }

        //endregion

        //region String Representation

        @Override
        public final String toString() {
            final String cn = this.className();
            final int size = this.size;
            final String vs = value.toString();

            final StringBuilder builder = new StringBuilder(cn.length() + (vs.length() + 2) * size);
            builder.append(cn).append('[').append(vs);
            for (int i = 1; i < size; i++) {
                builder.append(", ").append(vs);
            }
            builder.append(']');
            return builder.toString();
        }

        //endregion
    }

    static final class CopiesSeqView<E> extends CopiesSeqBase<E> implements SeqView<E> {
        public CopiesSeqView(@Range(from = 1, to = Integer.MAX_VALUE) int size, E value) {
            super(size, value);
        }

        @Override
        public @NotNull String className() {
            return "CopiesSeqView";
        }

        @Override
        public @NotNull SeqView<E> prepended(E value) {
            if (value == this.value) {
                return new CopiesSeqView<>(size + 1, value);
            }
            return SeqView.super.prepended(value);
        }

        @Override
        public @NotNull SeqView<E> appended(E value) {
            if (value == this.value) {
                return new CopiesSeqView<>(size + 1, value);
            }
            return SeqView.super.appended(value);
        }

        @Override
        public @NotNull SeqView<E> reversed() {
            return this;
        }

        //region Misc Operations

        @Override
        public @NotNull SeqView<E> slice(@Index int beginIndex, @Index int endIndex) {
            beginIndex = Indexes.checkBeginIndex(beginIndex, size);
            endIndex = Indexes.checkEndIndex(beginIndex, endIndex, size);

            final int ns = endIndex - beginIndex;
            if (ns == 0) {
                return SeqView.empty();
            }
            if (ns == 1) {
                return new SeqViews.Single<>(value);
            }
            return new CopiesSeqView<>(ns, value);
        }

        @Override
        public @NotNull SeqView<E> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }
            if (n >= size) {
                return SeqView.empty();
            }
            final int ns = size - n;
            if (ns == 1) {
                return new SeqViews.Single<>(value);
            }
            return new CopiesSeqView<>(ns, value);
        }

        @Override
        public @NotNull SeqView<E> dropLast(int n) {
            return drop(n);
        }

        @Override
        public @NotNull SeqView<E> take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return SeqView.empty();
            }
            if (n >= size) {
                return this;
            }
            if (n == 1) {
                return new SeqViews.Single<>(value);
            }
            return new CopiesSeqView<>(n, value);
        }

        @Override
        public @NotNull SeqView<E> takeLast(int n) {
            return take(n);
        }

        @Override
        public @NotNull SeqView<@NotNull E> filterNotNull() {
            return value == null ? SeqView.empty() : this;
        }

        @Override
        public @NotNull SeqView<E> sorted() {
            return this;
        }

        @Override
        public @NotNull SeqView<E> sorted(Comparator<? super E> comparator) {
            return this;
        }

        //endregion

    }

    @SuppressWarnings("unchecked")
    static final class CopiesSeq<E> extends CopiesSeqBase<E> implements ImmutableSeq<E>, Serializable {
        @Serial
        private static final long serialVersionUID = 6615175156982747837L;

        CopiesSeq(@Range(from = 1, to = Integer.MAX_VALUE) int size, E value) {
            super(size, value);
        }

        @Override
        public @NotNull SeqView<E> view() {
            return size == 1 ? new SeqViews.Single<>(value) : new CopiesSeqView<>(size, value);
        }

        @Override
        public @NotNull ImmutableSeq<E> reversed() {
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> prepended(E value) {
            final int size = this.size;
            if (value == this.value) {
                return new CopiesSeq<>(size + 1, value);
            }
            final E oldValue = this.value;
            if (size < ImmutableVectors.WIDTH) {
                final Object[] arr = new Object[size + 1];
                Arrays.fill(arr, oldValue);
                arr[0] = value;
                return new ImmutableVectors.Vector1<>(arr);
            }
            final ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
            builder.add(value);
            for (int i = 0; i < size; i++) {
                builder.add(oldValue);
            }
            return builder.build();
        }

        @Override
        public @NotNull ImmutableSeq<E> appended(E value) {
            final int size = this.size;
            if (value == this.value) {
                assert size != Integer.MAX_VALUE;
                return new CopiesSeq<>(size + 1, value);
            }
            final E oldValue = this.value;
            if (size < ImmutableVectors.WIDTH) {
                final Object[] arr = new Object[size + 1];
                Arrays.fill(arr, oldValue);
                arr[size] = value;
                return new ImmutableVectors.Vector1<>(arr);
            }
            final ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
            for (int i = 0; i < size; i++) {
                builder.add(oldValue);
            }
            builder.add(value);
            return builder.build();
        }

        //region Misc Operations

        @Override
        public @NotNull ImmutableSeq<E> slice(@Index int beginIndex, @Index int endIndex) {
            beginIndex = Indexes.checkBeginIndex(beginIndex, size);
            endIndex = Indexes.checkEndIndex(beginIndex, endIndex, size);

            final int ns = endIndex - beginIndex;
            return ns == 0 ? ImmutableSeq.empty() : new CopiesSeq<>(ns, value);
        }

        @Override
        public @NotNull ImmutableSeq<E> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return this;
            }
            if (n >= size) {
                return ImmutableSeq.empty();
            }
            return new CopiesSeq<>(size - n, value);
        }

        @Override
        public @NotNull ImmutableSeq<E> dropLast(int n) {
            return drop(n);
        }

        @Override
        public @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
            final int size = this.size;
            final E value = this.value;
            for (int i = 0; i < size; i++) {
                if (!predicate.test(value)) {
                    return drop(i);
                }
            }
            return ImmutableSeq.empty();
        }

        @Override
        public @NotNull ImmutableSeq<E> take(int n) {
            if (n < 0) {
                throw new IllegalArgumentException();
            }
            if (n == 0) {
                return ImmutableSeq.empty();
            }
            if (n >= size) {
                return this;
            }
            return new CopiesSeq<>(n, value);
        }

        @Override
        public @NotNull ImmutableSeq<E> takeLast(int n) {
            return take(n);
        }

        @Override
        public @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
            final int size = this.size;
            final E value = this.value;
            for (int i = 0; i < size; i++) {
                if (!predicate.test(value)) {
                    return take(i);
                }
            }
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> concat(@NotNull SeqLike<? extends E> other) {
            if (other instanceof CopiesSeq) {
                CopiesSeq<E> ics = (CopiesSeq<E>) other;
                if (ics.value == this.value) {
                    return new CopiesSeq<>(this.size + ics.size, value);
                }
            }
            Objects.requireNonNull(other);
            final ImmutableVectors.VectorBuilder<E> builder = new ImmutableVectors.VectorBuilder<>();
            for (int i = 0; i < size; i++) {
                builder.add(value);
            }
            builder.addAll(other);
            return builder.build();
        }

        @Override
        public @NotNull ImmutableSeq<E> filter(@NotNull Predicate<? super E> predicate) {
            final int size = this.size;
            final E value = this.value;

            int c = 0;
            for (int i = 0; i < size; i++) {
                if (predicate.test(value)) {
                    c++;
                }
            }

            if (c == 0) {
                return ImmutableSeq.empty();
            } else if (c == 1) {
                return ImmutableSeq.of(value);
            } else if (c == size) {
                return this;
            } else {
                return new CopiesSeq<>(c, value);
            }
        }

        @Override
        public @NotNull ImmutableSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
            final int size = this.size;
            final E value = this.value;

            int c = 0;
            for (int i = 0; i < size; i++) {
                if (!predicate.test(value)) {
                    c++;
                }
            }

            if (c == 0) {
                return ImmutableSeq.empty();
            } else if (c == 1) {
                return ImmutableSeq.of(value);
            } else if (c == size) {
                return this;
            } else {
                return new CopiesSeq<>(c, value);
            }
        }

        @Override
        public @NotNull ImmutableSeq<@NotNull E> filterNotNull() {
            return value == null ? ImmutableSeq.empty() : this;
        }

        @Override
        public @NotNull ImmutableSeq<E> sorted() {
            return this;
        }

        @Override
        public @NotNull ImmutableSeq<E> sorted(Comparator<? super E> comparator) {
            return this;
        }

        //endregion

        @Override
        public int hashCode() {
            return Seq.hashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Seq<?> other && Seq.equals(this, other);
        }
    }

    static final class Factory<E> implements CollectionFactory<E, ImmutableVectors.VectorBuilder<E>, ImmutableSeq<E>> {

        @Override
        public ImmutableSeq<E> empty() {
            return ImmutableSeq.empty();
        }

        @Override
        public ImmutableVectors.VectorBuilder<E> newBuilder() {
            return new ImmutableVectors.VectorBuilder<>();
        }

        @Override
        public void addToBuilder(ImmutableVectors.@NotNull VectorBuilder<E> builder, E value) {
            builder.add(value);
        }

        @Override
        public ImmutableVectors.VectorBuilder<E> mergeBuilder(ImmutableVectors.@NotNull VectorBuilder<E> builder1, ImmutableVectors.@NotNull VectorBuilder<E> builder2) {
            builder1.addVector(builder2.build());
            return builder1;
        }

        @Override
        public ImmutableSeq<E> build(ImmutableVectors.@NotNull VectorBuilder<E> builder) {
            return builder.buildSeq();
        }

        @Override
        public ImmutableSeq<E> from(E @NotNull [] values) {
            return ImmutableSeq.from(values);
        }

        @Override
        public ImmutableSeq<E> from(@NotNull Iterable<? extends E> values) {
            return ImmutableSeq.from(values);
        }

        @Override
        public ImmutableSeq<E> from(@NotNull Iterator<? extends E> it) {
            return ImmutableSeq.from(it);
        }

        @Override
        public ImmutableSeq<E> fill(int n, E value) {
            return ImmutableSeq.fill(n, value);
        }

        @Override
        public ImmutableSeq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
            return ImmutableSeq.fill(n, init);
        }
    }

    static final Factory<?> FACTORY = new Factory<>();
}
