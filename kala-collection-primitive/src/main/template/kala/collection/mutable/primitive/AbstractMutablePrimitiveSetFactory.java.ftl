package kala.collection.mutable.primitive;

import kala.collection.factory.primitive.${Type}CollectionFactory;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMutable${Type}SetFactory<B extends Mutable${Type}Set> implements ${Type}CollectionFactory<B, B> {
    @Override
    public void addToBuilder(@NotNull B builder, ${PrimitiveType} value) {
        builder.add(value);
    }

    @Override
    public B mergeBuilder(@NotNull B builder1, @NotNull B builder2) {
        builder1.addAll(builder2);
        return builder1;
    }

    @Override
    public B build(@NotNull B builder) {
        return builder;
    }
}