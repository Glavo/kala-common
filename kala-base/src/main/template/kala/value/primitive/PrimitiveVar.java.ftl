package kala.value.primitive;

import java.io.Serializable;

public final class ${Type}Var extends Abstract${Type}Value implements Mutable${Type}Value, Serializable {
    private static final long serialVersionUID = 0L;

    public ${PrimitiveType} value;

    public ${Type}Var() {
    }

    public ${Type}Var(${PrimitiveType} value) {
        this.value = value;
    }

    @Override
    public ${PrimitiveType} get() {
        return value;
    }

    @Override
    public void set(${PrimitiveType} value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "${Type}Var[" + value + "]";
    }
}
