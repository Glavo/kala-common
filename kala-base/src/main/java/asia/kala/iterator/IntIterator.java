package asia.kala.iterator;

import asia.kala.Tuple;
import asia.kala.Tuple2;
import asia.kala.control.OptionInt;
import asia.kala.internal.InternalIntLinkedBuffer;
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
        PrimIterator<Integer, IntIterator, int[], OptionInt, IntConsumer, IntPredicate>, PrimitiveIterator.OfInt {

    @NotNull
    static IntIterator empty() {
        return IntIterators.EMPTY;
    }

    @NotNull
    static IntIterator of() {
        return empty();
    }

    @NotNull
    static IntIterator of(int value) {
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

    default int size() {
        int i = 0;
        while (hasNext()) {
            nextInt();
            ++i;
        }
        return i;
    }

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
        for (int value : values) {
            if (!contains(value)) {
                return false;
            }
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

    @NotNull
    @Override
    default OptionInt find(@NotNull IntPredicate predicate) {
        while (hasNext()) {
            int value = nextInt();
            if (predicate.test(value)) {
                return OptionInt.some(value);
            }
        }
        return OptionInt.None;
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

    @Nullable
    default Integer maxOrNull() {
        if (!hasNext()) {
            return null;
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.max(value, nextInt());
        }
        return value;
    }

    @NotNull
    @Override
    default OptionInt maxOption() {
        if (!hasNext()) {
            return OptionInt.None;
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.max(value, nextInt());
        }
        return OptionInt.some(value);
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

    @Nullable
    default Integer minOrNull() {
        if (!hasNext()) {
            return null;
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.min(value, nextInt());
        }
        return value;
    }

    @NotNull
    @Override
    default OptionInt minOption() {
        if (!hasNext()) {
            return OptionInt.None;
        }

        int value = nextInt();
        while (hasNext()) {
            value = Math.min(value, nextInt());
        }
        return OptionInt.some(value);
    }

    @NotNull
    @Contract(mutates = "this")
    default IntIterator drop(int n) {
        while (n > 0 && hasNext()) {
            nextInt();
            --n;
        }
        return this;
    }

    @NotNull
    default IntIterator dropWhile(@NotNull IntPredicate predicate) {
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

    @NotNull
    @Override
    default IntIterator take(int n) {
        if (!hasNext() || n <= 0) {
            return empty();
        }

        return new IntIterators.Take(this, n);
    }

    @NotNull
    @Override
    default IntIterator takeWhile(@NotNull IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return this;
        }
        return new IntIterators.TakeWhile(this, predicate);
    }

    @NotNull
    default IntIterator updated(int n, int newValue) {
        if (!hasNext() || n < 0) {
            return this;
        }

        if (n == 0) {
            this.nextInt();
            return prepended(newValue);
        }

        return new IntIterators.Updated(this, n, newValue);
    }

    @NotNull
    default IntIterator prepended(int value) {
        return new IntIterators.Prepended(this, value);
    }

    @NotNull
    default IntIterator appended(int value) {
        return new IntIterators.Appended(this, value);
    }

    @NotNull
    @Override
    default IntIterator filter(@NotNull IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return empty();
        }
        return new IntIterators.Filter(this, predicate, false);
    }

    @NotNull
    @Override
    default IntIterator filterNot(@NotNull IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        if (!hasNext()) {
            return empty();
        }
        return new IntIterators.Filter(this, predicate, true);
    }

    @NotNull
    default IntIterator map(@NotNull IntUnaryOperator mapper) {
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

    @NotNull
    default <U> Iterator<U> mapToObj(@NotNull IntFunction<? extends U> mapper) {
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

    @NotNull
    @Override
    default Tuple2<@NotNull IntIterator, @NotNull IntIterator> span(@NotNull IntPredicate predicate) {
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

    @NotNull
    @Override
    default <A extends Appendable> A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix) {
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

    @NotNull
    @Override
    @Deprecated
    default Integer next() {
        return nextInt();
    }

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
}
