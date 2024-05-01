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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface IndexedBiFunction<T, U, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param index the index
     * @param t     the first function argument
     * @param u     the second function argument
     * @return the function result
     */
    R apply(int index, T t, U u);

    default <V> @NotNull IndexedBiFunction<T, U, V> andThen(@NotNull Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (IndexedBiFunction<T, U, V> & Serializable) (index, t, u) -> after.apply(apply(index, t, u));
    }
}
