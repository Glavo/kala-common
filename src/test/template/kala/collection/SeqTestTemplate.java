package kala.collection;

import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableLinkedSeq;
import kala.collection.immutable.ImmutableVector;
import kala.collection.mutable.MutableArray;
import kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public interface SeqTestTemplate extends CollectionTestTemplate, SeqLikeTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends Seq<? extends E>> factory();

    @Override
    default <E> Seq<E> of(E... elements) {
        return (Seq<E>) this.<E>factory().from(elements);
    }

    @Override
    default <E> Seq<E> from(E[] elements) {
        return (Seq<E>) this.<E>factory().from(elements);
    }

    @Override
    default <E> Seq<E> from(Iterable<? extends E> elements) {
        return (Seq<E>) this.<E>factory().from(elements);
    }

    @Test
    default void equalsTest() {
        Assertions.assertEquals(ImmutableArray.empty(), factory().empty());
        assertEquals(ImmutableVector.empty(), factory().empty());
        Assertions.assertEquals(ImmutableLinkedSeq.empty(), factory().empty());
        assertEquals(MutableArray.empty(), factory().empty());

        Seq<?> foo = factory().from(List.of("foo"));
        assertNotEquals(ImmutableArray.empty(), foo);
        assertNotEquals(ImmutableVector.empty(), foo);
        assertNotEquals(MutableArray.empty(), foo);
        assertEquals(ImmutableArray.of("foo"), foo);
        assertEquals(ImmutableVector.of("foo"), foo);
        assertEquals(MutableArray.of("foo"), foo);

        for (Integer[] data : data1()) {
            assertEquals(ImmutableArray.from(data), factory().from(data));
            assertEquals(ImmutableVector.from(data), factory().from(data));
            assertEquals(MutableArray.from(data), factory().from(data));
        }


    }
}
