package kala.collection.mutable;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface DynamicSeqOps<E, CC extends DynamicSeq<?>, COLL extends DynamicSeq<E>>
        extends MutableSeqOps<E, CC, COLL> {
    @Override
    default @NotNull DynamicSeqEditor<E, COLL> edit() {
        return new DynamicSeqEditor<E, COLL>((COLL) this);
    }
}
