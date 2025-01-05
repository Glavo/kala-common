/*
 * Copyright 2025 Glavo
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
package kala.collection.mutable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public interface MutableStack<E> extends MutableCollection<E> {

    @Contract("-> new")
    static <E> @NotNull MutableStack<E> create() {
        return MutableArrayList.create();
    }

    // boolean isEmpty();

    @Contract(mutates = "this")
    void push(E value);

    @Contract(mutates = "this")
    E pop();

    E peek();
}
