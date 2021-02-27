package org.glavo.kala.collection.base;

import org.glavo.kala.annotations.StaticClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

/**
 * Array operations based on {@code Object[]}.
 * <p>
 * These operations do not require reflection or generators to construct arrays,
 * so they are faster than operations in {@link GenericArrays}.
 */
@StaticClass
@SuppressWarnings("unchecked")
public final class ObjectArrays {
    private ObjectArrays() {
    }

    public static final Object[] EMPTY = GenericArrays.EMPTY_OBJECT_ARRAY;

    public static Object max(Object @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            throw new NoSuchElementException();
        }

        Object e = array[0];
        for (int i = 1; i < length; i++) {
            Object v = array[i];
            if (((Comparable<Object>) e).compareTo(v) < 0) {
                e = v;
            }
        }
        return e;
    }

    public static @Nullable Object maxOrNull(Object @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            return null;
        }

        Object e = array[0];
        for (int i = 1; i < length; i++) {
            Object v = array[i];
            if (((Comparable<Object>) e).compareTo(v) < 0) {
                e = v;
            }
        }
        return e;
    }

    public static Object min(Object @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            throw new NoSuchElementException();
        }

        Object e = array[0];
        for (int i = 1; i < length; i++) {
            Object v = array[i];
            if (((Comparable<Object>) e).compareTo(v) > 0) {
                e = v;
            }
        }
        return e;
    }

    public static @Nullable Object minOrNull(Object @NotNull [] array) {
        final int length = array.length;
        if (length == 0) {
            return null;
        }

        Object e = array[0];
        for (int i = 1; i < length; i++) {
            Object v = array[i];
            if (((Comparable<Object>) e).compareTo(v) > 0) {
                e = v;
            }
        }
        return e;
    }
}
