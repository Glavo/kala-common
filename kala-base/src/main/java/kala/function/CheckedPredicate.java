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

import java.util.function.Predicate;

@FunctionalInterface
public interface CheckedPredicate<T, Ex extends Throwable> extends Predicate<T> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, Ex extends Throwable> CheckedPredicate<T, Ex> of(
            CheckedPredicate<? super T, ? extends Ex> consumer) {
        return ((CheckedPredicate<T, Ex>) consumer);
    }

    boolean testChecked(T t) throws Ex;

    @Override
    default boolean test(T t) {
        try {
            return testChecked(t);
        } catch (Throwable ex) {
            return Try.sneakyThrow(ex);
        }
    }
}
