package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.FullSeqLikeTestTemplate;
import org.glavo.kala.collection.SeqTestTemplate;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.mutable.LinkedBuffer;
import org.glavo.kala.collection.mutable.MutableArray;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
