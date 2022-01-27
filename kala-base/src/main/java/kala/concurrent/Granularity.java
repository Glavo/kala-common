package kala.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

public enum Granularity {
    ATOM,
    VERY_FINE,
    FINE,
    MEDIUM,
    COARSE,
    VERY_COARSE;

    public static final Granularity DEFAULT = MEDIUM;

    public int pieceSize(ExecutorService executorService, int sizeEstimate) {
        if (this == Granularity.ATOM) {
            return 1;
        }
        int pieceCount;
        if (executorService instanceof ThreadPoolExecutor) {
            pieceCount = ((ThreadPoolExecutor) executorService).getMaximumPoolSize();
        } else {
            pieceCount = executorService instanceof ForkJoinPool
                    ? ((ForkJoinPool) executorService).getParallelism()
                    : ForkJoinPool.getCommonPoolParallelism();
        }

        switch (this) {
            case VERY_FINE:
                pieceCount <<= 6;
            case FINE:
                pieceCount <<= 4;
                break;
            case MEDIUM:
                pieceCount <<= 2;
                break;
            case VERY_COARSE:
                pieceCount >>>= 2;
                break;
        }

        return Integer.max(1, sizeEstimate / pieceCount);
    }
}


