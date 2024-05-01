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
package kala.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public sealed interface EmptyTuple extends Tuple permits Unit {
    @Override
    default int arity() {
        return 0;
    }

    @Override
    default <U> U elementAt(int index) {
        throw new IndexOutOfBoundsException("EmptyTuple.elementAt()");
    }

    @Override
    default <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        return generator.apply(0);
    }

    @Override
    default <H> @NotNull Tuple1<H> cons(H head) {
        return new Tuple1<>(head);
    }
}
