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
package kala.collection.base;

import kala.annotations.Covariant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * A {@code Mappable} is a data structure that can be {@link #map(Function)} to another {@code Mappable}.
 *
 * @param <T> the type of value
 * @author Glavo
 */
public interface Mappable<@Covariant T> {

    /**
     * Returns a container consisting of the results of applying the given
     * function to the elements of this stream.
     *
     * @param <U>    The element type of the new container
     * @param mapper a non-interfering stateless function to apply to each element
     * @return the new container
     */
    @Contract(pure = true)
    <U> @NotNull Mappable<U> map(@NotNull Function<? super T, ? extends U> mapper);
}

