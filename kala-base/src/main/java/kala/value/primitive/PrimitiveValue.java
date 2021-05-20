package kala.value.primitive;

import kala.value.AnyValue;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveValue<T> extends AnyValue<@NotNull T> {

    @Override
    @NotNull T getValue();

}
