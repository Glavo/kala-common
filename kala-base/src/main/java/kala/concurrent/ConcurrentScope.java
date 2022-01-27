package kala.concurrent;

import kala.function.CheckedRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

public final class ConcurrentScope implements AutoCloseable {
    private static final ThreadLocal<ConcurrentScope> current = new ThreadLocal<>();

    public static ExecutorService currentExecutorService() {
        ConcurrentScope currentScope = current.get();
        return currentScope == null ? ForkJoinPool.commonPool() : currentScope.executorService;
    }

    private final ConcurrentScope parent;
    private final ExecutorService executorService;
    private final boolean shutdownOnExit;

    private ConcurrentScope(ConcurrentScope parent, ExecutorService executorService, boolean shutdownOnExit) {
        this.parent = parent;
        this.executorService = executorService;
        this.shutdownOnExit = shutdownOnExit;
    }

    public static ConcurrentScope withExecutorService(@Nullable ExecutorService executorService) {
        return withExecutorService(executorService, false);
    }

    public static ConcurrentScope withExecutorService(@Nullable ExecutorService executorService, boolean shutdownOnExit) {
        Objects.requireNonNull(executorService);
        ConcurrentScope scope = new ConcurrentScope(current.get(), executorService, shutdownOnExit);
        current.set(scope);
        return scope;
    }

    public static <Ex extends Throwable> void withExecutorService(
            @Nullable ExecutorService executorService, @NotNull CheckedRunnable<Ex> action) throws Ex {
        withExecutorService(executorService, false, action);
    }

    public static <Ex extends Throwable> void withExecutorService(
            @Nullable ExecutorService executorService, boolean shutdownOnExit, @NotNull CheckedRunnable<Ex> action) throws Ex {
        try (ConcurrentScope scope = withExecutorService(executorService, shutdownOnExit)) {
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
            executorService.shutdown();
        }
    }
}
