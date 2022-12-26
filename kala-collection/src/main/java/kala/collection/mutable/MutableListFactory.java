package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MutableListFactory<E, B extends MutableList<E>> extends CollectionFactory<E, B, B> {

    @SuppressWarnings("unchecked")
    static <E, B extends MutableList<E>> MutableListFactory<E, B> cast(MutableListFactory<?, ?> factory) {
        return (MutableListFactory<E, B>) factory;
    }

    @Override
    default void addToBuilder(@NotNull B builder, E value) {
        builder.append(value);
    }

    @Override
    default B mergeBuilder(@NotNull B builder1, @NotNull B builder2) {
        builder1.appendAll(builder2);
        return builder1;
    }

    @Override
    default B build(@NotNull B builder) {
        return builder;
    }
}
