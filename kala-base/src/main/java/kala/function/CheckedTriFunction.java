package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CheckedTriFunction<T, U, V, R, Ex extends Throwable> extends TriFunction<T, U, V, R> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, U, V, R, Ex extends Throwable> CheckedTriFunction<T, U, V, R, Ex> of(
            CheckedTriFunction<? super T, ? super U, ? super V, ? extends R, ? extends Ex> function) {
        return (CheckedTriFunction<T, U, V, R, Ex>) function;
    }

    R applyChecked(T t, U u, V v) throws Ex;

    @Override
    default R apply(T t, U u, V v) {
        try {
            return applyChecked(t, u, v);
        } catch (Throwable e) {
            return Try.sneakyThrow(e);
        }
    }

    default @NotNull Try<R> tryApply(T t, U u, V v) {
        try {
            return Try.success(applyChecked(t, u, v));
        } catch (Throwable ex) {
            return Try.failure(ex);
        }
    }
}
