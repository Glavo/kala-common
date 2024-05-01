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

import kala.control.Try;
import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface CheckedTriFunction<T, U, V, R, Ex extends Throwable> extends TriFunction<T, U, V, R> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, U, V, R, Ex extends Throwable> CheckedTriFunction<T, U, V, R, Ex> of(
            CheckedTriFunction<? super T, ? super U, ? super V, ? extends R, ? extends Ex> function) {
        return (CheckedTriFunction<T, U, V, R, Ex>) function;
    }

    R applyChecked(T t, U u, V v) throws Ex;

    @Override
    default R apply(T t, U u, V v) {
        try {
            return applyChecked(t, u, v);
        } catch (Throwable e) {
            return Try.sneakyThrow(e);
        }
    }
}
