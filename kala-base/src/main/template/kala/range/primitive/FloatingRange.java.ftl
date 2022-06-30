package kala.range.primitive;

import kala.range.BoundType;
import kala.range.RangeType;
import org.jetbrains.annotations.NotNull;

public final class ${Type}Range extends FloatingRange<${WrapperType}> {
    private static final long serialVersionUID = ${SerialVersionUID};
    private static final int HASH_MAGIC = ${HashMagic};

    private static final ${Type}Range ALL = new ${Type}Range(RangeType.ALL, ${WrapperType}.NEGATIVE_INFINITY, ${WrapperType}.POSITIVE_INFINITY);
    private static final ${Type}Range EMPTY = new ${Type}Range(RangeType.EMPTY, ${WrapperType}.NaN, ${WrapperType}.NaN);

    private final @NotNull RangeType type;

    private final ${PrimitiveType} lowerBound;
    private final ${PrimitiveType} upperBound;

    // private final ${PrimitiveType} absoluteTolerance;

    private ${Type}Range(@NotNull RangeType type, ${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        this.type = type;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static @NotNull ${Type}Range all() {
        return ALL;
    }

    public static @NotNull ${Type}Range empty() {
        return EMPTY;
    }

    public static @NotNull ${Type}Range is(${PrimitiveType} value) {
        if (${WrapperType}.isNaN(value)) {
            throw new IllegalArgumentException("value should not be NaN");
        }
        if (${WrapperType}.isInfinite(value)) {
            throw new IllegalArgumentException();
        }
        return new ${Type}Range(RangeType.CLOSED, value, value);
    }

    public static @NotNull ${Type}Range open(${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        if (${WrapperType}.isNaN(lowerBound) || ${WrapperType}.isNaN(upperBound)) {
            throw new IllegalArgumentException("bound should not be NaN");
        }

        if (lowerBound >= upperBound) {
            throw new IllegalArgumentException("lowerBound should be less than upperBound");
        }
        if (lowerBound == ${WrapperType}.NEGATIVE_INFINITY) {
            if (upperBound == ${WrapperType}.POSITIVE_INFINITY) {
                return ALL;
            }
            if (upperBound == ${WrapperType}.MIN_VALUE) {
                return EMPTY;
            }
            return new ${Type}Range(RangeType.LESS_THAN, ${WrapperType}.NEGATIVE_INFINITY, upperBound);
        }

        if (upperBound == ${WrapperType}.POSITIVE_INFINITY) {
            if (lowerBound == ${WrapperType}.MAX_VALUE) {
                return EMPTY;
            }
            return new ${Type}Range(RangeType.GREATER_THAN, lowerBound, ${WrapperType}.POSITIVE_INFINITY);
        }

        return new ${Type}Range(RangeType.OPEN, lowerBound, upperBound);
    }

    public static @NotNull ${Type}Range closed(${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        if (${WrapperType}.isNaN(lowerBound) || ${WrapperType}.isNaN(upperBound)) {
            throw new IllegalArgumentException("bound should not be NaN");
        }
        if (${WrapperType}.isInfinite(lowerBound) || ${WrapperType}.isInfinite(upperBound)) {
            throw new IllegalArgumentException();
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("lowerBound should be less than or equal to upperBound");
        }

        return new ${Type}Range(RangeType.CLOSED, lowerBound, upperBound);
    }

    public static @NotNull ${Type}Range openClosed(${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        if (${WrapperType}.isNaN(lowerBound) || ${WrapperType}.isNaN(upperBound)) {
            throw new IllegalArgumentException("bound should not be NaN");
        }
        if (${WrapperType}.isInfinite(upperBound)) {
            throw new IllegalArgumentException();
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("lowerBound should be less than or equal to upperBound");
        }

        if (lowerBound == ${WrapperType}.NEGATIVE_INFINITY) {
            return new ${Type}Range(RangeType.AT_MOST, ${WrapperType}.NEGATIVE_INFINITY, upperBound);
        }

        return new ${Type}Range(RangeType.OPEN_CLOSED, lowerBound, upperBound);
    }

    public static @NotNull ${Type}Range closedOpen(${PrimitiveType} lowerBound, ${PrimitiveType} upperBound) {
        if (${WrapperType}.isNaN(lowerBound) || ${WrapperType}.isNaN(upperBound)) {
            throw new IllegalArgumentException("bound should not be NaN");
        }
        if (${WrapperType}.isInfinite(lowerBound)) {
            throw new IllegalArgumentException();
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("lowerBound should be less than or equal to upperBound");
        }

        if (upperBound == ${WrapperType}.POSITIVE_INFINITY) {
            return new ${Type}Range(RangeType.AT_LEAST, lowerBound, ${WrapperType}.POSITIVE_INFINITY);
        }

        return new ${Type}Range(RangeType.CLOSED_OPEN, lowerBound, upperBound);
    }

    public static @NotNull ${Type}Range greaterThan(${PrimitiveType} lowerBound) {
        if (${WrapperType}.isNaN(lowerBound)) {
            throw new IllegalArgumentException("lowerBound should not be NaN");
        }
        if (lowerBound == ${WrapperType}.NEGATIVE_INFINITY) {
            return ALL;
        }
        if (lowerBound == ${WrapperType}.POSITIVE_INFINITY) {
            return EMPTY;
        }
        return new ${Type}Range(RangeType.GREATER_THAN, lowerBound, ${WrapperType}.POSITIVE_INFINITY);
    }

    public static @NotNull ${Type}Range atLeast(${PrimitiveType} lowerBound) {
        if (${WrapperType}.isNaN(lowerBound)) {
            throw new IllegalArgumentException("lowerBound should not be NaN");
        }
        if (${WrapperType}.isInfinite(lowerBound)) {
            throw new IllegalArgumentException();
        }
        return new ${Type}Range(RangeType.AT_LEAST, lowerBound, ${WrapperType}.POSITIVE_INFINITY);
    }

    public static @NotNull ${Type}Range lessThan(${PrimitiveType} upperBound) {
        if (${WrapperType}.isNaN(upperBound)) {
            throw new IllegalArgumentException("upperBound should not be NaN");
        }
        if (upperBound == ${WrapperType}.POSITIVE_INFINITY) {
            return ALL;
        }
        if (upperBound == ${WrapperType}.NEGATIVE_INFINITY) {
            return EMPTY;
        }

        return new ${Type}Range(RangeType.LESS_THAN, ${WrapperType}.NEGATIVE_INFINITY, upperBound);
    }

    public static @NotNull ${Type}Range atMost(${PrimitiveType} upperBound) {
        if (${WrapperType}.isNaN(upperBound)) {
            throw new IllegalArgumentException("upperBound should not be NaN");
        }
        if (${WrapperType}.isInfinite(upperBound)) {
            throw new IllegalArgumentException();
        }
        return new ${Type}Range(RangeType.AT_MOST, ${WrapperType}.NEGATIVE_INFINITY, upperBound);
    }

    @Override
    public @NotNull RangeType getType() {
        return type;
    }

    public ${PrimitiveType} getLowerBound() {
        if (!hasLowerBound()) {
            throw new UnsupportedOperationException();
        }
        return lowerBound;
    }

    public ${PrimitiveType} getUpperBound() {
        if (!hasUpperBound()) {
            throw new UnsupportedOperationException();
        }
        return upperBound;
    }

    boolean contains(${PrimitiveType} value) {
        if (!${WrapperType}.isFinite(value) || type == RangeType.EMPTY) {
            return false;
        }

        BoundType lowerBoundType = type.getLowerBoundType();
        BoundType upperBoundType = type.getUpperBoundType();

        switch (lowerBoundType) {
            case OPEN:
                if (lowerBound >= value) {
                    return false;
                }
                break;
            case CLOSED:
                if (lowerBound > value) {
                    return false;
                }
                break;
        }

        switch (upperBoundType) {
            case OPEN:
                if (upperBound <= value) {
                    return false;
                }
                break;
            case CLOSED:
                if (upperBound < value) {
                    return false;
                }
                break;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = result * 31 + ${WrapperType}.hashCode(lowerBound);
        result = result * 31 + ${WrapperType}.hashCode(upperBound);
        return result + HASH_MAGIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ${Type}Range)) {
            return false;
        }
        ${Type}Range other = (${Type}Range) o;
        return type == other.type
                && ${PrimitiveEquals("lowerBound", "other.lowerBound")}
                && ${PrimitiveEquals("upperBound", "other.upperBound")};
    }

    @Override
    public String toString() {
        if (this == EMPTY) {
            return "${Type}Range.Empty";
        }
        if (this == ALL) {
            return "${Type}Range.All";
        }

        BoundType lowerBoundType = type.getLowerBoundType();
        BoundType upperBoundType = type.getUpperBoundType();

        StringBuilder res = new StringBuilder(32);
        res.append("${Type}Range");

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
