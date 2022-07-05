package kala.collection.mutable.primitive;

import kala.annotations.ReplaceWith;
<#if !IsSpecialized>
import kala.function.${Type}Consumer;
</#if>
import org.jetbrains.annotations.NotNull;
<#if IsSpecialized>

import java.util.function.${Type}Consumer;
</#if>

public interface Mutable${Type}ListIterator extends MutablePrimitiveListIterator<${WrapperType}, ${Type}Consumer>, Mutable${Type}SeqIterator {

    void add(${PrimitiveType} value);

    @Override
    void remove();

    @Override
    @Deprecated
    @ReplaceWith("add(${PrimitiveType})")
    default void add(@NotNull ${WrapperType} value) {
        add(value.${PrimitiveType}Value());
    }
}
