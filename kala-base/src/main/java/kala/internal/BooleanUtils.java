package kala.internal;

public final class BooleanUtils {
    private BooleanUtils() {
    }

    public static final int BITS_PRE_VALUE = Integer.SIZE;
    public static final int FULL_BITS = -1;

    public static boolean get(int bits, int position) {
        return ((bits >> position) & 1) != 0;
    }

    public static int set(int bits, int position, boolean newValue) {
        if (newValue) {
            return bits | (1 << position);
        } else {
            return bits & -(1 << position);
        }
    }

    public static boolean get(int[] bitsArray, int index) {
        return get(bitsArray[index / BITS_PRE_VALUE], index % BITS_PRE_VALUE);
    }

    public static void set(int[] bitsArray, int index, boolean newValue) {
        int i = index / BITS_PRE_VALUE;
        bitsArray[i] = set(bitsArray[i], index % BITS_PRE_VALUE, newValue);
    }
}
