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
package kala.collection.mutable.primitive;

import kala.collection.base.primitive.CharIterator;
import org.jetbrains.annotations.NotNull;

final class DefaultMutableCharSet extends AbstractMutableCharSet {

    private int size;
    private Node latin1;
    private Node root;

    @Override
    public @NotNull CharIterator iterator() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public boolean add(char value) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Override
    public boolean remove(char value) {
        throw new UnsupportedOperationException(); // TODO
    }

    private static final class Node {
        final short number;

        long bits0, bits1, bits2, bits3;

        Node parent, left, right;

        Node(short number) {
            this.number = number;
        }
    }
}
