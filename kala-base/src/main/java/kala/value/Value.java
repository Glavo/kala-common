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
package kala.value;

import kala.annotations.UnstableName;
import kala.collection.base.Iterators;
import kala.collection.base.Mappable;
import kala.collection.base.Traversable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public non-sealed interface Value<T> extends AnyValue<T>, Traversable<T>, Mappable<T>, Supplier<T> {

    static <T> @NotNull Value<T> of(T value) {
        return new DefaultValue<>(value);
    }

    @UnstableName
    static <T> @NotNull Value<T> by(@NotNull Supplier<? extends T> getter) {
        Objects.requireNonNull(getter);
        return new DelegateValue<>(getter);
    }

    static <T> @NotNull LazyValue<T> lazy(@NotNull Supplier<? extends T> getter) {
        return LazyValue.of(getter);
    }

    T get();

    @Override
    default T getValue() {
        return get();
    }

    default <U> @NotNull Value<U> map(@NotNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return Value.by(() -> mapper.apply(get()));
    }

    default @NotNull MutableValue<T> asMutable(@NotNull Consumer<? super T> setter) {
        Objects.requireNonNull(setter);
        return new DelegateMutableValue<>(this, setter);
    }

    // Traversable

    @Override
    default @NotNull Iterator<T> iterator() {
        return Iterators.of(get());
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default int size() {
        return 1;
    }

    @Override
    default int knownSize() {
        return 1;
    }

    @Override
    default void forEach(@NotNull Consumer<? super T> action) {
        action.accept(get());
    }
}
