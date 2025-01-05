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
package kala.collection;

import kala.control.Option;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractSeq<E> extends AbstractCollection<E> implements Seq<E> {

    public E getFirst() {
        return Seq.super.getFirst();
    }

    public @Nullable E getFirstOrNull() {
        return Seq.super.getFirstOrNull();
    }

    public @NotNull Option<E> getFirstOption() {
        return Seq.super.getFirstOption();
    }

    public E getLast() {
        return Seq.super.getLast();
    }

    public @Nullable E getLastOrNull() {
        return Seq.super.getLastOrNull();
    }

    public @NotNull Option<E> getLastOption() {
        return Seq.super.getLastOption();
    }

    @Override
    public int hashCode() {
        return Seq.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnySeq<?> && Seq.equals(this, ((AnySeq<?>) obj));
    }
}
