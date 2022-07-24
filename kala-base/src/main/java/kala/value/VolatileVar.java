package kala.value;

import java.io.Serializable;

public final class VolatileVar<T> extends AbstractMutableValue<T> implements Serializable {
    private static final long serialVersionUID = 0L;

    public volatile T value;

    public VolatileVar() {
    }

    public VolatileVar(T value) {
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
        return "VolatileVar[" + value + "]";
    }
}
