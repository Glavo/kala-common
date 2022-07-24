package kala.value;

import java.io.Serializable;

final class DefaultValue<T> extends AbstractValue<T> implements Serializable {
    private static final long serialVersionUID = 0L;

    private final T value;

    DefaultValue(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}
