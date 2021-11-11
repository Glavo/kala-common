package kala.control.primitive;

import kala.collection.base.primitive.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
<#if IsSpecialized>
import java.util.Optional${Type};
import java.util.function.*;
<#else>

import kala.function.*;
</#if>

public final class ${Type}Option extends PrimitiveOption<${WrapperType}> implements ${Type}Traversable {
    private static final long serialVersionUID = -8990024629462620023L;
    private static final int HASH_MAGIC = -818206074;

    public static final ${Type}Option None = new ${Type}Option();

    private final ${PrimitiveType} value;

    private ${Type}Option() {
        this.value = 0;
    }

    private ${Type}Option(${PrimitiveType} value) {
        this.value = value;
    }

    public static @NotNull ${Type}Option some(${PrimitiveType} value) {
        return new ${Type}Option(value);
    }

    public static @NotNull ${Type}Option none() {
        return None;
    }

    public static @NotNull ${Type}Option of(${PrimitiveType} value) {
        return some(value);
    }

    public static @NotNull ${Type}Option of(@Nullable ${WrapperType} value) {
        return value == null ? None : some(value);
    }

<#if IsSpecialized>
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static @NotNull ${Type}Option fromJava(@NotNull Optional${Type} optional) {
        return optional.isPresent() ? some(optional.getAs${Type}()) : none();
    }

</#if>
    public boolean isDefined() {
        return this != None;
    }

    public boolean isEmpty() {
        return this == None;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ${Type}Option)) {
            return false;
        }
        if (this == None || o == None) {
            return false;
        }

        return value == ((${Type}Option) o).value;
    }

    @Override
    public int hashCode() {
        return HASH_MAGIC + ${WrapperType}.hashCode(value);
    }

    @Override
    public String toString() {
        return this == None ? "${Type}Option.None" : "${Type}Option[" + value + "]";
    }

    @Override
    public @NotNull ${Type}Iterator iterator() {
        return isDefined() ? ${Type}Iterator.of(value) : ${Type}Iterator.empty();
    }

    @Override
    public void forEach(@NotNull ${Type}Consumer action) {
        if (isDefined()) {
            action.accept(value);
        }
    }

    private Object writeReplace() {
        return new Data(this == None ? null : value);
    }

    private static final class Data implements Serializable {
        private static final long serialVersionUID = -2044232156734869349L;
        private final ${WrapperType} value;

        Data(${WrapperType} value) {
            this.value = value;
        }

        Object readResolve() {
            return value == null ? None : some(value);
        }
    }
}
