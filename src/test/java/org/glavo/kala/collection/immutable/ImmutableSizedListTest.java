package org.glavo.kala.collection.immutable;

import org.glavo.kala.collection.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public final class ImmutableSizedListTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableSizedList<E>> factory() {
        return ImmutableSizedList.factory();
    }

    @Test
    public final void ofTest() {
        assertIterableEquals(List.of(), ImmutableSizedList.of());
        assertIterableEquals(List.of("str1"), ImmutableSizedList.of("str1"));
        assertIterableEquals(List.of("str1", "str2"), ImmutableSizedList.of("str1", "str2"));
        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableSizedList.of("str1", "str2", "str3"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableSizedList.of("str1", "str2", "str3", "str4"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableSizedList.of("str1", "str2", "str3", "str4", "str5"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableSizedList.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableSizedList.of(data));
        }
    }
}
