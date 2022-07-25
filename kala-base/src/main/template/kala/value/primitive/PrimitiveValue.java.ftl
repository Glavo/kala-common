package kala.value.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.base.primitive.${Type}Iterator;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
<#if IsSpecialized || Type == "Boolean">
import java.util.function.*;
</#if>

public interface ${Type}Value extends PrimitiveValue<${WrapperType}>, ${Type}Traversable, ${Type}Supplier {

    static @NotNull ${Type}Value of(${PrimitiveType} value) {
        return new Default${Type}Value(value);
    }

    static @NotNull ${Type}Value by(@NotNull ${Type}Supplier getter) {
        Objects.requireNonNull(getter);
        return new Delegate${Type}Value(getter);
    }

    static @NotNull Lazy${Type}Value lazy(@NotNull ${Type}Supplier getter) {
        return Lazy${Type}Value.of(getter);
    }

    ${PrimitiveType} get();

    @Override
    @Deprecated
    @ReplaceWith("get()")
    default ${PrimitiveType} getAs${Type}() {
        return get();
    }

    @Override
    default @NotNull ${WrapperType} getValue() {
        return get();
    }

    default @NotNull ${Type}Value map(@NotNull ${Type}UnaryOperator mapper) {
        Objects.requireNonNull(mapper);
        return ${Type}Value.by(() -> mapper.applyAs${Type}(get()));
    }

    default @NotNull Mutable${Type}Value asMutable(@NotNull ${Type}Consumer setter) {
        Objects.requireNonNull(setter);
        return new DelegateMutable${Type}Value(this, setter);
    }

    @Override
    default @NotNull ${Type}Iterator iterator() {
        return ${Type}Iterator.of(get());
    }

    @Override
    default void forEach(@NotNull ${Type}Consumer action) {
        action.accept(get());
    }
}
