package kala.collection.base.primitive;

import org.jetbrains.annotations.NotNull;

public interface ${Type}Growable extends PrimitiveGrowable<${WrapperType}> {
    void plusAssign(${PrimitiveType} value);

    default void plusAssign(@NotNull ${Type}Traversable values) {
        values.forEach(this::plusAssign);
    }

    default void plusAssign(${PrimitiveType} @NotNull [] values) {
        for (${PrimitiveType} value : values) {
            this.plusAssign(value);
        }
    }

    @Override
    default void plusAssign(@NotNull ${WrapperType} value) {
        plusAssign(value.${PrimitiveType}Value());
    }
}
