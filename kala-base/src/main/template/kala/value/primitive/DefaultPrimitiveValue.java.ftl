package kala.value.primitive;

import java.io.Serializable;

final class Default${Type}Value extends Abstract${Type}Value implements Serializable {
    private static final long serialVersionUID = 0L;

    private final ${PrimitiveType} value;

    Default${Type}Value(${PrimitiveType} value) {
        this.value = value;
    }

    @Override
    public ${PrimitiveType} get() {
        return value;
    }
}
