package kala.collection.mutable.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.primitive.${Type}SeqIterator;
<#if !IsSpecialized>
import kala.function.${Type}Consumer;
</#if>
import org.jetbrains.annotations.NotNull;
<#if IsSpecialized>

import java.util.function.${Type}Consumer;
</#if>

public interface Mutable${Type}SeqIterator extends MutablePrimitiveSeqIterator<${WrapperType}, ${Type}Consumer>, ${Type}SeqIterator {

    void set(${PrimitiveType} value);

    @Override
    default @NotNull ${Type}SeqIterator frozen() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    @Deprecated
    @ReplaceWith("set(${PrimitiveType})")
    default void set(@NotNull ${WrapperType} value) {
        this.set(value.${PrimitiveType}Value());
    }
}
