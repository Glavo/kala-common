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

import kala.collection.internal.view.SeqViews;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class MutableArraySliceView<E> extends SeqViews.OfArraySlice<E> {
    MutableArraySliceView(Object[] array, int beginIndex, int endIndex) {
        super(array, beginIndex, endIndex);
    }

    @Override
    public @NotNull String className() {
        return "MutableArraySliceView";
    }

    public void set(int index, E value) {
        Objects.checkIndex(index, endIndex - beginIndex);
        array[beginIndex + index] = value;
    }

    @Override
    public @NotNull MutableArraySliceView<E> slice(@Index int beginIndex, @Index int endIndex) {
        final int size = this.size();
        beginIndex = Indexes.checkBeginIndex(beginIndex, size);
        endIndex = Indexes.checkEndIndex(beginIndex, endIndex, size);

        return new MutableArraySliceView<>(array, this.beginIndex + beginIndex, this.beginIndex + endIndex);
    }

    @Override
    public @NotNull MutableArraySliceView<E> sliceView(@Index int beginIndex, @Index int endIndex) {
        return slice(beginIndex, endIndex);
    }
}
