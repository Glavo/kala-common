package asia.kala.function;

import asia.kala.control.Try;
import org.jetbrains.annotations.Contract;

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
            throw Try.throwExceptionUnchecked(e);
        }
    }
}
