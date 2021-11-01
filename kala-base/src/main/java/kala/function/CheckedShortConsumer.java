package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedShortConsumer<Ex extends Throwable> extends ShortConsumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> CheckedShortConsumer<Ex> of(CheckedShortConsumer<? extends Ex> consumer) {
        return ((CheckedShortConsumer<Ex>) consumer);
    }

    void acceptChecked(short value) throws Ex;

    @Override
    default void accept(short value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(short value) {
        try {
            acceptChecked(value);
            return Try.VOID;
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
