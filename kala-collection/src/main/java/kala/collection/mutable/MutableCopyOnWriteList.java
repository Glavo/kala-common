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
import kala.collection.internal.convert.AsImmutableConvert;
import kala.index.Index;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MutableCopyOnWriteList<E> extends MutableCopyOnWriteSeqBase<E, MutableList<E>> implements FreezableMutableList<E> {
    MutableCopyOnWriteList(MutableList<E> source, boolean exclusive) {
        super(source, exclusive);
    }

    @Contract("-> new")
    public static <E> @NotNull MutableCopyOnWriteList<E> create() {
        return new MutableCopyOnWriteList<>(new MutableArrayList<>(), true);
    }

    @Contract("_ -> new")
    public static <E> @NotNull MutableCopyOnWriteList<E> create(@NotNull CollectionFactory<E, ?, ? extends MutableList<E>> factory) {
        return new MutableCopyOnWriteList<>(factory.empty(), true);
    }

    @Override
    public @NotNull String className() {
        return "MutableCopyOnWriteList";
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull MutableCopyOnWriteList<E> clone() {
        this.exclusive = false;
        return new MutableCopyOnWriteList<>(source, false);
    }

    @Override
    public void append(E value) {
        ensureExclusive();
        source.append(value);
    }

    @Override
    public void appendAll(E... values) {
        ensureExclusive();
        source.appendAll(values);
    }

    @Override
    public void appendAll(@NotNull Iterable<? extends E> values) {
        ensureExclusive();
        source.appendAll(values);
    }

    @Override
    public void prepend(E value) {
        ensureExclusive();
        source.prepend(value);
    }

    @Override
    public void prependAll(E... values) {
        ensureExclusive();
        source.prependAll(values);
    }

    @Override
    public void prependAll(@NotNull Iterable<? extends E> values) {
        ensureExclusive();
        source.prependAll(values);
    }

    @Override
    public void insert(@Index int index, E value) {
        ensureExclusive();
        source.insert(index, value);
    }

    @Override
    public void insertAll(@Index int index, E... values) {
        ensureExclusive();
        source.insertAll(index, values);
    }

    @Override
    public void insertAll(@Index int index, @NotNull Iterable<? extends E> values) {
        ensureExclusive();
        source.insertAll(index, values);
    }

    @Override
    public E removeAt(@Index int index) {
        ensureExclusive();
        return source.removeAt(index);
    }

    @Override
    public void removeInRange(@Index int beginIndex, @Index int endIndex) {
        ensureExclusive();
        source.removeInRange(beginIndex, endIndex);
    }

    @Override
    public void clear() {
        if (exclusive) {
            source.clear();
        } else {
            source = source.<E>iterableFactory().empty();
            exclusive = true;
        }
    }

    @Override
    public @NotNull ImmutableSeq<E> freeze() {
        this.exclusive = false;
        return new AsImmutableConvert.SeqWrapper<>(source);
    }
}
