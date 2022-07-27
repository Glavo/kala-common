package kala.value.primitive;

import kala.Conditions;
import kala.value.AnyValue;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.Atomic${WrapperType};
import java.util.function.*;

@SuppressWarnings("NullableProblems")
public final class Atomic${Type}Var extends Atomic${WrapperType} implements Mutable${Type}Value {
    public Atomic${Type}Var() {
    }

    public Atomic${Type}Var(${PrimitiveType} initialValue) {
        super(initialValue);
    }

    @Override
    public void update(@NotNull ${Type}UnaryOperator updateFunction) {
        updateAndGet(updateFunction);
    }

    @Override
    public void accumulate(${PrimitiveType} t, @NotNull ${Type}BinaryOperator accumulatorFunction) {
        accumulateAndGet(t, accumulatorFunction);
    }

<#if Type == "Int" || Type == "Long">
    @Override
    public void increment() {
        incrementAndGet();
    }

    @Override
    public void decrement() {
        decrementAndGet();
    }

    @Override
    public void add(${PrimitiveType} delta) {
        addAndGet(delta);
    }

    @Override
    public void sub(${PrimitiveType} delta) {
        subAndGet(delta);
    }

    @Override
    public ${PrimitiveType} subAndGet(${PrimitiveType} delta) {
        return addAndGet(-delta);
    }

    @Override
    public ${PrimitiveType} getAndSub(${PrimitiveType} delta) {
        return getAndAdd(-delta);
    }

</#if>
    @Override
    public int hashCode() {
        return ${WrapperType}.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnyValue<?>))
            return false;
        AnyValue<?> other = (AnyValue<?>) obj;
        if (!this.canEqual(other) || !other.canEqual(this))
            return false;

        if (other instanceof ${Type}Value)
            return this.get() == ((${Type}Value) other).get();

        return Conditions.equals(this.get(), other.getValue());
    }

    @Override
    public String toString() {
        return "Atomic${Type}Var[" + get() + "]";
    }
}
