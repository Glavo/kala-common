package kala.concurrent;

import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentScopeTest {
    @Test
    void test() {
        assertSame(ForkJoinPool.commonPool(), ConcurrentScope.currentExecutor());

        ExecutorService pool1 = Executors.newFixedThreadPool(2);
        ExecutorService pool2 = Executors.newFixedThreadPool(2);
        try (ConcurrentScope scope = ConcurrentScope.withExecutor(pool1)) {
            assertSame(pool1, ConcurrentScope.currentExecutor());
            try (ConcurrentScope innerScope = ConcurrentScope.withExecutor(pool2)) {
                assertSame(pool2, ConcurrentScope.currentExecutor());
            }
            assertFalse(pool2.isShutdown());
            assertSame(pool1, ConcurrentScope.currentExecutor());
        }
        assertFalse(pool1.isShutdown());
        assertSame(ForkJoinPool.commonPool(), ConcurrentScope.currentExecutor());

        ConcurrentScope.withExecutor(pool1, () -> {
            assertSame(pool1, ConcurrentScope.currentExecutor());
            ConcurrentScope.withExecutor(pool2, () -> {
                assertSame(pool2, ConcurrentScope.currentExecutor());
            });
            assertFalse(pool2.isShutdown());
            assertSame(pool1, ConcurrentScope.currentExecutor());
        });

    }
}
