package kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface MutableSeqOps<E, CC extends MutableSeq<?>, COLL extends MutableSeq<E>>
        extends MutableCollectionOps<E, CC, COLL> {
    @Override
    default @NotNull MutableSeqEditor<E, COLL> edit() {
        return new MutableSeqEditor<E, COLL>((COLL) this);
    }
}
