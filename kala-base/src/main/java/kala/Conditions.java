package kala;

import kala.annotations.StaticClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
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
