package kala.value.primitive;

import java.io.Serializable;

public final class ${Type}VolatileVar extends Abstract${Type}Value implements Mutable${Type}Value, Serializable {
    private static final long serialVersionUID = 0L;

    public volatile ${PrimitiveType} value;

    public ${Type}VolatileVar() {
    }

    public ${Type}VolatileVar(${PrimitiveType} value) {
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

<#if IsIntegral>
    public void increment() {
        value++;
    }

    public void decrement() {
        value--;
    }

</#if>
    @Override
    public String toString() {
        return "${Type}VolatileVar[" + value + "]";
    }
}
