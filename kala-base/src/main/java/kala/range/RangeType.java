package kala.range;

public enum RangeType {
    OPEN(BoundType.OPEN, BoundType.OPEN),
    CLOSED(BoundType.CLOSED, BoundType.CLOSED),
    OPEN_CLOSED(BoundType.OPEN, BoundType.CLOSED),
    CLOSED_OPEN(BoundType.CLOSED, BoundType.OPEN),
    GREATER_THAN(BoundType.OPEN, BoundType.INFINITY),
    AT_LEAST(BoundType.CLOSED, BoundType.INFINITY),
    LESS_THAN(BoundType.INFINITY, BoundType.OPEN),
    AT_MOST(BoundType.INFINITY, BoundType.CLOSED),
    EMPTY(BoundType.OPEN, BoundType.OPEN),
    ALL(BoundType.INFINITY, BoundType.INFINITY);

    private final BoundType lowerBoundType;
    private final BoundType upperBoundType;

    RangeType(BoundType lowerBoundType, BoundType upperBoundType) {
        this.lowerBoundType = lowerBoundType;
        this.upperBoundType = upperBoundType;
    }

    public BoundType getLowerBoundType() {
        return lowerBoundType;
    }

    public BoundType getUpperBoundType() {
        return upperBoundType;
    }
}
