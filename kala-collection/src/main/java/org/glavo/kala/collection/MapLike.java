package org.glavo.kala.collection;

import org.glavo.kala.collection.base.MapBase;
import org.glavo.kala.collection.immutable.*;
import org.glavo.kala.collection.internal.view.MapViews;
import org.glavo.kala.tuple.Tuple2;
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

    default @NotNull SetView<K> keysView() {
        return new MapViews.Keys<>(this);
    }

    default @NotNull View<V> valuesView() {
        return new MapViews.Values<>(this);
    }

    default @NotNull MapView.WithDefault<K, V> withDefault(@NotNull Function<? super K, ? extends V> defaultFunction) {
        Objects.requireNonNull(defaultFunction);
        return new MapViews.WithDefaultImpl<>(this, defaultFunction);
    }

    default @NotNull ImmutableMap<K, V> toImmutableMap() {
        return ImmutableMap.from(this);
    }

    default @NotNull ImmutableSeq<Tuple2<K, V>> toImmutableSeq() {
        return ImmutableSeq.from(iterator());
    }

    default @NotNull ImmutableArray<Tuple2<K, V>> toImmutableArray() {
        return ImmutableArray.from(toArray());
    }

    default @NotNull ImmutableLinkedSeq<Tuple2<K, V>> toImmutableLinkedSeq() {
        return ImmutableLinkedSeq.from(this.iterator());
    }

    default @NotNull ImmutableVector<Tuple2<K, V>> toImmutableVector() {
        return ImmutableVector.from(iterator());
    }
}
