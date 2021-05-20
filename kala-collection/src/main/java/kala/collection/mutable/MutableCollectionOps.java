package kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface MutableCollectionOps<E, CC extends MutableCollection<?>, COLL extends MutableCollection<E>> {
    default @NotNull MutableCollectionEditor<E, COLL> edit() {
        return new MutableCollectionEditor<E, COLL>((COLL) this);
    }

    COLL clone();
}
