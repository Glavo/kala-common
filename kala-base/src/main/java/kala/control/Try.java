package kala.control;

import kala.collection.base.Iterators;
import kala.collection.base.Traversable;
import kala.function.*;
import kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class Try<@Covariant T> implements Traversable<T>, Serializable {
    private static final long serialVersionUID = -876749736621195838L;

    private static final int SUCCESS_HASH_MAGIC = 518848667;
    private static final int FAILURE_HASH_MAGIC = 1918688519;

    private static final Try<Void> VOID = new Try<>(null, null);

    private final T value;
    private final Throwable cause;

    private Try(T value, Throwable cause) {
        this.value = value;
        this.cause = cause;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Try<T> narrow(Try<? extends T> t) {
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
    public static <Ex extends Throwable> void maybeThrows() throws Ex {
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
    public static <R, Ex extends Throwable> R throwException(Ex exception) throws Ex {
        throw exception;
    }

    /**
     * Throws the exception without telling the verifier.
     * <p>
     * It never returns, so you can use it as an expression of any type.
     */
    @Contract("_ -> fail")
    public static <R> R sneakyThrow(Throwable exception) {
        sneakyThrow0(exception);
        throw new AssertionError(); // make compiler happy
    }

    private static <Ex extends Throwable> void sneakyThrow0(Throwable exception) throws Ex {
        throw (Ex) exception;
    }

    public static String getStackTraceAsString(@NotNull Throwable exception) {
        Objects.requireNonNull(exception);

        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream(4096);
            try (PrintStream stream = new PrintStream(buffer, false, "UTF-8")) {
                exception.printStackTrace(stream);
            }
            return buffer.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e);
        }
    }

    public static String getStackTraceAsString(StackTraceElement @NotNull [] stackTrace) {
        StringBuilder builder = new StringBuilder(4096);
        String lineSeparator = System.getProperty("line.separator");

        for (StackTraceElement traceElement : stackTrace) {
            builder.append("\tat ").append(traceElement).append(lineSeparator);
        }
        return builder.toString();
    }

    public static String getStackTraceAsString(@NotNull Iterable<StackTraceElement> stackTrace) {
        StringBuilder builder = new StringBuilder(4096);
        String lineSeparator = System.getProperty("line.separator");

        for (StackTraceElement traceElement : stackTrace) {
            builder.append("\tat ").append(traceElement).append(lineSeparator);
        }
        return builder.toString();
    }


    public static boolean isFatal(Throwable exception) {
        return exception instanceof InterruptedException
                || exception instanceof LinkageError
                || exception instanceof ThreadDeath
                || exception instanceof VirtualMachineError;
    }

    public static <T> @NotNull Try<T> success(T value) {
        return new Try<>(value, null);
    }

    public static <T> @NotNull Try<T> failure(@NotNull Throwable exception) {
        Objects.requireNonNull(exception);
        return new Try<>(null, exception);
    }

    public static <T> @NotNull Try<T> of(@NotNull CheckedSupplier<? extends T, ?> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return success(supplier.getChecked());
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public static <T> @NotNull Try<T> ofCallable(@NotNull Callable<? extends T> callable) {
        Objects.requireNonNull(callable);
        try {
            return success(callable.call());
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public static @NotNull Try<Void> run(@NotNull CheckedRunnable<?> runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.runChecked();
            return VOID;
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public static @NotNull Try<Void> runRunnable(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.run();
            return VOID;
        } catch (Throwable ex) {
            return failure(ex);
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

    public static <U, R0 extends AutoCloseable> @NotNull Try<U> using(
            R0 resource0,
            @NotNull CheckedFunction<? super R0, ? extends U, ?> action) {
        Objects.requireNonNull(action);
        try (R0 r0 = resource0) {
            return success(action.applyChecked(resource0));
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public static <U, R0 extends AutoCloseable, R1 extends AutoCloseable> @NotNull Try<U> using(
            R0 resource0, R1 resource1,
            @NotNull CheckedBiFunction<? super R0, ? super R1, ? extends U, ?> action) {
        Objects.requireNonNull(action);
        try (R0 r0 = resource0; R1 r1 = resource1) {
            return success(action.applyChecked(resource0, resource1));
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public static <U, R0 extends AutoCloseable, R1 extends AutoCloseable, R2 extends AutoCloseable> @NotNull Try<U> using(
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
    public boolean isSuccess() {
        return cause == null;
    }

    /**
     * Returns {@code true} if the {@code Try} is {@code Failure}, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Try} is {@code Failure}, otherwise {@code false}
     */
    public boolean isFailure() {
        return cause != null;
    }

    public T get() {
        if (cause != null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    public @Nullable T getOrNull() {
        return value;
    }

    public @NotNull Option<T> getOption() {
        return Option.of(value);
    }

    public T getOrDefault(T defaultValue) {
        return cause == null ? value : defaultValue;
    }

    public T getOrElse(@NotNull Supplier<? extends T> supplier) {
        return cause == null ? value : supplier.get();
    }

    public <Ex extends Throwable> T getOrThrow() throws Ex {
        if (cause != null) {
            throw (Ex) cause;
        }
        return value;
    }

    public <Ex extends Throwable> T getOrThrow(@NotNull Supplier<? extends Ex> supplier) throws Ex {
        if (cause != null) {
            throw supplier.get();
        }
        return value;
    }

    public <Ex extends Throwable> T getOrThrowException(Ex exception) throws Ex {
        if (cause != null) {
            throw exception;
        }
        return value;
    }

    public @NotNull Throwable getCause() {
        if (cause == null) {
            throw new UnsupportedOperationException();
        }
        return cause;
    }

    public @Nullable Throwable getCauseOrNull() {
        return cause;
    }

    public @NotNull Option<Throwable> getCauseOption() {
        return Option.of(cause);
    }

    public Throwable getCauseOrDefault(Throwable defaultValue) {
        return cause != null ? cause : defaultValue;
    }

    public Throwable getCauseOrElse(@NotNull Supplier<? extends Throwable> supplier) {
        return cause != null ? cause : supplier.get();
    }

    public @NotNull Try<T> recover(@NotNull CheckedFunction<? super Throwable, ? extends T, ?> op) {
        Objects.requireNonNull(op);
        if (cause == null) {
            return this;
        }
        try {
            return success(op.applyChecked(cause));
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public <X> @NotNull Try<T> recover(
            @NotNull Class<? extends X> type, @NotNull CheckedFunction<? super X, ? extends T, ?> op) {
        Objects.requireNonNull(op);
        if (!type.isInstance(cause)) {
            return this;
        }

        try {
            return success(op.applyChecked((X) cause));
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public @NotNull Try<T> recoverWith(@NotNull CheckedFunction<? super Throwable, ? extends Try<? extends T>, ?> op) {
        Objects.requireNonNull(op);
        if (cause == null) {
            return this;
        }
        try {
            return (Try<T>) op.applyChecked(cause);
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public <X> @NotNull Try<T> recoverWith(
            @NotNull Class<? extends X> type, @NotNull CheckedFunction<? super X, ? extends Try<? extends T>, ?> op) {
        Objects.requireNonNull(op);
        if (!type.isInstance(cause)) {
            return this;
        }
        try {
            return (Try<T>) op.applyChecked((X) cause);
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    /**
     * If the {@code Try} is a {@code Failure}, throw the {@code throwable}, otherwise returns {@code this}.
     *
     * @param <Ex> the type of the {@code throwable}
     * @return {@code this} if the {@code Try} is a {@code Success}
     * @throws Ex if the {@code Try} is a {@code Failure}
     */
    public <Ex extends Throwable> @NotNull Try<T> rethrow() throws Ex {
        if (cause != null) {
            throw (Ex) cause;
        }
        return this;
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
    public <Ex extends Throwable> @NotNull Try<T> rethrow(@NotNull Class<? extends Ex> type) throws Ex {
        if (type.isInstance(cause)) {
            throw (Ex) cause;
        }
        return this;
    }

    public @NotNull Try<T> rethrowFatal() {
        if (isFatal(cause)) {
            sneakyThrow(cause);
        }
        return this;
    }

    public @NotNull Either<@NotNull Throwable, T> toEither() {
        return cause == null ? Either.right(value) : Either.left(cause);
    }

    public @NotNull Result<T, @NotNull Throwable> toResult() {
        return cause == null ? Result.ok(value) : Result.err(cause);
    }

    /**
     * {@inheritDoc}
     */
    public <U> @NotNull Try<U> map(@NotNull CheckedFunction<? super T, ? extends U, ?> mapper) {
        Objects.requireNonNull(mapper);
        if (cause != null) {
            return (Try<U>) this;
        }
        try {
            return success(mapper.applyChecked(value));
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public <U> @NotNull Try<U> flatMap(@NotNull CheckedFunction<? super T, ? extends Try<? extends U>, ?> mapper) {
        Objects.requireNonNull(mapper);
        if (cause != null) {
            return (Try<U>) this;
        }

        try {
            return (Try<U>) mapper.applyChecked(value);
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return cause == null ? Iterators.of(value) : Iterators.empty();
    }

    @Override
    public void forEach(@NotNull Consumer<? super T> action) {
        if (cause == null) {
            action.accept(value);
        }
    }

    @Override
    public int hashCode() {
        if (cause == null) {
            return Objects.hashCode(value) + SUCCESS_HASH_MAGIC;
        } else {
            return cause.hashCode() + FAILURE_HASH_MAGIC;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Try)) {
            return false;
        }
        Try<?> other = (Try<?>) o;
        return Objects.equals(value, other.value) && Objects.equals(cause, other.cause);
    }

    @Override
    public String toString() {
        if (cause == null) {
            return "Try.Success[" + value + "]";
        } else {
            return "Try.Failure[" + cause + "]";
        }
    }

    private Object writeReplace() {
        if (cause == null) {
            return new Replaced(true, value);
        } else {
            return new Replaced(false, cause);
        }
    }

    private static final class Replaced implements Serializable {
        private static final long serialVersionUID = -3487244164062636325L;

        private final boolean isSuccess;
        private final Object value;

        Replaced(boolean isSuccess, Object value) {
            this.isSuccess = isSuccess;
            this.value = value;
        }

        private Object readResolve() {
            return isSuccess ? success(value) : failure((Throwable) value);
        }
    }
}
