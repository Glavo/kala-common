package kala.value.primitive;

import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.*;

final class DelegateMutable${Type}Value extends AbstractMutable${Type}Value implements Serializable {
    private static final long serialVersionUID = 0L;

    private final @NotNull ${Type}Supplier getter;
    private final @NotNull ${Type}Consumer setter;

    DelegateMutable${Type}Value(@NotNull ${Type}Supplier getter, @NotNull ${Type}Consumer setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public ${PrimitiveType} get() {
        return getter.getAs${Type}();
    }

    @Override
    public void set(${PrimitiveType} value) {
        setter.accept(value);
    }
}
