package kala.control;

import kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract class Result<@Covariant T, @Covariant E> implements OptionContainer<T>, Serializable {
    private static final long serialVersionUID = -3388441653871179293L;

    Result() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, E> Result<T, E> narrow(Result<? extends T, ? extends E> result) {
        return (Result<T, E>) result;
    }

    @Contract("_ -> new")
    public static <T, E> Result<T, E> ok(T value) {
        return new Result.Ok<>(value);
    }

    @Contract("_ -> new")
    public static <T, E> Result<T, E> err(E value) {
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

    public abstract @Nullable E getErrOrNull();

    public abstract @NotNull Option<E> getErrOption();

    @Override
    public abstract <U> @NotNull Result<U, E> map(@NotNull Function<? super T, ? extends U> mapper);

    public abstract <U> @NotNull Result<T, U> mapErr(@NotNull Function<? super E, ? extends U> mapper);

    public abstract <U> @NotNull Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<? extends U, ? extends E>> mapper);

    @Contract("-> new")
    public abstract @NotNull Either<E, T> toEither();

    private static final class Ok<T, E> extends Result<T, E> {
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
        public boolean isOk() {
            return true;
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
        @Contract("-> fail")
        public E getErr() {
            throw new NoSuchElementException("Result.Ok.getErr()");
        }

        @Override
        public @Nullable E getErrOrNull() {
            return null;
        }

        @Override
        public @NotNull Option<E> getErrOption() {
            return Option.none();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("_ -> new")
        public <U> @NotNull Result<U, E> map(@NotNull Function<? super T, ? extends U> mapper) {
            return Result.ok(mapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <U> @NotNull Ok<T, U> mapErr(@NotNull Function<? super E, ? extends U> mapper) {
            return (Ok<T, U>) this;
        }

        @Override
        public <U> @NotNull Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<? extends U, ? extends E>> mapper) {
            return Result.narrow(mapper.apply(value));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Either<E, T> toEither() {
            return Either.right(value);
        }

        //
        // -- Object
        //

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
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
        public int hashCode() {
            return Objects.hashCode(value) + HASH_MAGIC;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Result.Ok[" + value + "]";
        }
    }

    private static final class Err<T, E> extends Result<T, E> {
        private static final long serialVersionUID = 8182313103380510810L;
        private static final int HASH_MAGIC = 1638357662;

        private final E err;

        Err(E err) {
            this.err = err;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isOk() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Contract("-> fail")
        public T get() {
            throw new NoSuchElementException();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public E getErr() {
            return err;
        }

        @Override
        public @NotNull E getErrOrNull() {
            return err;
        }

        @Override
        public @NotNull Option<E> getErrOption() {
            return Option.some(err);
        }

        @Override
        @Contract("_ -> this")
        public <U> @NotNull Result<U, E> map(@NotNull Function<? super T, ? extends U> mapper) {
            return (Result.Err<U, E>) this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public <U> @NotNull Result<T, U> mapErr(@NotNull Function<? super E, ? extends U> mapper) {
            return Result.err(mapper.apply(err));
        }


        @Override
        public <U> @NotNull Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<? extends U, ? extends E>> mapper) {
            return ((Result<U, E>) this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Either<E, T> toEither() {
            return Either.left(err);
        }

        //
        // -- Object
        //

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Err<?, ?>)) {
                return false;
            }

            return Objects.equals(err, ((Err<?, ?>) o).err);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(err) + HASH_MAGIC;
        }

        @Override
        public String toString() {
            return "Result.Err[" + err + "]";
        }
    }
}
