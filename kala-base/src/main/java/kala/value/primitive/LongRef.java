package kala.value.primitive;

import java.io.Serializable;

public final class LongRef implements MutableLongValue, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 1556644513;

    public long value;

    public LongRef() {
    }

    public LongRef(long value) {
        this.value = value;
    }

    @Override
    public long get() {
        return value;
    }

    @Override
    public void set(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LongRef)) {
            return false;
        }
        return value == ((LongRef) o).value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value) + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "LongRef[" + value + "]";
    }
}
