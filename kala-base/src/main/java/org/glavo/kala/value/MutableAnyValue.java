package org.glavo.kala.value;

public interface MutableAnyValue<T> extends AnyValue<T> {
    void setValue(T value);
}
