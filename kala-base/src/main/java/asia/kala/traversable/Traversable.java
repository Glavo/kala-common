package asia.kala.traversable;

import asia.kala.annotations.Covariant;
import asia.kala.control.Option;
import asia.kala.factory.CollectionFactory;
import asia.kala.function.CheckedConsumer;
import asia.kala.iterator.Iterators;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("unchecked")
public interface Traversable<@Covariant T> extends AnyTraversable<T, Iterator<T>, Object[], Option<T>, Consumer<? super T>, Predicate<? super T>> {

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    static <T> Traversable<T> narrow(Traversable<? extends T> traversable) {
        return (Traversable<T>) traversable;
    }

    @NotNull
    Iterator<T> iterator();

    @NotNull
    default Spliterator<T> spliterator() {
        final int ks = this.knownSize();
        if (ks == 0) {
            return Spliterators.emptySpliterator();
        } else if (ks > 0) {
            return Spliterators.spliterator(iterator(), ks, 0);
        } else {
            return Spliterators.spliteratorUnknownSize(iterator(), 0);
        }
    }

    @NotNull
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @NotNull
    default Stream<T> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

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

    @Override
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
        return allMatch(predicate.negate());
    }

    default int count(@NotNull Predicate<? super T> predicate) {
        return Iterators.count(iterator(), predicate);
    }

    @NotNull
    default Option<T> find(@NotNull Predicate<? super T> predicate) {
        for (T t : this) {
            if (predicate.test(t)) {
                return Option.some(t);
            }
        }
        return Option.none();
    }

    default T max() {
        return max((Comparator<T>) Comparator.naturalOrder());
    }

    default T max(@NotNull Comparator<? super T> comparator) {
        return maxOption(comparator).getOrThrow(NoSuchElementException::new);
    }

    @Nullable
    default T maxOrNull() {
        return maxOrNull((Comparator<T>) Comparator.naturalOrder());
    }

    @Nullable
    default T maxOrNull(@NotNull Comparator<? super T> comparator) {
        return maxOption(comparator).getOrNull();
    }

    @NotNull
    default Option<T> maxOption() {
        return maxOption((Comparator<T>) Comparator.naturalOrder());
    }

    @NotNull
    default Option<T> maxOption(@NotNull Comparator<? super T> comparator) {
        return Iterators.maxOption(iterator(), comparator);
    }

    default T min() {
        return min((Comparator<T>) Comparator.naturalOrder());
    }

    default T min(@NotNull Comparator<? super T> comparator) {
        return minOption(comparator).getOrThrow(NoSuchElementException::new);
    }

    @Nullable
    default T minOrNull() {
        return minOrNull((Comparator<T>) Comparator.naturalOrder());
    }

    @Nullable
    default T minOrNull(@NotNull Comparator<? super T> comparator) {
        return minOption(comparator).getOrNull();
    }

    @NotNull
    default Option<T> minOption() {
        return minOption((Comparator<T>) Comparator.naturalOrder());
    }

    @NotNull
    default Option<T> minOption(@NotNull Comparator<? super T> comparator) {
        return Iterators.minOption(iterator(), comparator);
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

    /**
     * Reduces this elements by apply {@code op}.
     *
     * @param op the binary operator
     * @return the reduced value
     * @throws NoSuchElementException if this {@code Traversable} is empty
     */
    default T reduce(@NotNull BiFunction<? super T, ? super T, ? extends T> op) throws NoSuchElementException {
        Objects.requireNonNull(op);
        return reduceLeft(op);
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
    default T reduceLeft(@NotNull BiFunction<? super T, ? super T, ? extends T> op) throws NoSuchElementException {
        Option<T> opt = reduceLeftOption(op);
        if (opt.isDefined()) {
            return opt.get();
        }
        throw new NoSuchElementException("Traversable.reduceLeft");
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
        Option<T> opt = reduceRightOption(op);
        if (opt.isDefined()) {
            return opt.get();
        }
        throw new NoSuchElementException("Traversable.reduceRight");
    }

    /**
     * Reduces this elements by apply {@code op}.
     *
     * @param op the binary operator
     * @return an {@code Option} contain the reduced value or a empty {@code Option} if the {@code Traversable} is empty
     */
    @NotNull
    default Option<T> reduceOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op);
        return reduceLeftOption(op);
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
    @NotNull
    default Option<T> reduceLeftOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return Iterators.reduceLeftOption(iterator(), op);
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
    @NotNull
    default Option<T> reduceRightOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return Iterators.reduceRightOption(iterator(), op);
    }

    default <R, Builder> R collect(@NotNull Collector<? super T, Builder, ? extends R> collector) {
        return Iterators.collect(iterator(), collector);
    }

    default <R, Builder> R collect(@NotNull CollectionFactory<? super T, Builder, ? extends R> factory) {
        return Iterators.collect(iterator(), factory);
    }

    @Override
    default Object @NotNull [] toArray() {
        return toArray(Object[]::new);
    }

    /**
     * @see Collection#toArray(Object[])
     */
    default <U /*super E*/> U[] toArray(@NotNull U[] array) {
        final int size = this.size();
        final int arrayLength = array.length;
        U[] res = arrayLength > size
                ? array
                : (U[]) Array.newInstance(array.getClass().getComponentType(), size);

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

    @NotNull
    default <U /*super E*/> U[] toArray(@NotNull Class<U> type) {
        return toArray(JavaArray.generator(type));
    }

    @NotNull
    default <U /*super E*/> U[] toArray(@NotNull IntFunction<U[]> generator) {
        int s = knownSize();
        if (s == 0) {
            return generator.apply(0);
        } else if (s > 0) {
            U[] arr = generator.apply(s);
            int i = 0;
            for (T t : this) {
                arr[i++] = (U) t;
            }
            return arr;
        } else {
            return Iterators.toArray((Iterator<U>) iterator(), generator);
        }
    }

    @NotNull
    @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
    default <A extends Appendable> A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        return Iterators.joinTo(iterator(), buffer, separator, prefix, postfix);
    }

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
}
