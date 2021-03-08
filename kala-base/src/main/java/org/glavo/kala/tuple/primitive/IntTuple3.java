package org.glavo.kala.tuple.primitive;

import org.glavo.kala.annotations.DeprecatedReplaceWith;
import org.glavo.kala.tuple.Tuple;
import org.glavo.kala.tuple.Tuple3;
import org.jetbrains.annotations.NotNull;

public final class IntTuple3 implements PrimitiveTuple {
    private static final long serialVersionUID = 6568634810148911056L;
    private static final int HASH_MAGIC = -1472170394;

    public final int _1;
    public final int _2;
    public final int _3;

    @Deprecated
    @DeprecatedReplaceWith("of(i1, i2, i3)")
    @SuppressWarnings("DeprecatedIsStillUsed")
    public IntTuple3(int i1, int i2, int i3) {
        _1 = i1;
        _2 = i2;
        _3 = i3;
    }

    public static @NotNull IntTuple3 of(int i1, int i2, int i3) {
        return new IntTuple3(i1, i2, i3);
    }

    @Override
    public final int arity() {
        return 3;
    }

    public final int component1() {
        return _1;
    }

    public final int component2() {
        return _2;
    }

    public final int component3() {
        return _3;
    }

    public final @NotNull Tuple3<@NotNull Integer, @NotNull Integer, @NotNull Integer> toTuple3() {
        return Tuple.of(_1, _2, _3);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntTuple3)) {
            return false;
        }
        IntTuple3 other = (IntTuple3) o;
        return _1 == other._1 &&
                _2 == other._2 &&
                _3 == other._3;
    }

    @Override
    public final int hashCode() {
        int hash = 0;
        hash = 31 * hash + _1;
        hash = 31 * hash + _2;
        hash = 31 * hash + _3;
        return hash + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "IntTuple3(" + _1 + ", " + _2 + ", " + _3 + ")";
    }
}
