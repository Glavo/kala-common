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

import kala.annotations.ReplaceWith;
import kala.collection.base.Iterators;
import kala.collection.base.Traversable;
import kala.function.*;
import kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Represents a result value or the exception that caused it to fail.
 * It provides a new way to handle exceptions, and provides some static methods to help users handle exceptions.
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public sealed interface Try<@Covariant T> extends AnyTry<T>, Traversable<T>, Serializable {
    Try<Void> VOID = new Success<>(null);

    @Contract(value = "_ -> param1", pure = true)
    static <T> Try<T> narrow(Try<? extends T> t) {
        return (Try<T>) t;
    }

    /**
     * Used to prompt the compiler that an exception may be thrown here.
     * Usually used with {@link #sneakyThrow(Throwable)}.
     * <p>
     * Example:
     * <pre>{@code
     * try {
     *     list.forEach(value -> {
     *        try {
     *            // do something
     *        } catch (IOException e) {
     *            return sneakyThrow(e);
     *        }
     *     });
     *
     *     Try.<IOException>maybeThrows();
     * } catch (IOException e) {
     *     // do something
     * }}</pre>
     */
    static <Ex extends Throwable> void maybeThrows() throws Ex {
        // do nothing
    }

    /**
     * An alternative to the {@code throw} statement, used as an expression.
     *
     * <pre>{@code
     * var a = condition ? value : throwException(new IllegalArgumentException());
     * }</pre>
     */
    @Contract("_ -> fail")
    static <R, Ex extends Throwable> R throwException(Ex exception) throws Ex {
        throw exception;
    }

    /**
     * Throws the exception without telling the verifier.
     * <p>
     * It never returns, so you can use it as an expression of any type.
     */
    @Contract("_ -> fail")
    static <R> R sneakyThrow(Throwable exception) {
        sneakyThrow0(exception);
        return null; // make compiler happy
    }

    @Contract("_ -> fail")
    private static <Ex extends Throwable> void sneakyThrow0(Throwable exception) throws Ex {
        throw (Ex) exception;
    }

    static String getStackTraceAsString(@NotNull Throwable exception) {
        Objects.requireNonNull(exception);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream(4096);
        try (PrintStream stream = new PrintStream(buffer, false, StandardCharsets.UTF_8)) {
            exception.printStackTrace(stream);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    static String getStackTraceAsString(StackTraceElement @NotNull [] stackTrace) {
        StringBuilder builder = new StringBuilder(4096);
        String lineSeparator = System.lineSeparator();

        for (StackTraceElement traceElement : stackTrace) {
            builder.append("\tat ").append(traceElement).append(lineSeparator);
        }
        return builder.toString();
    }

    static String getStackTraceAsString(@NotNull Iterable<StackTraceElement> stackTrace) {
        StringBuilder builder = new StringBuilder(4096);
        String lineSeparator = System.lineSeparator();

        for (StackTraceElement traceElement : stackTrace) {
            builder.append("\tat ").append(traceElement).append(lineSeparator);
        }
        return builder.toString();
    }

    @SuppressWarnings("removal")
    static boolean isFatal(Throwable exception) {
        return exception instanceof InterruptedException
                || exception instanceof LinkageError
                || exception instanceof ThreadDeath
                || exception instanceof VirtualMachineError;
    }

    static <T> @NotNull Try<T> success(T value) {
        return new Success<>(value);
    }

    static <T> @NotNull Try<T> failure(@NotNull Throwable exception) {
        Objects.requireNonNull(exception);
        return new Failure<>(exception);
    }

    static <T> @NotNull Try<T> of(@NotNull CheckedSupplier<? extends T, ?> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return success(supplier.getChecked());
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    static <T> @NotNull Try<T> ofCallable(@NotNull Callable<? extends T> callable) {
        Objects.requireNonNull(callable);
        try {
            return success(callable.call());
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    static @NotNull Try<Void> runCatching(@NotNull CheckedRunnable<?> runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.runChecked();
            return VOID;
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    static @NotNull Try<Void> runRunnable(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.run();
            return VOID;
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    static void runIgnoreException(@NotNull CheckedRunnable<?> runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.runChecked();
        } catch (Throwable ignored) {
        }
    }

    static void runUnchecked(@NotNull CheckedRunnable<?> runnable) {
        runnable.run();
    }

    static <U, R0 extends AutoCloseable> @NotNull Try<U> using(
            R0 resource0,
            @NotNull CheckedFunction<? super R0, ? extends U, ?> action) {
        Objects.requireNonNull(action);
        try (R0 r0 = resource0) {
            return success(action.applyChecked(resource0));
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    static <U, R0 extends AutoCloseable, R1 extends AutoCloseable> @NotNull Try<U> using(
            R0 resource0, R1 resource1,
            @NotNull CheckedBiFunction<? super R0, ? super R1, ? extends U, ?> action) {
        Objects.requireNonNull(action);
        try (R0 r0 = resource0; R1 r1 = resource1) {
            return success(action.applyChecked(resource0, resource1));
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    static <U, R0 extends AutoCloseable, R1 extends AutoCloseable, R2 extends AutoCloseable> @NotNull Try<U> using(
            R0 resource0, R1 resource1, R2 resource2,
            @NotNull CheckedTriFunction<? super R0, ? super R1, ? super R2, ? extends U, ?> action) {
        Objects.requireNonNull(action);
        try (R0 r0 = resource0; R1 r1 = resource1; R2 r2 = resource2) {
            return success(action.applyChecked(resource0, resource1, resource2));
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    /**
     * Returns {@code true} if the {@code Try} is {@code Success}, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Try} is {@code Success}, otherwise {@code false}
     */
    @Override
    default boolean isSuccess() {
        return this instanceof Try.Success<T>;
    }

    /**
     * Returns {@code true} if the {@code Try} is {@code Failure}, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Try} is {@code Failure}, otherwise {@code false}
     */
    @Override
    default boolean isFailure() {
        return this instanceof Try.Failure<T>;
    }

    @Override
    @Deprecated
    @ReplaceWith("get()")
    default T getValue() {
        return get();
    }

    default T get() {
        if (this instanceof Try.Success(var value)) {
            return value;
        }
        throw new NoSuchElementException("No value present");
    }

    @Override
    default @Nullable T getOrNull() {
        return this instanceof Success(var value) ? value : null;
    }

    @Override
    default @NotNull Option<T> getOption() {
        return this instanceof Success(var value) ? Option.some(value) : Option.none();
    }

    default T getOrDefault(T defaultValue) {
        return this instanceof Success(var value) ? value : defaultValue;
    }

    default T getOrElse(@NotNull Supplier<? extends T> supplier) {
        return this instanceof Success(var value) ? value : supplier.get();
    }

    default <Ex extends Throwable> T getOrThrow() throws Ex {
        return switch (this) {
            case Success(var value) -> value;
            case Failure(var cause) -> throw (Ex) cause;
        };
    }

    default <Ex extends Throwable> T getOrThrow(@NotNull Supplier<? extends Ex> supplier) throws Ex {
        if (this instanceof Try.Success(var value)) {
            return value;
        }
        throw supplier.get();
    }

    default <Ex extends Throwable> T getOrThrowException(Ex exception) throws Ex {
        if (this instanceof Try.Success(var value)) {
            return value;
        }
        throw exception;
    }

    @Override
    default @NotNull Throwable getCause() {
        return switch (this) {
            case Success<T> __ -> throw new UnsupportedOperationException();
            case Failure(var cause) -> cause;
        };
    }

    default @NotNull Try<T> recover(@NotNull CheckedFunction<? super Throwable, ? extends T, ?> op) {
        Objects.requireNonNull(op);

        return switch (this) {
            case Success<T> success -> success;
            case Failure<T>(var cause) -> {
                try {
                    yield success(op.applyChecked(cause));
                } catch (Throwable ex) {
                    yield failure(ex);
                }
            }
        };
    }

    default <X> @NotNull Try<T> recover(@NotNull Class<? extends X> type, @NotNull CheckedFunction<? super X, ? extends T, ?> op) {
        Objects.requireNonNull(op);
        return switch (this) {
            case Success<T> __ -> this;
            case Failure<T>(var cause) -> {
                if (!type.isInstance(cause)) {
                    yield this;
                }
                try {
                    yield success(op.applyChecked((X) cause));
                } catch (Throwable ex) {
                    yield failure(ex);
                }
            }
        };
    }

    @NotNull
    default Try<T> recoverWith(@NotNull CheckedFunction<? super Throwable, ? extends Try<? extends T>, ?> op) {
        Objects.requireNonNull(op);
        return switch (this) {
            case Success<T> __ -> this;
            case Failure<T>(var cause) -> {
                try {
                    yield (Try<T>) success(op.applyChecked(cause));
                } catch (Throwable ex) {
                    yield failure(ex);
                }
            }
        };
    }

    default <X> @NotNull Try<T> recoverWith(
            @NotNull Class<? extends X> type, @NotNull CheckedFunction<? super X, ? extends Try<? extends T>, ?> op) {
        Objects.requireNonNull(op);
        return switch (this) {
            case Success<T> __ -> this;
            case Failure<T>(var cause) -> {
                if (!type.isInstance(cause)) {
                    yield this;
                }

                try {
                    yield (Try<T>) success(op.applyChecked((X) cause));
                } catch (Throwable ex) {
                    yield failure(ex);
                }
            }
        };
    }

    /**
     * If the {@code Try} is a {@code Failure}, throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <Ex> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * @throws Ex if the {@code Try} is a {@code Failure}
     */
    @Override
    default <Ex extends Throwable> @NotNull Try<T> rethrow() throws Ex {
        return switch (this) {
            case Success<T> __ -> this;
            case Failure<T>(var cause) -> throw (Ex) cause;
        };
    }

    /**
     * If the {@code Try} is a {@code Failure} and the {@code throwable} is an instance of {@code type},
     * throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <Ex> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * or the {@code throwable} not an instance of {@code type}
     * @throws Ex if the {@code Try} is a {@code Failure} and the {@code throwable}'s type is {@code type}
     */
    @Override
    default <Ex extends Throwable> @NotNull Try<T> rethrow(@NotNull Class<? extends Ex> type) throws Ex {
        if (this instanceof Try.Failure<T>(var cause) && type.isInstance(cause)) {
            throw (Ex) cause;
        }

        return this;
    }

    @Override
    default @NotNull Try<T> rethrowFatal() {
        if (this instanceof Try.Failure<T>(var cause) && isFatal(cause)) {
            sneakyThrow(cause);
        }
        return this;
    }

    default @NotNull Either<@NotNull Throwable, T> toEither() {
        return switch (this) {
            case Success<T>(var value) -> Either.right(value);
            case Failure<T>(var cause) -> Either.left(cause);
        };
    }

    default @NotNull Result<T, @NotNull Throwable> toResult() {
        return switch (this) {
            case Success<T>(var value) -> Result.ok(value);
            case Failure<T>(var cause) -> Result.err(cause);
        };
    }

    default <U> @NotNull Try<U> map(@NotNull CheckedFunction<? super T, ? extends U, ?> mapper) {
        Objects.requireNonNull(mapper);
        return switch (this) {
            case Success<T>(var value) -> {
                try {
                    yield success(mapper.applyChecked(value));
                } catch (Throwable ex) {
                    yield failure(ex);
                }
            }
            case Failure<T> failure -> failure.cast();
        };
    }

    default <U> @NotNull Try<U> flatMap(@NotNull CheckedFunction<? super T, ? extends Try<? extends U>, ?> mapper) {
        Objects.requireNonNull(mapper);

        return switch (this) {
            case Success<T>(var value) -> {
                try {
                    yield narrow(mapper.applyChecked(value));
                } catch (Throwable ex) {
                    yield failure(ex);
                }
            }
            case Failure<T> failure -> failure.cast();
        };
    }

    @Override
    default @NotNull Iterator<T> iterator() {
        return switch (this) {
            case Success<T>(var value) -> Iterators.of(value);
            case Failure<T> failure -> Iterators.empty();
        };
    }

    @Override
    default void forEach(@NotNull Consumer<? super T> action) {
        if (this instanceof Try.Success<T>(var value)) {
            action.accept(value);
        }
    }

    record Success<T>(T value) implements Try<T> {

        @Override
        public int hashCode() {
            return Objects.hashCode(value) + SUCCESS_HASH_MAGIC;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AnyTry<?> other) || other.isFailure()) return false;

            return Objects.equals(this.get(), other.getValue());
        }

        @Override
        public String toString() {
            return "Try.Success[" + value + "]";
        }
    }

    record Failure<T>(@NotNull Throwable cause) implements Try<T> {
        public Failure {
            Objects.requireNonNull(cause);
        }

        public <U> Failure<U> cast() {
            return (Failure<U>) this;
        }

        @Override
        public int hashCode() {
            return cause.hashCode() + FAILURE_HASH_MAGIC;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AnyTry<?> other) || other.isSuccess()) return false;

            return Objects.equals(cause, other.getCause());
        }

        @Override
        public String toString() {
            return "Try.Failure[" + cause + "]";
        }
    }
}
