package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;

import java.util.function.BiFunction;

@FunctionalInterface
public interface CheckedBiFunction<T, U, R, Ex extends Throwable> extends BiFunction<T, U, R> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, U, R, Ex extends Throwable> CheckedBiFunction<T, U, R, Ex> of(CheckedBiFunction<? super T, ? super U, ? extends R, ? extends Ex> function) {
        return (CheckedBiFunction<T, U, R, Ex>) function;
    }

    R applyChecked(T t, U u) throws Ex;

    @Override
    default R apply(T t, U u) {
        try {
            return applyChecked(t, u);
        } catch (Throwable e) {
            return Try.sneakyThrow(e);
        }
    }
}
