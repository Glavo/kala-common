package asia.kala.collection;

import asia.kala.annotations.Covariant;

public abstract class AbstractView<@Covariant E> implements View<E> {
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public String toString() {
        return className() + "[<not computed>]";
    }
}
