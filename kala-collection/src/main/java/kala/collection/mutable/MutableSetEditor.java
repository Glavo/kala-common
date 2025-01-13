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

import java.util.function.Predicate;

public class MutableSetEditor<E, C extends MutableSet<E>> extends MutableCollectionEditor<E, C> {
    public MutableSetEditor(@NotNull C source) {
        super(source);
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> add(E value) {
        source.add(value);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> addAll(@NotNull Iterable<? extends E> values) {
        source.addAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> addAll(E @NotNull [] values) {
        source.addAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> remove(E value) {
        source.remove(value);
        return this;
    }

    @Contract("-> this")
    public @NotNull MutableSetEditor<E, C> clear() {
        source.clear();
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> removeAll(@NotNull Iterable<?> values) {
        source.removeAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> removeAll(Object @NotNull [] values) {
        source.removeAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> removeIf(@NotNull Predicate<? super E> predicate) {
        source.removeIf(predicate);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> retainAll(@NotNull Iterable<?> values) {
        source.retainAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> retainAll(Object @NotNull [] values) {
        source.retainAll(values);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSetEditor<E, C> retainIf(@NotNull Predicate<? super E> predicate) {
        source.retainIf(predicate);
        return this;
    }
}
