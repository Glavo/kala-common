package org.glavo.kala.value.primitive;

import java.io.Serializable;

public final class SimpleMutableCharValue implements MutableCharValue, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = -492503597;

    public char value;

    public SimpleMutableCharValue() {
    }

    public SimpleMutableCharValue(char value) {
        this.value = value;
    }

    @Override
    public final char get() {
        return value;
    }

    @Override
    public final void set(char value) {
        this.value = value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleMutableCharValue)) {
            return false;
        }
        return value == ((SimpleMutableCharValue) o).value;
    }

    @Override
    public final int hashCode() {
        return value + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "CharRef[" + value + "]";
    }
}
