package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleConsumer;

@FunctionalInterface
public interface CheckedDoubleConsumer<Ex extends Throwable> extends DoubleConsumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> CheckedDoubleConsumer<Ex> of(CheckedDoubleConsumer<? extends Ex> consumer) {
        return ((CheckedDoubleConsumer<Ex>) consumer);
    }

    void acceptChecked(double value) throws Ex;

    @Override
    default void accept(double value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(double value) {
        try {
            acceptChecked(value);
            return Try.VOID;
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
