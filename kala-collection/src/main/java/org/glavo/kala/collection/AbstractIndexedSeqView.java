package org.glavo.kala.collection;

import org.glavo.kala.annotations.Covariant;

public abstract class AbstractIndexedSeqView<@Covariant E> extends AbstractSeqView<E> implements IndexedSeqView<E> {
}
