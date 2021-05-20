package kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface BufferOps<E, CC extends Buffer<?>, COLL extends Buffer<E>>
        extends MutableSeqOps<E, CC, COLL> {
    @Override
    default @NotNull BufferEditor<E, COLL> edit() {
        return new BufferEditor<E, COLL>((COLL) this);
    }
}
