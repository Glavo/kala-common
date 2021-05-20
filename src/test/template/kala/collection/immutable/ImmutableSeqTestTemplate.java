package kala.collection.immutable;

import kala.collection.FullSeqLikeTestTemplate;
import kala.collection.SeqTestTemplate;
import kala.collection.factory.CollectionFactory;

@SuppressWarnings({"unchecked"})
public interface ImmutableSeqTestTemplate extends ImmutableCollectionTestTemplate, SeqTestTemplate, FullSeqLikeTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends ImmutableSeq<? extends E>> factory();

    default <E> ImmutableSeq<E> of(E... elements) {
        return (ImmutableSeq<E>) factory().from(elements);
    }

    default <E> ImmutableSeq<E> from(E[] elements) {
        return (ImmutableSeq<E>) factory().from(elements);
    }

    default <E> ImmutableSeq<E> from(Iterable<? extends E> elements) {
        return (ImmutableSeq<E>) factory().from(elements);
    }
}
