package kala.value.primitive;

import java.io.Serializable;

public final class CharRef implements MutableCharValue, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = -492503597;

    public char value;

    public CharRef() {
    }

    public CharRef(char value) {
        this.value = value;
    }

    @Override
    public char get() {
        return value;
    }

    @Override
    public void set(char value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharRef)) {
            return false;
        }
        return value == ((CharRef) o).value;
    }

    @Override
    public int hashCode() {
        return value + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "CharRef[" + value + "]";
    }
}
