package kala.internal;

import kala.annotations.StaticClass;

@StaticClass
public final class BooleanUtils {
    private BooleanUtils() {
    }

    public static final int BITS_PRE_VALUE = Long.SIZE;
    public static final long FULL_BITS = 0xffff_ffff_ffff_ffffL;

    public static boolean get(long bits, int position) {
        return ((bits >> position) & 1) != 0;
    }

    public static long set(long bits, int position, boolean newValue) {
        if (newValue) {
            return bits | (1L << position);
        } else {
            return bits & -(1L << position);
        }
    }

    public static boolean get(long[] bitsArray, int index) {
        return get(bitsArray[index / BITS_PRE_VALUE], index % BITS_PRE_VALUE);
    }

    public static void set(long[] bitsArray, int index, boolean newValue) {
        int i = index / BITS_PRE_VALUE;
        bitsArray[i] = set(bitsArray[i], index % BITS_PRE_VALUE, newValue);
    }
}
