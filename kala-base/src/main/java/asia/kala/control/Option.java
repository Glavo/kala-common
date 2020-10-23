package asia.kala.control;

import asia.kala.traversable.Transformable;
import asia.kala.Tuple2;
import asia.kala.annotations.Covariant;
import asia.kala.traversable.TransformableOps;
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
public final class Option<@Covariant T> extends OptionAny<T>
        implements OptionContainer<T>, Transformable<T>, TransformableOps<T, Option<?>, Option<T>>, Serializable {
    private static final long serialVersionUID = -4962768465676381896L;

    private static final int HASH_MAGIC = -1623337737;

    /**
     * The single instance of empty {@code Option}.
     */
    public static final Option<?> None = new Option<>(InternalEmptyTag.INSTANCE);

    /**
     * The value if this {@code Option} is not empty, otherwise {@link InternalEmptyTag#INSTANCE}.
     */
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
     * Returns the single instance of empty {@code Option}.
     *
     * @param <T> the type of value
     * @return the single instance of empty {@code Option}.
     * @see Option#None
     */
    @SuppressWarnings("unchecked")
    public static <T> Option<T> none() {
        return (Option<T>) None;
    }

    /**
     * Returns a new {@code Option} contain the {@code value}.
     *
     * @param value the value
     * @param <T>   the type of the value
     * @return a new {@code Option} contain the {@code value}
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> Option<T> some(T value) {
        return new Option<>(value);
    }

    /**
     * Returns {@code Option.some(value)} if value is not null, otherwise returns {@code Option.none()}.
     *
     * @param value the value
     * @param <T>   the type of the value
     * @return {@code Option.some(value)} if value is not null, otherwise {@code Option.none()}
     */
    public static <T> Option<T> of(@Nullable T value) {
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
    public static <T> Option<T> fromJava(@NotNull Optional<? extends T> optional) {
        return of(optional.orElse(null));
    }

    /**
     * Returns {@code true} if the {@code Option} contain a value, otherwise return {@code false}.
     *
     * @return {@code true} if the {@code Option} contain a value, otherwise {@code false}
     */
    public final boolean isDefined() {
        return this != None;
    }

    /**
     * Returns the {@code Option}'s value.
     *
     * @return the {@code Option}'s value
     * @throws NoSuchElementException if the {@code Option} is empty
     */
    @Flow(sourceIsContainer = true)
    public final T get() {
        if (isEmpty()) {
            throw new NoSuchElementException("Option.None");
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final Option<T> getOption() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final Option<T> toOption() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final <U> Option<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        return isDefined() ? some(mapper.apply(value)) : none();
    }

    public final <U> Option<U> flatMap(@NotNull Function<? super T, ? extends Option<? extends U>> mapper) {
        return isDefined() ? narrow(mapper.apply(value)) : none();
    }

    @NotNull
    public final Option<T> filter(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(value) ? this : none();
    }

    @NotNull
    @Override
    public final Option<T> filterNot(@NotNull Predicate<? super T> predicate) {
        return isDefined() && !predicate.test(value) ? this : none();
    }

    @NotNull
    @Override
    public final Option<@NotNull T> filterNotNull() {
        return value == null ? none() : this;
    }


    @NotNull
    @Override
    public final Tuple2<Option<T>, Option<T>> span(@NotNull Predicate<? super T> predicate) {
        if (isDefined()) {
            if (predicate.test(value)) {
                return new Tuple2<>(this, none());
            } else {
                return new Tuple2<>(none(), this);
            }
        } else {
            return new Tuple2<>(none(), none());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Contract(pure = true)
    public final boolean contains(Object value) {
        return Objects.equals(this.value, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public final Option<T> find(@NotNull Predicate<? super T> predicate) {
        return isDefined() && predicate.test(value) ? this : none();
    }

    /**
     * Convert this {@code Option} to {@link Optional}.
     *
     * @return {@code Optional.of(get())} if the {@code Option} contain a value, otherwise {@link Optional#empty()}
     * @throws NullPointerException if {@link #isDefined()} but value is {@code null}
     */
    @NotNull
    public final Optional<T> asJava() {
        return isEmpty() ? Optional.empty() : Optional.of(Objects.requireNonNull(value));
    }

    @NotNull
    @Override
    public final Iterator<T> iterator() {
        return new OptionContainerIterator<>(value);
    }

    @NotNull
    @Override
    public final Spliterator<T> spliterator() {
        return new OptionContainerIterator<>(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Option<?>)) {
            return false;
        }
        Option<?> other = (Option<?>) o;
        return Objects.equals(value, other.value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return value == null ? HASH_MAGIC : value.hashCode() + HASH_MAGIC;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Contract(pure = true)
    @Override
    public final String toString() {
        if (this == None) {
            return "Option.None";
        }
        return "Option[" + value + "]";
    }

    private Object readResolve() {
        if (value == InternalEmptyTag.INSTANCE) {
            return None;
        }
        return this;
    }
}

