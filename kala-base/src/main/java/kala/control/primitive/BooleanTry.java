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
import kala.collection.base.primitive.BooleanIterator;
import kala.collection.base.primitive.BooleanTraversable;
import kala.control.AnyTry;
import kala.control.Try;
import kala.function.CheckedBooleanSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class BooleanTry implements PrimitiveTry<Boolean>, BooleanTraversable, Serializable {
    @Serial
    private static final long serialVersionUID = 8279218383491584792L;

    public static final BooleanTry True = new BooleanTry(null);
    public static final BooleanTry False = new BooleanTry(null);

    private final Throwable cause;

    private BooleanTry(Throwable cause) {
        this.cause = cause;
    }

    public static @NotNull BooleanTry success(boolean value) {
        return value ? True : False;
    }

    public static @NotNull BooleanTry failure(@NotNull Throwable exception) {
        Objects.requireNonNull(exception);
        return new BooleanTry(exception);
    }

    public static @NotNull BooleanTry of(@NotNull CheckedBooleanSupplier<?> supplier) {
        Objects.requireNonNull(supplier);
        try {
            return success(supplier.getAsBooleanChecked());
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
    public @NotNull Boolean getValue() {
        return get();
    }

    public boolean get() {
        if (isFailure()) throw new NoSuchElementException();
        return this == True;
    }

    @Override
    public @Nullable Boolean getOrNull() {
        return isSuccess() ? get() : null;
    }

    @Override
    public BooleanOption getOption() {
        return isSuccess() ? BooleanOption.some(get()) : BooleanOption.none();
    }

    @Override
    public @NotNull Throwable getCause() {
        return cause;
    }

    @Override
    public @NotNull <Ex extends Throwable> BooleanTry rethrow() throws Ex {
        if (isFailure()) throw (Ex) cause;
        return this;
    }

    @Override
    public @NotNull <Ex extends Throwable> BooleanTry rethrow(@NotNull Class<? extends Ex> type) throws Ex {
        if (type.isInstance(cause)) throw (Ex) cause;
        return this;
    }

    @Override
    public @NotNull BooleanTry rethrowFatal() {
        if (Try.isFatal(cause)) Try.sneakyThrow(cause);
        return this;
    }

    @Override
    public @NotNull BooleanIterator iterator() {
        return isSuccess() ? BooleanIterator.of(get()) : BooleanIterator.empty();
    }


    @Override
    public int hashCode() {
        if (isSuccess()) {
            return Boolean.hashCode(get()) + SUCCESS_HASH_MAGIC;
        } else {
            return getCause().hashCode() + FAILURE_HASH_MAGIC;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnyTry<?> other)) return false;

        if (this.isSuccess() && other.isSuccess()) {
            return Boolean.valueOf(this.get()).equals(other.getValue());
        } else if (this.isFailure() && other.isFailure()) {
            return this.getCause().equals(other.getCause());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (isSuccess()) {
            return "BooleanTry.Success[" + get() + "]";
        } else {
            return "BooleanTry.Failure[" + cause + "]";
        }
    }

    private static final class Replaced implements Serializable {
        private static final long serialVersionUID = 8898176688746795097L;

        private final boolean value;

        Replaced(boolean value) {
            this.value = value;
        }

        private Object readResolve() {
            return value ? BooleanTry.True : BooleanTry.False;
        }
    }
}
