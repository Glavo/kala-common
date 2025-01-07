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

import kala.collection.base.OrderedTraversable;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.IntFunction;

abstract class AbstractMutableListSet<E, L extends MutableList<E>> extends AbstractMutableSet<E>
        implements OrderedTraversable<E>, Serializable {
    @Serial
    private static final long serialVersionUID = 0L;

    protected final L values;

    AbstractMutableListSet(L values) {
        this.values = values;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return values.iterator();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public int size() {
        return values.size();
    }

    //region Modification Operations

    @Override
    public boolean add(E value) {
        if (values.contains(value))
            return false;

        values.append(value);
        return true;
    }

    @Override
    public boolean remove(Object value) {
        return values.remove(value);
    }

    @Override
    public void clear() {
        values.clear();
    }

    //endregion

    //region Element Conditions

    @Override
    public boolean contains(Object value) {
        return values.contains(value);
    }

    //endregion

    @Override
    public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        return values.toArray(generator);
    }
}
