package org.glavo.kala.control;

import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.base.Mappable;
import org.glavo.kala.collection.base.Traversable;
import org.glavo.kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * A container object which may or may not contain a value.
 *
 * @param <T> the type of value
 * @author Glavo
 */
interface OptionContainer<@Covariant T> extends Iterable<T>, Mappable<T>, Traversable<T> {


    /**
     * Returns {@code true} if the container contain a value, otherwise return {@code false}.
     *
     * @return {@code true} if the container contain a value, otherwise {@code false}
     */
    boolean isDefined();

    /**
     * Returns {@code true} if the container is empty, otherwise return {@code false}.
     *
     * @return {@code true} if the container is empty, otherwise {@code false}
     */
    default boolean isEmpty() {
        return !isDefined();
    }

    default @NotNull Stream<T> stream() {
        return isDefined() ? Stream.of(get()) : Stream.empty();
    }

    default @NotNull Stream<T> parallelStream() {
        return stream().parallel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default @NotNull Iterator<T> iterator() {
        return isDefined() ? Iterators.of(get()) : Iterators.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default int size() {
        return isDefined() ? 1 : 0;
    }

    /**
     * Returns the value of the container.
     *
     * @return the value of the container
     * @throws NoSuchElementException if the container is empty
     */
    T get();

    /**
     * Returns the value of the container if it is not empty, otherwise return the {@code defaultValue}.
     *
     * @param defaultValue the default value
     * @return the value of the container if the container {@link #isDefined()},
     * or the {@code defaultValue} if the container {@link #isEmpty()}
     */
    default T getOrDefault(T defaultValue) {
        return isDefined() ? get() : defaultValue;
    }

    default T getOrElse(@NotNull Supplier<? extends T> supplier) {
        return isDefined() ? get() : supplier.get();
    }

    /**
     * Returns the value of the container if it is not empty, otherwise return the {@code null}.
     *
     * @return the value of the container if the container {@link #isDefined()},
     * or the {@code null} if the container {@link #isEmpty()}
     */
    default @Nullable T getOrNull() {
        return isDefined() ? get() : null;
    }

    /**
     * Returns the value of the container if it is not empty, otherwise throw the {@code exception}.
     *
     * @return the value of the container if the container {@link #isDefined()}
     * @throws E if no value is present
     */
    default <E extends Throwable> T getOrThrowException(@NotNull E exception) throws E {
        if (isEmpty()) {
            Objects.requireNonNull(exception);
            throw exception;
        }
        return get();
    }

    /**
     * Returns the value of the container if it is not empty, otherwise throw {@code supplier.get()}.
     *
     * @return the value of the container if the container {@link #isDefined()}
     * @throws E if no value is present
     */
    default <E extends Throwable> T getOrThrow(@NotNull Supplier<? extends E> supplier) throws E {
        if (isEmpty()) {
            Objects.requireNonNull(supplier);
            throw supplier.get();
        }
        return get();
    }

    /**
     * Returns {@code Option.some(get())} if it is not empty, otherwise return the {@code Option.none()}.
     *
     * @return {@code Option.some(get())} if the container {@link #isDefined()},
     * or the {@code Option.none()} if the container {@link #isEmpty()}
     */
    default @NotNull Option<T> getOption() {
        return isDefined() ? Option.some(get()) : Option.none();
    }

    /**
     * Returns {@code Option.some(get())} if it is not empty, otherwise return the {@code Option.none()}.
     *
     * @return {@code Option.some(get())} if the container {@link #isDefined()},
     * or the {@code Option.none()} if the container {@link #isEmpty()}
     */
    default @NotNull Option<T> toOption() {
        return getOption();
    }

    @SuppressWarnings("unchecked")
    default <R, Builder> R collect(@NotNull Collector<? super T, Builder, ? extends R> factory) {
        final Collector<T, Object, R> f = ((Collector<T, Object, R>) factory);
        Object builder = f.supplier().get();
        if (isDefined()) {
            f.accumulator().accept(builder, get());
        }
        return f.finisher().apply(builder);
    }

    @Override
    <U> @NotNull OptionContainer<U> map(@NotNull Function<? super T, ? extends U> mapper);

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    default int count(@NotNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return anyMatch(predicate) ? 1 : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super T, ? extends U> op) {
        if (isEmpty()) {
            return zero;
        }
        return op.apply(zero, get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    default <U> U foldRight(U zero, @NotNull BiFunction<? super T, ? super U, ? extends U> op) {
        if (isEmpty()) {
            return zero;
        }
        return op.apply(get(), zero);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default T reduceLeft(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default T reduceRight(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    default @NotNull Option<T> reduceLeftOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return getOption();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    default @NotNull Option<T> reduceRightOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return getOption();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    default boolean anyMatch(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    default boolean allMatch(@NotNull Predicate<? super T> predicate) {
        return isEmpty() || predicate.test(get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    default boolean noneMatch(@NotNull Predicate<? super T> predicate) {
        return isEmpty() || !predicate.test(get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean contains(Object value) {
        return isDefined() && Objects.equals(value, get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    default Option<T> find(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(get()) // implicit null check of predicate
                ? Option.some(get())
                : Option.none();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void forEach(@NotNull Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (isDefined()) {
            action.accept(get());
        }
    }
}
