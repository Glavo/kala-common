package kala.value;

import kala.annotations.ReplaceWith;

import java.io.Serializable;
import java.util.Objects;

@Deprecated
@ReplaceWith("Var")
public final class Ref<T> extends AbstractMutableValue<T> implements Serializable {
    private static final long serialVersionUID = 0L;

    public T value;

    public Ref() {
    }

    public Ref(T value) {
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
        return "Ref[" + value + "]";
    }
}
