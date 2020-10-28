package asia.kala.function;

import asia.kala.control.Try;

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
