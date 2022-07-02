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
