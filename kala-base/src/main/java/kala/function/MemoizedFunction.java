package kala.function;

import kala.internal.InternalIdentifyObject;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
final class MemoizedFunction<T, R> implements Function<T, R>, Memoized, Serializable {
    private static final long serialVersionUID = -904511663627169337L;

    private static final Object NULL_HOLE = new InternalIdentifyObject();

    private final @NotNull Function<? super T, ? extends R> function;
    private final @NotNull Map<T, Object> cache;

    MemoizedFunction(@NotNull Function<? super T, ? extends R> function, @NotNull Map<T, Object> cache) {
        this.function = function;
        this.cache = cache;
    }

    @Override
    public R apply(T t) {
        Object res = cache.computeIfAbsent(t, key -> {
            R v = function.apply(key);
            return v != null ? v : NULL_HOLE;
        });

        return res != NULL_HOLE ? (R) res : null;
    }

    @Override
    public String toString() {
        return "MemoizedFunction[" +
                "function=" + function +
                ", cache=" + cache +
                ']';
    }
}
