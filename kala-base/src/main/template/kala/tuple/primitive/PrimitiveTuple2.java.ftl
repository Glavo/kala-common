package kala.tuple.primitive;

import kala.Conditions;
import kala.tuple.AnyTuple;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ${ClassName} implements PrimitiveTuple, Map.Entry<${WrapperType1}, ${WrapperType2}> {
    private static final long serialVersionUID = 0L;

    public final ${PrimitiveType1} _1;
    public final ${PrimitiveType2} _2;

    private ${ClassName}(${PrimitiveType1} v1, ${PrimitiveType2} v2) {
        _1 = v1;
        _2 = v2;
    }

    public static @NotNull ${ClassName} of(${PrimitiveType1} i1, ${PrimitiveType2} i2) {
        return new ${ClassName}(i1, i2);
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
                return (U) (Object) _1;
            case 1:
                return (U) (Object) _2;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public ${PrimitiveType1} component1() {
        return _1;
    }

    public ${PrimitiveType2} component2() {
        return _2;
    }

    public @NotNull Tuple2<${"@NotNull"} ${WrapperType1}, @NotNull ${WrapperType2}> toTuple2() {
        return Tuple.of(_1, _2);
    }

    @Override
    public ${WrapperType1} getKey() {
        return _1;
    }

    @Override
    public ${WrapperType2} getValue() {
        return _2;
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
            return Conditions.equals(_1, other._1) && Conditions.equals(_2, other._2);
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
        return ${WrapperType1}.hashCode(_1) ^ ${WrapperType2}.hashCode(_2);
    }

    @Override
    public String toString() {
        return "${ClassName}(" + _1 + ", " + _2 + ")" ;
    }
}
