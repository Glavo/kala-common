package kala.collection;

public interface AnySeq<E> extends AnyCollection<E>, AnySeqLike<E> {
    @Override
    default boolean canEqual(Object other) {
        return other instanceof AnySeq;
    }
}
