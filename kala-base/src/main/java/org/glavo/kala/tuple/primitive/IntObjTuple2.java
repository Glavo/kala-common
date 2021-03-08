package org.glavo.kala.tuple.primitive;

import org.glavo.kala.annotations.DeprecatedReplaceWith;
import org.glavo.kala.tuple.Tuple;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class IntObjTuple2<T> implements PrimitiveTuple {
    private static final long serialVersionUID = 5346453005675203380L;
    private static final int HASH_MAGIC = -1381225368;

    public final int _1;
    public final T _2;

    @Deprecated
    @DeprecatedReplaceWith("of(i, t)")
    @SuppressWarnings("DeprecatedIsStillUsed")
    public IntObjTuple2(int i, T t) {
        _1 = i;
        _2 = t;
    }

    public static <T> @NotNull IntObjTuple2<T> of(int i, T t) {
        return new IntObjTuple2<>(i, t);
    }

    @Override
    public final int arity() {
        return 2;
    }

    public final int component1() {
        return _1;
    }

    public final T component2() {
        return _2;
    }

    public final @NotNull Tuple2<@NotNull Integer, T> toTuple2() {
        return Tuple.of(_1, _2);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntObjTuple2<?>)) {
            return false;
        }
        IntObjTuple2<?> that = (IntObjTuple2<?>) o;
        return _1 == that._1 &&
                Objects.equals(_2, that._2);
    }

    @Override
    public final int hashCode() {
        return 31 * _1 + Objects.hashCode(_2) + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "IntObjTuple2(" + _1 + ", " + _2 + ")";
    }
}
