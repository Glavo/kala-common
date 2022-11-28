package kala.reflect;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

public class TypeLiteral<T> implements Serializable {
    private static final long serialVersionUID = 6116839399568406117L;
    private static final int HASH_MAGIC = 705347658;

    private final @NotNull Type type;

    public static <T> TypeLiteral<T> of(@NotNull Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return new TypeLiteral<>(clazz);
    }

    public static TypeLiteral<?> of(@NotNull Type type) {
        Objects.requireNonNull(type);
        return new TypeLiteral<>(type);
    }

    private TypeLiteral(@NotNull Type type) {
        this.type = type;
    }

    protected TypeLiteral() {
        this.type = capture();
    }

    private @NotNull Type capture() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType superType = (ParameterizedType) genericSuperclass;

            // Direct subclass
            if (superType.getRawType() == TypeLiteral.class) {
                return superType.getActualTypeArguments()[0];
            }
        }
        throw new IllegalStateException();
    }

    public final @NotNull Type getType() {
        return type;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeLiteral)) {
            return false;
        }
        return type.equals(((TypeLiteral<?>) o).type);
    }

    @Override
    public final int hashCode() {
        return type.hashCode() + HASH_MAGIC;
    }

    @Override
    public final String toString() {
        return "TypeLiteral[" + type + ']';
    }
}
