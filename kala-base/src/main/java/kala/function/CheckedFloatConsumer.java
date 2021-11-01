package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedFloatConsumer<Ex extends Throwable> extends FloatConsumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> CheckedFloatConsumer<Ex> of(CheckedFloatConsumer<? extends Ex> consumer) {
        return ((CheckedFloatConsumer<Ex>) consumer);
    }

    void acceptChecked(float value) throws Ex;

    @Override
    default void accept(float value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(float value) {
        try {
            acceptChecked(value);
            return Try.VOID;
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
