package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public final class IntObjTuple2<T> implements PrimitiveTuple {
    private static final long serialVersionUID = 0L;

    private final int component1;
    private final T component2;

    private IntObjTuple2(int i, T t) {
        component1 = i;
        component2 = t;
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
                return (U) Integer.valueOf(component1);
            case 1:
                return (U) component2;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public int component1() {
        return component1;
    }

    public T component2() {
        return component2;
    }

    public @NotNull Tuple2<@NotNull Integer, T> toTuple2() {
        return Tuple.of(component1, component2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof IntObjTuple2) {
            IntObjTuple2<?> other = (IntObjTuple2<?>) o;
            return component1 == other.component1 && Objects.equals(component2, other.component2);
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
        return Integer.hashCode(component1) ^ Objects.hashCode(component2);
    }

    @Override
    public String toString() {
        return "IntObjTuple2(" + component1 + ", " + component2 + ")";
    }
}
