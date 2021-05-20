package kala.value;

public interface MutableValue<T> extends MutableAnyValue<T>, Value<T> {
    void set(T value);

    @Override
    default void setValue(T value) {
        set(value);
    }
}
