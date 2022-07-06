package kala.collection;

import kala.annotations.Covariant;

public abstract class AbstractSetView<@Covariant E> extends AbstractCollectionView<E> implements SetView<E> {
}

