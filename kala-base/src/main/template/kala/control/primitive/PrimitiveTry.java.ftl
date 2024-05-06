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
package kala.control.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.base.primitive.${Type}Traversable;
import kala.control.AnyTry;
import kala.control.Try;
import kala.function.Checked${Type}Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class ${Type}Try implements PrimitiveTry<${WrapperType}>, ${Type}Traversable, Serializable {
    @Serial
    private static final long serialVersionUID = 8279218383491584792L;

    private final ${PrimitiveType} value;
    private final Throwable cause;

    private ${Type}Try(${PrimitiveType} value, Throwable cause) {
        this.value = value;
        this.cause = cause;
    }

    public static @NotNull ${Type}Try success(${PrimitiveType} value) {
        return new ${Type}Try(value, null);
    }

    public static @NotNull ${Type}Try failure(@NotNull Throwable exception) {
        Objects.requireNonNull(exception);
        return new ${Type}Try(${Values.Zero}, exception);
    }

    public static @NotNull ${Type}Try of(@NotNull Checked${Type}Supplier<?> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return success(supplier.getAs${Type}Checked());
        } catch (Throwable ex) {
            return failure(ex);
        }
    }

    @Override
    public boolean isSuccess() {
        return cause == null;
    }

    @Override
    public boolean isFailure() {
        return cause != null;
    }

    @Override
    @Deprecated
    @ReplaceWith("get()")
    public @NotNull ${WrapperType} getValue() {
        return get();
    }

    public ${PrimitiveType} get() {
        if (isFailure()) throw new NoSuchElementException();
        return value;
    }

    @Override
    public @Nullable ${WrapperType} getOrNull() {
        return isSuccess() ? get() : null;
    }

    @Override
    public ${Type}Option getOption() {
        return isSuccess() ? ${Type}Option.some(get()) : ${Type}Option.none();
    }

    @Override
    public @NotNull Throwable getCause() {
        return cause;
    }

    @Override
    public @NotNull <Ex extends Throwable> ${Type}Try rethrow() throws Ex {
        if (isFailure()) throw (Ex) cause;
        return this;
    }

    @Override
    public @NotNull <Ex extends Throwable> ${Type}Try rethrow(@NotNull Class<? extends Ex> type) throws Ex {
        if (type.isInstance(cause)) throw (Ex) cause;
        return this;
    }

    @Override
    public @NotNull ${Type}Try rethrowFatal() {
        if (Try.isFatal(cause)) Try.sneakyThrow(cause);
        return this;
    }

    @Override
    public @NotNull ${Type}Iterator iterator() {
        return isSuccess() ? ${Type}Iterator.of(get()) : ${Type}Iterator.empty();
    }

    @Override
    public int hashCode() {
        if (isSuccess()) {
            return ${WrapperType}.hashCode(get()) + SUCCESS_HASH_MAGIC;
        } else {
            return getCause().hashCode() + FAILURE_HASH_MAGIC;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnyTry)) return false;

        AnyTry<?> other = (AnyTry<?>) o;
        if (this.isSuccess() && other.isSuccess()) {
            if (other instanceof ${Type}Try) {
                return ${PrimitiveEquals("get()", "((${Type}Try) other).get()")};
            } else if (other instanceof Try) {
                Object v = other.getValue();
                return v instanceof ${WrapperType}
                        && ${PrimitiveEquals("get()", "(${WrapperType}) v")};
            } else {
                return false;
            }
        } else if (this.isFailure() && other.isFailure()) {
            return this.getCause().equals(other.getCause());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (isSuccess()) {
            return "${Type}Try.Success[" + get() + "]";
        } else {
            return "${Type}Try.Failure[" + cause + "]";
        }
    }
}
