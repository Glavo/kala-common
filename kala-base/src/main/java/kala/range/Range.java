package kala.range;

import kala.internal.ComparableUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Comparator;

public final class Range<T> implements AnyRange<T>, Serializable {
    private static final long serialVersionUID = 4151410859736356449L;

    private static final Range<?> EMPTY = new Range<>(null, null, RangeType.CLOSED_OPEN);
    private static final Range<?> ALL = new Range<>(null, null, RangeType.ALL);

    private final T lowerBound;
    private final T upperBound;

    private final RangeType type;
    private final @Nullable Comparator<? super T> comparator;

    private Range(T lowerBound, T upperBound, RangeType type) {
        this(lowerBound, upperBound, type, null);
    }

    private Range(T lowerBound, T upperBound, RangeType type, @Nullable Comparator<? super T> comparator) {
        this.type = type;
        this.comparator = comparator;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
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
        return new Range<>(value, value, RangeType.CLOSED, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> open(@NotNull T lowerBound, @NotNull T upperBound) {
        return open(lowerBound, upperBound, null);
    }

    public static <T> @NotNull Range<T> open(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        if (!(ComparableUtils.compare(lowerBound, upperBound, comparator) >= 0)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(lowerBound, upperBound, RangeType.OPEN, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> closed(@NotNull T lowerBound, @NotNull T upperBound) {
        return closed(lowerBound, upperBound, null);
    }

    public static <T> @NotNull Range<T> closed(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        if (!(ComparableUtils.compare(lowerBound, upperBound, comparator) > 0)) {
            throw new IllegalArgumentException();
        }
        return new Range<>(lowerBound, upperBound, RangeType.CLOSED, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> openClosed(@NotNull T lowerBound, @NotNull T upperBound) {
        return openClosed(lowerBound, upperBound, null);
    }

    public static <T> @NotNull Range<T> openClosed(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        if (!(ComparableUtils.compare(lowerBound, upperBound, comparator) > 0)) {
            throw new IllegalArgumentException();
        }
        return new Range<>(lowerBound, upperBound, RangeType.OPEN_CLOSED, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> closedOpen(@NotNull T lowerBound, @NotNull T upperBound) {
        return closedOpen(lowerBound, upperBound, null);
    }

    public static <T> @NotNull Range<T> closedOpen(T lowerBound, T upperBound, Comparator<? super T> comparator) {
        if (!(ComparableUtils.compare(lowerBound, upperBound, comparator) > 0)) {
            throw new IllegalArgumentException();
        }
        return new Range<>(lowerBound, upperBound, RangeType.CLOSED_OPEN, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> greaterThan(@NotNull T lowerBound) {
        return greaterThan(lowerBound, null);
    }

    public static <T> @NotNull Range<T> greaterThan(T lowerBound, Comparator<? super T> comparator) {
        if (comparator == null && !(lowerBound instanceof Comparable)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(lowerBound, null, RangeType.GREATER_THAN, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> atLeast(@NotNull T lowerBound) {
        return atLeast(lowerBound, null);
    }

    public static <T> @NotNull Range<T> atLeast(T lowerBound, Comparator<? super T> comparator) {
        if (comparator == null && !(lowerBound instanceof Comparable)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(lowerBound, null, RangeType.AT_LEAST, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> lessThan(@NotNull T upperBound) {
        return lessThan(upperBound, null);
    }

    public static <T> @NotNull Range<T> lessThan(T upperBound, Comparator<? super T> comparator) {
        if (comparator == null && !(upperBound instanceof Comparable)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(upperBound, null, RangeType.LESS_THAN, comparator);
    }

    public static <T extends Comparable<? super T>> @NotNull Range<T> atMost(@NotNull T upperBound) {
        return atMost(upperBound, null);
    }

    public static <T> @NotNull Range<T> atMost(T upperBound, Comparator<? super T> comparator) {
        if (comparator == null && !(upperBound instanceof Comparable)) {
            throw new IllegalArgumentException();
        }

        return new Range<>(upperBound, null, RangeType.AT_MOST, comparator);
    }

    public T getLowerBound() {
        return lowerBound;
    }

    public T getUpperBound() {
        return upperBound;
    }

    @Override
    public RangeType getType() {
        return type;
    }

    public Comparator<? super T> getComparator() {
        return comparator;
    }

    public boolean contains(T value) {
        if (this == EMPTY) {
            return false;
        }
        if (this == ALL) {
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

    @Override
    public String toString() {
        if (this == EMPTY) {
            return "Range.Empty";
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
}
