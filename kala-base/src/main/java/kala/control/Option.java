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

import kala.annotations.DelegateBy;
import kala.collection.base.Iterators;
import kala.control.primitive.PrimitiveOption;
import kala.function.CheckedConsumer;
import kala.annotations.Covariant;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A container object which may or may not contain a value.
 *
 * <p>{@code Option} is not the same as {@link Optional}.
 * {@code Option} is a container that supports collection operations, supports serialization
 * and distinguishes between a {@code Option} instance that contains null as the value and the empty Option.
 * {@link Optional} does not support serialization and cannot store null as its value.
 * Use the {@code Option} in situations where {@link Optional} is not suitable.
 *
 * @param <T> the type of value
 * @author Glavo
 * @see Optional
 */
public final class Option<@Covariant T> implements AnyOption<T>, OptionContainer<T>, Serializable {
    @Serial
    private static final long serialVersionUID = 4055633765420871779L;

    /**
     * The single instance of empty {@code Option}.
     */
    public static final Option<?> None = new Option<>(null);

    private final T value;

    private Option(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    public static <T> Option<T> narrow(Option<? extends T> option) {
        return (Option<T>) option;
    }

    /**
     * Returns a new {@code Option} contain the {@code value}.
     *
     * @param value the value
     * @param <T>   the type of the value
     * @return a new {@code Option} contain the {@code value}
     */
    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Option<T> some(T value) {
        return new Option<>(value);
    }

    /**
     * Returns the single instance of empty {@code Option}.
     *
     * @param <T> the type of value
     * @return the single instance of empty {@code Option}.
     * @see Option#None
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull Option<T> none() {
        return (Option<T>) None;
    }

    /**
     * Returns {@code Option.some(value)} if value is not null, otherwise returns {@code Option.none()}.
     *
     * @param value the value
     * @param <T>   the type of the value
     * @return {@code Option.some(value)} if value is not null, otherwise {@code Option.none()}
     */
    public static <T> @NotNull Option<T> ofNullable(@Nullable T value) {
        return value != null ? new Option<>(value) : none();
    }

    /**
     * Convert {@link Optional} to {@code Option}.
     *
     * @param optional a {@link Optional} value
     * @param <T>      the type of the value
     * @return {@code Option.some(optional.get())} if {@code optional} is present, otherwise {@code Option.none()}
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> @NotNull Option<T> fromJava(@NotNull Optional<? extends T> optional) {
        return ofNullable(optional.orElse(null));
    }

    /**
     * Returns {@code true} if the {@code Option} contain a value, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Option} contain a value, otherwise {@code false}
     */
    public boolean isDefined() {
        return this != None;
    }

    /**
     * Returns the {@code Option}'s value.
     *
     * @return the {@code Option}'s value
     * @throws NoSuchElementException if the {@code Option} is empty
     */
    @Flow(sourceIsContainer = true)
    public T get() {
        if (isEmpty()) {
            throw new NoSuchElementException("Option.None");
        }
        return value;
    }

    @Override
    @Contract(value = "-> this", pure = true)
    public @NotNull Option<T> getOption() {
        return this;
    }

    @Override
    @Contract(value = "-> this", pure = true)
    public @NotNull Option<T> toOption() {
        return this;
    }

    public @NotNull Option<T> orElse(Option<? extends T> other) {
        return this.isDefined() ? this : narrow(other);
    }

    public @NotNull Option<T> orElse(@NotNull Supplier<? extends @NotNull Option<? extends T>> other) {
        return this.isDefined() ? this : narrow(other.get());
    }

    @Override
    public <U> @NotNull Option<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        return isDefined() ? some(mapper.apply(value)) : none();
    }

    public <U> @NotNull Option<@NotNull U> mapNotNull(@NotNull Function<? super T, ? extends @Nullable U> mapper) {
        return isDefined() ? ofNullable(mapper.apply(value)) : none();
    }

    public <U> @NotNull Option<U> flatMap(@NotNull Function<? super T, ? extends Option<? extends U>> mapper) {
        return isDefined() ? narrow(mapper.apply(value)) : none();
    }

    public @NotNull Option<T> filter(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(value) ? this : none();
    }

    public @NotNull Option<T> filterNot(@NotNull Predicate<? super T> predicate) {
        return isDefined() && !predicate.test(value) ? this : none();
    }

    public @NotNull Option<@NotNull T> filterNotNull() {
        return value == null ? none() : this;
    }

    public <U> @NotNull Result<T, U> toResult(U errValue) {
        return this.isDefined() ? Result.ok(value) : Result.err(errValue);
    }

    @Override
    @Contract(pure = true)
    public boolean contains(Object value) {
        return isDefined() && Objects.equals(this.value, value);
    }

    @Override
    public @NotNull Option<T> find(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(value) ? this : none();
    }

    /**
     * Convert this {@code Option} to {@link Optional}.
     *
     * @return {@code Optional.of(get())} if the {@code Option} contain a value, otherwise {@link Optional#empty()}
     * @throws NullPointerException if {@link #isDefined()} but value is {@code null}
     */
    public @NotNull Optional<T> asJava() {
        return isDefined() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return isDefined() ? Iterators.of(value) : Iterators.empty();
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<T>)")
    @Contract(value = "_ -> this", pure = true)
    public @NotNull Option<T> onEach(@NotNull Consumer<? super T> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    public <Ex extends Throwable> @NotNull Option<T> onEachChecked(@NotNull CheckedConsumer<? super T, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    public @NotNull Option<T> onEachUnchecked(@NotNull CheckedConsumer<? super T, ?> action) {
        return onEach(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Option<?> other) {
            return Objects.equals(value, other.value)
                    && this != None
                    && o != None;
        }
        if (o instanceof PrimitiveOption) {
            return o.equals(this);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return isDefined() ? Objects.hashCode(value) + HASH_MAGIC : NONE_HASH;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        if (this == None) {
            return "Option.None";
        } else {
            return "Option[" + value + "]";
        }
    }

    @Serial
    private Object writeReplace() {
        return this == None ? NoneReplaced.INSTANCE : this;
    }

    static final class NoneReplaced implements Serializable {
        @Serial
        private static final long serialVersionUID = 0L;

        static final NoneReplaced INSTANCE = new NoneReplaced();

        private NoneReplaced() {
        }

        @Serial
        private Object readResolve() {
            return Option.None;
        }
    }
}

