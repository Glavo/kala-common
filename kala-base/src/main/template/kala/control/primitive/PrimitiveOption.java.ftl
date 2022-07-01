package kala.control.primitive;

import kala.collection.base.primitive.*;

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
<#if IsSpecialized>
import java.util.Optional${Type};
import java.util.function.*;
<#else>
import java.util.function.Supplier;

import kala.function.*;
</#if>

public final class ${Type}Option extends PrimitiveOption<${WrapperType}> implements ${Type}Traversable {
    private static final long serialVersionUID = -8990024629462620023L;

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

    public ${PrimitiveType} get() {
        if (isEmpty()) {
            throw new NoSuchElementException("${Type}Option.None");
        }
        return value;
    }

    public @Nullable ${WrapperType} getOrNull() {
        return isDefined() ? value : null;
    }

    public ${PrimitiveType} getOrDefault(${PrimitiveType} defaultValue) {
        return isDefined() ? value : defaultValue;
    }

    public ${PrimitiveType} getOrElse(@NotNull ${Type}Supplier supplier) {
        return isDefined() ? get() : supplier.getAs${Type}();
    }

    public <Ex extends Throwable> ${PrimitiveType} getOrThrowException(@NotNull Ex exception) throws Ex {
        if (isEmpty()) {
            Objects.requireNonNull(exception);
            throw exception;
        }
        return value;
    }

    public <Ex extends Throwable> ${PrimitiveType} getOrThrow(@NotNull Supplier<? extends Ex> supplier) throws Ex {
        if (isEmpty()) {
            Objects.requireNonNull(supplier);
            throw supplier.get();
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof ${Type}Option) {
            return this != None && o != None
                    && ${PrimitiveEquals("value", "((${Type}Option) o).value")};
        }

        if (o instanceof Option) {
            Option<?> other = (Option<?>) o;
            if (this.isEmpty()) return other.isEmpty();
            if (other.isEmpty()) return false;

            Object v = other.get();
            return v instanceof ${WrapperType}
                    && ${PrimitiveEquals("value", "(${WrapperType}) v")};
        }

        return false;
    }
    @Override
    public int hashCode() {
        return this.isDefined() ? HASH_MAGIC + ${WrapperType}.hashCode(value) : NONE_HASH;
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
