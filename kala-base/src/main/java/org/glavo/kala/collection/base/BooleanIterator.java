package org.glavo.kala.collection.base;

import org.glavo.kala.internal.InternalBooleanLinkedBuffer;
import org.glavo.kala.Tuple;
import org.glavo.kala.Tuple2;
import org.glavo.kala.control.OptionBoolean;
import org.glavo.kala.function.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("ConstantConditions")
public interface BooleanIterator
        extends PrimIterator<Boolean, BooleanIterator, boolean[], OptionBoolean, BooleanConsumer, BooleanPredicate> {


    static @NotNull BooleanIterator empty() {
        return BooleanIterators.EMPTY;
    }

    static @NotNull BooleanIterator of() {
        return BooleanIterators.EMPTY;
    }

    static @NotNull BooleanIterator of(boolean value) {
        return new AbstractBooleanIterator() {

            private boolean hasNext = true;

            @Override
            public final boolean hasNext() {
                return hasNext;
            }

            @Override
            public final boolean nextBoolean() {
                if (hasNext) {
                    hasNext = false;
                    return value;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    static @NotNull BooleanIterator of(boolean... values) {
        final int length = values.length;
        if (length == 0) {
            return empty();
        } else if (length == 1) {
            return of(values[0]);
        } else {
            return new BooleanIterators.OfArray(values);
        }
    }

    static @NotNull BooleanIterator ofIterator(@NotNull Iterator<@NotNull ? extends Boolean> it) {
        Objects.requireNonNull(it);
        if (it instanceof BooleanIterator) {
            return (BooleanIterator) it;
        }
        return new BooleanIterator() {
            @Override
            public final boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public final boolean nextBoolean() {
                return it.next();
            }

            @Override
            public final String toString() {
                return it.toString();
            }
        };
    }

    /**
     * Returns the next {@code boolean} element in the iteration.
     *
     * @return the next {@code boolean} element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    boolean nextBoolean();

    default int size() {
        int i = 0;
        while (hasNext()) {
            nextBoolean();
            ++i;
        }
        return i;
    }

    //region Element Conditions

    default boolean contains(boolean value) {
        while (hasNext()) {
            if (nextBoolean() == value) {
                return true;
            }
        }
        return false;
    }

    default boolean contains(Object value) {
        if (!(value instanceof Boolean)) {
            return false;
        }

        boolean v = (Boolean) value;

        while (hasNext()) {
            if (nextBoolean() == v) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean containsAll(boolean @NotNull [] values) {
        if (!hasNext()) {
            return values.length == 0;
        }
        boolean containsTrue = false;
        boolean containsFalse = false;

        while (hasNext()) {
            boolean v = nextBoolean();
            if (v) {
                containsTrue = true;
            } else {
                containsFalse = true;
            }
            if (containsTrue && containsFalse) {
                return true;
            }
        }

        if (containsTrue && containsFalse) {
            throw new AssertionError();
        } else if (containsTrue) {
            for (boolean value : values) {
                if (!value) {
                    return false;
                }
            }
        } else if (containsFalse) {
            for (boolean value : values) {
                if (value) {
                    return false;
                }
            }
        } else {
            throw new AssertionError();
        }
        return true;
    }

    @Override
    default boolean containsAll(Boolean @NotNull [] values) {
        if (values.length == 0) {
            return true;
        }
        if (!hasNext()) {
            return false;
        }

        boolean containsTrue = false;
        boolean containsFalse = false;

        while (hasNext()) {
            boolean v = nextBoolean();
            if (v) {
                containsTrue = true;
            } else {
                containsFalse = true;
            }
            if (containsTrue && containsFalse) {
                break;
            }
        }

        if (containsTrue && containsFalse) {
            for (Boolean value : values) {
                if (value == null) {
                    return false;
                }
            }
        } else if (containsTrue) {
            for (Boolean value : values) {
                if (value == null || !value) {
                    return false;
                }
            }
        } else if (containsFalse) {
            for (Boolean value : values) {
                if (value == null || value) {
                    return false;
                }
            }
        } else {
            throw new AssertionError();
        }
        return true;
    }

    @Override
    default boolean containsAll(@NotNull Iterable<?> values) {
        Iterator<?> it = values.iterator();
        if (!it.hasNext()) {
            return true;
        }
        if (!hasNext()) {
            return false;
        }

        boolean containsTrue = false;
        boolean containsFalse = false;

        while (hasNext()) {
            boolean v = nextBoolean();
            if (v) {
                containsTrue = true;
            } else {
                containsFalse = true;
            }
            if (containsTrue && containsFalse) {
                break;
            }
        }

        if (containsTrue && containsFalse) {
            while (it.hasNext()) {
                Object value = it.next();
                if (!(value instanceof Boolean)) {
                    return false;
                }
            }
        } else if (containsTrue) {
            while (it.hasNext()) {
                Object value = it.next();
                if (!(value instanceof Boolean) || !(Boolean) value) {
                    return false;
                }
            }
        } else if (containsFalse) {
            while (it.hasNext()) {
                Object value = it.next();
                if (!(value instanceof Boolean) || (Boolean) value) {
                    return false;
                }
            }
        } else {
            throw new AssertionError();
        }
        return true;
    }

    default boolean sameElements(@NotNull BooleanIterator other) {
        while (this.hasNext() && other.hasNext()) {
            if (this.nextBoolean() != other.nextBoolean()) {
                return false;
            }
        }
        return this.hasNext() == other.hasNext();
    }

    default boolean sameElements(@NotNull Iterator<?> other) {
        if (other instanceof BooleanIterator) {
            return sameElements(((BooleanIterator) other));
        }
        while (this.hasNext() && other.hasNext()) {
            if (!Objects.equals(other.next(), this.nextBoolean())) {
                return false;
            }
        }
        return this.hasNext() == other.hasNext();
    }

    default boolean anyMatch(@NotNull BooleanPredicate predicate) {
        while (hasNext()) {
            if (predicate.test(nextBoolean())) {
                return true;
            }
        }
        return false;
    }

    default boolean allMatch(@NotNull BooleanPredicate predicate) {
        while (hasNext()) {
            if (!predicate.test(nextBoolean())) {
                return false;
            }
        }
        return true;
    }

    default boolean noneMatch(@NotNull BooleanPredicate predicate) {
        while (hasNext()) {
            if (predicate.test(nextBoolean())) {
                return false;
            }
        }
        return true;
    }

    //endregion

    @Override
    default @NotNull OptionBoolean find(@NotNull BooleanPredicate predicate) {
        while (hasNext()) {
            boolean v = nextBoolean();
            if (predicate.test(v)) {
                return OptionBoolean.of(v);
            }
        }

        return OptionBoolean.None;
    }

    //region Addition Operations

    default @NotNull BooleanIterator appended(boolean value) {
        if (!hasNext()) {
            return BooleanIterator.of(value);
        }
        return new BooleanIterators.Appended(this, value);
    }

    default @NotNull BooleanIterator prepended(boolean value) {
        return new BooleanIterators.Prepended(this, value);
    }

    //endregion

    //region Aggregate Operations

    default int count(@NotNull BooleanPredicate predicate) {
        int c = 0;
        while (hasNext()) {
            if (predicate.test(nextBoolean())) {
                ++c;
            }
        }
        return c;
    }

    default boolean max() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        while (hasNext()) {
            if (nextBoolean()) {
                return true;
            }
        }
        return false;
    }

    default @Nullable Boolean maxOrNull() {
        if (!hasNext()) {
            return null;
        }

        while (hasNext()) {
            if (nextBoolean()) {
                return true;
            }
        }
        return false;
    }

    default @NotNull OptionBoolean maxOption() {
        if (!hasNext()) {
            return OptionBoolean.None;
        }

        while (hasNext()) {
            if (nextBoolean()) {
                return OptionBoolean.True;
            }
        }
        return OptionBoolean.False;
    }

    default boolean min() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        while (hasNext()) {
            if (!nextBoolean()) {
                return false;
            }
        }
        return true;
    }

    default @Nullable Boolean minOrNull() {
        if (!hasNext()) {
            return null;
        }

        while (hasNext()) {
            if (!nextBoolean()) {
                return false;
            }
        }
        return true;
    }

    default @NotNull OptionBoolean minOption() {
        if (!hasNext()) {
            return OptionBoolean.None;
        }

        while (hasNext()) {
            if (!nextBoolean()) {
                return OptionBoolean.False;
            }
        }
        return OptionBoolean.True;
    }

    //endregion

    //region Misc Operations

    @Contract(mutates = "this")
    default @NotNull BooleanIterator drop(int n) {
        while (n > 0 && hasNext()) {
            nextBoolean();
            --n;
        }
        return this;
    }

    default @NotNull BooleanIterator dropWhile(@NotNull BooleanPredicate predicate) {
        Objects.requireNonNull(predicate);

        if (!hasNext()) {
            return this;
        }

        boolean value = false;
        boolean p = false;
        while (hasNext()) {
            if (!predicate.test(value = nextBoolean())) {
                p = true;
                break;
            }
        }

        if (p) {
            return hasNext() ? prepended(value) : BooleanIterator.of(value);
        } else {
            return this;
        }
    }

    default @NotNull BooleanIterator take(int n) {
        if (!hasNext() || n <= 0) {
            return empty();
        }

        return new BooleanIterators.Take(this, n);
    }

    default @NotNull BooleanIterator takeWhile(@NotNull BooleanPredicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return this;
        }
        return new BooleanIterators.TakeWhile(this, predicate);
    }

    default @NotNull BooleanIterator updated(int n, boolean newValue) {
        if (!hasNext() || n < 0) {
            return this;
        }

        if (n == 0) {
            this.nextBoolean();
            return prepended(newValue);
        }

        return new BooleanIterators.Updated(this, n, newValue);
    }

    default @NotNull BooleanIterator filter(@NotNull BooleanPredicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return empty();
        }
        return new BooleanIterators.Filter(this, predicate, false);
    }

    default @NotNull BooleanIterator filterNot(@NotNull BooleanPredicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return empty();
        }
        return new BooleanIterators.Filter(this, predicate, true);
    }

    default @NotNull BooleanIterator map(@NotNull BooleanUnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        if (!hasNext()) {
            return this;
        }
        return new AbstractBooleanIterator() {
            @Override
            public final boolean hasNext() {
                return BooleanIterator.this.hasNext();
            }

            @Override
            public final boolean nextBoolean() {
                return mapper.applyAsBoolean(BooleanIterator.this.nextBoolean());
            }
        };
    }

    default <U> @NotNull Iterator<U> mapToObj(@NotNull BooleanFunction<? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!hasNext()) {
            return Iterators.empty();
        }
        return new AbstractIterator<U>() {
            @Override
            public final boolean hasNext() {
                return BooleanIterator.this.hasNext();
            }

            @Override
            public final U next() {
                return mapper.apply(BooleanIterator.this.nextBoolean());
            }
        };
    }

    default @NotNull Tuple2<BooleanIterator, BooleanIterator> span(
            @NotNull BooleanPredicate predicate
    ) {
        if (!hasNext()) {
            return Tuple.of(empty(), empty());
        }

        InternalBooleanLinkedBuffer buffer = new InternalBooleanLinkedBuffer();
        BooleanIterator it = this;

        while (it.hasNext()) {
            boolean e = it.nextBoolean();
            if (predicate.test(e)) {
                buffer.append(e);
            } else {
                it = it.prepended(e);
                break;
            }
        }

        return Tuple.of(buffer.iterator(), it);
    }

    //endregion

    //region Conversion Operations

    default boolean @NotNull [] toArray() {
        if (!hasNext()) {
            return new boolean[0];
        }
        InternalBooleanLinkedBuffer buffer = new InternalBooleanLinkedBuffer();
        while (hasNext()) {
            buffer.append(nextBoolean());
        }

        return buffer.toArray();
    }

    //endregion

    @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        try {
            buffer.append(prefix);
            if (hasNext()) {
                buffer.append(String.valueOf(nextBoolean()));
            }
            while (hasNext()) {
                buffer.append(separator).append(String.valueOf(nextBoolean()));
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return buffer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    default @NotNull Boolean next() {
        return nextBoolean();
    }

    default void forEach(@NotNull BooleanConsumer action) {
        while (hasNext()) {
            action.accept(nextBoolean());
        }
    }

    default <Ex extends Throwable> void forEachChecked(@NotNull CheckedBooleanConsumer<? extends Ex> action) throws Ex {
        forEach(action);
    }

    default void forEachUnchecked(@NotNull CheckedBooleanConsumer<?> action) {
        forEach(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void forEachRemaining(@NotNull Consumer<? super Boolean> action) {
        if (action instanceof BooleanConsumer) {
            forEach((BooleanConsumer) action);
        } else {
            forEach(action::accept);
        }
    }
}
