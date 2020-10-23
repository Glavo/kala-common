package asia.kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

public interface MutableCollectionOps<E, CC extends MutableCollection<?>, COLL extends MutableCollection<E>> {
    @NotNull MutableCollectionEditor<E, COLL> edit();

    COLL clone();
}
