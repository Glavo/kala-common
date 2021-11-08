package kala.range;

public interface AnyRange<T> {
    RangeType getType();

    default BoundType getLowerBoundType() {
        return getType().getLowerBoundType();
    }

    default BoundType getUpperBoundType() {
        return getType().getUpperBoundType();
    }

    default boolean hasLowerBound() {
        return getType() != RangeType.EMPTY && getLowerBoundType() != BoundType.INFINITY;
    }

    default boolean hasUpperBound() {
        return getType() != RangeType.EMPTY && getUpperBoundType() != BoundType.INFINITY;
    }
}
