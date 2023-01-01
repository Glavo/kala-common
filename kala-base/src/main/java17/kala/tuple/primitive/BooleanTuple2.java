package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record BooleanTuple2(boolean component1, boolean component2) implements Map.Entry<Boolean, Boolean>, PrimitiveTuple {
    private static final long serialVersionUID = 0L;

    private static final BooleanTuple2 TT = new BooleanTuple2(true, true);
    private static final BooleanTuple2 TF = new BooleanTuple2(true, false);
    private static final BooleanTuple2 FT = new BooleanTuple2(false, true);
    private static final BooleanTuple2 FF = new BooleanTuple2(false, false);

    private static final String TT_S = "BooleanTuple2(true, true)" ;
    private static final String TF_S = "BooleanTuple2(true, false)" ;
    private static final String FT_S = "BooleanTuple2(false, true)" ;
    private static final String FF_S = "BooleanTuple2(false, false)" ;

    public static @NotNull BooleanTuple2 of(boolean b1, boolean b2) {
        if (b1) {
            return b2 ? TT : TF;
        } else {
            return b2 ? FT : FF;
        }
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
                return (U) Boolean.valueOf(component1);
            case 1:
                return (U) Boolean.valueOf(component2);
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public @NotNull Tuple2<@NotNull Boolean, @NotNull Boolean> toTuple2() {
        return Tuple.of(component1, component2);
    }

    @Override
    public Boolean getKey() {
        return component1;
    }

    @Override
    public Boolean getValue() {
        return component2;
    }

    @Override
    public Boolean setValue(Boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof BooleanTuple2) {
            BooleanTuple2 other = (BooleanTuple2) o;
            return component1 == other.component1 && component2 == other.component2;
        }

        if (o instanceof Map.Entry) {
            Map.Entry<?, ?> other = (Map.Entry<?, ?>) o;
            return Conditions.equals(component1, other.getKey()) && Conditions.equals(component2, other.getValue());
        }

        if (o instanceof AnyTuple) {
            AnyTuple other = (AnyTuple) o;
            return other.arity() == 2
                    && Conditions.equals(component1, other.elementAt(0))
                    && Conditions.equals(component2, other.elementAt(1));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(component1) ^ Boolean.hashCode(component2);
    }

    @Override
    public String toString() {
        if (component1) {
            return component2 ? TT_S : TF_S;
        } else {
            return component2 ? FT_S : FF_S;
        }
    }

    private Object readResolve() {
        return of(component1, component2);
    }
}
