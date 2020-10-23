package asia.kala.function;

import asia.kala.control.Try;

@FunctionalInterface
public interface CheckedBooleanConsumer<Ex extends Throwable> extends BooleanConsumer {

    void acceptChecked(boolean value) throws Ex;

    default void accept(boolean value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            throw Try.throwExceptionUnchecked(e);
        }
    }
}
