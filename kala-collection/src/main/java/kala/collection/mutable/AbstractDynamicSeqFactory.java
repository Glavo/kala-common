package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractDynamicSeqFactory<E, B extends DynamicSeq<E>> implements CollectionFactory<E, B, B> {
    @Override
    public void addToBuilder(@NotNull B builder, E value) {
        builder.append(value);
    }

    @Override
    public B mergeBuilder(@NotNull B builder1, @NotNull B builder2) {
        builder1.appendAll(builder2);
        return builder1;
    }

    @Override
    public B build(@NotNull B builder) {
        return builder;
    }
}
