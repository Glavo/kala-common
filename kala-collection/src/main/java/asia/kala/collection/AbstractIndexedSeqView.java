package asia.kala.collection;

import asia.kala.annotations.Covariant;

public abstract class AbstractIndexedSeqView<@Covariant E> extends AbstractSeqView<E> implements IndexedSeqView<E> {
}
