package asia.kala.collection.mutable;

import asia.kala.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBufferFactory<E, B extends Buffer<E>> implements CollectionFactory<E, B, B> {
    @Override
    public void addToBuilder(@NotNull B buffer, E value) {
        buffer.append(value);
    }

    @Override
    public B mergeBuilder(@NotNull B builder1, @NotNull B builder2) {
        builder1.appendAll(builder2);
        return builder1;
    }

    @Override
    public B build(@NotNull B buffer) {
        return buffer;
    }
}
