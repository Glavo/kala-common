package org.glavo.kala.value.primitive;

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
    public final long get() {
        return value;
    }

    @Override
    public final void set(long value) {
        this.value = value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LongRef)) {
            return false;
        }
        return value == ((LongRef) o).value;
    }

    @Override
    public final int hashCode() {
        return Long.hashCode(value) + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "LongRef[" + value + "]";
    }
}
