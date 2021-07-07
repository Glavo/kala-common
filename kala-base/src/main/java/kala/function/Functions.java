package kala.function;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
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
        return sync
                ? new Memoized<>(function, new HashMap<>())
                : new Memoized<>(function, new HashMap<>(), null);
    }

    public static <T, R> @NotNull Function<T, R> weakMemoized(@NotNull Function<? super T, ? extends R> function) {
        return weakMemoized(function, false);
    }

    public static <T, R> @NotNull Function<T, R> weakMemoized(@NotNull Function<? super T, ? extends R> function, boolean sync) {
        Objects.requireNonNull(function);
        if (function instanceof kala.function.Memoized) {
            return narrow(function);
        }
        return sync
                ? new Memoized<>(function, new WeakHashMap<>())
                : new Memoized<>(function, new WeakHashMap<>(), null);
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

    static final class Memoized<T, R> implements Function<T, R>, kala.function.Memoized, Serializable {
        private static final Object NULL_HOLE = new Object();

        private final @NotNull Function<? super T, ? extends R> function;
        private final @NotNull Map<T, Object> cache;
        private final @Nullable Object lock;

        Memoized(@NotNull Function<? super T, ? extends R> function, @NotNull Map<T, Object> cache) {
            this(function, cache, cache);
        }

        Memoized(@NotNull Function<? super T, ? extends R> function, @NotNull Map<T, Object> cache, @Nullable Object lock) {
            this.function = function;
            this.cache = cache;
            this.lock = lock;
        }

        @Override
        public final R apply(T t) {
            final Object value = cache.getOrDefault(t, NULL_HOLE);
            if (value == NULL_HOLE) {
                if (lock == null) {
                    final R res = function.apply(t);
                    cache.put(t, res);
                    return res;
                } else synchronized (lock) {
                    final R res = function.apply(t);
                    cache.put(t, res);
                    return res;
                }
            } else {
                return (R) value;
            }
        }

        @Override
        public final String toString() {
            return "Functions.Memoized[" +
                    "function=" + function +
                    ", cache=" + cache +
                    ", lock=" + lock +
                    ']';
        }
    }
}
