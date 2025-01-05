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

import kala.control.Option;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;

public interface MutableQueue<E> extends MutableCollection<E> {

    @Contract("-> new")
    static <E> MutableQueue<E> create() {
        return new MutableLinkedList<>();
    }

    static <E> @NotNull MutableQueue<E> wrapJava(@NotNull java.util.Queue<E> queue) {
        Objects.requireNonNull(queue);

        return new MutableQueue<E>() {

            @Override
            public @NotNull MutableCollection<E> clone() {
                return MutableCollection.from(queue);
            }

            @Override
            public @NotNull Iterator<E> iterator() {
                return queue.iterator();
            }

            @Override
            public boolean isEmpty() {
                return queue.isEmpty();
            }

            @Override
            public void enqueue(E value) {
                queue.add(value);
            }

            @Override
            public @NotNull Option<E> dequeueOption() {
                return queue.isEmpty() ? Option.none() : Option.some(queue.remove());
            }
        };
    }

    @Override
    default @NotNull String className() {
        return "MutableQueue";
    }

    boolean isEmpty();

    void enqueue(E value);

    default E dequeue() {
        return dequeueOption().get();
    }

    default @Nullable E dequeueOrNull() {
        return dequeueOption().getOrNull();
    }

    @NotNull Option<E> dequeueOption();
}
