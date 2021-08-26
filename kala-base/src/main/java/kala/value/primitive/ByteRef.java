package kala.value.primitive;

import java.io.Serializable;

public final class ByteRef implements MutableByteValue, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 1781207501;

    public byte value;

    public ByteRef() {
    }

    public ByteRef(byte value) {
        this.value = value;
    }

    @Override
    public byte get() {
        return value;
    }

    @Override
    public void set(byte value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ByteRef)) {
            return false;
        }
        return value == ((ByteRef) o).value;
    }

    @Override
    public int hashCode() {
        return value + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "ByteRef[" + value + "]";
    }
}
