package kala.value;

import java.io.Serializable;

public final class Var<T> extends AbstractMutableValue<T> implements Cloneable, Serializable {
    private static final long serialVersionUID = 0L;

    public T value;

    public Var() {
    }

    public Var(T value) {
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
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Var<T> clone() {
        return new Var<>(value);
    }

    @Override
    public String toString() {
        return "Var[" + value + "]";
    }
}
