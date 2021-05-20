package kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface ShortValue extends PrimitiveValue<Short> {
    short get();

    @Override
    default @NotNull Short getValue() {
        return get();
    }
}
