package kala.value;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Supplier;

final class DelegateValue<T> extends AbstractValue<T> implements Serializable {
    private static final long serialVersionUID = 0L;

    private final @NotNull Supplier<? extends T> getter;

    DelegateValue(@NotNull Supplier<? extends T> getter) {
        this.getter = getter;
    }

    @Override
    public T get() {
        return getter.get();
    }
}
