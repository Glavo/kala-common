package kala.collection.base.primitive;

import kala.annotations.DeprecatedReplaceWith;
import kala.collection.base.Iterators;
import kala.control.primitive.IntOption;
import kala.internal.InternalIntLinkedBuffer;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.collection.base.AbstractIterator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.*;

public interface IntIterator
        extends
        PrimIterator<Integer, IntIterator, int[], IntOption, IntConsumer, IntPredicate>, PrimitiveIterator.OfInt {

    static @NotNull IntIterator empty() {
        return IntIterators.EMPTY;
    }

    static @NotNull IntIterator of() {
        return empty();
    }

    static @NotNull IntIterator of(int value) {
        return new AbstractIntIterator() {

            private boolean hasNext = true;

            @Override
            public final boolean hasNext() {
                return hasNext;
            }

            @Override
            public final int nextInt() {
                if (hasNext) {
                    hasNext = false;
                    return value;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public final String toString() {
                if (hasNext) {
                    return "IntIterator[" + value + "]";
                } else {
                    return "IntIterator[]";
                }
            }
        };
    }

    /**
     * Returns the next {@code int} element in the iteration.
     *
     * @return the next {@code int} element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    int nextInt();

    @Override
    default void nextIgnoreResult() {
        nextInt();
    }

    default int size() {
        int i = 0;
        while (hasNext()) {
            nextInt();
            ++i;
        }
        return i;
    }

    @Override
    @Deprecated
    @DeprecatedReplaceWith("nextInt()")
    default @NotNull Integer next() {
        return nextInt();
    }

    @Override
    default @NotNull IntOption find(@NotNull IntPredicate predicate) {
        while (hasNext()) {
            int value = nextInt();
            if (predicate.test(value)) {
                return IntOption.some(value);
            }
        }
        return IntOption.None;
    }

    //region Element Conditions

    default boolean contains(int value) {
        while (hasNext()) {
            if (nextInt() == value) {
                return true;
            }
        }
        return false;
    }

    default boolean contains(Object value) {
        if (!(value instanceof Integer)) {
            return false;
        }

        int v = (Integer) value;

        while (hasNext()) {
            if (nextInt() == v) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean containsAll(int @NotNull [] values) {
        loop:
        while (hasNext()) {
            final int v = nextInt();
            for (int i : values) {
                if (i == v) {
                    continue loop;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    default boolean sameElements(@NotNull IntIterator other) {
        return sameElements((PrimitiveIterator.OfInt) other);
    }

    default boolean sameElements(@NotNull PrimitiveIterator.OfInt other) {
        while (this.hasNext() && other.hasNext()) {
            if (this.nextInt() != other.nextInt()) {
                return false;
            }
        }
        return this.hasNext() == other.hasNext();
    }

    @Override
    default boolean sameElements(@NotNull Iterator<?> other) {
        if (other instanceof PrimitiveIterator.OfInt) {
            return sameElements(((PrimitiveIterator.OfInt) other));
        }
        while (this.hasNext() && other.hasNext()) {
            Object value = other.next();
            if (!(value instanceof Integer) || (Integer) value != this.nextInt()) {
                return false;
            }
        }
        return this.hasNext() == other.hasNext();
    }

    @Override
    default boolean anyMatch(@NotNull IntPredicate predicate) {
        while (hasNext()) {
            if (predicate.test(nextInt())) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean allMatch(@NotNull IntPredicate predicate) {
        while (hasNext()) {
            if (!predicate.test(nextInt())) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean noneMatch(@NotNull IntPredicate predicate) {
        while (hasNext()) {
            if (predicate.test(nextInt())) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Misc Operations

    @Contract(mutates = "this")
    default @NotNull IntIterator drop(int n) {
        while (n > 0 && hasNext()) {
            nextInt();
            --n;
        }
        return this;
    }

    default @NotNull IntIterator dropWhile(@NotNull IntPredicate predicate) {
        if (!hasNext()) {
            return this;
        }

        int value = 0;
        boolean p = false;
        while (hasNext()) {
            if (!predicate.test(value = nextInt())) {
                p = true;
                break;
            }
        }

        if (p) {
            return hasNext() ? prepended(value) : IntIterator.of(value);
        } else {
            return this;
        }
    }

    @Override
    default @NotNull IntIterator take(int n) {
        if (!hasNext() || n <= 0) {
            return empty();
        }

        return new IntIterators.Take(this, n);
    }

    @Override
    default @NotNull IntIterator takeWhile(@NotNull IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return this;
        }
        return new IntIterators.TakeWhile(this, predicate);
    }

    default @NotNull IntIterator updated(int n, int newValue) {
        if (!hasNext() || n < 0) {
            return this;
        }

        if (n == 0) {
            this.nextInt();
            return prepended(newValue);
        }

        return new IntIterators.Updated(this, n, newValue);
    }

    default @NotNull IntIterator prepended(int value) {
        return new IntIterators.Prepended(this, value);
    }

    default @NotNull IntIterator appended(int value) {
        return new IntIterators.Appended(this, value);
    }

    @Override
    default @NotNull IntIterator filter(@NotNull IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return empty();
        }
        return new IntIterators.Filter(this, predicate, false);
    }

    @Override
    default @NotNull IntIterator filterNot(@NotNull IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return empty();
        }
        return new IntIterators.Filter(this, predicate, true);
    }

    default @NotNull IntIterator map(@NotNull IntUnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        if (!hasNext()) {
            return this;
        }
        return new AbstractIntIterator() {
            @Override
            public final boolean hasNext() {
                return IntIterator.this.hasNext();
            }

            @Override
            public final int nextInt() {
                return mapper.applyAsInt(IntIterator.this.nextInt());
            }
        };
    }

    default <U> @NotNull Iterator<U> mapToObj(@NotNull IntFunction<? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!hasNext()) {
            return Iterators.empty();
        }
        return new AbstractIterator<U>() {
            @Override
            public final boolean hasNext() {
                return IntIterator.this.hasNext();
            }

            @Override
            public final U next() {
                return mapper.apply(IntIterator.this.nextInt());
            }
        };
    }

    @Override
    default @NotNull Tuple2<@NotNull IntIterator, @NotNull IntIterator> span(@NotNull IntPredicate predicate) {
        if (!hasNext()) {
            return Tuple.of(empty(), empty());
        }

        InternalIntLinkedBuffer buffer = new InternalIntLinkedBuffer();
        IntIterator it = this;

        while (it.hasNext()) {
            int e = it.nextInt();
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

    //region Aggregate Operations

    @Override
    default int count(@NotNull IntPredicate predicate) {
        int c = 0;
        while (hasNext()) {
            if (predicate.test(nextInt())) {
                ++c;
            }
        }
        return c;
    }

    default int max() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.max(value, nextInt());
        }
        return value;
    }

    default @Nullable Integer maxOrNull() {
        if (!hasNext()) {
            return null;
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.max(value, nextInt());
        }
        return value;
    }

    @Override
    default @NotNull IntOption maxOption() {
        if (!hasNext()) {
            return IntOption.None;
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.max(value, nextInt());
        }
        return IntOption.some(value);
    }

    default int min() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.min(value, nextInt());
        }
        return value;
    }

    default @Nullable Integer minOrNull() {
        if (!hasNext()) {
            return null;
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.min(value, nextInt());
        }
        return value;
    }

    @Override
    default @NotNull IntOption minOption() {
        if (!hasNext()) {
            return IntOption.None;
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.min(value, nextInt());
        }
        return IntOption.some(value);
    }

    //endregion

    //region Conversion Operations

    @Override
    default int @NotNull [] toArray() {
        if (!hasNext()) {
            return new int[0];
        }
        InternalIntLinkedBuffer buffer = new InternalIntLinkedBuffer();
        while (hasNext()) {
            buffer.append(nextInt());
        }

        return buffer.toArray();
    }

    //endregion

    //region Traverse Operations

    @Override
    default void forEach(@NotNull IntConsumer action) {
        while (hasNext()) {
            action.accept(nextInt());
        }
    }

    @Override
    default void forEachRemaining(@NotNull Consumer<? super Integer> action) {
        if (action instanceof IntConsumer) {
            forEach(((IntConsumer) action));
        } else {
            forEach(action::accept);
        }
    }

    @Override
    default void forEachRemaining(@NotNull IntConsumer action) {
        forEach(action);
    }

    //endregion

    //region

    @Override
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix) {
        try {
            buffer.append(prefix);
            if (hasNext()) {
                buffer.append(String.valueOf(nextInt()));
            }
            while (hasNext()) {
                buffer.append(separator).append(String.valueOf(nextInt()));
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return buffer;
    }

    //endregion
}
