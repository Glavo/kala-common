package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
            return Try.sneakyThrow(ex);
        }
    }

    @Deprecated
    default @NotNull Try<R> tryApply(int index, T t) {
        try {
            return Try.success(applyChecked(index, t));
        } catch (Throwable ex) {
            return Try.failure(ex);
        }
    }
}
