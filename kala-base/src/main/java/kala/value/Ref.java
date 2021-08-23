package kala.value;

import java.io.Serializable;
import java.util.Objects;

public final class Ref<T> implements MutableValue<T>, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 1281759194;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ref<?>)) {
            return false;
        }
        Ref<?> other = (Ref<?>) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value) + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "Ref[" + value + "]";
    }
}
