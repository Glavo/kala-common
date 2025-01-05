/*
 * Copyright 2025 Glavo
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
package kala.concurrent;

import java.util.concurrent.Executor;
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

    public int pieceSize(int sizeEstimate) {
        return pieceSize(ForkJoinPool.getCommonPoolParallelism(), sizeEstimate);
    }

    public int pieceSize(int threadCount, int sizeEstimate) {
        if (this == Granularity.ATOM) {
            return 1;
        }
        int pieceCount = threadCount;

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

    public int pieceSize(Executor executor, int sizeEstimate) {
        if (this == Granularity.ATOM) {
            return 1;
        }
        int pieceCount;
        if (executor instanceof ThreadPoolExecutor) {
            pieceCount = ((ThreadPoolExecutor) executor).getMaximumPoolSize();
        } else {
            pieceCount = executor instanceof ForkJoinPool
                    ? ((ForkJoinPool) executor).getParallelism()
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


