package kala.concurrent;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public final class LateInitCountDownLatch {
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -1871679293724769263L;

        volatile int threshold = -1;

        int getCount() {
            return getState();
        }

        boolean isDone() {
            return getState() == threshold;
        }

        protected int tryAcquireShared(int acquires) {
            return (getState() == threshold) ? 1 : -1;
        }

        protected boolean tryReleaseShared(int releases) {
            for (; ; ) {
                int c = getState();
                if (c == threshold) {
                    return false;
                }
                int nextc = c + 1;
                if (compareAndSetState(c, nextc)) {
                    return nextc == threshold;
                }
            }
        }
    }

    private final class AwaitFuture implements java.util.concurrent.Future<Void> {

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return sync.isDone();
        }

        @Override
        public Void get() throws InterruptedException, ExecutionException {
            await();
            return null;
        }

        @Override
        public Void get(long timeout, @NotNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (!await(timeout, unit)) {
                throw new TimeoutException();
            }
            return null;
        }
    }

    private final Sync sync = new Sync();

    public LateInitCountDownLatch() {
    }

    public LateInitCountDownLatch(int count) {
        sync.threshold = count;
    }

    public void init(int count) {
        sync.threshold = count;
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    public void countDown() {
        sync.releaseShared(1);
    }

    public Future<Void> awaitFuture() {
        return new AwaitFuture();
    }

    public int getCount() {
        return sync.getCount();
    }
}
