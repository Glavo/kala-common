package org.glavo.kala.collection;

import org.glavo.kala.annotations.Covariant;

public abstract class AbstractSeqView<@Covariant E> extends AbstractView<E> implements SeqView<E> {
}
