package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;

@FunctionalInterface
public interface CheckedIntConsumer<Ex extends Throwable> extends IntConsumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> CheckedIntConsumer<Ex> of(CheckedIntConsumer<? extends Ex> consumer) {
        return ((CheckedIntConsumer<Ex>) consumer);
    }

    void acceptChecked(int value) throws Ex;

    @Override
    default void accept(int value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(int value) {
        try {
            acceptChecked(value);
            return Try.success(null);
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
