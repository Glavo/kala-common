package kala.value;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

@SuppressWarnings("NullableProblems")
public final class AtomicVar<T> extends AtomicReference<T> implements MutableValue<T> {
    public AtomicVar() {
    }

    public AtomicVar(T initialValue) {
        super(initialValue);
    }

    @Override
    public void update(@NotNull UnaryOperator<T> updateFunction) {
        updateAndGet(updateFunction);
    }

    @Override
    public void accumulate(T t, @NotNull BinaryOperator<T> accumulatorFunction) {
        accumulateAndGet(t, accumulatorFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(get());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AnyValue<?>))
            return false;
        AnyValue<?> other = (AnyValue<?>) obj;
        if (!this.canEqual(other) || !other.canEqual(this))
            return false;

        return Objects.equals(this.get(), other.getValue());
    }

    @Override
    public String toString() {
        return "AtomicVar[" + get() + "]";
    }
}
