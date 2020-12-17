package asia.kala.control;

import asia.kala.annotations.Covariant;
import asia.kala.annotations.Sealed;
import asia.kala.function.CheckedRunnable;
import asia.kala.function.CheckedSupplier;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;

@SuppressWarnings("unchecked")
@Sealed(subclasses = {Try.Success.class, Try.Failure.class})
public abstract class Try<@Covariant T> implements OptionContainer<T>, Serializable {
    Try() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Try<T> narrow(Try<? extends T> t) {
        return (Try<T>) t;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Try.Success<T> narrow(Try.Success<? extends T> success) {
        return (Try.Success<T>) success;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Try.Failure<T> narrow(Try.Failure<? extends T> failure) {
        return (Try.Failure<T>) failure;
    }

    public static <E extends Throwable> void maybeThrows() throws E {
        // do nothing
    }

    @Contract("_ -> fail")
    public static RuntimeException throwExceptionUnchecked(@NotNull Throwable throwable) {
        throwExceptionUnchecked0(throwable);
        return null;
    }

    private static <E extends Throwable> void throwExceptionUnchecked0(Throwable throwable) throws E {
        throw (E) throwable;
    }

    @Contract("_ -> new")
    public static <T> Try.@NotNull Success<T> success(T value) {
        return new Try.Success<>(value);
    }

    @Contract("_ -> new")
    public static <T> Try.@NotNull Failure<T> failure(@NotNull Throwable throwable) {
        Objects.requireNonNull(throwable);
        return new Try.Failure<>(throwable);
    }

    @Contract("_ -> new")
    public static <T> @NotNull Try<T> run(@NotNull CheckedSupplier<? extends T, ? extends Throwable> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return success(supplier.getChecked());
        } catch (Throwable throwable) {
            return failure(throwable);
        }
    }

    @Contract("_ -> new")
    public static <T> @NotNull Try<T> runCallable(@NotNull Callable<? extends T> callable) {
        Objects.requireNonNull(callable);
        try {
            return success(callable.call());
        } catch (Throwable throwable) {
            return failure(throwable);
        }
    }

    public static void runIgnoreException(@NotNull CheckedRunnable<?> runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.runChecked();
        } catch (Throwable ignored) {
        }
    }

    public static void runUnchecked(@NotNull CheckedRunnable<?> runnable) {
        runnable.run();
    }

    /**
     * Returns {@code true} if the {@code Try} is {@code Success}, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Try} is {@code Success}, otherwise {@code false}
     */
    public abstract boolean isSuccess();

    /**
     * Returns {@code true} if the {@code Try} is {@code Failure}, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Try} is {@code Failure}, otherwise {@code false}
     */
    public final boolean isFailure() {
        return !isSuccess();
    }

    /**
     * Returns {@code true} if the {@code Try} contain a value, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Try} contain a value, otherwise {@code false}
     */
    public final boolean isDefined() {
        return isSuccess();
    }

    /**
     * Returns {@code true} if the {@code Try} is empty, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Try} is empty, otherwise {@code false}
     */
    public final boolean isEmpty() {
        return isFailure();
    }

    /**
     * Returns the {@code Either}'s value.
     *
     * @return the {@code Either}'s value
     * @throws NoSuchElementException if the {@code Either} is {@code Failure}
     */
    @Flow(sourceIsContainer = true)
    public abstract T get();

    public abstract @NotNull Throwable getThrowable();

    public abstract @Nullable Throwable getThrowableOrNull();

    public abstract @NotNull Success<T> recover(@NotNull Function<? super Throwable, ? extends T> op);

    public abstract @NotNull Try<T> recover(
            @NotNull Class<? extends Throwable> type, @NotNull Function<? super Throwable, ? extends T> op);

    public abstract @NotNull Try<T> recoverWith(
            @NotNull Class<? extends Throwable> type, @NotNull Function<? super Throwable, ? extends Try<? extends T>> op);

    public abstract @NotNull Try<T> recoverWith(@NotNull Function<? super Throwable, ? extends Try<? extends T>> op);

    /**
     * If the {@code Try} is a {@code Failure}, throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <E> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * @throws E if the {@code Try} is a {@code Failure}
     */
    public abstract <E extends Throwable> @NotNull Success<T> rethrow() throws E;

    /**
     * If the {@code Try} is a {@code Failure} and the {@code throwable} is an instance of {@code type},
     * throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <E> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * or the {@code throwable} not an instance of {@code type}
     * @throws E if the {@code Try} is a {@code Failure} and the {@code throwable}'s type is {@code type}
     */
    public abstract <E extends Throwable> @NotNull Try<T> rethrow(@NotNull Class<? extends E> type) throws E;

    @Contract("-> new")
    public abstract @NotNull Either<@NotNull Throwable, T> toEither();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <U> @NotNull Try<U> map(@NotNull Function<? super T, ? extends U> mapper);

    public static final class Success<T> extends Try<T> {
        private static final long serialVersionUID = 2848103842157024577L;
        private static final int HASH_MAGIC = 518848667;

        final T value;

        Success(T value) {
            this.value = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean isSuccess() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final T get() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Throwable getThrowable() {
            throw new NoSuchElementException("Try.Success.getThrowable");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @Nullable Throwable getThrowableOrNull() {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_-> this")
        public final @NotNull Success<T> recover(@NotNull Function<? super Throwable, ? extends T> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_, _ -> this")
        public final @NotNull Success<T> recover(
                @NotNull Class<? extends Throwable> type,
                @NotNull Function<? super Throwable, ? extends T> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_ -> this")
        public final @NotNull Success<T> recoverWith(@NotNull Function<? super Throwable, ? extends Try<? extends T>> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_, _ -> this")
        public final @NotNull Success<T> recoverWith(
                @NotNull Class<? extends Throwable> type,
                @NotNull Function<? super Throwable, ? extends Try<? extends T>> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        @Contract("-> this")
        public final <E extends Throwable> Success<T> rethrow() throws E {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_ -> this")
        public final <E extends Throwable> @NotNull Success<T> rethrow(@NotNull Class<? extends E> type) throws E {
            return this;
        }

        @Override
        @Contract("-> new")
        public final @NotNull Either<@NotNull Throwable, T> toEither() {
            return Either.right(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final <U> @NotNull Success<U> map(@NotNull Function<? super T, ? extends U> mapper) {
            return new Success<>(mapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Success<?>)) {
                return false;
            }

            return Objects.equals(value, ((Success<?>) o).value);
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
            return "Try.Success[" + value + "]";
        }
    }

    public static final class Failure<T> extends Try<T> {
        private static final long serialVersionUID = 1619478637599002563L;
        private static final int HASH_MAGIC = 1918688519;

        @NotNull
        final Throwable throwable;

        Failure(@NotNull Throwable throwable) {
            this.throwable = throwable;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean isSuccess() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("-> fail")
        public final T get() {
            throw new NoSuchElementException("Try.Failure.get");
        }

        @Override
        public final @NotNull Throwable getThrowable() {
            return throwable;
        }

        @Override
        public final @NotNull Throwable getThrowableOrNull() {
            return throwable;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_ -> new")
        public final @NotNull Success<T> recover(@NotNull Function<? super Throwable, ? extends T> op) {
            return Try.success(op.apply(throwable));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Try<T> recover(
                @NotNull Class<? extends Throwable> type,
                @NotNull Function<? super Throwable, ? extends T> op) {
            if (type.isInstance(throwable)) {
                return Try.success(op.apply(throwable));
            }
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Try<T> recoverWith(@NotNull Function<? super Throwable, ? extends Try<? extends T>> op) {
            return narrow(op.apply(throwable));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final @NotNull Try<T> recoverWith(
                @NotNull Class<? extends Throwable> type,
                @NotNull Function<? super Throwable, ? extends Try<? extends T>> op) {
            if (type.isInstance(throwable)) {
                return narrow(op.apply(throwable));
            }
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("-> fail")
        public <E extends Throwable> @NotNull Success<T> rethrow() throws E {
            throw (E) throwable;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final <E extends Throwable> @NotNull Failure<T> rethrow(@NotNull Class<? extends E> type) throws E {
            if (type.isInstance(throwable)) {
                throw (E) throwable;
            }
            return this;
        }

        @Override
        @Contract("-> new")
        public final @NotNull Either<@NotNull Throwable, T> toEither() {
            return Either.left(throwable);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final <U> @NotNull Failure<U> map(@NotNull Function<? super T, ? extends U> mapper) {
            return (Failure<U>) this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Failure<?>)) {
                return false;
            }

            return throwable.equals(((Failure<?>) o).throwable);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final int hashCode() {
            return throwable.hashCode() + HASH_MAGIC;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final String toString() {
            return "Try.Failure[" + throwable + "]";
        }
    }
}
