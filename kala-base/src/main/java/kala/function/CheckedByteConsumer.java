package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;

@FunctionalInterface
public interface CheckedByteConsumer<Ex extends Throwable> extends ByteConsumer {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <Ex extends Throwable> CheckedByteConsumer<Ex> of(CheckedByteConsumer<? extends Ex> consumer) {
        return ((CheckedByteConsumer<Ex>) consumer);
    }

    void acceptChecked(byte value) throws Ex;

    @Override
    default void accept(byte value) {
        try {
            acceptChecked(value);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(byte value) {
        try {
            acceptChecked(value);
            return Try.VOID;
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
