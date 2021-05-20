package kala.value.primitive;

import kala.value.MutableAnyValue;
import org.jetbrains.annotations.NotNull;

public interface MutablePrimitiveValue<T> extends MutableAnyValue<@NotNull T>, PrimitiveValue<@NotNull T> {
    @Override
    void setValue(@NotNull T value);
}
