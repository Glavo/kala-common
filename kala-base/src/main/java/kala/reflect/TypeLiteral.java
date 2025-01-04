/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.reflect;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public class TypeLiteral<T> implements Serializable {
    @Serial
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
        if (genericSuperclass instanceof ParameterizedType superType) {

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
        return this == o || o instanceof TypeLiteral<?> that && type.equals(that.type);
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
