package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.NotNull;

public record IntTuple3(int component1, int component2, int component3) implements PrimitiveTuple {
    private static final long serialVersionUID = 0L;

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
                return (U) Integer.valueOf(component1);
            case 1:
                return (U) Integer.valueOf(component2);
            case 2:
                return (U) Integer.valueOf(component3);
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public @NotNull Tuple3<@NotNull Integer, @NotNull Integer, @NotNull Integer> toTuple3() {
        return Tuple.of(component1, component2, component3);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof IntTuple3) {
            IntTuple3 other = (IntTuple3) o;
            return component1 == other.component1 && component2 == other.component2 && this.component3 == other.component3;
        }

        if (o instanceof AnyTuple) {
            AnyTuple other = (AnyTuple) o;
            return other.arity() == 3
                    && Conditions.equals(component1, other.elementAt(0))
                    && Conditions.equals(component2, other.elementAt(1))
                    && Conditions.equals(component3, other.elementAt(2));
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash = 31 * hash + component1;
        hash = 31 * hash + component2;
        hash = 31 * hash + component3;
        return hash;
    }

    @Override
    public String toString() {
        return "IntTuple3(" + component1 + ", " + component2 + ", " + component3 + ")" ;
    }
}
