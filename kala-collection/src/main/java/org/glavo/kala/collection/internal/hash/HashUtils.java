package org.glavo.kala.collection.internal.hash;

public final class HashUtils {
    private HashUtils() {
    }

    public static int improveHash(int originalHash) {
        return originalHash ^ (originalHash >>> 16);
    }

    public static int unimproveHash(int improvedHash) {
        return improveHash(improvedHash);
    }

    public static int computeHash(Object o) {
        return o == null ? 0 : improveHash(o.hashCode());
    }
}
