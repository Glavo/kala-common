package asia.kala.function;

import asia.kala.control.Try;
import org.jetbrains.annotations.Contract;

import java.util.function.BiConsumer;

public interface CheckedBiConsumer<T, U, Ex extends Throwable> extends BiConsumer<T, U> {
    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, U, Ex extends Throwable> CheckedBiConsumer<T, U, Ex> of(
            CheckedBiConsumer<? super T, ? super U, ? extends Ex> consumer
    ) {
        return (CheckedBiConsumer<T, U, Ex>) consumer;
    }

    void acceptChecked(T t, U u) throws Ex;

    @Override
    default void accept(T t, U u) {
        try {
            acceptChecked(t, u);
        } catch (Throwable ex) {
            Try.throwExceptionUnchecked(ex);
        }
    }
}
