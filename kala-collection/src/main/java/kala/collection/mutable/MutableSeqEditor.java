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

import kala.index.Index;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.Function;

public class MutableSeqEditor<E, C extends MutableSeq<E>> extends MutableCollectionEditor<E, C> {
    protected MutableSeqEditor(@NotNull C source) {
        super(source);
    }

    @Contract("_, _ -> this")
    public @NotNull MutableSeqEditor<E, C> set(@Index int index, E newValue) {
        source.set(index, newValue);
        return this;
    }

    @Contract("_, _ -> this")
    public @NotNull MutableSeqEditor<E, C> swap(@Index int index1, @Index int index2) {
        source.swap(index1, index2);
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSeqEditor<E, C> replaceAll(@NotNull Function<? super E, ? extends E> mapper) {
        source.replaceAll(mapper);
        return this;
    }

    @Contract("-> this")
    public @NotNull MutableSeqEditor<E, C> reverse() {
        source.reverse();
        return this;
    }

    @Contract("-> this")
    public @NotNull MutableSeqEditor<E, C> sort() {
        source.sort();
        return this;
    }

    @Contract("_ -> this")
    public @NotNull MutableSeqEditor<E, C> sort(@NotNull Comparator<? super E> comparator) {
        source.sort(comparator);
        return this;
    }
}
