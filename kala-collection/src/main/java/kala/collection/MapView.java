package kala.collection;

import kala.collection.internal.view.MapViews;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface MapView<K, V> extends MapLike<K, V> {

    interface WithDefault<K, V> extends MapView<K, V> {
        @NotNull Function<? super K, ? extends V> getDefaultFunction();
    }

    @SuppressWarnings("unchecked")
    static <K, V> @NotNull MapView<K, V> empty() {
        return (MapView<K, V>) MapViews.Empty.INSTANCE;
    }

    //region Collection Operations

    @Override
    default @NotNull String className() {
        return "MapView";
    }

    @Override
    default @NotNull MapView<K, V> view() {
        return this;
    }

    //endregion

    default <U> @NotNull View<U> map(@NotNull BiFunction<? super K, ? super V, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return new MapViews.Mapped<>(this, mapper);
    }

    default <NV> @NotNull MapView<K, NV> mapValues(@NotNull BiFunction<? super K, ? super V, ? extends NV> mapper) {
        Objects.requireNonNull(mapper);
        return new MapViews.MapValues<>(this, mapper);
    }

}
