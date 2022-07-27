package kala.value.primitive;

import kala.annotations.ReplaceWith;

import java.io.Serializable;

@Deprecated
@ReplaceWith("${Type}Var")
public final class ${Type}Ref extends Abstract${Type}Value implements Mutable${Type}Value, Serializable {
    private static final long serialVersionUID = 0L;

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
    @Override
    public void increment() {
        value++;
    }

    @Override
    public void decrement() {
        value--;
    }

</#if>
    @Override
    public String toString() {
        return "${Type}Ref[" + value + "]";
    }
}
