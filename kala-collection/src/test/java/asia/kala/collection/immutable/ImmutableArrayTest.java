package asia.kala.collection.immutable;

import asia.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static asia.kala.collection.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public final class ImmutableArrayTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableArray<E>> factory() {
        return ImmutableArray.factory();
    }

    @Test
    public final void ofTest() {
        assertIsEmpty(ImmutableArray.of());
        assertIterableEquals(List.of("str1"), ImmutableArray.of("str1"));
        assertIterableEquals(List.of("str1", "str2"), ImmutableArray.of("str1", "str2"));
        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableArray.of("str1", "str2", "str3"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableArray.of("str1", "str2", "str3", "str4"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableArray.of("str1", "str2", "str3", "str4", "str5"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableArray.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableArray.of(data));
        }
    }
}
