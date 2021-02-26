package org.glavo.kala.value;

import java.io.Serializable;
import java.util.Objects;

public final class SimpleMutableValue<T> implements MutableValue<T>, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 1281759194;

    public T value;

    public SimpleMutableValue() {
    }

    public SimpleMutableValue(T value) {
        this.value = value;
    }

    @Override
    public final T get() {
        return value;
    }

    @Override
    public final void set(T value) {
        this.value = value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleMutableValue<?>)) {
            return false;
        }
        SimpleMutableValue<?> other = (SimpleMutableValue<?>) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value) + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "Ref[" + value + "]";
    }
}
