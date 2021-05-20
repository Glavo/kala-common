package kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface BooleanValue extends PrimitiveValue<Boolean> {

    boolean get();

    @Override
    default @NotNull Boolean getValue() {
        return get();
    }
}
