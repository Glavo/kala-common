package kala.collection;

import kala.annotations.Covariant;

public abstract class AbstractSeqView<@Covariant E> extends AbstractView<E> implements SeqView<E> {
}
