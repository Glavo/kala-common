package kala.value;

import java.io.Serializable;

public final class CheckedVar<T> extends AbstractMutableValue<T> implements Cloneable, Serializable {
    private static final long serialVersionUID = 0L;

    private final Class<T> type;
    private T value;

    public CheckedVar(Class<T> type) {
        this.type = type;
    }

    public CheckedVar(Class<T> type, T value) {
        this.type = type;
        this.value = type.cast(value);
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) throws ClassCastException {
        this.value = type.cast(value);
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public CheckedVar<T> clone() {
        return new CheckedVar<>(type, value);
    }

    @Override
    public String toString() {
        return "CheckedVar[" + value + "]";
    }
}
