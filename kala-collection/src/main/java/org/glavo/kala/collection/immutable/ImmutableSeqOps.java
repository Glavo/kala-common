package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.internal.FullSeqOps;

public interface ImmutableSeqOps<E, CC extends ImmutableSeq<?>, COLL extends ImmutableSeq<E>>
        extends FullSeqOps<E, CC, COLL> {

}
