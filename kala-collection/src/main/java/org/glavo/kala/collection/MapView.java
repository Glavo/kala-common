package org.glavo.kala.collection;

import org.jetbrains.annotations.NotNull;

public interface MapView<K, V> extends MapLike<K, V> {

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
}
