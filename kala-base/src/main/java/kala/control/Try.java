package kala.control;

import kala.function.CheckedFunction;
import kala.function.CheckedRunnable;
import kala.function.CheckedSupplier;
import kala.annotations.Covariant;
import kala.annotations.Sealed;
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
public abstract class Try<@Covariant T> implements Serializable {
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

    public static <Ex extends Throwable> void maybeThrows() throws Ex {
        // do nothing
    }

    @Contract("_ -> fail")
    public static RuntimeException sneakyThrow(@NotNull Throwable throwable) {
        sneakyThrow0(throwable);
        return null;
    }

    private static <E extends Throwable> void sneakyThrow0(Throwable throwable) throws E {
        throw (E) throwable;
    }

    public static boolean isFatal(Throwable throwable) {
        return throwable instanceof InterruptedException
                || throwable instanceof LinkageError
                || throwable instanceof ThreadDeath
                || throwable instanceof VirtualMachineError;
    }

    @Contract("_ -> new")
    public static <T> Try.@NotNull Success<T> success(T value) {
        return value == null ? (Success<T>) Success.NULL : new Success<>(value);
    }

    @Contract("_ -> new")
    public static <T> Try.@NotNull Failure<T> failure(@NotNull Throwable throwable) {
        Objects.requireNonNull(throwable);
        if (isFatal(throwable)) {
            sneakyThrow(throwable);
        }
        return new Try.Failure<>(throwable);
    }

    @Contract("_ -> new")
    public static <T> @NotNull Try<T> of(@NotNull CheckedSupplier<? extends T, ?> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return success(supplier.getChecked());
        } catch (Throwable throwable) {
            return failure(throwable);
        }
    }

    @Contract("_ -> new")
    public static <T> @NotNull Try<T> ofCallable(@NotNull Callable<? extends T> callable) {
        Objects.requireNonNull(callable);
        try {
            return success(callable.call());
        } catch (Throwable throwable) {
            return failure(throwable);
        }
    }

    public static @NotNull Try<Void> run(@NotNull CheckedRunnable<?> runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.runChecked();
            return (Try<Void>) Success.NULL;
        } catch (Throwable ex) {
            return Try.failure(ex);
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
    public abstract boolean isFailure();

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

    public abstract @NotNull Throwable getCause();

    public abstract @Nullable Throwable getCauseOrNull();

    public final @NotNull Option<Throwable> getCauseOption() {
        return Option.of(getCauseOrNull());
    }

    public abstract @NotNull Try<T> recover(@NotNull CheckedFunction<? super Throwable, ? extends T, ?> op);

    public abstract <X> @NotNull Try<T> recover(
            @NotNull Class<? extends X> type, @NotNull CheckedFunction<? super X, ? extends T, ?> op);

    public abstract <X> @NotNull Try<T> recoverWith(
            @NotNull Class<? extends X> type, @NotNull CheckedFunction<? super X, ? extends Try<? extends T>, ?> op);

    public abstract @NotNull Try<T> recoverWith(@NotNull CheckedFunction<? super Throwable, ? extends Try<? extends T>, ?> op);

    /**
     * If the {@code Try} is a {@code Failure}, throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <Ex> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * @throws Ex if the {@code Try} is a {@code Failure}
     */
    public abstract <Ex extends Throwable> @NotNull Success<T> rethrow() throws Ex;

    /**
     * If the {@code Try} is a {@code Failure} and the {@code throwable} is an instance of {@code type},
     * throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <Ex> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * or the {@code throwable} not an instance of {@code type}
     * @throws Ex if the {@code Try} is a {@code Failure} and the {@code throwable}'s type is {@code type}
     */
    public abstract <Ex extends Throwable> @NotNull Try<T> rethrow(@NotNull Class<? extends Ex> type) throws Ex;

    @Contract("-> new")
    public abstract @NotNull Either<@NotNull Throwable, T> toEither();

    /**
     * {@inheritDoc}
     */
    public abstract <U> @NotNull Try<U> map(@NotNull CheckedFunction<? super T, ? extends U, ?> mapper);

    public abstract <U> @NotNull Try<U> flatMap(@NotNull CheckedFunction<? super T, ? extends Try<? extends U>, ?> mapper);

    public static final class Success<T> extends Try<T> {
        private static final long serialVersionUID = 2848103842157024577L;
        private static final int HASH_MAGIC = 518848667;

        private static final Try<?> NULL = new Try.Success<>(null);

        final T value;

        Success(T value) {
            this.value = value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T get() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Throwable getCause() {
            throw new NoSuchElementException("Try.Success.getThrowable");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @Nullable Throwable getCauseOrNull() {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_-> this")
        public @NotNull Try<T> recover(@NotNull CheckedFunction<? super Throwable, ? extends T, ?> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_, _ -> this")
        public <X> @NotNull Success<T> recover(
                @NotNull Class<? extends X> type,
                @NotNull CheckedFunction<? super X, ? extends T, ?> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_ -> this")
        public @NotNull Success<T> recoverWith(@NotNull CheckedFunction<? super Throwable, ? extends Try<? extends T>, ?> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_, _ -> this")
        public <X> @NotNull Success<T> recoverWith(
                @NotNull Class<? extends X> type,
                @NotNull CheckedFunction<? super X, ? extends Try<? extends T>, ?> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        @Contract("-> this")
        public <E extends Throwable> Success<T> rethrow() throws E {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_ -> this")
        public <E extends Throwable> @NotNull Success<T> rethrow(@NotNull Class<? extends E> type) throws E {
            return this;
        }

        @Override
        @Contract("-> new")
        public @NotNull Either<@NotNull Throwable, T> toEither() {
            return Either.right(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <U> @NotNull Try<U> map(@NotNull CheckedFunction<? super T, ? extends U, ?> mapper) {
            try {
                return Try.success(mapper.applyChecked(value));
            } catch (Throwable ex) {
                return Try.failure(ex);
            }
        }

        @Override
        public <U> @NotNull Try<U> flatMap(@NotNull CheckedFunction<? super T, ? extends Try<? extends U>, ?> mapper) {
            Objects.requireNonNull(mapper);
            try {
                return (Try<U>) mapper.applyChecked(value);
            } catch (Throwable ex) {
                return Try.failure(ex);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
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
        public int hashCode() {
            return Objects.hashCode(value) + HASH_MAGIC;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
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

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("-> fail")
        public T get() {
            throw new NoSuchElementException("Try.Failure.get");
        }

        @Override
        public @NotNull Throwable getCause() {
            return throwable;
        }

        @Override
        public @NotNull Throwable getCauseOrNull() {
            return throwable;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_ -> new")
        public @NotNull Try<T> recover(@NotNull CheckedFunction<? super Throwable, ? extends T, ?> op) {
            Objects.requireNonNull(op);
            try {
                return Try.success(op.applyChecked(throwable));
            } catch (Throwable ex) {
                return Try.failure(ex);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <X> @NotNull Try<T> recover(
                @NotNull Class<? extends X> type,
                @NotNull CheckedFunction<? super X, ? extends T, ?> op) {
            if (type.isInstance(throwable)) {
                try {
                    return Try.success(op.applyChecked((X) throwable));
                } catch (Throwable ex) {
                    return Try.failure(ex);
                }
            } else {
                return this;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Try<T> recoverWith(@NotNull CheckedFunction<? super Throwable, ? extends Try<? extends T>, ?> op) {
            try {
                return (Try<T>) op.applyChecked(throwable);
            } catch (Throwable ex) {
                return Try.failure(ex);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <X> @NotNull Try<T> recoverWith(
                @NotNull Class<? extends X> type,
                @NotNull CheckedFunction<? super X, ? extends Try<? extends T>, ?> op) {
            Objects.requireNonNull(op);
            if (type.isInstance(throwable)) { // implicit null check of type
                try {
                    return (Try<T>) op.applyChecked((X) throwable);
                } catch (Throwable ex) {
                    return Try.failure(ex);
                }
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
        public <E extends Throwable> @NotNull Failure<T> rethrow(@NotNull Class<? extends E> type) throws E {
            if (type.isInstance(throwable)) {
                throw (E) throwable;
            }
            return this;
        }

        @Override
        @Contract("-> new")
        public @NotNull Either<@NotNull Throwable, T> toEither() {
            return Either.left(throwable);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <U> @NotNull Try<U> map(@NotNull CheckedFunction<? super T, ? extends U, ?> mapper) {
            return (Try<U>) this;
        }

        @Override
        public @NotNull <U> Try<U> flatMap(@NotNull CheckedFunction<? super T, ? extends Try<? extends U>, ?> mapper) {
            return (Try<U>) this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
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
        public int hashCode() {
            return throwable.hashCode() + HASH_MAGIC;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Try.Failure[" + throwable + "]";
        }
    }
}
