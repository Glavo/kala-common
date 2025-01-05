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

import kala.control.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMutableList<E> extends AbstractMutableSeq<E> implements MutableList<E> {

    @Override
    public @NotNull MutableList<E> clone() {
        return MutableList.super.clone();
    }

    @Override
    public E removeFirst() {
        return MutableList.super.removeFirst();
    }

    @Override
    public @Nullable E removeFirstOrNull() {
        return MutableList.super.removeFirstOrNull();
    }

    @Override
    public @NotNull Option<E> removeFirstOption() {
        return MutableList.super.removeFirstOption();
    }

    @Override
    public E removeLast() {
        return MutableList.super.removeLast();
    }

    @Override
    public @Nullable E removeLastOrNull() {
        return MutableList.super.removeLastOrNull();
    }

    @Override
    public @NotNull Option<E> removeLastOption() {
        return MutableList.super.removeLastOption();
    }
}
