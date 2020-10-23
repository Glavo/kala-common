package asia.kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

public class MutableCollectionEditor<E, C extends MutableCollection<E>> {
    @NotNull
    protected final C source;

    protected MutableCollectionEditor(@NotNull C source) {
        this.source = source;
    }

    @NotNull
    public C done() {
        return source;
    }
}
