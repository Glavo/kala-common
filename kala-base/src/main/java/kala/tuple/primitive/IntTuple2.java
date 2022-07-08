package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class IntTuple2 implements PrimitiveTuple, Map.Entry<Integer, Integer> {
    private static final long serialVersionUID = 2586221246308875196L;

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

    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        switch (index) {
            case 0:
                return (U) Integer.valueOf(_1);
            case 1:
                return (U) Integer.valueOf(_2);
            default:
                throw new IndexOutOfBoundsException();
        }
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
    public Integer getKey() {
        return _1;
    }

    @Override
    public Integer getValue() {
        return _2;
    }

    @Override
    public Integer setValue(Integer value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof IntTuple2) {
            IntTuple2 other = (IntTuple2) o;
            return _1 == other._1 && _2 == other._2;
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
        return Integer.hashCode(_1) ^ Integer.hashCode(_2);
    }

    @Override
    public String toString() {
        return "IntTuple2(" + _1 + ", " + _2 + ")" ;
    }
}
