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

import kala.annotations.DelegateBy;
import kala.collection.immutable.ImmutableArray;
import kala.collection.internal.convert.AsJavaConvert;
import kala.function.CheckedConsumer;
import kala.function.CheckedIndexedConsumer;
import kala.function.IndexedBiConsumer;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.function.Predicates;
import kala.index.Index;
import kala.index.Indexes;
import kala.collection.internal.view.SeqViews;
import kala.annotations.Covariant;
import kala.collection.internal.CollectionHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public interface SeqView<@Covariant E> extends CollectionView<E>, SeqLike<E>, AnySeqView<E> {

    //region Narrow method

    @Contract(value = "_ -> param1", pure = true)
    static <E> SeqView<E> narrow(SeqView<? extends E> view) {
        return (SeqView<E>) view;
    }

    //endregion

    static <E> @NotNull SeqView<E> empty() {
        return (SeqView<E>) SeqViews.Empty.INSTANCE;
    }

    static <E> @NotNull SeqView<E> of(E value) {
        return new SeqViews.Single<>(value);
    }

    @Override
    default @NotNull String className() {
        return "SeqView";
    }

    @Override
    default @NotNull SeqView<E> view() {
        return this;
    }

    @Override
    default @NotNull SeqView<E> slice(@Index int beginIndex, @Index int endIndex) {
        final int size = this.size();
        beginIndex = Indexes.checkBeginIndex(beginIndex, size);
        endIndex = Indexes.checkEndIndex(beginIndex, endIndex, size);

        return new SeqViews.Slice<>(this, beginIndex, endIndex);
    }

    @Override
    default @NotNull SeqView<E> sliceView(@Index int beginIndex, @Index int endIndex) {
        return slice(beginIndex, endIndex);
    }

    default @NotNull SeqView<E> drop(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this;
        }
        final int ks = this.knownSize();
        if (ks == 0) {
            return SeqView.empty();
        }
        if (ks > 0 && n >= ks) {
            return SeqView.empty();
        }
        return new SeqViews.Drop<>(this, n);
    }

    default @NotNull SeqView<E> dropLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return this;
        }
        final int ks = this.knownSize();
        if (ks == 0) {
            return SeqView.empty();
        }
        if (ks > 0 && n >= ks) {
            return SeqView.empty();
        }
        return new SeqViews.DropLast<>(this, n);
    }

    default @NotNull SeqView<E> dropWhile(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SeqViews.DropWhile<>(this, predicate);
    }

    default @NotNull SeqView<E> take(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return SeqView.empty();
        }
        final int ks = this.knownSize();
        if (ks == 0) {
            return SeqView.empty();
        }
        if (ks > 0 && n >= ks) {
            return this;
        }
        return new SeqViews.Take<>(this, n);
    }

    default @NotNull SeqView<E> takeLast(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        if (n == 0) {
            return SeqView.empty();
        }
        final int ks = this.knownSize();
        if (ks == 0) {
            return SeqView.empty();
        }
        if (ks > 0 && n >= ks) {
            return this;
        }
        return new SeqViews.TakeLast<>(this, n);
    }

    default @NotNull SeqView<E> takeWhile(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SeqViews.TakeWhile<>(this, predicate);
    }

    default @NotNull SeqView<E> updated(int index, E newValue) {
        index = Indexes.checkIndex(index, size()); // TODO
        return new SeqViews.Updated<>(this, index, newValue);
    }

    default @NotNull SeqView<E> concat(@NotNull SeqLike<? extends E> other) {
        Objects.requireNonNull(other);
        return new SeqViews.Concat<>(this, narrow(other.view()));
    }

    @Override
    default @NotNull SeqView<E> concat(java.util.@NotNull List<? extends E> other) {
        Objects.requireNonNull(other);
        if (other instanceof AsJavaConvert.SeqAsJava<? extends E, ?> otherSeq) {
            return concat(otherSeq.source);
        } else {
            return concat(Seq.wrapJava(other).view());
        }
    }

    default @NotNull SeqView<E> appended(E value) {
        return new SeqViews.Appended<>(this, value);
    }

    default @NotNull SeqView<E> appendedAll(@NotNull Iterable<? extends E> postfix) {
        Objects.requireNonNull(postfix);
        return new SeqViews.Concat<>(this, CollectionHelper.asSeq(postfix));
    }

    default @NotNull SeqView<E> appendedAll(E... postfix) {
        Objects.requireNonNull(postfix);
        return new SeqViews.Concat<>(this, ArraySeq.wrap(postfix));
    }

    default @NotNull SeqView<E> prepended(E value) {
        return new SeqViews.Prepended<>(this, value);
    }

    default @NotNull SeqView<E> prependedAll(@NotNull Iterable<? extends E> prefix) {
        Objects.requireNonNull(prefix);
        return new SeqViews.Concat<>(CollectionHelper.asSeq(prefix), this);
    }

    default @NotNull SeqView<E> prependedAll(E... prefix) {
        Objects.requireNonNull(prefix);
        return new SeqViews.Concat<>(ArraySeq.wrap(prefix), this);
    }

    default @NotNull SeqLike<E> inserted(@Index int index, E value) {
        index = Indexes.checkPositionIndex(index, size());
        return new SeqViews.Inserted<>(this, index, value);
    }

    default @NotNull SeqView<E> removedAt(@Index int index) {
        return new SeqViews.RemovedAt<>(this, Indexes.checkIndex(index, size()));
    }

    @DelegateBy("sorted(Comparator<E>)")
    default @NotNull SeqView<E> sorted() {
        return sorted(null);
    }

    default @NotNull SeqView<E> sorted(@SuppressWarnings("ConstantConditions") Comparator<? super E> comparator) {
        return new SeqViews.Sorted<>(this, comparator);
    }

    default @NotNull SeqView<E> reversed() {
        return new SeqViews.Reversed<>(this);
    }

    @Override
    default @NotNull SeqView<E> filter(@NotNull Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        return new SeqViews.Filter<>(this, predicate);
    }

    @Override
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull SeqView<E> filterNot(@NotNull Predicate<? super E> predicate) {
        return filter(predicate.negate());
    }

    @Override
    @DelegateBy("filter(Predicate<E>)")
    default @NotNull SeqView<@NotNull E> filterNotNull() {
        return filter(Predicates.isNotNull());
    }

    @Override
    @DelegateBy("filter(Predicate<E>)")
    default <U> @NotNull SeqView<@NotNull U> filterIsInstance(@NotNull Class<? extends U> clazz) {
        return (SeqView<U>) filter(Predicates.isInstance(clazz));
    }

    @Override
    default <U> @NotNull SeqView<U> map(@NotNull Function<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqViews.Mapped<>(this, mapper);
    }

    @Override
    default <U> @NotNull SeqView<U> mapIndexed(@NotNull IndexedFunction<? super E, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqViews.MapIndexed<>(this, mapper);
    }

    @Override
    default <U> @NotNull SeqView<@NotNull U> mapNotNull(@NotNull Function<? super E, ? extends @Nullable U> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqViews.MapNotNull<>(this, mapper);
    }

    @Override
    default <U> @NotNull SeqView<@NotNull U> mapIndexedNotNull(@NotNull IndexedFunction<? super E, ? extends @Nullable U> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqViews.MapIndexedNotNull<>(this, mapper);
    }

    @Override
    default <U> @NotNull SeqView<U> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqViews.MapMulti<>(this, mapper);
    }

    @Override
    default <U> @NotNull SeqView<U> mapIndexedMulti(@NotNull IndexedBiConsumer<? super E, ? super Consumer<? super U>> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqViews.MapIndexedMulti<>(this, mapper);
    }

    @Override
    default <U> @NotNull SeqView<U> flatMap(@NotNull Function<? super E, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper);
        return new SeqViews.FlatMapped<>(this, mapper);
    }

    @Override
    default @NotNull SeqView<E> distinct() {
        LinkedHashSet<E> set = new LinkedHashSet<>();
        for (E e : this) {
            set.add(e);
        }
        return ((ImmutableArray<E>) ImmutableArray.Unsafe.wrap(set.toArray())).view();
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull SeqView<E> onEach(@NotNull Consumer<? super E> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull SeqView<E> onEachChecked(@NotNull CheckedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull SeqView<E> onEachUnchecked(@NotNull CheckedConsumer<? super E, ?> action) {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEachIndexed(IndexedConsumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull SeqView<E> onEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        forEachIndexed(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEachIndexed(IndexedConsumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull SeqView<E> onEachChecked(@NotNull CheckedIndexedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEachIndexed(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEachIndexed(IndexedConsumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull SeqView<E> onEachUnchecked(@NotNull CheckedIndexedConsumer<? super E, ?> action) {
        return onEachIndexed(action);
    }
}
