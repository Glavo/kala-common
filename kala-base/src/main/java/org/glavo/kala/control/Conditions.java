package org.glavo.kala.control;

import org.glavo.kala.annotations.StaticClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.Supplier;

@StaticClass
public final class Conditions {
    private Conditions() {
    }

    public static void checkElementIndex(int index, @Range(from = 0, to = Integer.MAX_VALUE) int size) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            // Optimized for execution by hotspot
            checkElementIndexFailed(index, size);
        }
    }

    @Contract("_, _ -> fail")
    private static void checkElementIndexFailed(int index, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size(" + size + ") < 0");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("index(" + index + ") < 0");
        }
        if (index >= size) {
            throw new IndexOutOfBoundsException("index(" + index + ") >= size(" + size + ")");
        }
        assert false;
    }

    public static void checkPositionIndex(int index, @Range(from = 0, to = Integer.MAX_VALUE) int size) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            // Optimized for execution by hotspot
            checkPositionIndexFailed(index, size);
        }
    }

    @Contract("_, _ -> fail")
    private static void checkPositionIndexFailed(int index, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size(" + size + ") < 0");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("index(" + index + ") < 0");
        }
        if (index > size) {
            throw new IndexOutOfBoundsException("index(" + index + ") > size(" + size + ")");
        }
        assert false;
    }

    public static void checkPositionIndices(int beginIndex, int endIndex,
                                            @Range(from = 0, to = Integer.MAX_VALUE) int size)
            throws IndexOutOfBoundsException, IllegalArgumentException {
        if (beginIndex < 0 || beginIndex > endIndex || endIndex > size) {
            // Optimized for execution by hotspot
            checkPositionIndicesFailed(beginIndex, endIndex, size);
        }
    }

    @Contract("_, _, _ -> fail")
    private static void checkPositionIndicesFailed(int beginIndex, int endIndex, int size) {
        if (size < 0) {
            throw new IllegalArgumentException("size(" + size + ") < 0");
        }
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") < 0");
        }
        if (beginIndex > size) {
            throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") > size(" + size + ")");
        }
        if (endIndex < 0) {
            throw new IndexOutOfBoundsException("endIndex(" + endIndex + ") < 0");
        }
        if (endIndex > size) {
            throw new IndexOutOfBoundsException("endIndex(" + endIndex + ") > size(" + size + ")");
        }
        if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex(" + beginIndex + ") > endIndex(" + endIndex + ")");
        }

        throw new AssertionError("checkPositionIndicesFailed");
    }
}
