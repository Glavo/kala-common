package kala.function;

import kala.annotations.StaticClass;
import kala.tuple.Tuple;
import kala.tuple.Tuple2;
import kala.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

@StaticClass
@SuppressWarnings("unchecked")
public final class Functions {
    private Functions() {
    }

    @Contract(value = "_ -> param1", pure = true)
    public static <T, R> Function<T, R> of(Function<? super T, ? extends R> function) {
        return (Function<T, R>) function;
    }

    public static <T> @NotNull Function<T, T> identity() {
        return ((Function<T, T>) Identity.INSTANCE);
    }

    public static <T, R> @NotNull Function<T, R> memoized(@NotNull Function<? super T, ? extends R> function) {
        if (function instanceof Memoized) {
            return of(function);
        }

        return memoized(function, false);
    }

    public static <T, R> @NotNull Function<T, R> memoized(@NotNull Function<? super T, ? extends R> function, boolean sync) {
        Objects.requireNonNull(function);
        return new MemoizedFunction<>(function, sync ? new ConcurrentHashMap<>() : new HashMap<>(), sync);
    }

    public static <T, R> @NotNull Function<T, R> weakMemoized(@NotNull Function<? super T, ? extends R> function) {
        return weakMemoized(function, false);
    }

    public static <T, R> @NotNull Function<T, R> weakMemoized(@NotNull Function<? super T, ? extends R> function, boolean sync) {
        Objects.requireNonNull(function);
        return new MemoizedFunction<>(function, sync ? Collections.synchronizedMap(new WeakHashMap<>()) : new WeakHashMap<>(), sync);
    }

    public static <T, U, R> @NotNull Function<Tuple2<T, U>, R> tupled(@NotNull BiFunction<? super T, ? super U, ? extends R> biFunction) {
        Objects.requireNonNull(biFunction);
        return tuple -> biFunction.apply(tuple.component1(), tuple.component2());
    }

    public static <T, U, R> @NotNull BiFunction<T, U, R> untupled(@NotNull Function<? super Tuple2<? extends T, ? extends U>, ? extends R> function) {
        Objects.requireNonNull(function);
        return (t, u) -> function.apply(Tuple.of(t, u));
    }

    public static <T, U, R> @NotNull Function<T, Function<U, R>> curried(@NotNull BiFunction<? super T, ? super U, ? extends R> biFunction) {
        Objects.requireNonNull(biFunction);
        return t -> u -> biFunction.apply(t, u);
    }

    public static <T, U, R> @NotNull BiFunction<T, U, R> uncurried(@NotNull Function<? super T, ? extends Function<? super U, ? extends R>> function) {
        Objects.requireNonNull(function);
        return (t, u) -> function.apply(t).apply(u);
    }

    private enum NullHole {
        INSTANCE;

        @Override
        public String toString() {
            return "null";
        }
    }

    private enum Identity implements Function<Object, Object> {
        INSTANCE;

        @Override
        public Object apply(Object o) {
            return o;
        }

        @Override
        public <V> @NotNull Function<V, Object> compose(@NotNull Function<? super V, ?> before) {
            return (Function<V, Object>) before;
        }

        @Override
        public <V> @NotNull Function<Object, V> andThen(@NotNull Function<? super Object, ? extends V> after) {
            return (Function<Object, V>) after;
        }

        @Override
        public String toString() {
            return "Functions.Identity";
        }
    }

    private static final class MemoizedFunction<T, R> implements Function<T, R>, Memoized, Serializable {
        private static final long serialVersionUID = -904511663627169337L;

        final @NotNull Function<? super T, ? extends R> function;
        final @NotNull Map<T, Object> cache;
        final boolean sync;

        MemoizedFunction(@NotNull Function<? super T, ? extends R> function, @NotNull Map<T, Object> cache, boolean sync) {
            this.function = function;
            this.cache = cache;
            this.sync = sync;
        }

        @Override
        public R apply(T t) {
            Object res = cache.computeIfAbsent(t, key -> {
                R v = function.apply(key);
                return v != null ? v : NullHole.INSTANCE;
            });

            return res != NullHole.INSTANCE ? (R) res : null;
        }

        @Override
        public String toString() {
            return "MemoizedFunction[" +
                    "function=" + function +
                    ", cache=" + cache +
                    ']';
        }


    }
}
