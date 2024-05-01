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
public interface CheckedIndexedFunction<T, R, Ex extends Throwable> extends IndexedFunction<T, R> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, R, Ex extends Throwable> CheckedIndexedFunction<T, R, Ex> of(
            CheckedIndexedFunction<? super T, ? extends R, ? extends Ex> consumer) {
        return ((CheckedIndexedFunction<T, R, Ex>) consumer);
    }

    R applyChecked(int index, T t) throws Ex;

    @Override
    default R apply(int index, T t) {
        try {
            return applyChecked(index, t);
        } catch (Throwable ex) {
            return Try.sneakyThrow(ex);
        }
    }
}
