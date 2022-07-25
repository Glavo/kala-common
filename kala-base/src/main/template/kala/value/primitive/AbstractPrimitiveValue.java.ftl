package kala.value.primitive;

import kala.Conditions;
import kala.value.AnyValue;

import java.util.Objects;

public abstract class Abstract${Type}Value implements ${Type}Value {
    @Override
    public int hashCode() {
        return ${WrapperType}.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnyValue<?>))
            return false;
        AnyValue<?> other = (AnyValue<?>) obj;
        if (!this.canEqual(other) || !other.equals(this))
            return false;

        if (other instanceof ${Type}Value)
            return ${PrimitiveEquals("this.get()", "((${Type}Value) other).get()")};

        return Conditions.equals(this.get(), other.getValue());
    }

    @Override
    public String toString() {
        return "${Type}Value[" + get() + "]";
    }
}
