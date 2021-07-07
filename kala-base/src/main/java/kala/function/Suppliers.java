package kala.function;

import kala.annotations.StaticClass;
import kala.value.LazyValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
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

    @Contract(value = "_ -> param1", pure = true)
    public static <T> Supplier<T> of(Supplier<? extends T> supplier) {
        return (Supplier<T>) supplier;
    }

    public static <T> @NotNull Supplier<T> constant(T value) {
        return new Constant<>(value);
    }

    public static <T> @NotNull Supplier<T> memoized(@NotNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier);
        if (supplier instanceof Memoized) {
            return narrow(supplier);
        }
        return LazyValue.of(supplier);
    }

    static final class Constant<T> implements Supplier<T>, Memoized, Serializable {
        private final T value;

        Constant(T value) {
            this.value = value;
        }

        @Override
        public final T get() {
            return value;
        }

        @Override
        public final String toString() {
            return "Suppliers.Constant[value=" + value + ']';
        }
    }
}
