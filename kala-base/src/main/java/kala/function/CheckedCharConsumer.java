package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedCharConsumer<Ex extends Throwable> extends CharConsumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> CheckedCharConsumer<Ex> of(CheckedCharConsumer<? extends Ex> consumer) {
        return ((CheckedCharConsumer<Ex>) consumer);
    }

    void acceptChecked(char value) throws Ex;

    @Override
    default void accept(char value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(char value) {
        try {
            acceptChecked(value);
            return Try.VOID;
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
