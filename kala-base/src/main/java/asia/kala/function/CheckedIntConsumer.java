package asia.kala.function;

import asia.kala.control.Try;

import java.util.function.IntConsumer;

@FunctionalInterface
public interface CheckedIntConsumer<Ex extends Throwable> extends IntConsumer {

    void acceptChecked(int value) throws Ex;

    default void accept(int value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            throw Try.throwExceptionUnchecked(e);
        }
    }
}
