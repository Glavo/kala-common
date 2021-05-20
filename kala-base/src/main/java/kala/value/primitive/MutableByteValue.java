package kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface MutableByteValue extends MutablePrimitiveValue<Byte>, ByteValue {

    void set(byte value);

    @Override
    default void setValue(@NotNull Byte value) {
        set(value);
    }
}
