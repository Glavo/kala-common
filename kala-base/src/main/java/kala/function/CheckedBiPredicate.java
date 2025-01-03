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
import org.jetbrains.annotations.ApiStatus;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface CheckedBiPredicate<T, U, Ex extends Throwable> extends BiPredicate<T, U> {

    boolean testChecked(T t, U u) throws Ex;

    @ApiStatus.NonExtendable
    @Override
    default boolean test(T t, U u) {
        try {
            return testChecked(t, u);
        } catch (Throwable ex) {
            return Try.sneakyThrow(ex);
        }
    }
}
