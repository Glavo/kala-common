package kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

public class MutableCollectionEditor<E, C extends MutableCollection<E>> {

    protected final @NotNull C source;

    protected MutableCollectionEditor(@NotNull C source) {
        this.source = source;
    }

    public @NotNull C done() {
        return source;
    }
}
