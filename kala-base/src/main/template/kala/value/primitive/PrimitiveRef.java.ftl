package kala.value.primitive;

import java.io.Serializable;

public final class ${Type}Ref implements Mutable${Type}Value, Serializable {
    private static final long serialVersionUID = 0L;

    private static final int HASH_MAGIC = ${HashMagic};

    public ${PrimitiveType} value;

    public ${Type}Ref() {
    }

    public ${Type}Ref(${PrimitiveType} value) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ${Type}Ref)) {
            return false;
        }
        return value == ((${Type}Ref) o).value;
    }

    @Override
    public int hashCode() {
        return ${WrapperType}.hashCode(value) + HASH_MAGIC;
    }

    @Override
    public String toString() {
        return "${Type}Ref[" + value + "]";
    }
}
