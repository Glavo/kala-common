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
package kala.control;

import kala.collection.base.Iterators;
import kala.collection.base.Traversable;
import kala.collection.base.Mappable;
import kala.annotations.Covariant;
import org.jetbrains.annotations.ApiStatus;
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
public interface OptionContainer<@Covariant T> extends Iterable<T>, Mappable<T>, Traversable<T> {

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
    @ApiStatus.NonExtendable
    default boolean isEmpty() {
        return !isDefined();
    }

    default @NotNull Stream<T> stream() {
        return isDefined() ? Stream.of(get()) : Stream.empty();
    }

    default @NotNull Stream<T> parallelStream() {
        return stream().parallel();
    }

    @Override
    default @NotNull Iterator<T> iterator() {
        return isDefined() ? Iterators.of(get()) : Iterators.empty();
    }

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
     * Returns the value of the container if it is not empty, otherwise return the {@code null}.
     *
     * @return the value of the container if the container {@link #isDefined()},
     * or the {@code null} if the container {@link #isEmpty()}
     */
    default @Nullable T getOrNull() {
        return isDefined() ? get() : null;
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
     * Returns the value of the container if it is not empty, otherwise throw the {@code exception}.
     *
     * @return the value of the container if the container {@link #isDefined()}
     * @throws Ex if no value is present
     */
    default <Ex extends Throwable> T getOrThrowException(@NotNull Ex exception) throws Ex {
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
     * @throws Ex if no value is present
     */
    default <Ex extends Throwable> T getOrThrow(@NotNull Supplier<? extends Ex> supplier) throws Ex {
        if (isEmpty()) {
            Objects.requireNonNull(supplier);
            throw supplier.get();
        }
        return get();
    }

    default <U> U get(@NotNull Function<? super T, ? extends U> mapper) {
        return mapper.apply(get());
    }

    default <U> @Nullable U getOrNull(@NotNull Function<? super T, ? extends U> mapper) {
        return isDefined() ? mapper.apply(get()) : null;
    }

    default <U> @NotNull Option<U> getOption(@NotNull Function<? super T, ? extends U> mapper) {
        return isDefined() ? Option.some(mapper.apply(get())) : Option.none();
    }

    default <U> U getOrDefault(@NotNull Function<? super T, ? extends U> mapper, U defaultValue) {
        return isDefined() ? mapper.apply(get()) : defaultValue;
    }

    default <U> U getOrElse(@NotNull Function<? super T, ? extends U> mapper, @NotNull Supplier<? extends U> supplier) {
        return isDefined() ? mapper.apply(get()) : supplier.get();
    }

    default <U, Ex extends Throwable> U getOrThrowException(@NotNull Function<? super T, ? extends U> mapper, @NotNull Ex exception) throws Ex {
        if (isEmpty()) {
            Objects.requireNonNull(exception);
            throw exception;
        }
        return mapper.apply(get());
    }

    default <U, Ex extends Throwable> U getOrThrow(@NotNull Function<? super T, ? extends U> mapper, @NotNull Supplier<? extends Ex> supplier) throws Ex {
        if (isEmpty()) {
            Objects.requireNonNull(supplier);
            throw supplier.get();
        }
        return mapper.apply(get());
    }

    default void ifDefined(@NotNull Consumer<? super T> action) {
        if (isDefined()) {
            action.accept(get());
        }
    }

    default void ifDefinedOrElse(@NotNull Consumer<? super T> action, Runnable emptyAction) {
        if (isDefined()) {
            action.accept(get());
        } else {
            emptyAction.run();
        }
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

    @Override
    @Contract(pure = true)
    default int count(@NotNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return anyMatch(predicate) ? 1 : 0;
    }

    @Override
    default <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super T, ? extends U> op) {
        if (isEmpty()) {
            return zero;
        }
        return op.apply(zero, get());
    }

    @Override
    @Contract(pure = true)
    default <U> U foldRight(U zero, @NotNull BiFunction<? super T, ? super U, ? extends U> op) {
        if (isEmpty()) {
            return zero;
        }
        return op.apply(get(), zero);
    }

    @Override
    default T reduceLeft(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return get();
    }

    @Override
    default T reduceRight(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return get();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Option<T> reduceLeftOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return getOption();
    }

    @Override
    @Contract(pure = true)
    default @NotNull Option<T> reduceRightOption(@NotNull BiFunction<? super T, ? super T, ? extends T> op) {
        return getOption();
    }

    @Override
    @Contract(pure = true)
    default boolean anyMatch(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(get());
    }

    @Override
    @Contract(pure = true)
    default boolean allMatch(@NotNull Predicate<? super T> predicate) {
        return isEmpty() || predicate.test(get());
    }

    @Override
    @Contract(pure = true)
    default boolean noneMatch(@NotNull Predicate<? super T> predicate) {
        return isEmpty() || !predicate.test(get());
    }

    @Override
    default boolean contains(Object value) {
        return isDefined() && Objects.equals(value, get());
    }

    @Override
    default @NotNull Option<T> find(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(get()) // implicit null check of predicate
                ? Option.some(get())
                : Option.none();
    }

    @Override
    default void forEach(@NotNull Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (isDefined()) {
            action.accept(get());
        }
    }
}
