package kala.value.primitive;

import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.function.*;

final class Delegate${Type}Value extends Abstract${Type}Value implements Serializable {
    private static final long serialVersionUID = 0L;

    private final @NotNull ${Type}Supplier getter;

    Delegate${Type}Value(@NotNull ${Type}Supplier getter) {
        this.getter = getter;
    }

    @Override
    public ${PrimitiveType} get() {
        return getter.getAs${Type}();
    }
}
