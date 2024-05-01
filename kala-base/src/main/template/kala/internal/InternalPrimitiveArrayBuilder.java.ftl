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
package kala.internal;

import kala.collection.base.primitive.*;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public final class Internal${Type}ArrayBuilder {
    private static final int DEFAULT_CAPACITY = 16;

    private ${PrimitiveType}[] elements = ${Type}Arrays.EMPTY;
    private int size = 0;

    private void grow() {
        int oldCapacity = elements.length;

        ${PrimitiveType}[] newArray;
        if (oldCapacity == 0) {
            newArray = new ${PrimitiveType}[Math.max(DEFAULT_CAPACITY, size + 1)];
        } else {
            int newCapacity = Math.max(Math.max(oldCapacity, size + 1), oldCapacity + (oldCapacity >> 1));
            newArray = new ${PrimitiveType}[newCapacity];
        }

        if (elements.length != 0) {
            System.arraycopy(elements, 0, newArray, 0, size);
        }
        elements = newArray;
    }

    public int size() {
        return size;
    }

    public ${PrimitiveType} get(int idx) {
        return elements[idx];
    }

    public void set(int idx, ${PrimitiveType} newValue) {
        elements[idx] = newValue;
    }

    public void append(${PrimitiveType} value) {
        if (size == elements.length) {
            grow();
        }
        elements[size++] = value;
    }

    public ${PrimitiveType}[] toArray() {
        return elements.length == size ? elements : Arrays.copyOf(elements, size);
    }

    public ${Type}Iterator iterator() {
        return ${Type}Arrays.iterator(elements, 0, size);
    }
}
