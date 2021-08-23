package kala.tuple.primitive;

import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

public final class IntTuple2 implements PrimitiveTuple {
    private static final long serialVersionUID = 2586221246308875196L;
    private static final int HASH_MAGIC = -1464965121;

    public final int _1;
    public final int _2;

    private IntTuple2(int i1, int i2) {
        _1 = i1;
        _2 = i2;
    }

    public static @NotNull IntTuple2 of(int i1, int i2) {
        return new IntTuple2(i1, i2);
    }

    @Override
    public int arity() {
        return 2;
    }

    public int component1() {
        return _1;
    }

    public int component2() {
        return _2;
    }

    public @NotNull Tuple2<@NotNull Integer, @NotNull Integer> toTuple2() {
        return Tuple.of(_1, _2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntTuple2)) {
            return false;
        }
        IntTuple2 that = (IntTuple2) o;
        return _1 == that._1 &&
                _2 == that._2;
    }

    @Override
    public int hashCode() {
        return 31 * _1 + _2 + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "IntTuple2(" + _1 + ", " + _2 + ")";
    }
}
