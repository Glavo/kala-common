package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public final class IntObjTuple2<T> implements PrimitiveTuple {
    private static final long serialVersionUID = 5346453005675203380L;

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

    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        switch (index) {
            case 0:
                return (U) Integer.valueOf(_1);
            case 1:
                return (U) _2;
            default:
                throw new IndexOutOfBoundsException();
        }
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
        if (this == o) return true;

        if (o instanceof IntObjTuple2) {
            IntObjTuple2<?> other = (IntObjTuple2<?>) o;
            return _1 == other._1 && Objects.equals(_2, other._2);
        }

        if (o instanceof Map.Entry) {
            Map.Entry<?, ?> other = (Map.Entry<?, ?>) o;
            return Conditions.equals(_1, other.getKey()) && Conditions.equals(_2, other.getValue());
        }

        if (o instanceof AnyTuple) {
            AnyTuple other = (AnyTuple) o;
            return other.arity() == 2
                    && Conditions.equals(_1, other.elementAt(0))
                    && Conditions.equals(_2, other.elementAt(1));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(_1) ^ Objects.hashCode(_2);
    }

    @Override
    public String toString() {
        return "IntObjTuple2(" + _1 + ", " + _2 + ")";
    }
}
