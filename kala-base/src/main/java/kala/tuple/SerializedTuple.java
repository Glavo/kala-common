package kala.tuple;

import java.io.Serializable;

final class SerializedTuple implements Serializable {
    private static final long serialVersionUID = 0L;

    private final Object[] values;

    SerializedTuple(Object[] values) {
        this.values = values;
    }

    private Object readResolve() {
        return Tuple.of(values);
    }
}
