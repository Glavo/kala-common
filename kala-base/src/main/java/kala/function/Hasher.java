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

import kala.annotations.ReplaceWith;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiPredicate;

@FunctionalInterface
@SuppressWarnings("unchecked")
public interface Hasher<T> extends BiPredicate<T, T> {

    static <T> Hasher<T> narrow(Hasher<? super T> hasher) {
        return (Hasher<T>) hasher;
    }

    static <T> Hasher<T> defaultHasher() {
        return (Hasher<T>) Hashers.DEFAULT;
    }

    static <T> Hasher<T> optimizedHasher() {
        return (Hasher<T>) Hashers.OPTIMIZED;
    }

    static <T> Hasher<T> identityHasher() {
        return (Hasher<T>) Hashers.IDENTITY;
    }

    int hash(T obj);

    default boolean equals(T t1, T t2) {
        return Objects.equals(t1, t2);
    }

    @Deprecated
    @ReplaceWith("equals(T, T)")
    default boolean test(T t1, T t2) {
        return equals(t1, t2);
    }
}
