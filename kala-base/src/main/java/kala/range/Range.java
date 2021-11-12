package kala.range;

import kala.collection.base.AbstractIterator;
import kala.collection.base.Iterators;
import kala.collection.base.Traversable;
import kala.internal.ComparableUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class Range<T> extends AnyRange<T> implements Serializable {
    private static final long serialVersionUID = 4151410859736356449L;

    private static final int HASH_MAGIC = 1249967851;

    private static final Range<?> EMPTY = new Range<>(RangeType.EMPTY, null, null);
    private static final Range<?> ALL = new Range<>(RangeType.ALL, null, null);

    private final @NotNull RangeType type;

    private final T lowerBound;
    private final T upperBound;

    private final Comparator<? super T> comparator;

    private Range(RangeType type, T lowerBound, T upperBound) {
        this(type, lowerBound, upperBound, null);
    }

    private Range(RangeType type, T lowerBound, T upperBound, Comparator<? super T> comparator) {
        this.type = type;
        this.comparator = comparator;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull Range<T> empty() {
        return (Range<T>) EMPTY;
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull Range<T> all() {
        return (Range<T>) ALL;
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> is(T value) {
        return is(value, null);
    }

    public static <T> @NotNull Range<T> is(T value, Comparator<? super T> comparator) {
        if (comparator == null && !(value instanceof Comparable)) {
            throw new IllegalArgumentException();
        }
        return new Range<>(RangeType.CLOSED, value, value, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> open(@NotNull T lowerBound, @NotNull T upperBound) {
        return open(lowerBound, upperBound, null);
    }

    public static <T> @NotNull Range<T> open(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        if (ComparableUtils.compare(lowerBound, upperBound, comparator) >= 0) {
            throw new IllegalArgumentException("lowerBound should be less than upperBound");
        }

        return new Range<>(RangeType.OPEN, lowerBound, upperBound, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> closed(@NotNull T lowerBound, @NotNull T upperBound) {
        return closed(lowerBound, upperBound, null);
    }

    public static <T> @NotNull Range<T> closed(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        if (ComparableUtils.compare(lowerBound, upperBound, comparator) > 0) {
            throw new IllegalArgumentException("lowerBound should be less than or equal to upperBound");
        }
        return new Range<>(RangeType.CLOSED, lowerBound, upperBound, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> openClosed(@NotNull T lowerBound, @NotNull T upperBound) {
        return openClosed(lowerBound, upperBound, null);
    }

    public static <T> @NotNull Range<T> openClosed(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        if (ComparableUtils.compare(lowerBound, upperBound, comparator) > 0) {
            throw new IllegalArgumentException("lowerBound should be less than or equal to upperBound");
        }
        return new Range<>(RangeType.OPEN_CLOSED, lowerBound, upperBound, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> closedOpen(@NotNull T lowerBound, @NotNull T upperBound) {
        return closedOpen(lowerBound, upperBound, null);
    }

    public static <T> @NotNull Range<T> closedOpen(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        if (ComparableUtils.compare(lowerBound, upperBound, comparator) > 0) {
            throw new IllegalArgumentException("lowerBound should be less than or equal to upperBound");
        }
        return new Range<>(RangeType.CLOSED_OPEN, lowerBound, upperBound, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> greaterThan(@NotNull T lowerBound) {
        return greaterThan(lowerBound, null);
    }

    public static <T> @NotNull Range<T> greaterThan(T lowerBound, Comparator<? super T> comparator) {
        if (comparator == null && !(lowerBound instanceof Comparable)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(RangeType.GREATER_THAN, lowerBound, null, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> atLeast(@NotNull T lowerBound) {
        return atLeast(lowerBound, null);
    }

    public static <T> @NotNull Range<T> atLeast(T lowerBound, Comparator<? super T> comparator) {
        if (comparator == null && !(lowerBound instanceof Comparable)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(RangeType.AT_LEAST, lowerBound, null, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> lessThan(@NotNull T upperBound) {
        return lessThan(upperBound, null);
    }

    public static <T> @NotNull Range<T> lessThan(T upperBound, Comparator<? super T> comparator) {
        if (comparator == null && !(upperBound instanceof Comparable)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(RangeType.LESS_THAN, upperBound, null, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> atMost(@NotNull T upperBound) {
        return atMost(upperBound, null);
    }

    public static <T> @NotNull Range<T> atMost(T upperBound, Comparator<? super T> comparator) {
        if (comparator == null && !(upperBound instanceof Comparable)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(RangeType.AT_MOST, upperBound, null, comparator);
    }

    @Override
    public @NotNull RangeType getType() {
        return type;
    }

    public T getLowerBound() {
        if (!hasLowerBound()) {
            throw new UnsupportedOperationException();
        }
        return lowerBound;
    }

    public T getUpperBound() {
        if (!hasUpperBound()) {
            throw new UnsupportedOperationException();
        }
        return upperBound;
    }

    public Comparator<? super T> getComparator() {
        return comparator;
    }

    public boolean isEmpty() {
        if (type == RangeType.EMPTY) {
            return true;
        }

        BoundType lowerBoundType = type.getLowerBoundType();
        BoundType upperBoundType = type.getUpperBoundType();

        if ((lowerBoundType == BoundType.CLOSED && upperBoundType == BoundType.CLOSED)
                || lowerBoundType == BoundType.INFINITY
                || upperBoundType == BoundType.INFINITY) {
            return false;
        }

        return ComparableUtils.compare(lowerBound, upperBound, comparator) == 0;
    }

    public boolean contains(T value) {
        if (type == RangeType.EMPTY) {
            return false;
        }
        if (type == RangeType.ALL) {
            return true;
        }

        BoundType lowerBoundType = type.getLowerBoundType();
        BoundType upperBoundType = type.getUpperBoundType();

        switch (lowerBoundType) {
            case OPEN:
                if (ComparableUtils.compare(lowerBound, value, comparator) >= 0) {
                    return false;
                }
                break;
            case CLOSED:
                if (ComparableUtils.compare(lowerBound, value, comparator) > 0) {
                    return false;
                }
                break;
        }

        switch (upperBoundType) {
            case OPEN:
                if (ComparableUtils.compare(upperBound, value, comparator) <= 0) {
                    return false;
                }
                break;
            case CLOSED:
                if (ComparableUtils.compare(upperBound, value, comparator) < 0) {
                    return false;
                }
                break;
        }

        return true;
    }

    public WithStep<T> withStep(@NotNull UnaryOperator<T> step) {
        Objects.requireNonNull(step);
        if (getLowerBoundType() == BoundType.INFINITY) {
            throw new UnsupportedOperationException();
        }
        return new WithStep<>(this, step);
    }

    @Override
    public int hashCode() {
        int result = comparator == null ? Comparator.naturalOrder().hashCode() : comparator.hashCode();

        result = result * 31 + type.hashCode();
        result = result * 31 + Objects.hashCode(lowerBound);
        result = result * 31 + Objects.hashCode(upperBound);

        return result + HASH_MAGIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Range)) {
            return false;
        }
        Range<?> other = (Range<?>) o;

        return type == other.type
                && ComparableUtils.comparatorEquals(comparator, other.comparator)
                && Objects.equals(lowerBound, other.lowerBound)
                && Objects.equals(upperBound, other.upperBound);
    }

    @Override
    public String toString() {
        if (this == EMPTY) {
            return "Range.Empty";
        }
        if (this == ALL) {
            return "Range.All";
        }

        BoundType lowerBoundType = type.getLowerBoundType();
        BoundType upperBoundType = type.getUpperBoundType();

        StringBuilder res = new StringBuilder();
        res.append("Range");

        switch (lowerBoundType) {
            case OPEN:
                res.append('(').append(lowerBound);
                break;
            case CLOSED:
                res.append('[').append(lowerBound);
                break;
            case INFINITY:
                res.append("(-Infinity");
                break;
        }

        res.append("..");

        switch (upperBoundType) {
            case OPEN:
                res.append(upperBound).append(')');
                break;
            case CLOSED:
                res.append(upperBound).append(']');
                break;
            case INFINITY:
                res.append("+Infinity)");
                break;
        }
        return res.toString();
    }

    public static final class WithStep<T> implements Traversable<T> {
        private final @NotNull Range<T> range;
        private final @NotNull UnaryOperator<T> step;

        WithStep(@NotNull Range<T> range, @NotNull UnaryOperator<T> step) {
            this.range = range;
            this.step = step;
        }

        @Override
        public @NotNull Iterator<T> iterator() {
            RangeType type = range.getType();
            if (type == RangeType.EMPTY) {
                return Iterators.empty();
            }

            BoundType lowerBoundType = type.getLowerBoundType();
            BoundType upperBoundType = type.getUpperBoundType();

            T initialValue = range.getLowerBound();
            if (lowerBoundType == BoundType.OPEN) {
                initialValue = step.apply(initialValue);
            }

            return new Itr(step, initialValue);
        }

        public void forEach(@NotNull Consumer<? super T> action) {
            Objects.requireNonNull(action);

            RangeType type = range.getType();

            if (type == RangeType.EMPTY) {
                return;
            }

            BoundType lowerBoundType = type.getLowerBoundType();
            BoundType upperBoundType = type.getUpperBoundType();

            final T lowerBound = range.getLowerBound();
            final T upperBound = range.getUpperBound();

            T value = lowerBound;
            if (lowerBoundType == BoundType.OPEN) {
                value = step.apply(value);
            }

            switch (upperBoundType) {
                case OPEN:
                    while (ComparableUtils.compare(value, upperBound) < 0) {
                        action.accept(value);
                        value = step.apply(value);
                    }
                    break;
                case CLOSED:
                    while (ComparableUtils.compare(value, upperBound) <= 0) {
                        action.accept(value);
                        value = step.apply(value);
                    }
                    break;
                default:
                    //noinspection InfiniteLoopStatement
                    while (true) {
                        action.accept(value);
                        value = step.apply(value);
                    }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof WithStep)) {
                return false;
            }
            WithStep<?> other = (WithStep<?>) o;
            return range.equals(other.range) && step.equals(other.step);
        }

        @Override
        public int hashCode() {
            return range.hashCode() * 31 + step.hashCode();
        }

        private final class Itr extends AbstractIterator<T> {
            private T value;
            private final @NotNull UnaryOperator<T> step;

            Itr(@NotNull UnaryOperator<T> step, T initialValue) {
                this.value = initialValue;
                this.step = step;
            }

            @Override
            public boolean hasNext() {
                switch (range.getType().getLowerBoundType()) {
                    case OPEN:
                        return ComparableUtils.compare(value, range.upperBound, range.comparator) < 0;
                    case CLOSED:
                        return ComparableUtils.compare(value, range.upperBound, range.comparator) <= 0;
                    default:
                        return true;
                }
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T res = value;
                value = step.apply(res);
                return res;
            }
        }
    }
}
