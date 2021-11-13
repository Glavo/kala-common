package kala.value.primitive;

import org.jetbrains.annotations.NotNull;

public interface ${Type}Value extends PrimitiveValue<${WrapperType}> {
    ${PrimitiveType} get();

    @Override
    default @NotNull ${WrapperType} getValue() {
        return get();
    }
}
