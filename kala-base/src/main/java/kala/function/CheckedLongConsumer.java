package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongConsumer;

@FunctionalInterface
public interface CheckedLongConsumer<Ex extends Throwable> extends LongConsumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> CheckedLongConsumer<Ex> of(CheckedLongConsumer<? extends Ex> consumer) {
        return ((CheckedLongConsumer<Ex>) consumer);
    }

    void acceptChecked(long value) throws Ex;

    @Override
    default void accept(long value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(long value) {
        try {
            acceptChecked(value);
            return Try.VOID;
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
