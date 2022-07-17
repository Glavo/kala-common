package kala.collection.immutable.primitive;

import kala.collection.primitive.*;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractImmutable${Type}Set extends Abstract${Type}Set implements Immutable${Type}Set {
    static <T, Builder> T added(
            @NotNull ImmutableSet<? extends E> set,
            ${PrimitiveType} value,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, set, 1);
        factory.addAllToBuilder(builder, set);
        factory.addToBuilder(builder, value);
        return factory.build(builder);
    }

    static <T, Builder> T addedAll(
            @NotNull ImmutableSet<? extends E> set,
            @NotNull ${Type}Traversable values,
            @NotNull ${Type}CollectionFactory<Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, set);
        factory.addAllToBuilder(builder, set);
        factory.sizeHint(builder, values);
        factory.addAllToBuilder(builder, values);

        return factory.build(builder);
    }
}
