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
package kala.collection.mutable;

import kala.annotations.DelegateBy;
import kala.collection.Collection;
import kala.collection.immutable.ImmutableCollection;
import kala.collection.internal.convert.AsJavaConvert;
import kala.collection.factory.CollectionFactory;
import kala.function.CheckedConsumer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface MutableCollection<E> extends Collection<E>, MutableAnyCollection<E> {

    //region Static Factories

    static <E> @NotNull CollectionFactory<E, ?, MutableCollection<E>> factory() {
        return CollectionFactory.narrow(MutableSeq.factory());
    }

    static <E> @NotNull MutableCollection<E> of() {
        return MutableSeq.of();
    }

    @Contract("_ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1) {
        return MutableSeq.of(value1);
    }

    @Contract("_, _ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1, E value2) {
        return MutableSeq.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1, E value2, E value3) {
        return MutableSeq.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1, E value2, E value3, E value4) {
        return MutableSeq.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static <E> @NotNull MutableCollection<E> of(E value1, E value2, E value3, E value4, E value5) {
        return MutableSeq.of(value1, value2, value3, value4, value5);
    }

    @SafeVarargs
    static <E> @NotNull MutableCollection<E> of(E... values) {
        return from(values);
    }

    static <E> @NotNull MutableCollection<E> from(E @NotNull [] values) {
        return MutableSeq.from(values);
    }

    static <E> @NotNull MutableCollection<E> from(@NotNull Iterable<? extends E> values) {
        return MutableSeq.from(values);
    }

    static <E> @NotNull MutableCollection<E> from(@NotNull Iterator<? extends E> it) {
        return MutableSeq.from(it);
    }

    static <E> @NotNull MutableCollection<E> from(@NotNull Stream<? extends E> stream) {
        return MutableSeq.from(stream);
    }

    static <E, C extends MutableCollection<E>> @NotNull MutableCollectionEditor<E, C> edit(@NotNull C collection) {
        return new MutableCollectionEditor<>(collection);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "MutableCollection";
    }

    @Override
    default <U> @NotNull CollectionFactory<U, ?, ? extends MutableCollection<U>> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull java.util.Collection<E> asJava() {
        return new AsJavaConvert.MutableCollectionAsJava<>(this);
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    default @NotNull MutableCollection<E> clone() {
        return this.<E>iterableFactory().from(this);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("forEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull MutableCollection<E> onEach(@NotNull Consumer<? super E> action) {
        forEach(action);
        return this;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<E, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default <Ex extends Throwable> @NotNull MutableCollection<E> onEachChecked(@NotNull CheckedConsumer<? super E, ? extends Ex> action) throws Ex {
        return onEach(action);
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("onEach(Consumer<T, Ex>)")
    @Contract(value = "_ -> this", pure = true)
    default @NotNull MutableCollection<E> onEachUnchecked(@NotNull CheckedConsumer<? super E, ?> action) {
        return onEach(action);
    }
}
