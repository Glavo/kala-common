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
package kala.function;

import kala.annotations.StaticClass;
import kala.value.LazyValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

@StaticClass
@SuppressWarnings("unchecked")
public final class Suppliers {
    private Suppliers() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Supplier<T> of(Supplier<? extends T> supplier) {
        return (Supplier<T>) supplier;
    }

    public static <T> @NotNull Supplier<T> constant(T value) {
        return new Constant<>(value);
    }

    public static <T> @NotNull Supplier<T> memoized(@NotNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        if (supplier instanceof Memoized) {
            return (Supplier<T>) supplier;
        }
        return LazyValue.of(supplier);
    }

    private static final class Constant<T> implements Supplier<T>, Memoized, Serializable {
        private final T value;

        Constant(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public String toString() {
            return "Suppliers.Constant[value=" + value + ']';
        }
    }
}
