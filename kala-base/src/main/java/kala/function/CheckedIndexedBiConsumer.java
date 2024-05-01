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
public interface CheckedIndexedBiConsumer<T, U, Ex extends Throwable> extends IndexedBiConsumer<T, U> {
    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, U, Ex extends Throwable> CheckedIndexedBiConsumer<T, U, Ex> of(CheckedIndexedBiConsumer<? super T, ? super U, ? extends Ex> consumer) {
        return (CheckedIndexedBiConsumer<T, U, Ex>) consumer;
    }

    void acceptChecked(int index, T t, U u) throws Ex;

    @Override
    default void accept(int index, T t, U u) {
        try {
            acceptChecked(index, t, u);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }
}
