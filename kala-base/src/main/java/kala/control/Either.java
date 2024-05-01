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
import kala.annotations.UnstableName;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract sealed class Either<@Covariant A, @Covariant B> implements Serializable {
    Either() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <A, B> Either<A, B> narrow(Either<? extends A, ? extends B> either) {
        return (Either<A, B>) either;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <A, B> Either.Left<A, B> narrow(Either.Left<? extends A, ? extends B> left) {
        return (Either.Left<A, B>) left;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <A, B> Either.Right<A, B> narrow(Either.Right<? extends A, ? extends B> right) {
        return (Either.Right<A, B>) right;
    }

    @Contract("_ -> new")
    public static <A, B> Either.@NotNull Left<A, B> left(A value) {
        return new Left<>(value);
    }

    @Contract("_ -> new")
    public static <A, B> Either.@NotNull Right<A, B> right(B value) {
        return new Right<>(value);
    }

    @UnstableName
    public static <T> T join(@NotNull Either<? extends T, ? extends T> either) {
        if (either.isLeft()) {
            return either.getLeftValue();
        } else {
            return either.getRightValue();
        }
    }

    public abstract boolean isLeft();

    public abstract boolean isRight();

    public abstract A getLeftValue();

    public abstract @NotNull Option<A> getLeftOption();

    public abstract B getRightValue();

    public abstract @NotNull Option<B> getRightOption();

    public abstract <U, V> @NotNull Either<U, V> map(
            @NotNull Function<? super A, ? extends U> leftMapper,
            @NotNull Function<? super B, ? extends V> rightMapper
    );

    public abstract <U> @NotNull Either<U, B> mapLeft(@NotNull Function<? super A, ? extends U> mapper);

    public abstract <U> @NotNull Either<A, U> mapRight(@NotNull Function<? super B, ? extends U> mapper);

    public abstract <U> U fold(
            @NotNull Function<? super A, ? extends U> leftMapper,
            @NotNull Function<? super B, ? extends U> rightMapper
    );

    public abstract <C, D, U> U bifold(
            @NotNull Either<? extends C, ? extends D> other,
            U defaultValue,
            @NotNull BiFunction<? super A, ? super C, ? extends U> leftMapper,
            @NotNull BiFunction<? super B, ? super D, ? extends U> rightMapper
    );

    @Contract("-> new")
    public abstract @NotNull Either<B, A> swap();

    @Contract("-> new")
    public abstract @NotNull Result<B, A> toResult();

    @Contract(" -> new")
    public final @NotNull LeftProjection leftProjection() {
        return this.new LeftProjection();
    }

    @Contract(" -> new")
    public final @NotNull RightProjection rightProjection() {
        return this.new RightProjection();
    }

    public abstract void forEach(
            @NotNull Consumer<? super A> leftConsumer,
            @NotNull Consumer<? super B> rightConsumer
    );

    public final static class Left<@Covariant A, @Covariant B> extends Either<A, B> {
        @Serial
        private static final long serialVersionUID = -1160729620210301179L;

        private static final int HASH_MAGIC = -1951578063;

        private final A value;

        Left(A value) {
            this.value = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isLeft() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isRight() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public A getLeftValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Option<A> getLeftOption() {
            return Option.some(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("-> fail")
        public B getRightValue() {
            throw new NoSuchElementException("Either.Left.getRightValue");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Option<B> getRightOption() {
            return Option.none();
        }

        @Override
        public <U, V> @NotNull Either<U, V> map(
                @NotNull Function<? super A, ? extends U> leftMapper,
                @NotNull Function<? super B, ? extends V> rightMapper) {
            return Either.left(leftMapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <U> @NotNull Either<U, B> mapLeft(@NotNull Function<? super A, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            return Either.left(mapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <U> @NotNull Either<A, U> mapRight(@NotNull Function<? super B, ? extends U> mapper) {
            return (Either<A, U>) this;
        }

        @Override
        public <U> U fold(
                @NotNull Function<? super A, ? extends U> leftMapper,
                @NotNull Function<? super B, ? extends U> rightMapper) {
            return leftMapper.apply(value);
        }

        @Override
        public <C, D, U> U bifold(
                @NotNull Either<? extends C, ? extends D> other,
                U defaultValue,
                @NotNull BiFunction<? super A, ? super C, ? extends U> leftMapper,
                @NotNull BiFunction<? super B, ? super D, ? extends U> rightMapper) {

            if (other instanceof Left) {
                return leftMapper.apply(this.value, ((Left<? extends C, ? extends D>) other).value);
            } else {
                return defaultValue;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Either<B, A> swap() {
            return Either.right(value);
        }

        @Override
        public @NotNull Result<B, A> toResult() {
            return Result.err(value);
        }

        @Override
        public void forEach(@NotNull Consumer<? super A> leftConsumer, @NotNull Consumer<? super B> rightConsumer) {
            leftConsumer.accept(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Left<?, ?>)) {
                return false;
            }

            return Objects.equals(value, ((Left<?, ?>) obj).value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(value) + HASH_MAGIC;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Either.Left[" + value + "]";
        }
    }

    public final static class Right<@Covariant A, @Covariant B> extends Either<A, B> {
        @Serial
        private static final long serialVersionUID = -3372589401685464421L;

        private static final int HASH_MAGIC = 1973604283;

        private final B value;

        Right(B value) {
            this.value = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isLeft() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isRight() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("-> fail")
        public A getLeftValue() {
            throw new NoSuchElementException("Either.Right.getLeftValue");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Option<A> getLeftOption() {
            return Option.none();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public B getRightValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Option<B> getRightOption() {
            return Option.some(value);
        }

        @Override
        public <U, V> @NotNull Either<U, V> map(
                @NotNull Function<? super A, ? extends U> leftMapper,
                @NotNull Function<? super B, ? extends V> rightMapper
        ) {
            return Either.right(rightMapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <U> @NotNull Either<U, B> mapLeft(@NotNull Function<? super A, ? extends U> mapper) {
            return (Either<U, B>) this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <U> @NotNull Either<A, U> mapRight(@NotNull Function<? super B, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            return Either.right(mapper.apply(value));
        }

        @Override
        public <U> U fold(
                @NotNull Function<? super A, ? extends U> leftMapper,
                @NotNull Function<? super B, ? extends U> rightMapper) {
            return rightMapper.apply(value);
        }

        @Override
        public <C, D, U> U bifold(
                @NotNull Either<? extends C, ? extends D> other,
                U defaultValue,
                @NotNull BiFunction<? super A, ? super C, ? extends U> leftMapper,
                @NotNull BiFunction<? super B, ? super D, ? extends U> rightMapper) {

            if (other instanceof Right) {
                return rightMapper.apply(this.value, ((Right<? extends C, ? extends D>) other).value);
            } else {
                return defaultValue;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Either<B, A> swap() {
            return Either.left(value);
        }

        @Override
        public @NotNull Result<B, A> toResult() {
            return Result.ok(value);
        }

        @Override
        public void forEach(@NotNull Consumer<? super A> leftConsumer, @NotNull Consumer<? super B> rightConsumer) {
            rightConsumer.accept(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Right<?, ?>)) {
                return false;
            }

            return Objects.equals(value, ((Right<?, ?>) obj).value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(value) + HASH_MAGIC;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Either.Right[" + value + "]";
        }
    }

    public abstract sealed class Projection<@Covariant T> implements OptionContainer<T> {
        Projection() {
        }

        private static final int HASH_MAGIC = 905770825;

        public final @NotNull Either<A, B> getEither() {
            return Either.this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            return Either.this.equals(((Projection<?>) o).getEither());
        }

        @Override
        public final int hashCode() {
            return Either.this.hashCode() + HASH_MAGIC;
        }
    }

    public final class LeftProjection extends Projection<A> {
        @Override
        public boolean isDefined() {
            return Either.this.isLeft();
        }

        @Override
        public A get() {
            return Either.this.getLeftValue();
        }

        @Override
        @NotNull
        public <U> Either<U, B>.LeftProjection map(@NotNull Function<? super A, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            if (isDefined()) {
                return Either.<U, B>left(mapper.apply(getLeftValue())).new LeftProjection();
            }
            return (Either<U, B>.LeftProjection) this;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public String toString() {
            return Either.this + ".LeftProjection";
        }
    }

    public final class RightProjection extends Projection<B> {
        @Override
        public boolean isDefined() {
            return Either.this.isRight();
        }

        @Override
        public B get() {
            return Either.this.getRightValue();
        }

        @Override
        @NotNull
        public <U> Either<A, U>.RightProjection map(@NotNull Function<? super B, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            if (isDefined()) {
                return Either.<A, U>right(mapper.apply(getRightValue())).new RightProjection();
            }
            return (Either<A, U>.RightProjection) this;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public String toString() {
            return Either.this + ".RightProjection";
        }
    }
}
