package asia.kala.ref;

import java.io.Serializable;
import java.util.Objects;

public final class ByteRef implements Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = 1781207501;

    public byte value;

    public ByteRef() {
    }

    public ByteRef(byte value) {
        this.value = value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ByteRef)) {
            return false;
        }
        return value == ((ByteRef) o).value;
    }

    @Override
    public final int hashCode() {
        return value + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "ByteRef[" + value + "]";
    }
}
