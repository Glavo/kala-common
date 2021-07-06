package kala.function;

import kala.annotations.StaticClass;
import kala.value.LazyValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

@StaticClass
@SuppressWarnings("unchecked")
public final class Suppliers {
    private Suppliers() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Supplier<T> narrow(Supplier<? extends T> supplier) {
        return (Supplier<T>) supplier;
    }

    public static <T> @NotNull Supplier<T> memoized(@NotNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        if (supplier instanceof Memoized) {
            return narrow(supplier);
        }
        return LazyValue.of(supplier);
    }
}
