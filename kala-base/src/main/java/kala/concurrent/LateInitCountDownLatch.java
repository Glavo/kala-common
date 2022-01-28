package kala.concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public final class LateInitCountDownLatch {
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -1871679293724769263L;

        volatile int threshold = -1;

        int getCount() {
            return getState();
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

    public void countDown() {
        sync.releaseShared(1);
    }

    public int getCount() {
        return sync.getCount();
    }
}
