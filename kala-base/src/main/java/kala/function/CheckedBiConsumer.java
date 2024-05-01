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

import java.util.function.BiConsumer;

public interface CheckedBiConsumer<T, U, Ex extends Throwable> extends BiConsumer<T, U> {
    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, U, Ex extends Throwable> CheckedBiConsumer<T, U, Ex> of(
            CheckedBiConsumer<? super T, ? super U, ? extends Ex> consumer
    ) {
        return (CheckedBiConsumer<T, U, Ex>) consumer;
    }

    void acceptChecked(T t, U u) throws Ex;

    @Override
    default void accept(T t, U u) {
        try {
            acceptChecked(t, u);
        } catch (Throwable ex) {
            Try.sneakyThrow(ex);
        }
    }
}
