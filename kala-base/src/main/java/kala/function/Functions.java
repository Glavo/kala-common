package kala.function;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public final class Functions {
    private Functions() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, R> Function<T, R> narrow(Function<? super T, ? extends R> function) {
        return (Function<T, R>) function;
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, R> Function<T, R> of(Function<? super T, ? extends R> function) {
        return (Function<T, R>) function;
    }

    public static <T> @NotNull Function<T, T> identity() {
        return ((Function<T, T>) Identity.INSTANCE);
    }

    public static <T, R> @NotNull Function<T, R> memoized(@NotNull Function<? super T, ? extends R> function) {
        return memoized(function, false);
    }

    public static <T, R> @NotNull Function<T, R> memoized(@NotNull Function<? super T, ? extends R> function, boolean sync) {
        Objects.requireNonNull(function);
        if (function instanceof kala.function.Memoized) {
            return narrow(function);
        }
        return new MemoizedFunction<>(function, new HashMap<>(), sync);
    }

    public static <T, R> @NotNull Function<T, R> weakMemoized(@NotNull Function<? super T, ? extends R> function) {
        return weakMemoized(function, false);
    }

    public static <T, R> @NotNull Function<T, R> weakMemoized(@NotNull Function<? super T, ? extends R> function, boolean sync) {
        Objects.requireNonNull(function);
        if (function instanceof kala.function.Memoized) {
            return narrow(function);
        }
        return new MemoizedFunction<>(function, new WeakHashMap<>(), sync);
    }

    enum Identity implements Function<Object, Object> {
        INSTANCE;

        @Override
        public Object apply(Object o) {
            return o;
        }

        @Override
        public <V> @NotNull Function<V, Object> compose(@NotNull Function<? super V, ?> before) {
            return narrow(before);
        }

        @Override
        public <V> @NotNull Function<Object, V> andThen(@NotNull Function<? super Object, ? extends V> after) {
            return narrow(after);
        }

        @Override
        public String toString() {
            return "Functions.Identity";
        }
    }

}
