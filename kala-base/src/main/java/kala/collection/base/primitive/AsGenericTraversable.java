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
package kala.collection.base.primitive;

import kala.collection.base.Traversable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

final class AsGenericTraversable<T> implements Traversable<T> {
    private final PrimitiveTraversable<T> source;

    AsGenericTraversable(PrimitiveTraversable<T> source) {
        this.source = source;
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public int knownSize() {
        return source.knownSize();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return source.iterator();
    }

    @Override
    public String toString() {
        return "AsGenericTraversable[" + source.toString() + "]";
    }
}
