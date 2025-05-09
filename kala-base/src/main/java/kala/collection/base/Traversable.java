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
package kala.collection.base;

import kala.annotations.Covariant;
import kala.annotations.DelegateBy;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.*;
import kala.concurrent.Granularity;
import kala.concurrent.ConcurrentScope;
import kala.concurrent.LateInitCountDownLatch;
import kala.control.Option;
import kala.collection.factory.CollectionFactory;
import kala.function.*;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.value.primitive.IntVar;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

@FunctionalInterface
@SuppressWarnings("unchecked")
public interface Traversable<@Covariant T> extends Iterable<T>, AnyTraversable<T> {

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> @NotNull Traversable<T> narrow(Traversable<? extends T> traversable) {
        return (Traversable<T>) traversable;
    }

    static <T> @NotNull Traversable<T> wrap(@NotNull Iterable<T> iterable) {
        Objects.requireNonNull(iterable);

        if (iterable instanceof Traversable) {
            return narrow((Traversable<T>) iterable);
        }

        return ofSupplier(iterable::iterator);
    }

    static <T> @NotNull Traversable<T> ofSupplier(@NotNull Supplier<? extends Iterator<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        return new Traversable<>() {
            @Override
            public @NotNull Iterator<T> iterator() {
                return Iterators.narrow(supplier.get());
            }

            @Override
            public String toString() {
                return joinToString(", ", "Traversable[", "]");
            }
        };
    }

    @ApiStatus.NonExtendable
    default @NotNull Traversable<T> asGeneric() {
        return this;
    }

    @NotNull Iterator<T> iterator();

    @Override
    default Spliterator<T> spliterator() {
        return AnyTraversable.super.spliterator();
    }

    /// Returns a sequential [Stream] with this as its source.
    ///
    /// @return a sequential [Stream] over the elements in this `Traversable`
    @Override
    default @NotNull Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Returns a possibly parallel {@code Stream} with this as its source.
     * It is allowable for this method to return a sequential stream.
     *
     * @return a possibly parallel {@code Stream} over the elements in this
     */
    @Override
    default @NotNull Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    default T elementAt(int index) {
        final Iterator<T> it = this.iterator();
        for (int i = 0; i < index; i++) {
            if (it.hasNext()) {
                it.next();
            } else {
                throw new IndexOutOfBoundsException("index: " + index);
            }
        }
        if (it.hasNext()) {
            return it.next();
        } else {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    default T getAny() {
        return iterator().next();
    }

    default @Nullable T getAnyOrNull() {
        return isNotEmpty() ? getAny() : null;
    }

    default @NotNull Option<T> getAnyOption() {
        return isNotEmpty() ? Option.some(getAny()) : Option.none();
    }

    //region Element Retrieval Operations

    default @NotNull Option<T> find(@NotNull Predicate<? super T> predicate) {
        for (T t : this) {
            if (predicate.test(t)) {
                return Option.some(t);
            }
        }
        return Option.none();
    }

    //endregion

    //region Element Conditions

    default boolean contains(Object value) {
        return Iterators.contains(iterator(), value);
    }

    default boolean containsAll(Object @NotNull [] values) {
        for (Object value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    //@Override
    default boolean containsAll(@NotNull Iterable<?> values) {
        for (Object value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    default boolean sameElements(@NotNull Iterable<?> other) {
        return Iterators.sameElements(iterator(), other.iterator());
    }

    default boolean sameElements(@NotNull Iterable<?> other, boolean identity) {
        if (!identity) {
            return sameElements(other);
        } else {
            return Iterators.sameElements(iterator(), other.iterator(), true);
        }
    }

    /**
     * Tests whether any element of this {@code Traversable} match the {@code predicate}.
     *
     * @return {@code true} if either any element of this {@code Traversable} match the {@code predicate},
     * otherwise {@code false}
     */
    default boolean anyMatch(@NotNull Predicate<? super T> predicate) {
        return Iterators.anyMatch(iterator(), predicate);
    }

    @ApiStatus.NonExtendable
    default <Ex extends Throwable> boolean anyMatchChecked(@NotNull CheckedPredicate<? super T, ? extends Ex> predicate) throws Ex {
        return anyMatch(predicate);
    }

    @ApiStatus.NonExtendable
    default boolean anyMatchUnchecked(@NotNull CheckedPredicate<? super T, ?> predicate) {
        return anyMatch(predicate);
    }

    /**
     * Tests whether all elements of this {@code Traversable} match the {@code predicate}.
     *
     * @return {@code true} if either all elements of this {@code Traversable} match the {@code predicate} or
     * the {@code Traversable} is empty, otherwise {@code false}
     */
    default boolean allMatch(@NotNull Predicate<? super T> predicate) {
        return Iterators.allMatch(iterator(), predicate);
    }

    @ApiStatus.NonExtendable
    default <Ex extends Throwable> boolean allMatchChecked(@NotNull CheckedPredicate<? super T, ? extends Ex> predicate) throws Ex {
        return allMatch(predicate);
    }

    @ApiStatus.NonExtendable
    default boolean allMatchUnchecked(@NotNull CheckedPredicate<? super T, ?> predicate) {
        return allMatch(predicate);
    }

    /**
     * Tests whether none elements of this {@code Traversable} match the {@code predicate}.
     *
     * @return {@code true} if either none elements of this {@code Traversable} match the {@code predicate} or
     * the {@code Traversable} is empty, otherwise {@code false}
     */
    default boolean noneMatch(@NotNull Predicate<? super T> predicate) {
        return Iterators.noneMatch(iterator(), predicate);
    }

    @ApiStatus.NonExtendable
    default <Ex extends Throwable> boolean noneMatchChecked(@NotNull CheckedPredicate<? super T, ? extends Ex> predicate) throws Ex {
        return noneMatch(predicate);
    }

    @ApiStatus.NonExtendable
    default boolean noneMatchUnchecked(@NotNull CheckedPredicate<? super T, ?> predicate) {
        return noneMatch(predicate);
    }

    /**
     * Equivalent to {@code this.zip(other).anyMatch(it -> predicate.test(it._1, it._2))}
     */
    default <U> boolean anyMatchWith(@NotNull Iterable<? extends U> other, @NotNull BiPredicate<? super T, ? super U> predicate) {
        return Iterators.anyMatchWith(iterator(), other.iterator(), predicate);
    }

    @ApiStatus.NonExtendable
    default <U, Ex extends Throwable> boolean anyMatchWithChecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiPredicate<? super T, ? super U, ? extends Ex> predicate) throws Ex {
        return anyMatchWith(other, predicate);
    }

    @ApiStatus.NonExtendable
    default <U> boolean anyMatchWithUnchecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiPredicate<? super T, ? super U, ?> predicate) {
        return anyMatchWith(other, predicate);
    }

    /**
     * Equivalent to {@code this.zip(other).allMatch(it -> predicate.test(it._1, it._2))}
     */
    default <U> boolean allMatchWith(@NotNull Iterable<? extends U> other, @NotNull BiPredicate<? super T, ? super U> predicate) {
        return Iterators.allMatchWith(iterator(), other.iterator(), predicate);
    }

    @ApiStatus.NonExtendable
    default <U, Ex extends Throwable> boolean allMatchWithChecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiPredicate<? super T, ? super U, ? extends Ex> predicate) throws Ex {
        return allMatchWith(other, predicate);
    }

    @ApiStatus.NonExtendable
    default <U> boolean allMatchWithUnchecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiPredicate<? super T, ? super U, ?> predicate) {
        return allMatchWith(other, predicate);
    }

    /**
     * Equivalent to {@code this.zip(other).noneMatch(it -> predicate.test(it._1, it._2))}
     */
    default <U> boolean noneMatchWith(@NotNull Iterable<? extends U> other, @NotNull BiPredicate<? super T, ? super U> predicate) {
        return Iterators.noneMatchWith(iterator(), other.iterator(), predicate);
    }

    @ApiStatus.NonExtendable
    default <U, Ex extends Throwable> boolean noneMatchWithChecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiPredicate<? super T, ? super U, ? extends Ex> predicate) throws Ex {
        return noneMatchWith(other, predicate);
    }

    @ApiStatus.NonExtendable
    default <U> boolean noneMatchWithUnchecked(
            @NotNull Iterable<? extends U> other,
            @NotNull CheckedBiPredicate<? super T, ? super U, ?> predicate) {
        return noneMatchWith(other, predicate);
    }

    //endregion

    @DelegateBy("filterTo(Growable<T>, Predicate<T>)")
    default <R> R filter(@NotNull CollectionFactory<T, ?, R> factory, @NotNull Predicate<? super T> predicate) {
        return filterTo(factory.newCollectionBuilder(), predicate).build();
    }

    @ApiStatus.NonExtendable
    @DelegateBy("filter(CollectionFactory<T, ?, R>, Predicate<T>)")
    default <R> R filterNot(@NotNull CollectionFactory<T, ?, R> factory, @NotNull Predicate<? super T> predicate) {
        return filter(factory, predicate.negate());
    }

    @ApiStatus.NonExtendable
    @DelegateBy("filter(CollectionFactory<T, ?, R>, Predicate<T>)")
    default <R> R filterNotNull(@NotNull CollectionFactory<T, ?, R> factory) {
        return filter(factory, Predicates.isNotNull());
    }

    @DelegateBy("filterIsInstanceTo(Growable<U>, Class<U>)")
    default <U, R> R filterIsInstance(@NotNull CollectionFactory<U, ?, R> factory, Class<? extends U> clazz) {
        return filterIsInstanceTo(factory.newCollectionBuilder(), clazz).build();
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends Growable<? super T>> @NotNull G filterTo(@NotNull G destination, @NotNull Predicate<? super T> predicate) {
        for (T e : this) {
            if (predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @ApiStatus.NonExtendable
    @DelegateBy("filterTo(G, Predicate<T>)")
    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends Growable<? super T>> @NotNull G filterNotTo(@NotNull G destination, @NotNull Predicate<? super T> predicate) {
        return filterTo(destination, predicate.negate());
    }

    @ApiStatus.NonExtendable
    @DelegateBy("filterTo(G, Predicate<T>)")
    @Contract(value = "_ -> param1", mutates = "param1")
    default <G extends Growable<? super T>> @NotNull G filterNotNullTo(@NotNull G destination) {
        return filterTo(destination, Predicates.isNotNull());
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<? super U>> @NotNull G filterIsInstanceTo(@NotNull G destination, @NotNull Class<? extends U> clazz) {
        for (T value : this) {
            if (clazz.isInstance(value)) {
                destination.plusAssign((U) value);
            }
        }
        return destination;
    }

    @DelegateBy("mapTo(Growable<U>, Function<T, U>)")
    default <U, R> R map(@NotNull CollectionFactory<U, ?, R> factory, @NotNull Function<? super T, ? extends U> mapper) {
        return mapTo(factory.newCollectionBuilder(knownSize()), mapper).build();
    }

    default <R> R mapToInt(@NotNull IntCollectionFactory<?, R> factory, @NotNull ToIntFunction<? super T> mapper) {
        return IntCollectionFactory.buildBy(factory, consumer -> {
            for (T e : this) {
                consumer.accept(mapper.applyAsInt(e));
            }
        });
    }

    default <R> R mapToLong(@NotNull LongCollectionFactory<?, R> factory, @NotNull ToLongFunction<? super T> mapper) {
        return LongCollectionFactory.buildBy(factory, consumer -> {
            for (T e : this) {
                consumer.accept(mapper.applyAsLong(e));
            }
        });
    }

    default <R> R mapToDouble(@NotNull DoubleCollectionFactory<?, R> factory, @NotNull ToDoubleFunction<? super T> mapper) {
        return DoubleCollectionFactory.buildBy(factory, consumer -> {
            for (T e : this) {
                consumer.accept(mapper.applyAsDouble(e));
            }
        });
    }

    default <R> R mapToChar(@NotNull CharCollectionFactory<?, R> factory, @NotNull ToCharFunction<? super T> mapper) {
        return CharCollectionFactory.buildBy(factory, consumer -> {
            for (T e : this) {
                consumer.accept(mapper.applyAsChar(e));
            }
        });
    }

    default <R> R mapToBoolean(@NotNull BooleanCollectionFactory<?, R> factory, @NotNull ToBooleanFunction<? super T> mapper) {
        return BooleanCollectionFactory.buildBy(factory, consumer -> {
            for (T e : this) {
                consumer.accept(mapper.applyAsBoolean(e));
            }
        });
    }

    @DelegateBy("mapNotNullTo(Growable<U>, Function<T, U>)")
    default <U, R> R mapNotNull(@NotNull CollectionFactory<U, ?, R> factory, @NotNull Function<? super T, ? extends U> mapper) {
        return mapNotNullTo(factory.newCollectionBuilder(knownSize()), mapper).build();
    }

    default @NotNull <U, R> R mapMulti(
            @NotNull CollectionFactory<U, ?, R> factory,
            @NotNull BiConsumer<? super T, ? super Consumer<? super U>> mapper) {
        return mapMultiTo(factory.newCollectionBuilder(), mapper).build();
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<? super U>> @NotNull G mapTo(@NotNull G destination, @NotNull Function<? super T, ? extends U> mapper) {
        for (T e : this) {
            destination.plusAssign(mapper.apply(e));
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends IntGrowable> @NotNull G mapToIntTo(@NotNull G destination, @NotNull ToIntFunction<? super T> mapper) {
        for (T e : this) {
            destination.plusAssign(mapper.applyAsInt(e));
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends LongGrowable> @NotNull G mapToLongTo(@NotNull G destination, @NotNull ToLongFunction<? super T> mapper) {
        for (T e : this) {
            destination.plusAssign(mapper.applyAsLong(e));
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends DoubleGrowable> @NotNull G mapToDoubleTo(@NotNull G destination, @NotNull ToDoubleFunction<? super T> mapper) {
        for (T e : this) {
            destination.plusAssign(mapper.applyAsDouble(e));
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends CharGrowable> @NotNull G mapToCharTo(@NotNull G destination, @NotNull ToCharFunction<? super T> mapper) {
        for (T e : this) {
            destination.plusAssign(mapper.applyAsChar(e));
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends BooleanGrowable> @NotNull G mapToBooleanTo(@NotNull G destination, @NotNull ToBooleanFunction<? super T> mapper) {
        for (T e : this) {
            destination.plusAssign(mapper.applyAsBoolean(e));
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<? super U>> @NotNull G mapNotNullTo(
            @NotNull G destination,
            @NotNull Function<? super T, ? extends U> mapper) {
        for (T e : this) {
            U u = mapper.apply(e);
            if (u != null) {
                destination.plusAssign(u);
            }
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default @NotNull <U, G extends Growable<? super U>> G mapMultiTo(
            @NotNull G destination,
            @NotNull BiConsumer<? super T, ? super Consumer<? super U>> mapper) {
        Consumer<? super U> consumer = destination::plusAssign;
        for (T value : this) {
            mapper.accept(value, consumer);
        }
        return destination;
    }

    @DelegateBy("flatMapTo(Growable<U>, Function<T, Iterable<U>>)")
    default <U, R> @NotNull R flatMap(
            @NotNull CollectionFactory<U, ?, R> factory,
            @NotNull Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return flatMapTo(factory.newCollectionBuilder(), mapper).build();
    }

    @DelegateBy("flatMapToIntTo(IntGrowable, Function<T, IntTraversable>)")
    default <R> @NotNull R flatMapToInt(
            @NotNull IntCollectionFactory<?, R> factory,
            @NotNull Function<? super T, ? extends IntTraversable> mapper) {
        return flatMapToIntTo(factory.newCollectionBuilder(), mapper).build();
    }

    @DelegateBy("flatMapToLongTo(LongGrowable, Function<T, LongTraversable>)")
    default <R> @NotNull R flatMapToLong(
            @NotNull LongCollectionFactory<?, R> factory,
            @NotNull Function<? super T, ? extends LongTraversable> mapper) {
        return flatMapToLongTo(factory.newCollectionBuilder(), mapper).build();
    }

    @DelegateBy("flatMapToDoubleTo(DoubleGrowable, Function<T, DoubleTraversable>)")
    default <R> @NotNull R flatMapToDouble(
            @NotNull DoubleCollectionFactory<?, R> factory,
            @NotNull Function<? super T, ? extends DoubleTraversable> mapper) {
        return flatMapToDoubleTo(factory.newCollectionBuilder(), mapper).build();
    }

    default <U, G extends Growable<? super U>> G flatMapTo(
            @NotNull G destination,
            @NotNull Function<? super T, ? extends Iterable<? extends U>> mapper) {
        for (T value : this) {
            destination.plusAssign(mapper.apply(value));
        }

        return destination;
    }

    default <G extends IntGrowable> G flatMapToIntTo(
            @NotNull G destination,
            @NotNull Function<? super T, ? extends IntTraversable> mapper) {
        for (T value : this) {
            destination.plusAssign(mapper.apply(value));
        }
        return destination;
    }

    default <G extends LongGrowable> G flatMapToLongTo(
            @NotNull G destination,
            @NotNull Function<? super T, ? extends LongTraversable> mapper) {
        for (T value : this) {
            destination.plusAssign(mapper.apply(value));
        }
        return destination;
    }

    default <G extends DoubleGrowable> G flatMapToDoubleTo(
            @NotNull G destination,
            @NotNull Function<? super T, ? extends DoubleTraversable> mapper) {
        for (T value : this) {
            destination.plusAssign(mapper.apply(value));
        }
        return destination;
    }

    default <R> Tuple2<R, R> partition(@NotNull CollectionFactory<T, ?, R> factory, @NotNull Predicate<? super T> predicate) {
        @SuppressWarnings("rawtypes")
        CollectionFactory uncheckedFactory = factory;

        Object builder1 = uncheckedFactory.newBuilder();
        Object builder2 = uncheckedFactory.newBuilder();

        for (T e : this) {
            Object builder = predicate.test(e) ? builder1 : builder2;
            uncheckedFactory.addToBuilder(builder, e);
        }

        return Tuple.of((R) uncheckedFactory.build(builder1), (R) uncheckedFactory.build(builder2));
    }

    @DelegateBy("distinctTo(Growable<T>)")
    default <R> R distinct(@NotNull CollectionFactory<T, ?, R> factory) {
        return distinctTo(factory.newCollectionBuilder()).build();
    }

    @DelegateBy("distinctByTo(Growable<T>, Function<T, U>)")
    default <U, R> R distinctBy(@NotNull CollectionFactory<T, ?, R> factory, Function<? super T, ? extends U> mapper) {
        return distinctByTo(factory.newCollectionBuilder(), mapper).build();
    }

    @DelegateBy("distinctByIntTo(Growable<T>, ToIntFunction<T>)")
    default <R> R distinctByInt(@NotNull CollectionFactory<T, ?, R> factory, ToIntFunction<? super T> mapper) {
        return distinctByIntTo(factory.newCollectionBuilder(), mapper).build();
    }

    default <G extends Growable<? super T>> @NotNull G distinctTo(G destination) {
        HashSet<T> iteratedValues = new HashSet<>();
        for (T value : this) {
            if (iteratedValues.add(value)) {
                destination.plusAssign(value);
            }
        }
        return destination;
    }

    default <U, G extends Growable<? super T>> @NotNull G distinctByTo(G destination, Function<? super T, ? extends U> mapper) {
        HashSet<U> iteratedValues = new HashSet<>();
        for (T value : this) {
            if (iteratedValues.add(mapper.apply(value))) {
                destination.plusAssign(value);
            }
        }
        return destination;
    }

    default <G extends Growable<? super T>> @NotNull G distinctByIntTo(G destination, ToIntFunction<? super T> mapper) {
        HashSet<Integer> iteratedValues = new HashSet<>(); // TODO
        for (T value : this) {
            if (iteratedValues.add(mapper.applyAsInt(value))) {
                destination.plusAssign(value);
            }
        }
        return destination;
    }

    //region Aggregate Operations

    default int count(@NotNull Predicate<? super T> predicate) {
        return Iterators.count(iterator(), predicate);
    }

    @DelegateBy("max(Comparator<T>)")
    default T max() {
        return max(null);
    }

    default T max(Comparator<? super T> comparator) {
        if (isEmpty()) throw new NoSuchElementException();

        return Iterators.max(iterator(), comparator);
    }

    @DelegateBy("max()")
    @ApiStatus.NonExtendable
    default @Nullable T maxOrNull() {
        return isNotEmpty() ? max() : null;
    }

    @DelegateBy("max(Comparator<T>)")
    @ApiStatus.NonExtendable
    default @Nullable T maxOrNull(@NotNull Comparator<? super T> comparator) {
        return isNotEmpty() ? max(comparator) : null;
    }

    @DelegateBy("max()")
    @ApiStatus.NonExtendable
    default @NotNull Option<T> maxOption() {
        return isNotEmpty() ? Option.some(max()) : Option.none();
    }

    @DelegateBy("max(Comparator<T>)")
    @ApiStatus.NonExtendable
    default @NotNull Option<T> maxOption(Comparator<? super T> comparator) {
        return isNotEmpty() ? Option.some(max(comparator)) : Option.none();
    }

    @DelegateBy("min(Comparator<T>)")
    default T min() {
        return min(null);
    }

    default T min(Comparator<? super T> comparator) {
        if (isEmpty()) throw new NoSuchElementException();

        return Iterators.min(iterator(), comparator);
    }

    @DelegateBy("min()")
    @ApiStatus.NonExtendable
    default @Nullable T minOrNull() {
        return isNotEmpty() ? min() : null;
    }

    @DelegateBy("min(Comparator<T>)")
    @ApiStatus.NonExtendable
    default @Nullable T minOrNull(@NotNull Comparator<? super T> comparator) {
        return isNotEmpty() ? min(comparator) : null;
    }

    @DelegateBy("min()")
    @ApiStatus.NonExtendable
    default @NotNull Option<T> minOption() {
        return isNotEmpty() ? Option.some(min()) : Option.none();
    }

    @DelegateBy("min(Comparator<T>)")
    @ApiStatus.NonExtendable
    default @NotNull Option<T> minOption(Comparator<? super T> comparator) {
        return isNotEmpty() ? Option.some(min(comparator)) : Option.none();
    }

    /**
     * Folds this elements by apply {@code op}, starting with {@code zero}, unknown folding order.
     * Because the implementation can specify the folding order freely,
     * the {@code fold} function is usually more efficient than {@link #foldLeft} and {@link #foldRight}.
     *
     * @param zero the start value
     * @param op   the binary operator
     * @return the folded value
     */
    default T fold(T zero, @NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op);
        return foldLeft(zero, op);
    }

    /**
     * Folds this elements by apply {@code op}, starting with {@code zero}, going left to right.
     *
     * <p>If this is a sequential container, use {@code $N} to refer to the Nth element,
     * the return value of the function is {@code op(...op(z, $1), $2, ..., $N)}.
     *
     * @param zero the start value
     * @param op   the binary operator
     * @param <U>  the result type of the binary operator
     * @return the folded value
     */
    default <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super T, ? extends U> op) {
        return Iterators.foldLeft(iterator(), zero, op);
    }

    /**
     * Folds this elements by apply {@code op}, starting with {@code zero}, going right to left.
     *
     * <p>If this is a sequential container, use {@code $N} to refer to the Nth element,
     * the return value of the function is {@code op($1, op($2, ... op($n, z)...))}.
     *
     * @param zero the start value
     * @param op   the binary operator
     * @param <U>  the result type of the binary operator
     * @return the folded value
     */
    default <U> U foldRight(U zero, @NotNull BiFunction<? super T, ? super U, ? extends U> op) {
        return Iterators.foldRight(iterator(), zero, op);
    }

    default <Ex extends Throwable> T foldChecked(
            T zero, @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return fold(zero, op);
    }

    default T foldUnchecked(T zero, @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return fold(zero, op);
    }

    default <U, Ex extends Throwable> U foldLeftChecked(
            U zero, @NotNull CheckedBiFunction<? super U, ? super T, ? extends U, ? extends Ex> op) throws Ex {
        return foldLeft(zero, op);
    }

    default <U> U foldLeftUnchecked(U zero, @NotNull CheckedBiFunction<? super U, ? super T, ? extends U, ?> op) {
        return foldLeft(zero, op);
    }

    default <U, Ex extends Throwable> U foldRightChecked(
            U zero, @NotNull CheckedBiFunction<? super T, ? super U, ? extends U, ? extends Ex> op) throws Ex {
        return foldRight(zero, op);
    }

    default <U> U foldRightUnchecked(U zero, @NotNull CheckedBiFunction<? super T, ? super U, ? extends U, ?> op) {
        return foldRight(zero, op);
    }

    /**
     * Reduces this elements by apply {@code op}.
     *
     * @param op the binary operator
     * @return the reduced value
     * @throws NoSuchElementException if this {@code Traversable} is empty
     */
    default T reduce(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return reduceLeft(op);
    }

    default @Nullable T reduceOrNull(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return reduceLeftOrNull(op);
    }

    /**
     * Reduces this elements by apply {@code op}.
     *
     * @param op the binary operator
     * @return an {@code Option} contain the reduced value or a empty {@code Option} if the {@code Traversable} is empty
     */
    default @NotNull Option<T> reduceOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return reduceLeftOption(op);
    }

    /**
     * Reduces this elements by apply {@code op}, going left to right.
     *
     * <p>If this is a sequential container, use {@code $N} to refer to the Nth element,
     * the return value of the function is {@code op( op( ... op($1, $2) ..., ${n-1}), $n)}.
     *
     * @param op the binary operator
     * @return the reduced value
     * @throws NoSuchElementException if this {@code Traversable} is empty
     */
    default T reduceLeft(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return Iterators.reduceLeft(iterator(), op);
    }

    default @Nullable T reduceLeftOrNull(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return this.isNotEmpty() ? reduceLeft(op) : null;

    }

    /**
     * Reduces this elements by apply {@code op}, going left to right.
     *
     * <p>If this is a sequential container, use {@code $N} to refer to the Nth element,
     * the return value of the function is {@code Option.some(op( op( ... op($1, $2) ..., ${n-1}), $n))}.
     *
     * @param op the binary operator
     * @return an {@code Option} contain the reduced value or a empty {@code Option} if the {@code Traversable} is empty
     */
    default @NotNull Option<T> reduceLeftOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return this.isNotEmpty() ? Option.some(reduceLeft(op)) : Option.none();

    }

    /**
     * Reduces this elements by apply {@code op}, going right to left.
     *
     * <p>If this is a sequential container, use {@code $N} to refer to the Nth element,
     * the return value of the function is {@code op($1, op($2, ..., op(${n-1}, $n)...))}.
     *
     * @param op the binary operator
     * @return the reduced value
     * @throws NoSuchElementException if this {@code Traversable} is empty
     */
    default T reduceRight(@NotNull BiFunction<? super T, ? super T, ? extends T> op) throws NoSuchElementException {
        return Iterators.reduceRight(iterator(), op);
    }

    default @Nullable T reduceRightOrNull(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return this.isNotEmpty() ? reduceRight(op) : null;
    }

    /**
     * Reduces this elements by apply {@code op}, going right to left.
     *
     * <p>If this is a sequential container, use {@code $N} to refer to the Nth element,
     * the return value of the function is {@code Option.some(op($1, op($2, ..., op(${n-1}, $n)...)))}.
     *
     * @param op the binary operator
     * @return an {@code Option} contain the reduced value or a empty {@code Option} if the {@code Foldable} is empty
     */
    default @NotNull Option<T> reduceRightOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return this.isNotEmpty() ? Option.some(reduceRight(op)) : Option.none();
    }

    default <Ex extends Throwable> T reduceChecked(
            @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return reduce(op);
    }

    default T reduceUnchecked(@NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return reduce(op);
    }

    default <Ex extends Throwable> @Nullable T reduceOrNullChecked(
            @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return reduceOrNull(op);
    }

    default @Nullable T reduceOrNullUnchecked(@NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return reduceOrNull(op);
    }

    default <Ex extends Throwable> @NotNull Option<T> reduceOptionChecked(
            @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return reduceOption(op);
    }

    default @NotNull Option<T> reduceOptionUnchecked(@NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return reduceOption(op);
    }

    default <Ex extends Throwable> T reduceLeftChecked(
            @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return reduceLeft(op);
    }

    default T reduceLeftUnchecked(@NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return reduceLeft(op);
    }

    default <Ex extends Throwable> @Nullable T reduceLeftOrNullChecked(
            @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return reduceLeftOrNull(op);
    }

    default @Nullable T reduceLeftOrNullUnchecked(@NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return reduceLeftOrNull(op);
    }

    default <Ex extends Throwable> @NotNull Option<T> reduceLeftOptionChecked(
            @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return reduceLeftOption(op);
    }

    default @NotNull Option<T> reduceLeftOptionUnchecked(@NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return reduceLeftOption(op);
    }

    default <Ex extends Throwable> T reduceRightChecked(
            @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return reduceRight(op);
    }

    default T reduceRightUnchecked(@NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return reduceRight(op);
    }

    default <Ex extends Throwable> @Nullable T reduceRightOrNullChecked(
            @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return reduceRightOrNull(op);
    }

    default @Nullable T reduceRightOrNullUnchecked(@NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return reduceRightOrNull(op);
    }

    default <Ex extends Throwable> @NotNull Option<T> reduceRightOptionChecked(
            @NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ? extends Ex> op) throws Ex {
        return reduceRightOption(op);
    }

    default @NotNull Option<T> reduceRightOptionUnchecked(@NotNull CheckedBiFunction<? super T, ? super T, ? extends T, ?> op) {
        return reduceRightOption(op);
    }

    //endregion

    //region Copy Operations

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(Object @NotNull [] dest) {
        return copyToArray(dest, 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(Object @NotNull [] dest, int destPos) {
        return copyToArray(dest, destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(Object @NotNull [] dest, int destPos, int limit) {
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        if (limit <= 0) {
            return 0;
        }

        final int dl = dest.length; //implicit null check of dest
        if (destPos > dl) {
            return 0;
        }

        int end = Math.min(dl - destPos, limit) + destPos;

        Iterator<T> it = this.iterator();

        int idx = destPos;
        while (it.hasNext() && idx < end) {
            dest[idx++] = it.next();
        }
        return idx - destPos;
    }

    //endregion

    //region Conversion Operations

    @Override
    @DelegateBy("toArray(IntFunction<U[]>)")
    default Object @NotNull [] toArray() {
        return toArray(ObjectArrays.generator());
    }

    @DelegateBy("toArray(IntFunction<U[]>)")
    default <U /*super E*/> U @NotNull [] toArray(@NotNull Class<U> type) {
        return toArray(GenericArrays.generator(type));
    }

    default <U /*super E*/> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        int s = knownSize();
        if (s == 0) {
            return generator.apply(0);
        } else if (s > 0) {
            U[] arr = generator.apply(s);
            this.copyToArray(arr);
            return arr;
        } else {
            return Iterators.toArray((Iterator<U>) iterator(), generator);
        }
    }

    /**
     * @see Collection#toArray(Object[])
     */
    default <U /*super E*/> U @NotNull [] toArray(U @NotNull [] array) {
        final int size = this.size();
        final int arrayLength = array.length;
        U[] res;
        if (arrayLength > size) {
            res = array;
        } else {
            res = (U[]) GenericArrays.create(array.getClass().getComponentType(), size);
        }

        Iterator<T> it = iterator();
        for (int i = 0; i < res.length; i++) {
            if (it.hasNext()) {
                res[i] = (U) it.next();
            } else {
                if (arrayLength > size) {
                    res[i] = null;
                } else if (arrayLength < i) {
                    return Arrays.copyOf(res, i);
                } else {
                    System.arraycopy(res, 0, array, 0, i);
                    if (arrayLength > i) {
                        array[i] = null;
                    }
                }
                return array;
            }
        }
        if (it.hasNext()) {
            throw new ConcurrentModificationException();
        }
        return res;
    }

    //endregion

    //region Traverse Operations

    @Override
    default void forEach(@NotNull Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    @DelegateBy("forEach(Consumer<T>)")
    default <Ex extends Throwable> void forEachChecked(@NotNull CheckedConsumer<? super T, ? extends Ex> action) throws Ex {
        forEach(action);
    }

    @DelegateBy("forEach(Consumer<T>)")
    default void forEachUnchecked(@NotNull CheckedConsumer<? super T, ?> action) {
        forEach(action);
    }

    default void forEachBreakable(@NotNull Predicate<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            if (!action.test(t)) {
                break;
            }
        }
    }

    @DelegateBy("forEachBreakable(Predicate<T>)")
    default <Ex extends Throwable> void forEachBreakableChecked(
            @NotNull CheckedPredicate<? super T, ? extends Ex> action) throws Ex {
        forEachBreakable(action);
    }

    @DelegateBy("forEachBreakable(Predicate<T>)")
    default void forEachBreakableUnchecked(@NotNull CheckedPredicate<? super T, ?> action) {
        forEachBreakable(action);
    }

    default void forEachParallel(@NotNull Consumer<? super T> action) {
        forEachParallel(action, ConcurrentScope.currentExecutor(), Granularity.DEFAULT);
    }

    default void forEachParallel(@NotNull Consumer<? super T> action, Granularity granularity) {
        forEachParallel(action, ConcurrentScope.currentExecutor(), granularity);
    }

    default void forEachParallel(@NotNull Consumer<? super T> action, @NotNull Executor executor) {
        forEachParallel(action, executor, Granularity.DEFAULT);
    }

    default void forEachParallel(
            @NotNull Consumer<? super T> action, @NotNull Executor executor, @NotNull Granularity granularity) {
        try {
            forEachAsync(action, executor, granularity).get();
        } catch (InterruptedException | ExecutionException ignored) {
        }
    }

    default @NotNull Future<Void> forEachAsync(@NotNull Consumer<? super T> action) {
        return forEachAsync(action, ConcurrentScope.currentExecutor(), Granularity.DEFAULT);
    }

    default @NotNull Future<Void> forEachAsync(@NotNull Consumer<? super T> action, @NotNull Executor executor) {
        return forEachAsync(action, executor, Granularity.DEFAULT);
    }

    default @NotNull Future<Void> forEachAsync(@NotNull Consumer<? super T> action, Granularity granularity) {
        return forEachAsync(action, ConcurrentScope.currentExecutor(), granularity);
    }

    default @NotNull Future<Void> forEachAsync(
            @NotNull Consumer<? super T> action, @NotNull Executor executor, @NotNull Granularity granularity) {
        if (granularity != Granularity.ATOM && executor instanceof ForkJoinPool) {
            return ((ForkJoinPool) executor).submit(() -> parallelStream().forEach(action), null);
        } else {
            LateInitCountDownLatch latch = new LateInitCountDownLatch();
            IntVar count = new IntVar();
            forEach(value -> {
                count.increment();
                executor.execute(() -> {
                    try {
                        action.accept(value);
                    } catch (Throwable ignored) {
                    } finally {
                        latch.countDown();
                    }
                });
            });
            latch.init(count.value);
            return latch.awaitFuture();
        }
    }

    default <U> void forEachWith(@NotNull Iterable<? extends U> other, @NotNull BiConsumer<? super T, ? super U> action) {
        Iterators.forEachWith(this.iterator(), other.iterator(), action);
    }

    @ApiStatus.NonExtendable
    @DelegateBy("forEachWith(Iterable<U>, BiConsumer<T, U>)")
    default <U, Ex extends Throwable> void forEachWithChecked(@NotNull Iterable<? extends U> other,
                                                              @NotNull CheckedBiConsumer<? super T, ? super U, ? extends Ex> action) throws Ex {
        forEachWith(other, action);
    }

    @ApiStatus.NonExtendable
    @DelegateBy("forEachWith(Iterable<U>, BiConsumer<T, U>)")
    default <U> void forEachWithUnchecked(@NotNull Iterable<? extends U> other,
                                          @NotNull CheckedBiConsumer<? super T, ? super U, ?> action) {
        forEachWith(other, action);
    }

    default <U> void forEachCross(@NotNull Iterable<? extends U> other, @NotNull BiConsumer<? super T, ? super U> action) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(action);

        for (T value1 : this) {
            for (U value2 : other) {
                action.accept(value1, value2);
            }
        }
    }

    @ApiStatus.NonExtendable
    @DelegateBy("forEachCross(Iterable<U>, BiConsumer<T, U>)")
    default <U, Ex extends Throwable> void forEachCrossChecked(@NotNull Iterable<? extends U> other,
                                                               @NotNull CheckedBiConsumer<? super T, ? super U, ? extends Ex> action) throws Ex {
        forEachCross(other, action);
    }

    @ApiStatus.NonExtendable
    @DelegateBy("forEachCross(Iterable<U>, BiConsumer<T, U>)")
    default <U> void forEachCrossUnchecked(@NotNull Iterable<? extends U> other,
                                           @NotNull CheckedBiConsumer<? super T, ? super U, ?> action) {
        forEachCross(other, action);
    }


    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<T>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Traversable<T> onEach(@NotNull Consumer<? super T> action) {
        forEach(action);
        return this;
    }

    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull Traversable<T> onEachChecked(@NotNull CheckedConsumer<? super T, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Traversable<T> onEachUnchecked(@NotNull CheckedConsumer<? super T, ?> action) {
        return onEach(action);
    }

    //endregion

    //region String Representation

    @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        if (knownSize() == 0) {
            try {
                buffer.append(prefix).append(postfix);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return buffer;
        } else {
            return Iterators.joinTo(iterator(), buffer, separator, prefix, postfix);
        }
    }

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, @NotNull Function<? super T, ? extends CharSequence> transform) {
        return joinTo(buffer, ", ", transform);
    }

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, @NotNull Function<? super T, ? extends CharSequence> transform) {
        return joinTo(buffer, separator, "", "", transform);
    }

    @Contract(value = "_, _, _, _, _ -> param1", mutates = "param1")
    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull Function<? super T, ? extends CharSequence> transform
    ) {
        if (knownSize() == 0) {
            try {
                buffer.append(prefix).append(postfix);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return buffer;
        } else {
            return Iterators.joinTo(iterator(), buffer, separator, prefix, postfix, transform);
        }
    }

    default @NotNull String joinToString(@NotNull Function<? super T, ? extends CharSequence> transform) {
        return joinTo(new StringBuilder(), transform).toString();
    }

    default @NotNull String joinToString(CharSequence separator, @NotNull Function<? super T, ? extends CharSequence> transform) {
        return joinTo(new StringBuilder(), separator, transform).toString();
    }

    default @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix, @NotNull Function<? super T, ? extends CharSequence> transform) {
        return joinTo(new StringBuilder(), separator, prefix, postfix, transform).toString();
    }

    //endregion
}
