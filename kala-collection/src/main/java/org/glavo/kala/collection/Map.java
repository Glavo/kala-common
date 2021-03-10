package org.glavo.kala.collection;

import org.glavo.kala.Equatable;
import org.glavo.kala.collection.factory.MapFactory;
import org.glavo.kala.collection.internal.convert.AsJavaConvert;
import org.glavo.kala.collection.internal.convert.FromJavaConvert;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.CheckedBiConsumer;
import org.glavo.kala.collection.base.MapIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public interface Map<K, V> extends MapLike<K, V>, Equatable {

    int HASH_MAGIC = 124549981;

    static int hashCode(@NotNull Map<?, ?> map) {
        return map.iterator().hash() + Map.HASH_MAGIC;
    }

    @SuppressWarnings("unchecked")
    static boolean equals(@NotNull Map<?, ?> map1, @NotNull Map<?, ?> map2) {
        if (!map1.canEqual(map2) || !map2.canEqual(map1)) {
            return false;
        }
        if (map1.size() != map2.size()) {
            return false;
        }

        MapIterator<?, ?> it = map1.iterator();
        try {
            while (it.hasNext()) {
                if (!((Map<Object, ?>) map2).getOption(it.nextKey()).contains(it.getValue())) {
                    return false;
                }
            }
        } catch (ClassCastException e) {
            return false;
        }
        return true;
    }

    //region Static Factories

    static <K, V> Map<K, V> wrapJava(java.util.@NotNull @UnmodifiableView Map<K, V> map) {
        return new FromJavaConvert.MapFromJava<>(map);
    }

    //endregion

    @Override
    default @NotNull String className() {
        return "Map";
    }

    default <NK, NV> @NotNull MapFactory<NK, NV, ?, ? extends Map<NK, NV>> mapFactory() {
        throw new UnsupportedOperationException(); // TODO
    }

    default java.util.@NotNull @UnmodifiableView Map<K, V> asJava() {
        return new AsJavaConvert.MapAsJava<>(this);
    }

    @Override
    default boolean canEqual(Object other) {
        return other instanceof Map<?, ?>;
    }
}
