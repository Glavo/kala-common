package asia.kala.collection.mutable;

import asia.kala.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMutableSetFactory<E, S extends MutableSet<E>> implements CollectionFactory<E, S, S> {
    @Override
    public void addToBuilder(@NotNull S es, E value) {
        es.add(value);
    }

    @Override
    public S mergeBuilder(@NotNull S builder1, @NotNull S builder2) {
        builder1.addAll(builder2);
        return builder1;
    }

    @Override
    public S build(@NotNull S es) {
        return es;
    }
}
