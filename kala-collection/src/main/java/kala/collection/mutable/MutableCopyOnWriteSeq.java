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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MutableCopyOnWriteSeq<E> extends MutableCopyOnWriteSeqBase<E, MutableSeq<E>> {
    MutableCopyOnWriteSeq(MutableSeq<E> source, boolean exclusive) {
        super(source, exclusive);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableCopyOnWriteSeq<E> create(@NotNull CollectionFactory<E, ?, ? extends MutableSeq<E>> factory) {
        return new MutableCopyOnWriteSeq<>(factory.empty(), true);
    }

    @Override
    public @NotNull String className() {
        return "MutableCopyOnWriteSeq";
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableCopyOnWriteSeq<E> clone() {
        this.exclusive = false;
        return new MutableCopyOnWriteSeq<>(source, false);
    }
}
