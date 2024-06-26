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

import kala.annotations.StaticClass;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@StaticClass
@SuppressWarnings("unchecked")
public final class Consumers {
    private Consumers() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Consumer<T> of(Consumer<? super T> supplier) {
        return (Consumer<T>) supplier;
    }

    public static <T> @NotNull Consumer<T> noop() {
        return (Consumer<T>) Noop.INSTANCE;
    }

    public static <T, U> @NotNull Consumer<Tuple2<T, U>> tupled(@NotNull BiConsumer<? super T, ? super U> biConsumer) {
        Objects.requireNonNull(biConsumer);

        return tuple -> biConsumer.accept(tuple.component1(), tuple.component2());
    }

    public static <T, U> @NotNull BiConsumer<T, U> untupled(@NotNull Consumer<? super Tuple2<? extends T, ? extends U>> consumer) {
        Objects.requireNonNull(consumer);

        return (t, u) -> consumer.accept(Tuple.of(t, u));
    }

    private enum Noop implements Consumer<Object> {
        INSTANCE;

        @Override
        public void accept(Object o) {
            // noop
        }

        @Override
        public @NotNull Consumer<Object> andThen(@NotNull Consumer<? super Object> after) {
            return after;
        }

        @Override
        public final String toString() {
            return "Consumers.Noop";
        }
    }
}
