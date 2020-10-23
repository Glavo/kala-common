package asia.kala.collection;

import asia.kala.annotations.Covariant;

public abstract class AbstractSetView<@Covariant E> extends AbstractView<E> implements SetView<E> {
}

