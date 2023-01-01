package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ${ClassName} implements PrimitiveTuple, Map.Entry<${WrapperType1}, ${WrapperType2}> {
    private static final long serialVersionUID = 0L;

    private final ${PrimitiveType1} component1;
    private final ${PrimitiveType2} component2;

    private ${ClassName}(${PrimitiveType1} v1, ${PrimitiveType2} v2) {
        component1 = v1;
        component2 = v2;
    }

    public static @NotNull ${ClassName} of(${PrimitiveType1} v1, ${PrimitiveType2} v2) {
        return new ${ClassName}(v1, v2);
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
                return (U) (Object) component1;
            case 1:
                return (U) (Object) component2;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public ${PrimitiveType1} component1() {
        return component1;
    }

    public ${PrimitiveType2} component2() {
        return component2;
    }

    public @NotNull Tuple2<${"@NotNull"} ${WrapperType1}, @NotNull ${WrapperType2}> toTuple2() {
        return Tuple.of(component1, component2);
    }

    @Override
    public ${WrapperType1} getKey() {
        return component1;
    }

    @Override
    public ${WrapperType2} getValue() {
        return component2;
    }

    @Override
    @Deprecated
    public ${WrapperType2} setValue(${WrapperType2} value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof ${ClassName}) {
            ${ClassName} other = (${ClassName}) o;
            return Conditions.equals(component1, other.component1) && Conditions.equals(component2, other.component2);
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
        return ${WrapperType1}.hashCode(component1) ^ ${WrapperType2}.hashCode(component2);
    }

    @Override
    public String toString() {
        return "${ClassName}(" + component1 + ", " + component2 + ")" ;
    }
}
