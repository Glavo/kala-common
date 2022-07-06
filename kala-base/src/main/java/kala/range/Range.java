package kala.range;

import org.jetbrains.annotations.NotNull;

public abstract class Range<T> {
    public abstract @NotNull RangeType getType();

    public final BoundType getLowerBoundType() {
        return getType().getLowerBoundType();
    }

    public final BoundType getUpperBoundType() {
        return getType().getUpperBoundType();
    }

    public final boolean hasLowerBound() {
        return getType() != RangeType.EMPTY && getLowerBoundType() != BoundType.INFINITY;
    }

    public final boolean hasUpperBound() {
        return getType() != RangeType.EMPTY && getUpperBoundType() != BoundType.INFINITY;
    }
}
