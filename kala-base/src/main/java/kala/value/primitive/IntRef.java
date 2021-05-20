package kala.value.primitive;

import java.io.Serializable;

public final class IntRef implements MutableIntValue, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 1278657047;

    public int value;

    public IntRef() {
    }

    public IntRef(int value) {
        this.value = value;
    }

    @Override
    public final int get() {
        return value;
    }

    @Override
    public final void set(int value) {
        this.value = value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntRef)) {
            return false;
        }
        return value == ((IntRef) o).value;
    }

    @Override
    public final int hashCode() {
        return value + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "IntRef[" + value + "]";
    }
}
