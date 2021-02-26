package org.glavo.kala.collection.base;

import org.glavo.kala.annotations.StaticClass;

/**
 * Array operations based on {@code Object[]}.
 * <p>
 * These operations do not require reflection or generators to construct arrays,
 * so they are faster than operations in {@link GenericArrays}.
 */
@StaticClass
public final class ObjectArrays {
    private ObjectArrays() {
    }

    public static final Object[] EMPTY = GenericArrays.EMPTY_OBJECT_ARRAY;

}
