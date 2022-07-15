package kala.value;

import java.io.Serializable;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransientRef<?>)) {
            return false;
        }
        TransientRef<?> other = (TransientRef<?>) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value) + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "TransientRef[" + value + "]";
    }
}
