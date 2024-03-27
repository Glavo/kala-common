package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Map;
import java.util.Objects;

public record IntObjTuple2<T>(int component1, T component2) implements PrimitiveTuple {
    @Serial
    private static final long serialVersionUID = 0L;

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

    public @NotNull Tuple2<@NotNull Integer, T> toTuple2() {
        return Tuple.of(component1, component2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof IntObjTuple2<?> other) {
            return component1 == other.component1 && Objects.equals(component2, other.component2);
        }

        if (o instanceof Map.Entry<?, ?> other) {
            return Conditions.equals(component1, other.getKey()) && Conditions.equals(component2, other.getValue());
        }

        if (o instanceof AnyTuple other) {
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
