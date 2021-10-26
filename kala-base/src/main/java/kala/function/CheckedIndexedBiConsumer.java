package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedIndexedBiConsumer<T, U, Ex extends Throwable> extends IndexedBiConsumer<T, U> {
    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, U, Ex extends Throwable> CheckedIndexedBiConsumer<T, U, Ex> of(CheckedIndexedBiConsumer<? super T, ? super U, ? extends Ex> consumer) {
        return (CheckedIndexedBiConsumer<T, U, Ex>) consumer;
    }

    void acceptChecked(int index, T t, U u) throws Ex;

    @Override
    default void accept(int index, T t, U u) {
        try {
            acceptChecked(index, t, u);
        } catch (Throwable e) {
            Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(int index, T t, U u) {
        try {
            acceptChecked(index, t, u);
            return Try.success(null);
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
