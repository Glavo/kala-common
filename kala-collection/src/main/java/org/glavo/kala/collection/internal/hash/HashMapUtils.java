package org.glavo.kala.collection.internal.hash;

import java.util.Objects;

public final class HashMapUtils {
    private HashMapUtils() {
    }

    public static int improveHash(int originalHash) {
        return originalHash ^ (originalHash >>> 16);
    }

    public static int unimproveHash(int improvedHash) {
        return improveHash(improvedHash);
    }

    public static int computeHash(Object o) {
        return HashMapUtils.improveHash(Objects.hashCode(o));
    }
}
