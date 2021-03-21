package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.View;
import org.glavo.kala.collection.ViewTestTemplate;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public final class ImmutableSeqTest implements ImmutableSeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ImmutableSeq<E>> factory() {
        return ImmutableSeq.factory();
    }

    @Test
    public final void ofTest() {
        assertIterableEquals(List.of(),ImmutableSeq.of());
        assertIterableEquals(List.of("str1"), ImmutableSeq.of("str1"));
        assertIterableEquals(List.of("str1", "str2"), ImmutableSeq.of("str1", "str2"));
        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableSeq.of("str1", "str2", "str3"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableSeq.of("str1", "str2", "str3", "str4"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableSeq.of("str1", "str2", "str3", "str4", "str5"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableSeq.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableSeq.of(data));
        }
    }

    static final class ViewTest implements ViewTestTemplate {
        @Override
        public <E> View<E> of(E... elements) {
            return ImmutableSeq.from(elements).view();
        }

        @Override
        public <E> View<E> from(E[] elements) {
            return ImmutableSeq.from(elements).view();
        }

        @Override
        public <E> View<E> from(Iterable<? extends E> elements) {
            return ImmutableSeq.<E>from(elements).view();
        }
    }
}
