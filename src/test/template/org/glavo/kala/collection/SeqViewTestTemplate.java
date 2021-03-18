package org.glavo.kala.collection;

public interface SeqViewTestTemplate extends SeqLikeTestTemplate, FullSeqLikeTestTemplate {
    @Override
    <E> SeqView<E> of(E... elements);

    @Override
    <E> SeqView<E> from(E[] elements);

    @Override
    <E> SeqView<E> from(Iterable<? extends E> elements);
}
