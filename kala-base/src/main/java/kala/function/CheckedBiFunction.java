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

import java.util.function.BiFunction;

@FunctionalInterface
public interface CheckedBiFunction<T, U, R, Ex extends Throwable> extends BiFunction<T, U, R> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, U, R, Ex extends Throwable> CheckedBiFunction<T, U, R, Ex> of(CheckedBiFunction<? super T, ? super U, ? extends R, ? extends Ex> function) {
        return (CheckedBiFunction<T, U, R, Ex>) function;
    }

    R applyChecked(T t, U u) throws Ex;

    @Override
    default R apply(T t, U u) {
        try {
            return applyChecked(t, u);
        } catch (Throwable e) {
            return Try.sneakyThrow(e);
        }
    }
}
