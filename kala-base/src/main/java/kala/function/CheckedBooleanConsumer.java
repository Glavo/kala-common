package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedBooleanConsumer<Ex extends Throwable> extends BooleanConsumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> CheckedBooleanConsumer<Ex> of(CheckedBooleanConsumer<? extends Ex> consumer) {
        return (CheckedBooleanConsumer<Ex>) consumer;
    }

    void acceptChecked(boolean value) throws Ex;

    @Override
    default void accept(boolean value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            throw Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(boolean value) {
        try {
            acceptChecked(value);
            return Try.success(null);
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
