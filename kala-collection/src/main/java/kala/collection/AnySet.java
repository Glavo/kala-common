package kala.collection;

public interface AnySet<E> extends AnyCollection<E>, AnySetLike<E> {
    @Override
    default boolean canEqual(Object other) {
        return other instanceof AnySet;
    }
}
