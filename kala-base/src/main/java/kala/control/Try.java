package kala.control;

import kala.collection.base.Iterators;
import kala.collection.base.Traversable;
import kala.function.*;
import kala.annotations.Covariant;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class Try<@Covariant T> implements Traversable<T>, Serializable {
    private static final long serialVersionUID = -876749736621195838L;

    private static final int SUCCESS_HASH_MAGIC = 518848667;
    private static final int FAILURE_HASH_MAGIC = 1918688519;

    private static final Try<?> NULL = new Try<>(null, null);

    private final T value;
    private final Throwable cause;

    Try(T value, Throwable cause) {
        this.value = value;
        this.cause = cause;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Try<T> narrow(Try<? extends T> t) {
        return (Try<T>) t;
    }

    public static <Ex extends Throwable> void maybeThrows() throws Ex {
        // do nothing
    }

    @Contract("_ -> fail")
    public static RuntimeException sneakyThrow(Throwable throwable) {
        sneakyThrow0(throwable);
        return null;
    }

    private static <Ex extends Throwable> void sneakyThrow0(Throwable throwable) throws Ex {
        throw (Ex) throwable;
    }

    public static boolean isFatal(Throwable throwable) {
        return throwable instanceof InterruptedException
                || throwable instanceof LinkageError
                || throwable instanceof ThreadDeath
                || throwable instanceof VirtualMachineError;
    }

    @Contract("_ -> new")
    public static <T> @NotNull Try<T> success(T value) {
        return value == null ? (Try<T>) NULL : new Try<>(value, null);
    }

    @Contract("_ -> new")
    public static <T> @NotNull Try<T> failure(@NotNull Throwable throwable) {
        Objects.requireNonNull(throwable);
        if (isFatal(throwable)) {
            sneakyThrow(throwable);
        }
        return new Try<>(null, throwable);
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
            return (Try<Void>) NULL;
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    public static @NotNull Try<Void> runRunnable(@NotNull Runnable runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.run();
            return (Try<Void>) NULL;
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

    @Flow(sourceIsContainer = true)
    public <Ex extends Throwable> T get() throws Ex {
        if (cause != null) {
            throw (Ex) cause;
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

    @Contract("-> new")
    public @NotNull Either<@NotNull Throwable, T> toEither() {
        return cause == null ? Either.right(value) : Either.left(cause);
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
        return cause != null ? Iterators.of(value) : Iterators.empty();
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
