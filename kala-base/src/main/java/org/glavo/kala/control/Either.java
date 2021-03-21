package org.glavo.kala.control;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.annotations.Sealed;
import org.glavo.kala.annotations.UnstableName;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Sealed(subclasses = {Either.Left.class, Either.Right.class})
@SuppressWarnings("unchecked")
public abstract class Either<@Covariant A, @Covariant B> implements Serializable {
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
        public final boolean isLeft() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean isRight() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final A getLeftValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Option<A> getLeftOption() {
            return Option.some(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("-> fail")
        public final B getRightValue() {
            throw new NoSuchElementException("Either.Left.getRightValue");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Option<B> getRightOption() {
            return Option.none();
        }

        @Override
        public final <U, V> @NotNull Either<U, V> map(
                @NotNull Function<? super A, ? extends U> leftMapper,
                @NotNull Function<? super B, ? extends V> rightMapper) {
            return Either.left(leftMapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final <U> @NotNull Either<U, B> mapLeft(@NotNull Function<? super A, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            return Either.left(mapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final <U> @NotNull Either<A, U> mapRight(@NotNull Function<? super B, ? extends U> mapper) {
            return (Either<A, U>) this;
        }

        @Override
        public final <U> U fold(
                @NotNull Function<? super A, ? extends U> leftMapper,
                @NotNull Function<? super B, ? extends U> rightMapper) {
            return leftMapper.apply(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Either<B, A> swap() {
            return Either.right(value);
        }

        @Override
        public final @NotNull Result<B, A> toResult() {
            return Result.err(value);
        }

        @Override
        public final void forEach(@NotNull Consumer<? super A> leftConsumer, @NotNull Consumer<? super B> rightConsumer) {
            leftConsumer.accept(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean equals(Object obj) {
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
        public final int hashCode() {
            return Objects.hashCode(value) + HASH_MAGIC;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final String toString() {
            return "Either.Left[" + value + "]";
        }
    }

    public final static class Right<@Covariant A, @Covariant B> extends Either<A, B> {
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
        public final boolean isLeft() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean isRight() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("-> fail")
        public final A getLeftValue() {
            throw new NoSuchElementException("Either.Right.getLeftValue");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Option<A> getLeftOption() {
            return Option.none();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final B getRightValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Option<B> getRightOption() {
            return Option.none();
        }

        @Override
        public final <U, V> @NotNull Either<U, V> map(
                @NotNull Function<? super A, ? extends U> leftMapper,
                @NotNull Function<? super B, ? extends V> rightMapper
        ) {
            return Either.right(rightMapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final <U> @NotNull Either<U, B> mapLeft(@NotNull Function<? super A, ? extends U> mapper) {
            return (Either<U, B>) this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final <U> @NotNull Either<A, U> mapRight(@NotNull Function<? super B, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
            return Either.right(mapper.apply(value));
        }

        @Override
        public final <U> U fold(
                @NotNull Function<? super A, ? extends U> leftMapper,
                @NotNull Function<? super B, ? extends U> rightMapper) {
            return rightMapper.apply(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Either<B, A> swap() {
            return Either.left(value);
        }

        @Override
        public final @NotNull Result<B, A> toResult() {
            return Result.ok(value);
        }

        @Override
        public final void forEach(@NotNull Consumer<? super A> leftConsumer, @NotNull Consumer<? super B> rightConsumer) {
            rightConsumer.accept(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean equals(Object obj) {
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
        public final int hashCode() {
            return Objects.hashCode(value) + HASH_MAGIC;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final String toString() {
            return "Either.Right[" + value + "]";
        }
    }

    public abstract class Projection<@Covariant T> implements OptionContainer<T> {
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
        public final A get() {
            return Either.this.getLeftValue();
        }

        @Override
        @NotNull
        public final <U> Either<U, B>.LeftProjection map(@NotNull Function<? super A, ? extends U> mapper) {
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
        public final String toString() {
            return Either.this + ".LeftProjection";
        }
    }

    public final class RightProjection extends Projection<B> {
        @Override
        public boolean isDefined() {
            return Either.this.isRight();
        }

        @Override
        public final B get() {
            return Either.this.getRightValue();
        }

        @Override
        @NotNull
        public final <U> Either<A, U>.RightProjection map(@NotNull Function<? super B, ? extends U> mapper) {
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
        public final String toString() {
            return Either.this + ".RightProjection";
        }
    }
}
