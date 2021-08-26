package kala.value.primitive;

import java.io.Serializable;

public final class ShortRef implements MutableShortValue, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 1553970454;

    public short value;

    public ShortRef() {
    }

    public ShortRef(short value) {
        this.value = value;
    }

    @Override
    public short get() {
        return value;
    }

    @Override
    public void set(short value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShortRef)) {
            return false;
        }
        return value == ((ShortRef) o).value;
    }

    @Override
    public int hashCode() {
        return value + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "ShortRef[" + value + "]";
    }
}
