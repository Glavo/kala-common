package kala.value;

import java.util.Objects;

public abstract class AbstractValue<T> implements Value<T> {
    @Override
    public int hashCode() {
        return Objects.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnyValue<?>))
            return false;
        AnyValue<?> other = (AnyValue<?>) obj;
        if (!this.canEqual(other) || !other.canEqual(this))
            return false;

        return Objects.equals(this.get(), other.getValue());
    }

    @Override
    public String toString() {
        return "Value[" + get() + "]";
    }
}
