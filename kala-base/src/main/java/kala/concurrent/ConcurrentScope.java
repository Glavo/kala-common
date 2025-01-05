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

import kala.function.CheckedRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public final class ConcurrentScope implements AutoCloseable {
    private static final ThreadLocal<ConcurrentScope> current = new ThreadLocal<>();

    public static Executor currentExecutor() {
        ConcurrentScope currentScope = current.get();
        return currentScope == null ? ForkJoinPool.commonPool() : currentScope.executor;
    }

    private final ConcurrentScope parent;
    private final Executor executor;
    private final boolean shutdownOnExit;

    private ConcurrentScope(ConcurrentScope parent, Executor executor, boolean shutdownOnExit) {
        this.parent = parent;
        this.executor = executor;
        this.shutdownOnExit = shutdownOnExit;
    }

    public static ConcurrentScope withExecutor(@Nullable Executor executor) {
        return withExecutor(executor, false);
    }

    public static ConcurrentScope withExecutorAndShutdown(@Nullable ExecutorService executor) {
        return withExecutor(executor, true);
    }

    private static ConcurrentScope withExecutor(@Nullable Executor executor, boolean shutdownOnExit) {
        Objects.requireNonNull(executor);
        ConcurrentScope scope = new ConcurrentScope(current.get(), executor, shutdownOnExit);
        current.set(scope);
        return scope;
    }

    public static <Ex extends Throwable> void withExecutor(
            @Nullable Executor executor, @NotNull CheckedRunnable<Ex> action) throws Ex {
        withExecutor(executor, false, action);
    }

    public static <Ex extends Throwable> void withExecutorAndShutdown(
            @Nullable ExecutorService executor, @NotNull CheckedRunnable<Ex> action) throws Ex {
        withExecutor(executor, true, action);
    }

    private static <Ex extends Throwable> void withExecutor(
            @Nullable Executor executor, boolean shutdownOnExit, @NotNull CheckedRunnable<Ex> action) throws Ex {
        try (ConcurrentScope scope = withExecutor(executor, shutdownOnExit)) {
            action.runChecked();
        } catch (Throwable ex) {
            @SuppressWarnings("unchecked")
            Ex e = (Ex) ex;
            throw e;
        }
    }

    @Override
    public void close() {
        if (current.get() != this) {
            throw new IllegalStateException();
        }

        current.set(parent);
        if (shutdownOnExit) {
            ((ExecutorService) executor).shutdown();
        }
    }
}
