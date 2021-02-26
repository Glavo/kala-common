package org.glavo.kala.value.primitive;

import java.io.Serializable;

public final class SimpleMutableShortValue implements MutableShortValue, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 1553970454;

    public short value;

    public SimpleMutableShortValue() {
    }

    public SimpleMutableShortValue(short value) {
        this.value = value;
    }

    @Override
    public final short get() {
        return value;
    }

    @Override
    public final void set(short value) {
        this.value = value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleMutableShortValue)) {
            return false;
        }
        return value == ((SimpleMutableShortValue) o).value;
    }

    @Override
    public final int hashCode() {
        return value + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "ShortRef[" + value + "]";
    }
}
