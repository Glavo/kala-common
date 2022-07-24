package kala.value;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class DelegateMutableValue<T> extends AbstractMutableValue<T> implements Serializable {
    private static final long serialVersionUID = 0L;

    private final @NotNull Supplier<? extends T> getter;
    private final @NotNull Consumer<? super T> setter;

    DelegateMutableValue(@NotNull Supplier<? extends T> getter, @NotNull Consumer<? super T> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public T get() {
        return getter.get();
    }

    @Override
    public void set(T value) {
        setter.accept(value);
    }
}
