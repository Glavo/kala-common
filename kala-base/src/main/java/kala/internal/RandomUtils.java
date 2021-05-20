package kala.internal;

import java.util.Random;

public final class RandomUtils {
    private static final ThreadLocal<Random> THREAD_LOCAL_RANDOM = ThreadLocal.withInitial(Random::new);

    private RandomUtils() {
    }

    public static Random threadLocal() {
        return THREAD_LOCAL_RANDOM.get();
    }
}
