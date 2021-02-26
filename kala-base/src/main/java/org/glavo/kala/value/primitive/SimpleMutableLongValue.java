package org.glavo.kala.value.primitive;

import java.io.Serializable;

public final class SimpleMutableLongValue implements MutableLongValue, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 1556644513;

    public long value;

    public SimpleMutableLongValue() {
    }

    public SimpleMutableLongValue(long value) {
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
        if (!(o instanceof SimpleMutableLongValue)) {
            return false;
        }
        return value == ((SimpleMutableLongValue) o).value;
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
