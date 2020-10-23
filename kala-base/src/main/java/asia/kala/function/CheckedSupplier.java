package asia.kala.function;

import asia.kala.control.Try;
import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

/**
 * Represents a supplier of results, may throw checked exception.
 *
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a functional interface whose functional method is {@link #getChecked()}.
 *
 * @param <T>  the type of results supplied by this supplier
 * @param <Ex> the type of the checked exception
 * @author Glavo
 */
@FunctionalInterface
public interface CheckedSupplier<T, Ex extends Throwable> extends Supplier<T> {

    @Contract(value = "_ -> param1", pure = true)
    @SuppressWarnings("unchecked")
    static <T, Ex extends Throwable> CheckedSupplier<T, Ex> of(CheckedSupplier<? extends T, ? extends Ex> supplier) {
        return (CheckedSupplier<T, Ex>) supplier;
    }

    T getChecked() throws Ex;

    default T get() {
        try {
            return getChecked();
        } catch (Throwable e) {
            throw Try.throwExceptionUnchecked(e);
        }
    }
}
