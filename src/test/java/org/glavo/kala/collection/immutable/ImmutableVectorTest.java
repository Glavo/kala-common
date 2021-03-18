package org.glavo.kala.collection.immutable;


import org.glavo.kala.collection.SeqView;
import org.glavo.kala.collection.SeqViewTestTemplate;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class ImmutableVectorTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableVector<E>> factory() {
        return ImmutableVector.factory();
    }

    @Test
    public final void ofTest() {
        assertIterableEquals(List.of(),ImmutableVector.of());

        assertIterableEquals(List.of("str1"), ImmutableVector.of("str1"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1").getClass());

        assertIterableEquals(List.of("str1", "str2"), ImmutableVector.of("str1", "str2"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2").getClass());

        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableVector.of("str1", "str2", "str3"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2", "str3").getClass());

        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableVector.of("str1", "str2", "str3", "str4"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2", "str3", "str4").getClass());

        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableVector.of("str1", "str2", "str3", "str4", "str5"));
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2", "str3", "str4", "str5").getClass());

        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableVector.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            ImmutableVector<Integer> v = ImmutableVector.of(data);
            assertIterableEquals(Arrays.asList(data), v );
            if (data.length == 0) {
                assertSame(ImmutableVector.empty(), v);
            } else if (data.length <= ImmutableVectors.WIDTH) {
                assertSame(ImmutableVectors.Vector1.class, v.getClass());
            } else {
                assertNotSame(ImmutableVectors.Vector1.class, v.getClass());
            }
        }
    }

    @Test
    public final void fillTest() {
        ImmutableVector<String> v;

        v = ImmutableVector.fill(ImmutableVectors.WIDTH, "str");
        assertSame(ImmutableVectors.Vector1.class, v.getClass());
        v = ImmutableVector.fill(ImmutableVectors.WIDTH, () -> "str");
        assertSame(ImmutableVectors.Vector1.class, v.getClass());
        v = ImmutableVector.fill(ImmutableVectors.WIDTH, (i) -> "str");
        assertSame(ImmutableVectors.Vector1.class, v.getClass());

        v = ImmutableVector.fill(ImmutableVectors.WIDTH + 1, "str");
        assertSame(ImmutableVectors.Vector2.class, v.getClass());
        v = ImmutableVector.fill(ImmutableVectors.WIDTH + 1, () -> "str");
        assertSame(ImmutableVectors.Vector2.class, v.getClass());
        v = ImmutableVector.fill(ImmutableVectors.WIDTH + 1, (i) -> "str");
        assertSame(ImmutableVectors.Vector2.class, v.getClass());
    }

    static final class ViewTest implements SeqViewTestTemplate {
        @Override
        public <E> SeqView<E> of(E... elements) {
            return ImmutableVector.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(E[] elements) {
            return ImmutableVector.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(Iterable<? extends E> elements) {
            return ImmutableVector.<E>from(elements).view();
        }
    }
}
