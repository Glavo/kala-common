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

/**
 * Represents an operation that accepts an index and an object-valued argument, and returns no result.
 *
 * <p>This is a functional interface whose functional method is {@link #accept(long, Object)}.
 *
 * @param <T> the type of the input to the operation
 * @author Glavo
 */
@FunctionalInterface
public interface LargeIndexedConsumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param index the index of {@code t} in the container
     * @param t     the input argument
     */
    void accept(long index, T t);
}
