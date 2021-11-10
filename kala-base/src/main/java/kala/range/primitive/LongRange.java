package kala.range.primitive;

import kala.annotations.UnstableName;
import kala.collection.base.primitive.AbstractLongIterator;
import kala.collection.base.primitive.LongIterator;
import kala.collection.base.primitive.LongTraversable;
import kala.range.BoundType;
import kala.range.RangeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public final class LongRange extends IntegralRange<Long> implements LongTraversable, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 0;

    private static final long DEFAULT_STEP = 1L;

    private static final LongRange ALL = new LongRange(RangeType.CLOSED, Long.MIN_VALUE, Long.MAX_VALUE);
    private static final LongRange EMPTY = new LongRange(RangeType.EMPTY, 0, 0);

    private final @NotNull RangeType type;

    private final long lowerBound;
    private final long upperBound;

    private LongRange(@NotNull RangeType type, long lowerBound, long upperBound) {
        this.type = type;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static @NotNull LongRange all() {
        return ALL;
    }

    public static @NotNull LongRange empty() {
        return EMPTY;
    }

    public static @NotNull LongRange is(long value) {
        return new LongRange(RangeType.CLOSED, value, value);
    }

    public static @NotNull LongRange open(long lowerBound, long upperBound) {
        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException();
        }
        return new LongRange(RangeType.OPEN, lowerBound, upperBound);
    }

    public static @NotNull LongRange closed(long lowerBound, long upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException();
        }
        return new LongRange(RangeType.CLOSED, lowerBound, upperBound);
    }

    public static @NotNull LongRange openClosed(long lowerBound, long upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException();
        }
        return new LongRange(RangeType.OPEN_CLOSED, lowerBound, upperBound);
    }

    public static @NotNull LongRange closedOpen(long lowerBound, long upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException();
        }
        return new LongRange(RangeType.CLOSED_OPEN, lowerBound, upperBound);
    }

    public static @NotNull LongRange greaterThan(long lowerBound) {
        return new LongRange(RangeType.OPEN_CLOSED, lowerBound, Long.MAX_VALUE);
    }

    public static @NotNull LongRange atLeast(long lowerBound) {
        return new LongRange(RangeType.CLOSED, lowerBound, Long.MAX_VALUE);
    }

    public static @NotNull LongRange lessThan(long upperBound) {
        return new LongRange(RangeType.CLOSED_OPEN, Long.MIN_VALUE, upperBound);
    }

    public static @NotNull LongRange atMost(long upperBound) {
        return new LongRange(RangeType.CLOSED, Long.MIN_VALUE, upperBound);
    }

    @Override
    public @NotNull RangeType getType() {
        return type;
    }

    public long getLowerBound() {
        if (!hasLowerBound()) {
            throw new UnsupportedOperationException();
        }
        return lowerBound;
    }

    public long getUpperBound() {
        if (!hasUpperBound()) {
            throw new UnsupportedOperationException();
        }
        return upperBound;
    }

    private long strictLowerBound() {
        // assert this.isNotEmpty();

        return type.getLowerBoundType() == BoundType.OPEN
                ? lowerBound + 1
                : lowerBound;
    }

    private long strictUpperBound() {
        // assert this.isNotEmpty();

        return type.getUpperBoundType() == BoundType.OPEN
                ? upperBound - 1
                : upperBound;
    }

    @UnstableName
    public long fit(long value) {
        if (isEmpty()) {
            throw new UnsupportedOperationException("Range is empty");
        }

        final long strictLowerBound = strictLowerBound();
        final long strictUpperBound = strictUpperBound();

        if (strictLowerBound >= value) {
            return strictLowerBound;
        }

        //noinspection ManualMinMaxCalculation
        if (strictUpperBound <= value) {
            return strictUpperBound;
        }

        return value;
    }

    public boolean isEmpty() {
        return type == RangeType.EMPTY
                || (lowerBound == upperBound && type.getLowerBoundType() != type.getUpperBoundType());
    }

    public boolean contains(long value) {
        if (this == ALL) {
            return true;
        }
        if (isEmpty()) {
            return false;
        }

        return value >= strictLowerBound() && value <= strictUpperBound();
    }

    @Override
    public @NotNull LongIterator iterator() {
        if (isEmpty()) {
            return LongIterator.empty();
        }
        long strictLowerBound = strictLowerBound();
        long strictUpperBound = strictUpperBound();

        if (strictLowerBound == strictUpperBound) {
            return LongIterator.of(strictLowerBound);
        }

        return new PositiveItr(strictUpperBound, DEFAULT_STEP, strictLowerBound);
    }

    @Override
    public void forEachPrimitive(@NotNull LongConsumer action) {
        forEachByStep(DEFAULT_STEP, action);
    }

    void forEachByStep(long step, @NotNull LongConsumer action) {
        Objects.requireNonNull(action);
        if (step == 0) {
            throw new IllegalArgumentException("step mush not be zero");
        }
        if (isEmpty()) {
            return;
        }

        final long strictLowerBound = strictLowerBound();
        final long strictUpperBound = strictUpperBound();

        if (step > 0) {
            long value = strictLowerBound;
            while (value <= strictUpperBound) {
                action.accept(value);
                if (Long.MAX_VALUE - step < value) {
                    break;
                }
                value += step;
            }
        } else {
            long value = strictUpperBound;
            while (value >= strictLowerBound) {
                action.accept(value);
                if (Long.MIN_VALUE - step > value) {
                    break;
                }
                value += step;
            }
        }
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = result * 31 + Long.hashCode(lowerBound);
        result = result * 31 + Long.hashCode(upperBound);
        return result + HASH_MAGIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LongRange)) {
            return false;
        }
        LongRange longRange = (LongRange) o;
        return lowerBound == longRange.lowerBound && upperBound == longRange.upperBound && type == longRange.type;
    }

    private static String prettyToString(long value) {
        if (value == Long.MAX_VALUE) {
            return "Long.MAX_VALUE";
        }
        if (value == Long.MIN_VALUE) {
            return "Long.MIN_VALUE";
        }
        return String.valueOf(value);
    }

    @Override
    public String toString() {
        if (this == EMPTY) {
            return "LongRange.Empty";
        }
        if (this == ALL) {
            return "LongRange.All";
        }

        BoundType lowerBoundType = type.getLowerBoundType();
        BoundType upperBoundType = type.getUpperBoundType();

        StringBuilder res = new StringBuilder(32);
        res.append("LongRange");

        switch (lowerBoundType) {
            case OPEN:
                res.append('(');
                break;
            case CLOSED:
                res.append('[');
                break;
            case INFINITY:
                throw new AssertionError();
        }

        res.append(prettyToString(lowerBound)).append("..").append(prettyToString(upperBound));

        switch (upperBoundType) {
            case OPEN:
                res.append(')');
                break;
            case CLOSED:
                res.append(']');
                break;
            case INFINITY:
                throw new AssertionError();
        }
        return res.toString();
    }

    private static final class PositiveItr extends AbstractLongIterator {
        private long upperBound;
        private final @Range(from = 1, to = Long.MAX_VALUE) long step;

        private long value;

        PositiveItr(long upperBound, long step, long initialValue) {
            this.upperBound = upperBound;
            this.step = step;
            this.value = initialValue;
        }

        @Override
        public boolean hasNext() {
            return value <= upperBound;
        }

        @Override
        public long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final long res = value;
            if (Long.MAX_VALUE - step < value) {
                upperBound = Long.MIN_VALUE;
                value = Long.MAX_VALUE;
            } else {
                value += step;
            }
            return res;
        }
    }

    private static final class ReverseItr extends AbstractLongIterator {
        private long lowerBound;
        private final @Range(from = Long.MIN_VALUE, to = -1) long step;

        private long value;

        private ReverseItr(long lowerBound, @Range(from = Long.MIN_VALUE, to = -1) long step, long initialValue) {
            this.lowerBound = lowerBound;
            this.step = step;
            this.value = initialValue;
        }

        @Override
        public boolean hasNext() {
            return value >= lowerBound;
        }

        @Override
        public long nextLong() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final long res = this.value;
            if (Long.MIN_VALUE - step > value) {
                lowerBound = Long.MAX_VALUE;
                value = Long.MIN_VALUE;
            } else {
                value += step;
            }
            return res;
        }
    }
}
