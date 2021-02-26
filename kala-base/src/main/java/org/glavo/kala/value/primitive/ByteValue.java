package org.glavo.kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface ByteValue extends PrimitiveValue<Byte> {
    byte get();

    @Override
    default @NotNull Byte getValue() {
        return get();
    }
}
