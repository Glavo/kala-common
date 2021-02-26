package org.glavo.kala.value;

public interface Value<T> extends AnyValue<T> {
    T get();

    @Override
    default T getValue() {
        return get();
    }
}
