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
