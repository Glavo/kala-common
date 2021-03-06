package kala.function;

import kala.control.Try;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@FunctionalInterface
public interface CheckedPredicate<T, Ex extends Throwable> extends Predicate<T> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, Ex extends Throwable> CheckedPredicate<T, Ex> of(
            CheckedPredicate<? super T, ? extends Ex> consumer) {
        return ((CheckedPredicate<T, Ex>) consumer);
    }

    boolean testChecked(T t) throws Ex;

    @Override
    default boolean test(T t) {
        try {
            return testChecked(t);
        } catch (Throwable ex) {
            throw Try.sneakyThrow(ex);
        }
    }

    default @NotNull Try<Boolean> tryTest(T t) {
        try {
            return Try.success(testChecked(t));
        } catch (Throwable ex) {
            return Try.failure(ex);
        }
    }
}
