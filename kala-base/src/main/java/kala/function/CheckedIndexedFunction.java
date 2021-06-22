package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;

@FunctionalInterface
public interface CheckedIndexedFunction<T, R, Ex extends Throwable> extends IndexedFunction<T, R> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, R, Ex extends Throwable> CheckedIndexedFunction<T, R, Ex> of(
            CheckedIndexedFunction<? super T, ? extends R, ? extends Ex> consumer) {
        return ((CheckedIndexedFunction<T, R, Ex>) consumer);
    }

    R applyChecked(int index, T t) throws Ex;

    @Override
    default R apply(int index, T t) {
        try {
            return applyChecked(index, t);
        } catch (Throwable ex) {
            throw Try.throwExceptionUnchecked(ex);
        }
    }
}
