package kala.collection.base;

import kala.annotations.Covariant;
import kala.annotations.UnstableName;
import kala.comparator.Comparators;
import kala.concurrent.Granularity;
import kala.concurrent.ConcurrentScope;
import kala.concurrent.LateInitCountDownLatch;
import kala.control.Option;
import kala.collection.factory.CollectionFactory;
import kala.function.CheckedBiFunction;
import kala.function.CheckedConsumer;
import kala.function.CheckedPredicate;
import kala.value.primitive.IntRef;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

@SuppressWarnings("unchecked")
public interface Traversable<@Covariant T> extends Iterable<T>, AnyTraversable<T> {

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> Traversable<T> narrow(Traversable<? extends T> traversable) {
        return (Traversable<T>) traversable;
    }

    //region Collection Operations

    @NotNull Iterator<T> iterator();

    default @NotNull Spliterator<T> spliterator() {
        final int ks = this.knownSize();
        if (ks == 0) {
            return Spliterators.emptySpliterator();
        } else if (ks > 0) {
            return Spliterators.spliterator(iterator(), ks, 0);
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), 0);
        }
    }

    /**
     * Returns a sequential {@link Stream} with this as its source.
     *
     * @return a sequential {@link Stream} over the elements in this {@code Traversable}
     */
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

    //endregion

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

    /**
     * {@inheritDoc}
     */
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

    /**
     * Tests whether all elements of this {@code Traversable} match the {@code predicate}.
     *
     * @return {@code true} if either all elements of this {@code Traversable} match the {@code predicate} or
     * the {@code Traversable} is empty, otherwise {@code false}
     */
    default boolean allMatch(@NotNull Predicate<? super T> predicate) {
        return Iterators.allMatch(iterator(), predicate);
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

    //endregion

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends Growable<? super T>> @NotNull G filterTo(@NotNull G destination, @NotNull Predicate<? super T> predicate) {
        for (T e : this) {
            if (predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <G extends Growable<? super T>> @NotNull G filterNotTo(@NotNull G destination, @NotNull Predicate<? super T> predicate) {
        for (T e : this) {
            if (!predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Contract(value = "_ -> param1", mutates = "param1")
    default <G extends Growable<? super T>> @NotNull G filterNotNullTo(@NotNull G destination) {
        for (T e : this) {
            if (e != null) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Contract(value = "_, _ -> param1", mutates = "param1")
    default <U, G extends Growable<? super U>> @NotNull G mapTo(@NotNull G destination, @NotNull Function<? super T, ? extends U> mapper) {
        for (T e : this) {
            destination.plusAssign(mapper.apply(e));
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

    //region Aggregate Operations

    default int count(@NotNull Predicate<? super T> predicate) {
        return Iterators.count(iterator(), predicate);
    }

    default T max() {
        return max(Comparators.naturalOrder());
    }

    default T max(Comparator<? super T> comparator) {
        if (isEmpty()) throw new NoSuchElementException();

        return Iterators.max(iterator(), comparator);
    }

    default @Nullable T maxOrNull() {
        return isNotEmpty() ? max() : null;
    }

    default @Nullable T maxOrNull(@NotNull Comparator<? super T> comparator) {
        return isNotEmpty() ? max(comparator) : null;
    }

    default @NotNull Option<T> maxOption() {
        return isNotEmpty() ? Option.some(max()) : Option.none();
    }

    default @NotNull Option<T> maxOption(Comparator<? super T> comparator) {
        return isNotEmpty() ? Option.some(max(comparator)) : Option.none();
    }

    default T min() {
        return min(Comparators.naturalOrder());
    }

    default T min(Comparator<? super T> comparator) {
        if (isEmpty()) throw new NoSuchElementException();

        return Iterators.min(iterator(), comparator);
    }

    default @Nullable T minOrNull() {
        return isNotEmpty() ? min() : null;
    }

    default @Nullable T minOrNull(@NotNull Comparator<? super T> comparator) {
        return isNotEmpty() ? min(comparator) : null;
    }

    default @NotNull Option<T> minOption() {
        return isNotEmpty() ? Option.some(min()) : Option.none();
    }

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
        return copyToArray(0, dest, 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(Object @NotNull [] dest, int destPos) {
        return copyToArray(0, dest, destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param1")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(Object @NotNull [] dest, int destPos, int limit) {
        return copyToArray(0, dest, destPos, limit);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, Object @NotNull [] dest) {
        return copyToArray(srcPos, dest, 0, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, Object @NotNull [] dest, int destPos) {
        return copyToArray(srcPos, dest, destPos, Integer.MAX_VALUE);
    }

    @Contract(mutates = "param2")
    @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
    default int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + srcPos + ") < 0");
        }
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

        final int kn = this.knownSize();
        if (kn >= 0 && srcPos >= kn) {
            return 0;
        }

        int end = Math.min(dl - destPos, limit) + destPos;

        int n = 0;
        Iterator<T> it = this.iterator();
        while (n++ < srcPos) {
            if (it.hasNext()) {
                it.next();
            } else {
                return 0;
            }
        }

        int idx = destPos;
        while (it.hasNext() && idx < end) {
            dest[idx++] = it.next();
        }
        return idx - destPos;
    }

    //endregion

    //region Conversion Operations

    default <R, Builder> R collect(@NotNull Collector<? super T, Builder, ? extends R> collector) {
        return Iterators.collect(iterator(), collector);
    }

    default <R, Builder> R collect(@NotNull CollectionFactory<? super T, Builder, ? extends R> factory) {
        return Iterators.collect(iterator(), factory);
    }

    @Override
    default Object @NotNull [] toArray() {
        return toArray(Object.class);
    }

    default <U /*super E*/> U @NotNull [] toArray(@NotNull Class<U> type) {
        int s = knownSize();
        if (s == 0) {
            return (U[]) Array.newInstance(type, 0);
        } else if (s > 0) {
            U[] arr = (U[]) Array.newInstance(type, s);
            this.copyToArray(arr);
            return arr;
        } else {
            return Iterators.toArray((Iterator<U>) iterator(), type);
        }
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
            Class<? extends Object[]> cls = array.getClass();
            res = (U[]) (cls == Object[].class
                    ? new Object[size]
                    : Array.newInstance(cls.getComponentType(), size));
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

    default <Ex extends Throwable> void forEachChecked(@NotNull CheckedConsumer<? super T, ? extends Ex> action) throws Ex {
        forEach(action);
    }

    default void forEachUnchecked(@NotNull CheckedConsumer<? super T, ?> action) {
        forEach(action);
    }

    @UnstableName
    default void forEachBreakable(@NotNull Predicate<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            if (!action.test(t)) {
                break;
            }
        }
    }

    @UnstableName
    default <Ex extends Throwable> void forEachBreakableChecked(
            @NotNull CheckedPredicate<? super T, ? extends Ex> action) throws Ex {
        forEachBreakable(action);
    }

    @UnstableName
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
            IntRef count = new IntRef();
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
