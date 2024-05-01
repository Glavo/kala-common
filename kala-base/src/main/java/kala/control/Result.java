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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract class Result<@Covariant T, @Covariant E> implements OptionContainer<T>, Serializable {
    @Serial
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

    public final boolean isOk() {
        return this instanceof Ok;
    }

    public final boolean isErr() {
        return this instanceof Err;
    }

    @Override
    public final boolean isDefined() {
        return isOk();
    }

    @Override
    public abstract T get();

    public abstract E getErr();

    public final @Nullable E getErrOrNull() {
        return isErr() ? getErr() : null;
    }

    public final @NotNull Option<E> getErrOption() {
        return isErr() ? Option.some(getErr()) : Option.none();
    }

    @Override
    public final <U> @NotNull Result<U, E> map(@NotNull Function<? super T, ? extends U> mapper) {
        return isOk() ? ok(mapper.apply(get())) : (Result<U, E>) this;
    }

    public final <U> @NotNull Result<T, U> mapErr(@NotNull Function<? super E, ? extends U> mapper) {
        return isErr() ? err(mapper.apply(getErr())) : (Result<T, U>) this;
    }

    public final <U> @NotNull Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<? extends U, ? extends E>> mapper) {
        return isOk() ? (Result<U, E>) mapper.apply(get()) : (Result<U, E>) this;
    }

    @Contract("-> new")
    public final @NotNull Either<E, T> toEither() {
        return isOk() ? Either.right(get()) : Either.left(getErr());
    }

    @Override
    public final String toString() {
        if (isOk()) {
            return "Result.Ok[" + get() + "]";
        } else {
            return "Result.Err[" + getErr() + "]";
        }
    }

    private static final class Ok<T, E> extends Result<T, E> {
        @Serial
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
            if (!(o instanceof Ok<?, ?> other)) {
                return false;
            }

            return Objects.equals(value, other.value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(value) + HASH_MAGIC;
        }
    }

    private static final class Err<T, E> extends Result<T, E> {
        @Serial
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
            if (!(o instanceof Err<?, ?> other)) {
                return false;
            }

            return Objects.equals(err, other.err);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(err) + HASH_MAGIC;
        }
    }
}
