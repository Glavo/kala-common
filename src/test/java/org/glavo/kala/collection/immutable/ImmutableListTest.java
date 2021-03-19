package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.SeqView;
import org.glavo.kala.collection.SeqViewTestTemplate;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.mutable.LinkedBuffer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class ImmutableListTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableList<E>> factory() {
        return ImmutableList.factory();
    }

    @Test
    public final void ofTest() {
        assertIterableEquals(List.of(), ImmutableList.of());
        assertIterableEquals(List.of("str1"), ImmutableList.of("str1"));
        assertIterableEquals(List.of("str1", "str2"), ImmutableList.of("str1", "str2"));
        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableList.of("str1", "str2", "str3"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableList.of("str1", "str2", "str3", "str4"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableList.of("str1", "str2", "str3", "str4", "str5"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableList.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableList.of(data));
        }
    }

    @Test
    public final void mergeBuilderTest() {
        LinkedBuffer<String> b1 = new LinkedBuffer<>();
        LinkedBuffer<String> b2 = new LinkedBuffer<>();

        assertTrue(ImmutableList.Builder.merge(b1, b2).isEmpty());

        b1 = LinkedBuffer.of("str1", "str2");
        b2 = LinkedBuffer.of("str3", "str4");

        var m = ImmutableList.Builder.merge(b1, b2);
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), m);
    }

    static final class ViewTest implements SeqViewTestTemplate {
        @Override
        public <E> SeqView<E> of(E... elements) {
            return ImmutableList.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(E[] elements) {
            return ImmutableList.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(Iterable<? extends E> elements) {
            return ImmutableList.<E>from(elements).view();
        }
    }
}
