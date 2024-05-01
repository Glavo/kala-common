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
package kala;

import kala.annotations.StaticClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.function.BooleanSupplier;
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
        throw new AssertionError();
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
        throw new AssertionError();
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

    //region Equals

    public static boolean equals(boolean v1, boolean v2) {
        return v1 == v2;
    }

    public static boolean equals(char v1, char v2) {
        return v1 == v2;
    }

    public static boolean equals(byte v1, byte v2) {
        return v1 == v2;
    }

    public static boolean equals(short v1, short v2) {
        return v1 == v2;
    }

    public static boolean equals(int v1, int v2) {
        return v1 == v2;
    }

    public static boolean equals(long v1, long v2) {
        return v1 == v2;
    }

    public static boolean equals(float v1, float v2) {
        return Float.floatToIntBits(v1) == Float.floatToIntBits(v2);
    }

    public static boolean equals(double v1, double v2) {
        return Double.doubleToLongBits(v1) == Double.doubleToLongBits(v2);
    }

    public static boolean equals(Object v1, boolean v2) {
        return v1 instanceof Boolean && (Boolean) v1 == v2;
    }

    public static boolean equals(Object v1, char v2) {
        return v1 instanceof Character && (Character) v1 == v2;
    }

    public static boolean equals(Object v1, byte v2) {
        return v1 instanceof Byte && (Byte) v1 == v2;
    }

    public static boolean equals(Object v1, short v2) {
        return v1 instanceof Short && (Short) v1 == v2;
    }

    public static boolean equals(Object v1, int v2) {
        return v1 instanceof Integer && (Integer) v1 == v2;
    }

    public static boolean equals(Object v1, long v2) {
        return v1 instanceof Long && (Long) v1 == v2;
    }

    public static boolean equals(Object v1, float v2) {
        return v1 instanceof Float && Float.floatToIntBits((Float) v1) == Float.floatToIntBits(v2);
    }

    public static boolean equals(Object v1, double v2) {
        return v1 instanceof Double && Double.doubleToLongBits((Double) v1) == Double.doubleToLongBits(v2);
    }

    public static boolean equals(boolean v1, Object v2) {
        return v2 instanceof Boolean && (Boolean) v2 == v1;
    }

    public static boolean equals(char v1, Object v2) {
        return v2 instanceof Character && (Character) v2 == v1;
    }

    public static boolean equals(byte v1, Object v2) {
        return v2 instanceof Byte && (Byte) v2 == v1;
    }

    public static boolean equals(short v1, Object v2) {
        return v2 instanceof Short && (Short) v2 == v1;
    }

    public static boolean equals(int v1, Object v2) {
        return v2 instanceof Integer && (Integer) v2 == v1;
    }

    public static boolean equals(long v1, Object v2) {
        return v2 instanceof Long && (Long) v2 == v1;
    }

    public static boolean equals(float v1, Object v2) {
        return v2 instanceof Float && Float.floatToIntBits((Float) v2) == Float.floatToIntBits(v1);
    }

    public static boolean equals(double v1, Object v2) {
        return v2 instanceof Double && Double.doubleToLongBits((Double) v2) == Double.doubleToLongBits(v1);
    }

    public static boolean equals(Object v1, Object v2) {
        //noinspection EqualsReplaceableByObjectsCall
        return v1 == v2 || v1 != null && v1.equals(v2);
    }

    //endregion

    //region Assertions

    private static final boolean isAssertionEnabled;

    static {
        boolean b = false;
        //noinspection AssertWithSideEffects
        assert b = true;
        //noinspection ConstantConditions
        isAssertionEnabled = b;
    }

    public static boolean isAssertionEnabled() {
        return isAssertionEnabled;
    }

    //region assertFail

    public static void assertFail() {
        if (isAssertionEnabled) {
            throw new AssertionError("assertion failed");
        }
    }

    public static void assertFail(String message) {
        if (isAssertionEnabled) {
            throw new AssertionError(message);
        }
    }

    public static void assertFail(Throwable cause) {
        if (isAssertionEnabled) {
            throw new AssertionError("assertion failed", cause);
        }
    }

    public static void assertFail(String message, Throwable cause) {
        if (isAssertionEnabled) {
            throw new AssertionError(message, cause);
        }
    }

    public static void assertFail(@NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            throw new AssertionError(messageSupplier.get());
        }
    }

    public static void assertFail(@NotNull Supplier<?> messageSupplier, Throwable cause) {
        if (isAssertionEnabled) {
            throw new AssertionError(Objects.toString(messageSupplier.get()), cause);
        }
    }

    //endregion

    //region assertTrue

    public static void assertTrue(boolean condition) {
        if (isAssertionEnabled) {
            if (!condition) {
                throw new AssertionError("assertion failed");
            }
        }
    }

    public static void assertTrue(boolean condition, Object message) {
        if (isAssertionEnabled) {
            if (!condition) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertTrue(boolean condition, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (!condition) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    public static void assertTrue(@NotNull BooleanSupplier condition) {
        if (isAssertionEnabled) {
            if (!condition.getAsBoolean()) {
                throw new AssertionError("assertion failed");
            }
        }
    }

    public static void assertTrue(@NotNull BooleanSupplier condition, Object message) {
        if (isAssertionEnabled) {
            if (!condition.getAsBoolean()) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertTrue(@NotNull BooleanSupplier condition, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (!condition.getAsBoolean()) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    //endregion

    //region assertFalse

    public static void assertFalse(boolean condition) {
        if (isAssertionEnabled) {
            if (condition) {
                throw new AssertionError("assertion failed");
            }
        }
    }

    public static void assertFalse(boolean condition, Object message) {
        if (isAssertionEnabled) {
            if (condition) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertFalse(boolean condition, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (condition) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    public static void assertFalse(@NotNull BooleanSupplier condition) {
        if (isAssertionEnabled) {
            if (condition.getAsBoolean()) {
                throw new AssertionError("assertion failed");
            }
        }
    }

    public static void assertFalse(@NotNull BooleanSupplier condition, Object message) {
        if (isAssertionEnabled) {
            if (condition.getAsBoolean()) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertFalse(@NotNull BooleanSupplier condition, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (condition.getAsBoolean()) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    //endregion

    //region assertNull

    public static void assertNull(Object actual) {
        if (isAssertionEnabled) {
            if (actual != null) {
                throw new AssertionError("Expected value to be null, but was: <" + actual + ">");
            }
        }
    }

    public static void assertNull(Object actual, Object message) {
        if (isAssertionEnabled) {
            if (actual != null) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertNull(Object actual, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (actual != null) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    public static void assertNull(@NotNull Supplier<?> actualSupplier) {
        if (isAssertionEnabled) {
            final Object actual = actualSupplier.get();
            if (actual != null) {
                throw new AssertionError("Expected value to be null, but was: <" + actual + ">");
            }
        }
    }

    public static void assertNull(@NotNull Supplier<?> actualSupplier, Object message) {
        if (isAssertionEnabled) {
            final Object actual = actualSupplier.get();
            if (actual != null) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertNull(@NotNull Supplier<?> actualSupplier, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            final Object actual = actualSupplier.get();
            if (actual != null) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    //endregion

    //region assertNotNull

    public static void assertNotNull(Object actual) {
        if (isAssertionEnabled) {
            if (actual == null) {
                throw new AssertionError("Expected value to be not null");
            }
        }
    }

    public static void assertNotNull(Object actual, Object message) {
        if (isAssertionEnabled) {
            if (actual == null) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertNotNull(Object actual, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (actual == null) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    public static void assertNotNull(@NotNull Supplier<?> actualSupplier) {
        if (isAssertionEnabled) {
            final Object actual = actualSupplier.get();
            if (actual == null) {
                throw new AssertionError("Expected value to be not null");
            }
        }
    }

    public static void assertNotNull(@NotNull Supplier<?> actualSupplier, Object message) {
        if (isAssertionEnabled) {
            final Object actual = actualSupplier.get();
            if (actual == null) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertNotNull(@NotNull Supplier<?> actualSupplier, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            final Object actual = actualSupplier.get();
            if (actual == null) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    //endregion

    //region assertEquals

    public static void assertEquals(Object expected, Object actual) {
        if (isAssertionEnabled) {
            if (!Objects.equals(expected, actual)) {
                throw new AssertionError("Expected < " + expected + ">, actual <" + actual + ">");
            }
        }
    }

    public static void assertEquals(Object expected, Object actual, Object message) {
        if (isAssertionEnabled) {
            if (!Objects.equals(expected, actual)) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertEquals(Object expected, Object actual, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (!Objects.equals(expected, actual)) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    public static void assertEquals(float expected, float actual, float absoluteTolerance) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Float.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }


            if (!(Float.floatToIntBits(expected) == Float.floatToIntBits(actual) || Math.abs(expected - actual) <= absoluteTolerance)) {
                throw new AssertionError("Expected < " + expected + ">, actual <" + actual + ">");
            }
        }
    }

    public static void assertEquals(float expected, float actual, float absoluteTolerance, Object message) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Float.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }


            if (!(Float.floatToIntBits(expected) == Float.floatToIntBits(actual) || Math.abs(expected - actual) <= absoluteTolerance)) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertEquals(float expected, float actual, float absoluteTolerance, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Float.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }


            if (!(Float.floatToIntBits(expected) == Float.floatToIntBits(actual) || Math.abs(expected - actual) <= absoluteTolerance)) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    public static void assertEquals(double expected, double actual, double absoluteTolerance) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Double.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }


            if (!(Double.doubleToLongBits(expected) == Double.doubleToLongBits(actual) || Math.abs(expected - actual) <= absoluteTolerance)) {
                throw new AssertionError("Expected < " + expected + ">, actual <" + actual + ">");
            }
        }
    }

    public static void assertEquals(double expected, double actual, double absoluteTolerance, Object message) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Double.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }

            if (!(Double.doubleToLongBits(expected) == Double.doubleToLongBits(actual) || Math.abs(expected - actual) <= absoluteTolerance)) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertEquals(double expected, double actual, double absoluteTolerance, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Double.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }

            if (!(Double.doubleToLongBits(expected) == Double.doubleToLongBits(actual) || Math.abs(expected - actual) <= absoluteTolerance)) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    //endregion

    //region assertNotEquals

    public static void assertNotEquals(Object illegal, Object actual) {
        if (isAssertionEnabled) {
            if (Objects.equals(illegal, actual)) {
                throw new AssertionError("Illegal value: <" + actual + ">");
            }
        }
    }

    public static void assertNotEquals(Object illegal, Object actual, Object message) {
        if (isAssertionEnabled) {
            if (Objects.equals(illegal, actual)) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertNotEquals(Object illegal, Object actual, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (Objects.equals(illegal, actual)) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    public static void assertNotEquals(float illegal, float actual, float absoluteTolerance) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Float.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }

            if (Float.floatToIntBits(illegal) == Float.floatToIntBits(actual) || Math.abs(illegal - actual) <= absoluteTolerance) {
                throw new AssertionError("Illegal value: <" + actual + ">");
            }
        }
    }

    public static void assertNotEquals(float illegal, float actual, float absoluteTolerance, Object message) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Float.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }


            if (Float.floatToIntBits(illegal) == Float.floatToIntBits(actual) || Math.abs(illegal - actual) <= absoluteTolerance) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertNotEquals(float illegal, float actual, float absoluteTolerance, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Float.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }


            if (Float.floatToIntBits(illegal) == Float.floatToIntBits(actual) || Math.abs(illegal - actual) <= absoluteTolerance) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    public static void assertNotEquals(double illegal, double actual, double absoluteTolerance) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Double.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }


            if (Double.doubleToLongBits(illegal) == Double.doubleToLongBits(actual) || Math.abs(illegal - actual) <= absoluteTolerance) {
                throw new AssertionError("Illegal value: <" + actual + ">");
            }
        }
    }

    public static void assertNotEquals(double illegal, double actual, double absoluteTolerance, Object message) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Double.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }

            if (Double.doubleToLongBits(illegal) == Double.doubleToLongBits(actual) || Math.abs(illegal - actual) <= absoluteTolerance) {
                throw new AssertionError(message);
            }
        }
    }

    public static void assertNotEquals(double illegal, double actual, double absoluteTolerance, @NotNull Supplier<?> messageSupplier) {
        if (isAssertionEnabled) {
            if (absoluteTolerance < 0.0) {
                throw new IllegalArgumentException("Illegal negative absolute tolerance <" + absoluteTolerance + '>');
            }
            if (Double.isNaN(absoluteTolerance)) {
                throw new IllegalArgumentException("Illegal NaN absolute tolerance <" + absoluteTolerance + ">");
            }

            if (Double.doubleToLongBits(illegal) == Double.doubleToLongBits(actual) || Math.abs(illegal - actual) <= absoluteTolerance) {
                throw new AssertionError(messageSupplier.get());
            }
        }
    }

    //endregion

    //endregion
}
