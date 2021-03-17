package org.glavo.kala.collection;

import org.glavo.kala.collection.base.MapBase;
import org.glavo.kala.collection.internal.view.MapViews;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.*;

public interface MapLike<K, V> extends MapBase<K, V> {

    default @NotNull String className() {
        return "MapLike";
    }

    default @NotNull MapView<K, V> view() {
        return new MapViews.Of<>(this);
    }

    default @NotNull MapView.WithDefault<K, V> withDefault(@NotNull Function<? super K, ? extends V> defaultFunction) {
        Objects.requireNonNull(defaultFunction);
        return new MapViews.WithDefaultImpl<>(this, defaultFunction);
    }

}
