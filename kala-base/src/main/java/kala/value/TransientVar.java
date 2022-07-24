package kala.value;

import java.io.Serializable;

public final class TransientVar<T> implements MutableValue<T>, Serializable {
    private static final long serialVersionUID = 0L;

    public transient T value;

    public TransientVar() {
    }

    public TransientVar(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TransientVar[" + value + "]";
    }
}
