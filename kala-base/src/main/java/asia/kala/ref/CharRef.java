package asia.kala.ref;

import java.io.Serializable;

public final class CharRef implements Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = -492503597;

    public char value;

    public CharRef() {
    }

    public CharRef(char value) {
        this.value = value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharRef)) {
            return false;
        }
        return value == ((CharRef) o).value;
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
