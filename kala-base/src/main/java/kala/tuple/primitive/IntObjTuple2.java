package kala.tuple.primitive;

import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class IntObjTuple2<T> implements PrimitiveTuple {
    private static final long serialVersionUID = 5346453005675203380L;
    private static final int HASH_MAGIC = -1381225368;

    public final int _1;
    public final T _2;

    private IntObjTuple2(int i, T t) {
        _1 = i;
        _2 = t;
    }

    public static <T> @NotNull IntObjTuple2<T> of(int i, T t) {
        return new IntObjTuple2<>(i, t);
    }

    @Override
    public int arity() {
        return 2;
    }

    public int component1() {
        return _1;
    }

    public T component2() {
        return _2;
    }

    public @NotNull Tuple2<@NotNull Integer, T> toTuple2() {
        return Tuple.of(_1, _2);
    }

    @Override
    public boolean equals(Object o) {
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
    public int hashCode() {
        return 31 * _1 + Objects.hashCode(_2) + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "IntObjTuple2(" + _1 + ", " + _2 + ")";
    }
}
