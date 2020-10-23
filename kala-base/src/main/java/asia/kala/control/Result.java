package asia.kala.control;

import asia.kala.annotations.Covariant;
import asia.kala.annotations.Sealed;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unchecked")
@Sealed(subclasses = {Result.Ok.class, Result.Err.class})
public abstract class Result<@Covariant T, @Covariant E> implements OptionContainer<T>, Serializable {
    Result() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, E> Result<T, E> narrow(Result<? extends T, ? extends E> result) {
        return (Result<T, E>) result;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, E> Result.Ok<T, E> narrow(Result.Ok<? extends T, ? extends E> ok) {
        return (Result.Ok<T, E>) ok;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, E> Result.Err<T, E> narrow(Result.Err<? extends T, ? extends E> err) {
        return (Result.Err<T, E>) err;
    }

    @NotNull
    @Contract("_ -> new")
    public static <T, E> Result.Ok<T, E> ok(T value) {
        return new Result.Ok<>(value);
    }

    @NotNull
    @Contract("_ -> new")
    public static <T, E> Result.Err<T, E> err(E value) {
        return new Result.Err<>(value);
    }

    public abstract boolean isOk();

    public final boolean isErr() {
        return !isOk();
    }

    @Override
    public final boolean isDefined() {
        return isOk();
    }

    @Override
    public abstract T get();

    public abstract E getErr();

    @Nullable
    public abstract E getErrOrNull();

    @NotNull
    @Override
    public abstract <U> Result<U, E> map(@NotNull Function<? super T, ? extends U> mapper);

    @NotNull
    public abstract <U> Result<T, U> mapErr(@NotNull Function<? super E, ? extends U> mapper);

    public abstract <U> Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<? extends U, ? extends E>> mapper);

    @NotNull
    @Contract("-> new")
    public abstract Either<E, T> toEither();

    public static final class Ok<T, E> extends Result<T, E> {
        private static final long serialVersionUID = -7623929614408282297L;
        private static final int HASH_MAGIC = 227556744;

        private final T value;

        Ok(T value) {
            this.value = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean isOk() {
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
        @Contract("-> fail")
        public final E getErr() {
            throw new NoSuchElementException("Result.Ok.getErr()");
        }

        @Nullable
        @Override
        public final E getErrOrNull() {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        @Contract("_ -> new")
        public final <U> Ok<U, E> map(@NotNull Function<? super T, ? extends U> mapper) {
            return Result.ok(mapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public final <U> Ok<T, U> mapErr(@NotNull Function<? super E, ? extends U> mapper) {
            return (Ok<T, U>) this;
        }

        @Override
        public final <U> Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<? extends U, ? extends E>> mapper) {
            return Result.narrow(mapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public final Either<E, T> toEither() {
            return Either.right(value);
        }

        //
        // -- Object
        //

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Ok<?, ?>)) {
                return false;
            }

            return Objects.equals(value, ((Ok<?, ?>) o).value);
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
            return "Result.Ok[" + value + "]";
        }
    }

    public static final class Err<T, E> extends Result<T, E> {
        private static final long serialVersionUID = -2334924456757611037L;
        private static final int HASH_MAGIC = 1638357662;

        private final E value;

        Err(E value) {
            this.value = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean isOk() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("-> fail")
        public final T get() {
            throw new NoSuchElementException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final E getErr() {
            return value;
        }

        @NotNull
        @Override
        public final E getErrOrNull() {
            return value;
        }

        @NotNull
        @Override
        @Contract("_ -> this")
        public final <U> Result.Err<U, E> map(@NotNull Function<? super T, ? extends U> mapper) {
            return (Result.Err<U, E>) this;
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public final <U> Result.Err<T, U> mapErr(@NotNull Function<? super E, ? extends U> mapper) {
            return Result.err(mapper.apply(value));
        }


        @Override
        public final <U> Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<? extends U, ? extends E>> mapper) {
            return ((Result<U, E>) this);
        }

        /**
         * {@inheritDoc}
         */
        @NotNull
        @Override
        public final Either<E, T> toEither() {
            return Either.left(value);
        }

        //
        // -- Object
        //

        /**
         * {@inheritDoc}
         */
        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Err<?, ?>)) {
                return false;
            }

            return Objects.equals(value, ((Err<?, ?>) o).value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public final int hashCode() {
            return Objects.hashCode(value) + HASH_MAGIC;
        }

        @Override
        public final String toString() {
            return "Result.Err[" + value + "]";
        }
    }
}
