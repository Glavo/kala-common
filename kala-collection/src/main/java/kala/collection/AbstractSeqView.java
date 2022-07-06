package kala.collection;

import kala.annotations.Covariant;

public abstract class AbstractSeqView<@Covariant E> extends AbstractCollectionView<E> implements SeqView<E> {
}
