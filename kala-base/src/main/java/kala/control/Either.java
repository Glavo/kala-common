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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public sealed interface Either<@Covariant A, @Covariant B> extends Serializable permits Either.Left, Either.Right {
    @Contract(value = "_ -> param1", pure = true)
    static <A, B> Either<A, B> narrow(Either<? extends A, ? extends B> either) {
        return (Either<A, B>) either;
    }

    @Contract(value = "_ -> param1", pure = true)
    static <A, B> Either.Left<A, B> narrow(Either.Left<? extends A, ? extends B> left) {
        return (Either.Left<A, B>) left;
    }

    @Contract(value = "_ -> param1", pure = true)
    static <A, B> Either.Right<A, B> narrow(Either.Right<? extends A, ? extends B> right) {
        return (Either.Right<A, B>) right;
    }

    @Contract("_ -> new")
    static <A, B> Either.@NotNull Left<A, B> left(A value) {
        return new Left<>(value);
    }

    @Contract("_ -> new")
    static <A, B> Either.@NotNull Right<A, B> right(B value) {
        return new Right<>(value);
    }

    @ApiStatus.Experimental
    static <T> T join(@NotNull Either<? extends T, ? extends T> either) {
        if (either.isLeft()) {
            return either.getLeftValue();
        } else {
            return either.getRightValue();
        }
    }

    default boolean isLeft() {
        return this instanceof Either.Left;
    }

    default boolean isRight() {
        return this instanceof Either.Right;
    }

    default A getLeftValue() {
        if (this instanceof Either.Left(var value)) {
            return value;
        } else {
            throw new NoSuchElementException("Either.Right#getLeftValue");
        }
    }

    default @NotNull Option<A> getLeftOption() {
        return this instanceof Left(var value) ? Option.some(value) : Option.none();
    }

    default B getRightValue() {
        if (this instanceof Either.Right(var value)) {
            return value;
        } else {
            throw new NoSuchElementException("Either.Right#getLeftValue");
        }
    }

    default @NotNull Option<B> getRightOption() {
        return this instanceof Right(var value) ? Option.some(value) : Option.none();
    }

    default <U, V> @NotNull Either<U, V> map(
            @NotNull Function<? super A, ? extends U> leftMapper,
            @NotNull Function<? super B, ? extends V> rightMapper
    ) {
        return switch (this) {
            case Left(var value) -> Either.left(leftMapper.apply(value));
            case Right(var value) -> Either.right(rightMapper.apply(value));
        };
    }

    default <U> @NotNull Either<U, B> mapLeft(@NotNull Function<? super A, ? extends U> mapper) {
        return this instanceof Left(var value) ? Either.left(mapper.apply(value)) : (Either<U, B>) this;
    }

    default <U> @NotNull Either<A, U> mapRight(@NotNull Function<? super B, ? extends U> mapper) {
        return this instanceof Right(var value) ? Either.right(mapper.apply(value)) : (Either<A, U>) this;
    }

    default <U> U fold(
            @NotNull Function<? super A, ? extends U> leftMapper,
            @NotNull Function<? super B, ? extends U> rightMapper
    ) {
        return switch (this) {
            case Left(var value) -> leftMapper.apply(value);
            case Right(var value) -> rightMapper.apply(value);
        };
    }

    default <C, D, U> U bifold(
            @NotNull Either<? extends C, ? extends D> other,
            U defaultValue,
            @NotNull BiFunction<? super A, ? super C, ? extends U> leftMapper,
            @NotNull BiFunction<? super B, ? super D, ? extends U> rightMapper
    ) {
        if (this instanceof Either.Left(var value1) && other instanceof Either.Left(var value2)) {
            return leftMapper.apply(value1, value2);
        }

        if (this instanceof Either.Right(var value1) && other instanceof Either.Right(var value2)) {
            return rightMapper.apply(value1, value2);
        }

        return defaultValue;
    }

    @Contract("-> new")
    default @NotNull Either<B, A> swap() {
        return switch (this) {
            case Left(var value) -> Either.right(value);
            case Right(var value) -> Either.left(value);
        };
    }

    @Contract("-> new")
    default @NotNull Result<B, A> toResult() {
        return switch (this) {
            case Left(var value) -> Result.err(value);
            case Right(var value) -> Result.ok(value);
        };
    }

    @Contract(" -> new")
    default @NotNull LeftProjection<A, B> leftProjection() {
        return new LeftProjection<>(this);
    }

    @Contract(" -> new")
    default @NotNull RightProjection<A, B> rightProjection() {
        return new RightProjection<>(this);
    }

    default void forEach(
            @NotNull Consumer<? super A> leftConsumer,
            @NotNull Consumer<? super B> rightConsumer
    ) {
        switch (this) {
            case Left(var value) -> leftConsumer.accept(value);
            case Right(var value) -> rightConsumer.accept(value);
        }
    }

    record Left<@Covariant A, @Covariant B>(A value) implements Either<A, B> {
        @Override
        public String toString() {
            return "Either.Left[" + value + "]";
        }
    }

    record Right<@Covariant A, @Covariant B>(B value) implements Either<A, B> {
        @Override
        public String toString() {
            return "Either.Right[" + value + "]";
        }
    }

    sealed interface Projection<@Covariant T, A, B> extends OptionContainer<T> permits LeftProjection, RightProjection {
        @NotNull
        Either<A, B> getEither();
    }

    record LeftProjection<A, B>(Either<A, B> either) implements Projection<A, A, B> {
        @Override
        public @NotNull Either<A, B> getEither() {
            return either;
        }

        @Override
        public boolean isDefined() {
            return either.isLeft();
        }

        @Override
        public A get() {
            return either.getLeftValue();
        }

        @Override
        public <U> @NotNull LeftProjection<U, B> map(@NotNull Function<? super A, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            if (isDefined()) {
                return new LeftProjection<>(Either.left(mapper.apply(either.getLeftValue())));
            }
            return (LeftProjection<U, B>) this;
        }

        @NotNull
        @Override
        public String toString() {
            return either + ".LeftProjection";
        }
    }

    record RightProjection<A, B>(Either<A, B> either) implements Projection<B, A, B> {

        @Override
        public @NotNull Either<A, B> getEither() {
            return either;
        }

        @Override
        public boolean isDefined() {
            return either.isRight();
        }

        @Override
        public B get() {
            return either.getRightValue();
        }

        @Override
        public <U> @NotNull RightProjection<A, U> map(@NotNull Function<? super B, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            if (isDefined()) {
                return new RightProjection<>(Either.right(mapper.apply(either.getRightValue())));
            }
            return (RightProjection<A, U>) this;
        }

        @Override
        public @NotNull String toString() {
            return either + ".RightProjection";
        }
    }
}
