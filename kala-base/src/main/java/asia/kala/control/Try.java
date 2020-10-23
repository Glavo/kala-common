package asia.kala.control;

import asia.kala.annotations.Covariant;
import asia.kala.annotations.Sealed;
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

    @NotNull
    @Contract("_ -> new")
    public static <T> Try.Success<T> success(T value) {
        return new Try.Success<>(value);
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> Try.Failure<T> failure(@NotNull Throwable throwable) {
        Objects.requireNonNull(throwable);
        return new Try.Failure<>(throwable);
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> Try<T> run(@NotNull CheckedSupplier<? extends T, ? extends Throwable> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return success(supplier.getChecked());
        } catch (Throwable throwable) {
            return failure(throwable);
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static <T> Try<T> runCallable(@NotNull Callable<? extends T> callable) {
        Objects.requireNonNull(callable);
        try {
            return success(callable.call());
        } catch (Throwable throwable) {
            return failure(throwable);
        }
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

    @NotNull
    public abstract Throwable getThrowable();

    @Nullable
    public abstract Throwable getThrowableOrNull();

    @NotNull
    public abstract Success<T> recover(@NotNull Function<? super Throwable, ? extends T> op);

    @NotNull
    public abstract Try<T> recover(
            @NotNull Class<? extends Throwable> type, @NotNull Function<? super Throwable, ? extends T> op);

    @NotNull
    public abstract Try<T> recoverWith(
            @NotNull Class<? extends Throwable> type, @NotNull Function<? super Throwable, ? extends Try<? extends T>> op);

    @NotNull
    public abstract Try<T> recoverWith(@NotNull Function<? super Throwable, ? extends Try<? extends T>> op);

    /**
     * If the {@code Try} is a {@code Failure}, throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <E> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * @throws E if the {@code Try} is a {@code Failure}
     */
    @NotNull
    public abstract <E extends Throwable> Try<T> rethrow() throws E;

    /**
     * If the {@code Try} is a {@code Failure} and the {@code throwable} is an instance of {@code type},
     * throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <E> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * or the {@code throwable} not an instance of {@code type}
     * @throws E if the {@code Try} is a {@code Failure} and the {@code throwable}'s type is {@code type}
     */
    public abstract <E extends Throwable> Try<T> rethrow(Class<? extends E> type) throws E;

    @NotNull
    @Contract("-> new")
    public abstract Either<Throwable, T> toEither();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public abstract <U> Try<U> map(@NotNull Function<? super T, ? extends U> mapper);

    public static final class Success<T> extends Try<T> {
        private static final long serialVersionUID = 2848103842157024577L;

        final T value;

        Success(T value) {
            this.value = value;
        }

        //
        // -- Try
        //

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
        @NotNull
        public final Throwable getThrowable() {
            throw new NoSuchElementException("Try.Success.getThrowable");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Nullable
        public final Throwable getThrowableOrNull() {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        @Contract("_-> this")
        public final Success<T> recover(@NotNull Function<? super Throwable, ? extends T> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        @Contract("_, _ -> this")
        public final Success<T> recover(
                @NotNull Class<? extends Throwable> type,
                @NotNull Function<? super Throwable, ? extends T> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        @Contract("_ -> this")
        public final Success<T> recoverWith(@NotNull Function<? super Throwable, ? extends Try<? extends T>> op) {
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        @Contract("_, _ -> this")
        public final Success<T> recoverWith(
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
        public final <E extends Throwable> Try<T> rethrow(@NotNull Class<? extends E> type) throws E {
            return this;
        }

        @NotNull
        @Override
        @Contract("-> new")
        public final Either<Throwable, T> toEither() {
            return Either.right(value);
        }

        //
        // -- Functor
        //

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull
        public final <U> Success<U> map(@NotNull Function<? super T, ? extends U> mapper) {
            Objects.requireNonNull(mapper);
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
            return Objects.hashCode(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final String toString() {
            return "Success[value=" + value + "]";
        }
    }

    public static final class Failure<T> extends Try<T> {
        private static final long serialVersionUID = 1619478637599002563L;

        @NotNull
        final Throwable throwable;

        Failure(@NotNull Throwable throwable) {
            this.throwable = throwable;
        }

        //
        // -- Try
        //

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

        @NotNull
        @Override
        public final Throwable getThrowable() {
            return throwable;
        }

        @Override
        @NotNull
        public final Throwable getThrowableOrNull() {
            return throwable;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        @Contract("_ -> new")
        public final Success<T> recover(@NotNull Function<? super Throwable, ? extends T> op) {
            Objects.requireNonNull(op);
            return Try.success(op.apply(throwable));
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public final Try<T> recover(
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
        @NotNull
        @Override
        public final Try<T> recoverWith(@NotNull Function<? super Throwable, ? extends Try<? extends T>> op) {
            return narrow(op.apply(throwable));
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public final Try<T> recoverWith(
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
        @NotNull
        @Override
        @Contract("-> fail")
        public <E extends Throwable> Try<T> rethrow() throws E {
            throw (E) throwable;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final <E extends Throwable> Try<T> rethrow(@NotNull Class<? extends E> type) throws E {
            Objects.requireNonNull(type);
            if (type.isInstance(throwable)) {
                throw (E) throwable;
            }
            return this;
        }

        @NotNull
        @Override
        @Contract("-> new")
        public final Either<Throwable, T> toEither() {
            return Either.left(throwable);
        }

        //
        // -- Functor
        //

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public final <U> Failure<U> map(@NotNull Function<? super T, ? extends U> mapper) {
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

            return Objects.equals(throwable, ((Failure<?>) o).throwable);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final int hashCode() {
            return Objects.hashCode(throwable);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final String toString() {
            return "Failure[throwable=" + throwable + "]";
        }
    }
}
