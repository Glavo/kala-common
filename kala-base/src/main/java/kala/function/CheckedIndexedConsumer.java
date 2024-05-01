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
public interface CheckedIndexedConsumer<T, Ex extends Throwable> extends IndexedConsumer<T> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, Ex extends Throwable> CheckedIndexedConsumer<T, Ex> of(CheckedIndexedConsumer<? super T, ? extends Ex> consumer) {
        return (CheckedIndexedConsumer<T, Ex>) consumer;
    }

    void acceptChecked(int index, T t) throws Ex;

    @Override
    default void accept(int index, T t) {
        try {
            acceptChecked(index, t);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }
}
