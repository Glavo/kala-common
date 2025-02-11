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
package kala.collection;

import kala.annotations.Covariant;
import kala.annotations.DelegateBy;
import kala.collection.base.AnyTraversable;
import kala.collection.base.Iterators;
import kala.collection.base.OrderedTraversable;
import kala.collection.factory.CollectionBuilder;
import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.internal.CollectionHelper;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.internal.convert.FromJavaConvert;
import kala.collection.internal.view.SeqViews;
import kala.function.*;
import kala.index.Index;
import kala.index.Indexes;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public interface Seq<@Covariant E> extends Collection<E>, OrderedTraversable<E>, SeqLike<E>, AnySeq<E> {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <E> Seq<E> narrow(Seq<? extends E> seq) {
        return (Seq<E>) seq;
    }

    //endregion

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, Seq<E>> factory() {
        return CollectionFactory.narrow(ImmutableSeq.factory());
    }

    @Contract("-> new")
    static <E> @NotNull CollectionBuilder<E, Seq<E>> newBuilder() {
        return Seq.<E>factory().newCollectionBuilder();
    }

    static <E> @NotNull Seq<E> empty() {
        return ImmutableSeq.empty();
    }

    static <E> @NotNull Seq<E> of() {
        return ImmutableSeq.of();
    }

    static <E> @NotNull Seq<E> of(E value1) {
        return ImmutableSeq.of(value1);
    }

    static <E> @NotNull Seq<E> of(E value1, E value2) {
        return ImmutableSeq.of(value1, value2);
    }

    static <E> @NotNull Seq<E> of(E value1, E value2, E value3) {
        return ImmutableSeq.of(value1, value2, value3);
    }

    static <E> @NotNull Seq<E> of(E value1, E value2, E value3, E value4) {
        return ImmutableSeq.of(value1, value2, value3, value4);
    }

    static <E> @NotNull Seq<E> of(E value1, E value2, E value3, E value4, E value5) {
        return ImmutableSeq.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull Seq<E> of(E... values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull Seq<E> from(E @NotNull [] values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull Seq<E> from(@NotNull Iterable<? extends E> values) {
        return ImmutableSeq.from(values);
    }

    static <E> @NotNull Seq<E> from(@NotNull Iterator<? extends E> it) {
        return ImmutableSeq.from(it);
    }

    static <E> @NotNull Seq<E> from(@NotNull Stream<? extends E> stream) {
        return ImmutableSeq.from(stream);
    }

    static <E> @NotNull Seq<E> fill(int n, E value) {
        return ImmutableSeq.fill(n, value);
    }

    static <E> @NotNull Seq<E> fill(int n, @NotNull IntFunction<? extends E> init) {
        return ImmutableSeq.fill(n, init);
    }

    static <E> @NotNull Seq<E> generateUntil(@NotNull Supplier<? extends E> supplier, @NotNull Predicate<? super E> predicate) {
        return ImmutableSeq.generateUntil(supplier, predicate);
    }

    static <E> @NotNull Seq<E> generateUntilNull(@NotNull Supplier<? extends @Nullable E> supplier) {
        return ImmutableSeq.generateUntilNull(supplier);
    }

    static <E> @NotNull Seq<E> wrapJava(java.util.@NotNull List<? extends E> source) {
        Objects.requireNonNull(source);
        if (source instanceof AsJavaConvert.SeqAsJava<?, ?>) {
            return ((AsJavaConvert.SeqAsJava<E, ?>) source).source;
        }
        return new FromJavaConvert.SeqFromJava<>((List<E>) source);
    }

    //endregion

    static int hashCode(@NotNull Seq<?> seq) {
        return Iterators.hash(seq.iterator()) + SEQ_HASH_MAGIC;
    }

    static boolean equals(@NotNull Seq<?> seq1, @NotNull AnySeq<?> seq2) {
        if (seq1 == seq2) return true;
        if (!seq1.canEqual(seq2) || !seq2.canEqual(seq1)) return false;

        return seq1.sameElements(seq2.asGeneric());
    }

    @Override
    default @NotNull String className() {
        return "Seq";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends Seq<U>> iterableFactory() {
        return Seq.factory();
    }

    @Override
    default @NotNull SeqView<E> view() {
        return knownSize() == 0 ? SeqView.empty() : new SeqViews.Of<>(this);
    }

    @Override
    default @NotNull @UnmodifiableView List<E> asJava() {
        return this.supportsFastRandomAccess()
                ? new AsJavaConvert.IndexedSeqAsJava<>(this)
                : new AsJavaConvert.SeqAsJava<>(this);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> slice(@Index int beginIndex, @Index int endIndex) {
        final int size = size();
        beginIndex = Indexes.checkBeginIndex(beginIndex, size);
        endIndex = Indexes.checkEndIndex(beginIndex, endIndex, size);

        int newSize = endIndex - beginIndex;
        if (newSize == 0) {
            return CollectionHelper.emptyImmutableSeqBy(this);
        }
        if (newSize == size) {
            return toImmutableSeq();
        }

        var builder = CollectionHelper.<E>newImmutableSeqBuilderBy(this);
        builder.sizeHint(newSize);

        if (supportsFastRandomAccess()) {
            for (int i = beginIndex; i < endIndex; i++) {
                builder.plusAssign(get(i));
            }
        } else {
            Iterator<? extends E> it = iterator(beginIndex);
            for (int i = 0; i < newSize; i++) {
                builder.plusAssign(it.next());
            }
        }
        return builder.build();
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("slice(int, int)")
    default @NotNull ImmutableSeq<E> drop(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return toImmutableSeq();
        }
        final int size = size();
        if (n >= size) {
            return CollectionHelper.emptyImmutableSeqBy(this);
        }
        return slice(n, size);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> dropLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return toImmutableSeq();
        }
        final int size = size();
        if (n >= size) {
            return CollectionHelper.emptyImmutableSeqBy(this);
        }
        return slice(0, size - n);
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("drop(int)")
    default @NotNull ImmutableSeq<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        int count = 0;
        boolean empty = true;
        for (E e : this) {
            if (predicate.test(e)) {
                count++;
            } else {
                empty = false;
                break;
            }
        }

        if (empty) {
            return CollectionHelper.emptyImmutableSeqBy(this);
        }
        return drop(count);
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("slice(int, int)")
    default @NotNull ImmutableSeq<E> take(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return CollectionHelper.emptyImmutableSeqBy(this);
        }
        final int size = size();
        if (n >= size) {
            return toImmutableSeq();
        }
        return slice(0, n);
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("slice(int, int)")
    default @NotNull ImmutableSeq<E> takeLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return CollectionHelper.emptyImmutableSeqBy(this);
        }
        final int size = size();
        if (n >= size) {
            return toImmutableSeq();
        }
        return slice(size - n, size);
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("take(int)")
    default @NotNull ImmutableSeq<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        int count = 0;
        boolean takeAll = true;
        for (E e : this) {
            if (predicate.test(e)) {
                count++;
            } else {
                takeAll = false;
                break;
            }
        }

        if (takeAll) {
            return toImmutableSeq();
        }
        return take(count);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> updated(@Index int index, E newValue) {
        final int size = size();
        index = Indexes.checkIndex(index, size);

        var builder = CollectionHelper.<E>newImmutableSeqBuilderBy(this);
        builder.sizeHint(size);

        for (E e : this) {
            if (index-- == 0) {
                builder.plusAssign(newValue);
            } else {
                builder.plusAssign(e);
            }
        }

        return builder.build();
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("appendedAll(Iterable<E>)")
    default @NotNull ImmutableSeq<E> concat(@NotNull SeqLike<? extends E> other) {
        return appendedAll(other);
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("appendedAll(Iterable<E>)")
    default @NotNull ImmutableSeq<E> concat(@NotNull List<? extends E> other) {
        return appendedAll(other);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prepended(E value) {
        var builder = CollectionHelper.<E>newImmutableSeqBuilderBy(this);
        builder.sizeHint(size() + 1);

        builder.plusAssign(value);
        for (E e : this) {
            builder.plusAssign(e);
        }

        return builder.build();
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("prependedAll(Iterable<E>)")
    default @NotNull ImmutableSeq<E> prependedAll(E... values) {
        return prependedAll(ArraySeq.wrap(values));
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> prependedAll(@NotNull Iterable<? extends E> values) {
        if (isEmpty()) {
            return CollectionHelper.<E>immutableSeqFactoryBy(this).from(values);
        }

        var builder = CollectionHelper.<E>newImmutableSeqBuilderBy(this);
        for (E e : values) {
            builder.plusAssign(e);
        }
        for (E e : this) {
            builder.plusAssign(e);
        }
        return builder.build();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appended(E value) {
        var builder = CollectionHelper.<E>newImmutableSeqBuilderBy(this);
        builder.sizeHint(size() + 1);

        for (E e : this) {
            builder.plusAssign(e);
        }
        builder.plusAssign(value);

        return builder.build();
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("appendedAll(Iterable<E>)")
    default @NotNull ImmutableSeq<E> appendedAll(E... values) {
        return appendedAll(ArraySeq.wrap(values));
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> appendedAll(@NotNull Iterable<? extends E> values) {
        if (isEmpty()) {
            return CollectionHelper.<E>immutableSeqFactoryBy(this).from(values);
        }

        var builder = CollectionHelper.<E>newImmutableSeqBuilderBy(this);
        for (E e : this) {
            builder.plusAssign(e);
        }
        for (E e : values) {
            builder.plusAssign(e);
        }
        return builder.build();
    }

    @Override
    default @NotNull ImmutableSeq<E> inserted(@Index int index, E value) {
        final int size = size();
        index = Indexes.checkPositionIndex(index, size);

        var builder = CollectionHelper.<E>newImmutableSeqBuilderBy(this);
        builder.sizeHint(size + 1);
        Iterator<? extends E> iterator = iterator();
        for (int i = 0; i < index; i++) {
            builder.plusAssign(iterator.next());
        }
        builder.plusAssign(value);
        while (iterator.hasNext()) {
            builder.plusAssign(iterator.next());
        }
        return builder.build();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> removedAt(@Index int index) {
        final int size = size();
        index = Indexes.checkIndex(index, size);

        if (size == 1) {
            return CollectionHelper.emptyImmutableSeqBy(this);
        }

        var builder = CollectionHelper.<E>newImmutableSeqBuilderBy(this);
        builder.sizeHint(size - 1);

        Iterator<? extends E> iterator = iterator();

        for (int i = 0; i < index; i++) {
            builder.plusAssign(iterator.next());
        }

        iterator.next();
        while (iterator.hasNext()) {
            builder.plusAssign(iterator.next());
        }
        return builder.build();
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("sorted(Comparator<E>)")
    default @NotNull ImmutableSeq<E> sorted() {
        return sorted(null);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> sorted(@Nullable Comparator<? super E> comparator) {
        Object[] arr = toArray();
        Arrays.sort(arr, ((Comparator<? super Object>) comparator));
        return CollectionHelper.<E>immutableSeqFactoryBy(this).from((E[]) arr);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> reversed() {
        final int size = this.size();
        if (size == 0) {
            return CollectionHelper.emptyImmutableSeqBy(this);
        }
        var builder = CollectionHelper.<E>newImmutableSeqBuilderBy(this);
        builder.sizeHint(size);
        reverseIterator().forEachRemaining(builder::plusAssign);
        return builder.build();
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> filter(@NotNull Predicate<? super E> predicate) {
        return filter(CollectionHelper.immutableSeqFactoryBy(this), predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull <Ex extends Throwable> ImmutableSeq<E> filterChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filter(predicate);
    }

    @Override
    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> filterUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filter(predicate);
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull ImmutableSeq<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filter(predicate.negate());
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("filterNot(Predicate<E>)")
    default @NotNull <Ex extends Throwable> ImmutableSeq<E> filterNotChecked(
            @NotNull CheckedPredicate<? super E, ? extends Ex> predicate) throws Ex {
        return filterNot(predicate);
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("filterNot(Predicate<E>)")
    default @NotNull ImmutableSeq<E> filterNotUnchecked(
            @NotNull CheckedPredicate<? super E, ?> predicate) {
        return filterNot(predicate);
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull ImmutableSeq<@NotNull E> filterNotNull() {
        return this.filter(Predicates.isNotNull());
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("filter(Predicate<E>)")
    default <U> @NotNull ImmutableSeq<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return view().<U>filterIsInstance(clazz).toImmutableSeq();
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        return map(CollectionHelper.<U>immutableSeqFactoryBy(this), mapper);
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("map(Function<E, U>)")
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> mapChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return map(mapper);
    }

    @Override
    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("map(Function<E, U>)")
    default <U> @NotNull ImmutableSeq<U> mapUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return map(mapper);
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        return mapIndexed(CollectionHelper.<U>immutableSeqFactoryBy(this), mapper);
    }

    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("mapIndexed(IndexedFunction<E, U>)")
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> mapIndexedChecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapIndexed(mapper);
    }

    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("mapIndexed(IndexedFunction<E, U>)")
    default <U> @NotNull ImmutableSeq<U> mapIndexedUnchecked(@NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
        return mapIndexed(mapper);
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        return mapNotNull(CollectionHelper.<U>immutableSeqFactoryBy(this), mapper);
    }

    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("mapNotNull(Function<E, U>)")
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> mapNotNullChecked(
            @NotNull CheckedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapNotNull(mapper);
    }

    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("mapNotNull(Function<E, U>)")
    default <U> @NotNull ImmutableSeq<U> mapNotNullUnchecked(@NotNull CheckedFunction<? super E, ? extends U, ?> mapper) {
        return mapNotNull(mapper);
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<@NotNull U> mapIndexedNotNull(
            @NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        return mapIndexedNotNull(CollectionHelper.<U>immutableSeqFactoryBy(this), mapper);
    }

    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("mapIndexedNotNull(IndexedFunction<E, U>)")
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> mapIndexedNotNullChecked(
            @NotNull CheckedIndexedFunction<? super E, ? extends U, ? extends Ex> mapper) throws Ex {
        return mapIndexedNotNull(mapper);
    }

    @Contract(pure = true)
    @ApiStatus.NonExtendable
    @DelegateBy("mapIndexedNotNull(IndexedFunction<E, U>)")
    default <U> @NotNull ImmutableSeq<U> mapIndexedNotNullUnchecked(@NotNull CheckedIndexedFunction<? super E, ? extends U, ?> mapper) {
        return mapIndexedNotNull(mapper);
    }

    @Override
    @Contract(pure = true)
    default @NotNull <U> ImmutableSeq<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        return mapMulti(CollectionHelper.immutableSeqFactoryBy(this), mapper);
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("mapMulti(BiConsumer<E, Consumer<U>>)")
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> mapMultiChecked(
            @NotNull CheckedBiConsumer<? super E, ? super Consumer<? super U>, Ex> mapper) throws Ex {
        return mapMulti(mapper);
    }

    @Override
    @Contract(pure = true)
    @DelegateBy("mapMulti(BiConsumer<E, Consumer<U>>)")
    default <U> @NotNull ImmutableSeq<U> mapMultiUnchecked(
            @NotNull CheckedBiConsumer<? super E, ? super Consumer<? super U>, ?> mapper) {
        return mapMulti(mapper);
    }

    @Override
    @Contract(pure = true)
    default @NotNull <U> ImmutableSeq<U> mapIndexedMulti(@NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        return mapIndexedMulti(CollectionHelper.immutableSeqFactoryBy(this), mapper);
    }

    @Contract(pure = true)
    @DelegateBy("mapIndexedMulti(IndexedBiConsumer<E, Consumer<U>>)")
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> mapIndexedMultiChecked(
            @NotNull CheckedIndexedBiConsumer<? super E, ? super Consumer<? super U>, Ex> mapper) throws Ex {
        return mapIndexedMulti(mapper);
    }

    @Contract(pure = true)
    @DelegateBy("mapIndexedMulti(IndexedBiConsumer<E, Consumer<U>>)")
    default <U> @NotNull ImmutableSeq<U> mapIndexedMultiUnchecked(
            @NotNull CheckedIndexedBiConsumer<? super E, ? super Consumer<? super U>, ?> mapper) {
        return mapIndexedMulti(mapper);
    }

    @Override
    @Contract(pure = true)
    default <U> @NotNull ImmutableSeq<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        return flatMap(CollectionHelper.immutableSeqFactoryBy(this), mapper);
    }

    @Override
    @DelegateBy("flatMap(Function<E, Iterable<U>>)")
    default <U, Ex extends Throwable> @NotNull ImmutableSeq<U> flatMapChecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ? extends Ex> mapper) throws Ex {
        return flatMap(mapper);
    }

    @Override
    @DelegateBy("flatMap(Function<E, Iterable<U>>)")
    default <U> @NotNull ImmutableSeq<U> flatMapUnchecked(
            @NotNull CheckedFunction<? super E, ? extends Iterable<? extends U>, ?> mapper) {
        return flatMap(mapper);
    }

    @Override
    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U> @NotNull ImmutableSeq<@NotNull Tuple2<E, U>> zip(@NotNull Iterable<? extends U> other) {
        return zip(other, Tuple::of);
    }

    @Contract(pure = true)
    default <U, R> @NotNull ImmutableSeq<R> zip(@NotNull Iterable<? extends U> other, @NotNull BiFunction<? super E, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(mapper);

        var factory = CollectionHelper.<R>immutableSeqFactoryBy(this);

        if (this.isEmpty() || AnyTraversable.knownSize(other) == 0) {
            return factory.empty();
        }

        var builder = factory.newCollectionBuilder();
        Iterator<? extends E> it1 = this.iterator();
        Iterator<? extends U> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            builder.plusAssign(mapper.apply(it1.next(), it2.next()));
        }
        return builder.build();
    }

    @Contract(pure = true)
    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U, R, Ex extends Throwable> @NotNull ImmutableSeq<R> zipChecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiFunction<? super E, ? super U, ? extends R, ? extends Ex> mapper) throws Ex {
        return zip(other, mapper);
    }

    @Contract(pure = true)
    @DelegateBy("zip(Iterable<U>, BiFunction<E, U, R>)")
    default <U, R> @NotNull ImmutableSeq<R> zipUnchecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiFunction<? super E, ? super U, ? extends R, ?> mapper) {
        return zip(other, mapper);
    }

    @Override
    default <U, V> @NotNull ImmutableSeq<@NotNull Tuple3<E, U, V>> zip3(@NotNull Iterable<? extends U> other1, @NotNull Iterable<? extends V> other2) {
        Objects.requireNonNull(other1);
        Objects.requireNonNull(other2);

        var factory = CollectionHelper.<Tuple3<E, U, V>>immutableSeqFactoryBy(this);

        if (this.isEmpty() || AnyTraversable.knownSize(other1) == 0 || AnyTraversable.knownSize(other2) == 0) {
            return factory.empty();
        }

        var builder = factory.newCollectionBuilder();
        Iterator<? extends E> it1 = this.iterator();
        Iterator<? extends U> it2 = other1.iterator();
        Iterator<? extends V> it3 = other2.iterator();

        while (it1.hasNext() && it2.hasNext() && it3.hasNext()) {
            builder.plusAssign(Tuple.of(it1.next(), it2.next(), it3.next()));
        }
        return builder.build();
    }

    @Override
    @Contract(pure = true)
    default <R> Tuple2<? extends ImmutableSeq<E>, ? extends ImmutableSeq<E>> partition(@NotNull Predicate<? super E> predicate) {
        return partition(ImmutableSeq.factory(), predicate);
    }

    @Contract(pure = true)
    default @NotNull ImmutableSeq<E> distinct() {
        return view().distinct().toImmutableSeq();
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Seq<E> onEach(@NotNull Consumer<? super E> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull Seq<E> onEachChecked(@NotNull CheckedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Seq<E> onEachUnchecked(@NotNull CheckedConsumer<? super E, ?> action) {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEachIndexed(IndexedConsumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Seq<E> onEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        forEachIndexed(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEachIndexed(IndexedConsumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull Seq<E> onEachChecked(@NotNull CheckedIndexedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEachIndexed(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEachIndexed(IndexedConsumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Seq<E> onEachUnchecked(@NotNull CheckedIndexedConsumer<? super E, ?> action) {
        return onEachIndexed(action);
    }
}
