package kala.function;

import kala.internal.InternalIdentifyObject;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
final class MemoizedFunction<T, R> implements Function<T, R>, Memoized, Serializable {
    private static final long serialVersionUID = -376053341729150782L;

    private static final Object NULL_HOLE = new InternalIdentifyObject();

    private final @NotNull Function<? super T, ? extends R> function;
    private final @NotNull Map<T, Object> cache;
    private final boolean sync;

    MemoizedFunction(@NotNull Function<? super T, ? extends R> function, @NotNull Map<T, Object> cache, boolean sync) {
        this.function = function;
        this.cache = cache;
        this.sync = sync;
    }

    @Override
    public R apply(T t) {
        final Object value = cache.getOrDefault(t, NULL_HOLE);
        if (value == NULL_HOLE) {
            if (sync) {
                synchronized (cache) {
                    final R res = function.apply(t);
                    cache.put(t, res);
                    return res;
                }
            } else {
                final R res = function.apply(t);
                cache.put(t, res);
                return res;
            }
        } else {
            return (R) value;
        }
    }

    @Override
    public String toString() {
        return "MemoizedFunction[" +
                "function=" + function +
                ", cache=" + cache +
                ", sync=" + sync +
                ']';
    }
}
