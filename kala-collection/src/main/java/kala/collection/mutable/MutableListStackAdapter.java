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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Iterator;

final class MutableListStackAdapter<E> implements MutableStack<E>, Serializable {
    private static final long serialVersionUID = -146156819892776856L;

    private final @NotNull MutableList<E> seq;

    MutableListStackAdapter(@NotNull MutableList<E> seq) {
        this.seq = seq;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return seq.iterator();
    }

    @Override
    public @NotNull MutableStack<E> clone() {
        return new MutableListStackAdapter<>(seq.clone());
    }

    @Override
    public int size() {
        return seq.size();
    }

    @Override
    public void push(E value) {
        seq.append(value);
    }

    @Override
    public E pop() {
        return seq.removeLast();
    }

    @Override
    public E peek() {
        return seq.getLast();
    }

    @Override
    public boolean isEmpty() {
        return seq.isEmpty();
    }

    @Override
    public String toString() {
        return "MutableListStackAdapter[" + seq + ']';
    }
}
