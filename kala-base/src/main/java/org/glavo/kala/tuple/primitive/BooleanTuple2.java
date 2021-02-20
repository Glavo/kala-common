package org.glavo.kala.tuple.primitive;

import org.glavo.kala.tuple.Tuple;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

public final class BooleanTuple2 implements PrimitiveTuple {
    private static final long serialVersionUID = 3436771981901568081L;
    private static final int HASH_MAGIC = -1290448591;

    public final boolean _1;
    public final boolean _2;

    public BooleanTuple2(boolean i1, boolean i2) {
        _1 = i1;
        _2 = i2;
    }

    @Override
    public final int arity() {
        return 2;
    }

    public final boolean component1() {
        return _1;
    }

    public final boolean component2() {
        return _2;
    }

    public final @NotNull Tuple2<@NotNull Boolean, @NotNull Boolean> toTuple2() {
        return Tuple.of(_1, _2);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BooleanTuple2)) {
            return false;
        }
        BooleanTuple2 that = (BooleanTuple2) o;
        return _1 == that._1 &&
                _2 == that._2;
    }

    @Override
    public final int hashCode() {
        return 31 * Boolean.hashCode(_1) + Boolean.hashCode(_2) + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "BooleanTuple2(" + _1 + ", " + _2 + ")";
    }
}
