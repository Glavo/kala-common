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

import kala.function.IndexedFunction;
import kala.index.Index;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

sealed class MutableCopyOnWriteSeqBase<E, S extends MutableSeq<E>> extends AbstractMutableSeq<E>
        permits MutableCopyOnWriteList, MutableCopyOnWriteSeq {
    S source;
    boolean exclusive = true;

    MutableCopyOnWriteSeqBase(S source, boolean exclusive) {
        this.source = source;
        this.exclusive = exclusive;
    }

    @SuppressWarnings("unchecked")
    void ensureExclusive() {
        if (!exclusive) {
            source = (S) source.clone();
            exclusive = true;
        }
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return source.iterator();
    }

    @Override
    public @NotNull Iterator<E> iterator(@Index int beginIndex) {
        return source.iterator(beginIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        return source.spliterator();
    }

    @Override
    public @NotNull Stream<E> stream() {
        return source.stream();
    }

    @Override
    public @NotNull Stream<E> parallelStream() {
        return source.parallelStream();
    }

    //region Size Info


    @Override
    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public int knownSize() {
        return source.knownSize();
    }

    //endregion

    @Override
    public void set(@Index int index, E newValue) {
        ensureExclusive();
        source.set(index, newValue);
    }

    @Override
    public void swap(int index1, int index2) {
        ensureExclusive();
        source.swap(index1, index2);
    }

    @Override
    public void replaceAll(@NotNull Function<? super E, ? extends E> operator) {
        ensureExclusive();
        source.replaceAll(operator);
    }

    @Override
    public void replaceAllIndexed(@NotNull IndexedFunction<? super E, ? extends E> operator) {
        ensureExclusive();
        source.replaceAllIndexed(operator);
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        ensureExclusive();
        source.sort(comparator);
    }

    @Override
    public void reverse() {
        ensureExclusive();
        source.reverse();
    }

    @Override
    public void shuffle(@NotNull RandomGenerator random) {
        ensureExclusive();
        source.shuffle(random);
    }
}
