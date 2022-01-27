package kala.concurrent;

import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentScopeTest {
    @Test
    void test() {
        assertSame(ForkJoinPool.commonPool(), ConcurrentScope.currentExecutorService());

        ExecutorService pool1 = Executors.newFixedThreadPool(2);
        ExecutorService pool2 = Executors.newFixedThreadPool(2);
        try (ConcurrentScope scope = ConcurrentScope.withExecutorService(pool1)) {
            assertSame(pool1, ConcurrentScope.currentExecutorService());
            try (ConcurrentScope innerScope = ConcurrentScope.withExecutorService(pool2)) {
                assertSame(pool2, ConcurrentScope.currentExecutorService());
            }
            assertFalse(pool2.isShutdown());
            assertSame(pool1, ConcurrentScope.currentExecutorService());
        }
        assertFalse(pool1.isShutdown());
        assertSame(ForkJoinPool.commonPool(), ConcurrentScope.currentExecutorService());

        ConcurrentScope.withExecutorService(pool1, () -> {
            assertSame(pool1, ConcurrentScope.currentExecutorService());
            ConcurrentScope.withExecutorService(pool2, () -> {
                assertSame(pool2, ConcurrentScope.currentExecutorService());
            });
            assertFalse(pool2.isShutdown());
            assertSame(pool1, ConcurrentScope.currentExecutorService());
        });

    }
}
