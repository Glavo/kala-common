package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedRunnable<Ex extends Throwable> extends Runnable {

    void runChecked() throws Ex;

    @Override
    default void run() {
        try {
            runChecked();
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryRun() {
        try {
            runChecked();
            return Try.success(null);
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
