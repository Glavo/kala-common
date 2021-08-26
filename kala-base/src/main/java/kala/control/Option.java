package kala.control;

import kala.collection.base.Iterators;
import kala.tuple.Tuple2;
import kala.annotations.Covariant;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A container object which may or may not contain a value.
 *
 * <p>{@code Option} is not the same as {@link Optional}.
 * {@code Option} is a container that supports collection operations, supports serialization,
 * and distinguishes between a {@code Option} instance that contains null as the value and the empty Option.
 * {@link Optional} does not support serialization and cannot store null as its value.
 * Use the {@code Option} in situations where {@link Optional} is not suitable.
 *
 * @param <T> the type of value
 * @author Glavo
 * @see Optional
 */
public final class Option<@Covariant T> extends AnyOption<T>
        implements OptionContainer<T>, Serializable {
    private static final long serialVersionUID = 4055633765420871779L;

    private static final int HASH_MAGIC = -1623337737;
    private static final int NONE_HASH = 1937147281;

    /**
     * The single instance of empty {@code Option}.
     */
    public static final Option<?> None = new Option<>(null);

    private final T value;

    private Option(T value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> param1", pure = true)
    public static <T> Option<T> narrow(Option<? extends T> option) {
        return (Option<T>) option;
    }

    /**
     * Returns a new {@code Option} contain the {@code value}.
     *
     * @param value the value
     * @param <T>   the type of the value
     * @return a new {@code Option} contain the {@code value}
     */
    @Contract(value = "_ -> new", pure = true)
    public static <T> @NotNull Option<T> some(T value) {
        return new Option<>(value);
    }

    /**
     * Returns the single instance of empty {@code Option}.
     *
     * @param <T> the type of value
     * @return the single instance of empty {@code Option}.
     * @see Option#None
     */
    @SuppressWarnings("unchecked")
    public static <T> @NotNull Option<T> none() {
        return (Option<T>) None;
    }

    /**
     * Returns {@code Option.some(value)} if value is not null, otherwise returns {@code Option.none()}.
     *
     * @param value the value
     * @param <T>   the type of the value
     * @return {@code Option.some(value)} if value is not null, otherwise {@code Option.none()}
     */
    public static <T> @NotNull Option<T> of(@Nullable T value) {
        return value == null ? none() : new Option<>(value);
    }

    /**
     * Convert {@link Optional} to {@code Option}.
     *
     * @param optional a {@link Optional} value
     * @param <T>      the type of the value
     * @return {@code Option.some(optional.get())} if {@code optional} is present, otherwise {@code Option.none()}
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> @NotNull Option<T> fromJava(@NotNull Optional<? extends T> optional) {
        return of(optional.orElse(null));
    }

    /**
     * Returns {@code true} if the {@code Option} contain a value, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Option} contain a value, otherwise {@code false}
     */
    public boolean isDefined() {
        return this != None;
    }

    @Override
    public boolean isEmpty() {
        return this == None;
    }

    /**
     * Returns the {@code Option}'s value.
     *
     * @return the {@code Option}'s value
     * @throws NoSuchElementException if the {@code Option} is empty
     */
    @Flow(sourceIsContainer = true)
    public T get() {
        if (this == None) {
            throw new NoSuchElementException("Option.None");
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(value = "-> this", pure = true)
    public @NotNull Option<T> getOption() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(value = "-> this", pure = true)
    public @NotNull Option<T> toOption() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> @NotNull Option<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        return isDefined() ? some(mapper.apply(value)) : none();
    }

    public <U> @NotNull Option<U> flatMap(@NotNull Function<? super T, ? extends Option<? extends U>> mapper) {
        return isDefined() ? narrow(mapper.apply(value)) : none();
    }

    public @NotNull Option<T> filter(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(value) ? this : none();
    }

    public @NotNull Option<T> filterNot(@NotNull Predicate<? super T> predicate) {
        return isDefined() && !predicate.test(value) ? this : none();
    }

    public @NotNull Option<@NotNull T> filterNotNull() {
        return value == null ? none() : this;
    }

    public @NotNull Tuple2<@NotNull Option<T>, @NotNull Option<T>> span(@NotNull Predicate<? super T> predicate) {
        if (isEmpty()) {
            return new Tuple2<>(none(), none());
        } else if (predicate.test(value)) {
            return new Tuple2<>(this, none());
        } else {
            return new Tuple2<>(none(), this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    public boolean contains(Object value) {
        return isDefined() && Objects.equals(this.value, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Option<T> find(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(value) ? this : none();
    }

    /**
     * Convert this {@code Option} to {@link Optional}.
     *
     * @return {@code Optional.of(get())} if the {@code Option} contain a value, otherwise {@link Optional#empty()}
     * @throws NullPointerException if {@link #isDefined()} but value is {@code null}
     */
    public @NotNull Optional<T> asJava() {
        return isDefined() ? Optional.of(value) : Optional.empty();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return isDefined() ? Iterators.of(value) : Iterators.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option<?>)) {
            return false;
        }
        return Objects.equals(value, ((Option<?>) o).value)
                && this != None
                && o != None;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this == None ? NONE_HASH : Objects.hashCode(value) + HASH_MAGIC;
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        if (this == None) {
            return "Option.None";
        } else {
            return "Option[" + value + "]";
        }
    }

    private Object writeReplace() {
        return this == None ? NoneReplaced.INSTANCE : this;
    }

    static final class NoneReplaced implements Serializable {
        private static final long serialVersionUID = 0L;

        static final NoneReplaced INSTANCE = new NoneReplaced();

        private NoneReplaced() {
        }

        private Object readResolve() {
            return Option.None;
        }
    }
}

