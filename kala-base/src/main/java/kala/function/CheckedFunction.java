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

import java.util.function.Function;

/**
 * Represents a function that accepts one argument and produces a result, may throw checked exception.
 *
 * <p>This is a functional interface whose functional method is {@link #applyChecked(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @author Glavo
 */
@FunctionalInterface
public interface CheckedFunction<T, R, Ex extends Throwable> extends Function<T, R> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, R, Ex extends Throwable> CheckedFunction<T, R, Ex> of(CheckedFunction<? super T, ? extends R, ? extends Ex> function) {
        return (CheckedFunction<T, R, Ex>) function;
    }

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R applyChecked(T t) throws Ex;

    @Override
    default R apply(T t) {
        try {
            return applyChecked(t);
        } catch (Throwable e) {
            return Try.sneakyThrow(e);
        }
    }
}
