package org.glavo.kala.function;

import org.glavo.kala.control.Try;

@FunctionalInterface
public interface CheckedRunnable<Ex extends Throwable> extends Runnable {

    void runChecked() throws Ex;

    @Override
    default void run() {
        try {
            runChecked();
        } catch (Throwable e) {
            Try.throwExceptionUnchecked(e);
        }
    }
}
