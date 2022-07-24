package kala.value;

import kala.annotations.ReplaceWith;

import java.io.Serializable;

@Deprecated
@ReplaceWith("TransientVar")
public final class TransientRef<T> implements MutableValue<T>, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 994181066;

    public transient T value;

    public TransientRef() {
    }

    public TransientRef(T value) {
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
        return "TransientRef[" + value + "]";
    }
}
