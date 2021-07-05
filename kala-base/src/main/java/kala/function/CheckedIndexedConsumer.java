package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedIndexedConsumer<T, Ex extends Throwable> extends IndexedConsumer<T> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, Ex extends Throwable> CheckedIndexedConsumer<T, Ex> of(CheckedIndexedConsumer<? super T, ? extends Ex> consumer) {
        return (CheckedIndexedConsumer<T, Ex>) consumer;
    }

    void acceptChecked(int index, T t) throws Ex;

    @Override
    default void accept(int index, T t) {
        try {
            acceptChecked(index, t);
        } catch (Throwable e) {
            throw Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<Void> tryAccept(int index, T t) {
        try {
            acceptChecked(index, t);
            return Try.success(null);
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
}
