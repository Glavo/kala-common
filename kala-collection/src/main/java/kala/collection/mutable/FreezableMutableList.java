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

import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableSeq;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface FreezableMutableList<E> extends MutableList<E> {

    @Contract("-> new")
    static <E> FreezableMutableList<E> create() {
        return MutableCopyOnWriteList.create();
    }

    @Contract("_ -> new")
    static <E> @NotNull FreezableMutableList<E> create(@NotNull CollectionFactory<E, ?, ? extends MutableList<E>> factory) {
        return MutableCopyOnWriteList.create(factory);
    }

    @NotNull ImmutableSeq<E> freeze();

    @Override
    default @NotNull ImmutableSeq<E> toSeq() {
        return freeze();
    }
}
