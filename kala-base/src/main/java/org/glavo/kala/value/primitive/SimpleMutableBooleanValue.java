package org.glavo.kala.value.primitive;

import java.io.Serializable;

public final class SimpleMutableBooleanValue implements MutableBooleanValue, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = -1071790882;

    public boolean value;

    public SimpleMutableBooleanValue() {
    }

    public SimpleMutableBooleanValue(boolean value) {
        this.value = value;
    }

    @Override
    public final boolean get() {
        return value;
    }

    @Override
    public final void set(boolean value) {
        this.value = value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleMutableBooleanValue)) {
            return false;
        }
        return value == ((SimpleMutableBooleanValue) o).value;
    }

    @Override
    public final int hashCode() {
        return Boolean.hashCode(value) + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "BooleanRef[" + value + "]";
    }
}
