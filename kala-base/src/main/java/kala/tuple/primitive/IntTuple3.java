package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class IntTuple3 implements PrimitiveTuple {
    private static final long serialVersionUID = 6568634810148911056L;

    public final int _1;
    public final int _2;
    public final int _3;

    private IntTuple3(int i1, int i2, int i3) {
        _1 = i1;
        _2 = i2;
        _3 = i3;
    }

    public static @NotNull IntTuple3 of(int i1, int i2, int i3) {
        return new IntTuple3(i1, i2, i3);
    }

    @Override
    public int arity() {
        return 3;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U elementAt(int index) {
        switch (index) {
            case 0:
                return (U) Integer.valueOf(_1);
            case 1:
                return (U) Integer.valueOf(_2);
            case 2:
                return (U) Integer.valueOf(_3);
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

    public int component3() {
        return _3;
    }

    public @NotNull Tuple3<@NotNull Integer, @NotNull Integer, @NotNull Integer> toTuple3() {
        return Tuple.of(_1, _2, _3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof IntTuple3) {
            IntTuple3 other = (IntTuple3) o;
            return _1 == other._1 && _2 == other._2 && this._3 == other._3;
        }

        if (o instanceof AnyTuple) {
            AnyTuple other = (AnyTuple) o;
            return other.arity() == 3
                    && Conditions.equals(_1, other.elementAt(0))
                    && Conditions.equals(_2, other.elementAt(1))
                    && Conditions.equals(_3, other.elementAt(2));
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash = 31 * hash + _1;
        hash = 31 * hash + _2;
        hash = 31 * hash + _3;
        return hash + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "IntTuple3(" + _1 + ", " + _2 + ", " + _3 + ")" ;
    }
}
