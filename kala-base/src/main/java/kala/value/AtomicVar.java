package kala.value;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("NullableProblems")
public final class AtomicVar<T> extends AtomicReference<T> implements MutableValue<T> {
    private static final long serialVersionUID = 0L;

    public AtomicVar() {
    }

    public AtomicVar(T initialValue) {
        super(initialValue);
    }

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
        return "AtomicVar[" + get() + "]";
    }
}
