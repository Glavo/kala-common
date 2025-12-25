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

import kala.annotations.Covariant;
import kala.annotations.DelegateBy;
import kala.collection.base.Traversable;
import kala.function.CheckedConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public sealed interface Result<@Covariant T, @Covariant E> extends OptionContainer<T>, Serializable permits Result.Ok, Result.Err {
    @Contract(value = "_ -> param1", pure = true)
    static <T, E> Result<T, E> narrow(Result<? extends T, ? extends E> result) {
        return (Result<T, E>) result;
    }

    @Contract("_ -> new")
    static <T, E> Result<T, E> ok(T value) {
        return new Result.Ok<>(value);
    }

    @Contract("_ -> new")
    static <T, E> Result<T, E> err(E value) {
        return new Result.Err<>(value);
    }

    default boolean isOk() {
        return this instanceof Result.Ok;
    }

    default boolean isErr() {
        return this instanceof Result.Err;
    }

    @Override
    default boolean isDefined() {
        return isOk();
    }

    @Override
    default T get() {
        if (this instanceof Ok(var value)) {
            return value;
        } else {
            throw new NoSuchElementException("Result.Err#get()");
        }
    }

    default T getOrElse(Function<? super E, ? extends T> onErr) {
        return switch (this) {
            case Ok(var value) -> value;
            case Err(var err) -> onErr.apply(err);
        };
    }

    default E getErr() {
        if (this instanceof Err(var err)) {
            return err;
        } else {
            throw new NoSuchElementException("Result.Ok#getErr()");
        }
    }

    default @Nullable E getErrOrNull() {
        return this instanceof Err(var err) ? err : null;
    }

    default @NotNull Option<E> getErrOption() {
        return this instanceof Err(var err) ? Option.some(err) : Option.none();
    }

    @Override
    default <U> @NotNull Result<U, E> map(@NotNull Function<? super T, ? extends U> mapper) {
        return this instanceof Ok(var value) ? ok(mapper.apply(value)) : (Result<U, E>) this;
    }

    default <U> @NotNull Result<T, U> mapErr(@NotNull Function<? super E, ? extends U> mapper) {
        return this instanceof Err(var err) ? err(mapper.apply(err)) : (Result<T, U>) this;
    }

    default <U> @NotNull Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<? extends U, ? extends E>> mapper) {
        return this instanceof Ok(var value) ? narrow(mapper.apply(value)) : (Result<U, E>) this;
    }

    @Contract("-> new")
    default @NotNull Either<E, T> toEither() {
        return switch (this) {
            case Ok(var value) -> Either.right(value);
            case Err(var err) -> Either.left(err);
        };
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<T>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Result<T, E> onEach(@NotNull Consumer<? super T> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull Result<T, E> onEachChecked(@NotNull CheckedConsumer<? super T, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull Result<T, E> onEachUnchecked(@NotNull CheckedConsumer<? super T, ?> action) {
        return onEach(action);
    }

    record Ok<T, E>(T value) implements Result<T, E> {

        public <U> Ok<T, U> cast() {
            return (Ok<T, U>) this;
        }

        @Override
        public @NotNull String toString() {
            return "Result.Ok[" + get() + "]";
        }
    }

    record Err<T, E>(E err) implements Result<T, E> {

        public <U> Err<U, E> cast() {
            return (Err<U, E>) this;
        }

        @Override
        public @NotNull String toString() {
            return "Result.Err[" + getErr() + "]";
        }
    }
}
