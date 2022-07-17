package kala.collection.primitive;

import org.jetbrains.annotations.NotNull;

import kala.function.*;
<#if IsSpecialized>

import java.util.function.*;
</#if>

public interface ${Type}SetLike extends PrimitiveSetLike<${WrapperType}>, ${Type}CollectionLike {
    @Override
    default @NotNull String className() {
        return "${Type}SetLike";
    }

    @Override
    @NotNull ${Type}SetView view();

    @Override
    @NotNull ${Type}SetLike filter(@NotNull ${Type}Predicate predicate);

    @Override
    @NotNull ${Type}SetLike filterNot(@NotNull ${Type}Predicate predicate);
}
