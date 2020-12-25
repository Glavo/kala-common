package asia.kala.collection.immutable;

import asia.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import static asia.kala.collection.Assertions.*;

public final class ImmutableArrayTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableArray<E>> factory() {
        return ImmutableArray.factory();
    }

    @Test
    public final void ofTest() {
        assertIsEmpty(ImmutableArray.of());
        assertElements(ImmutableArray.of("str1"), "str1");
        assertElements(ImmutableArray.of("str1", "str2"), "str1", "str2");
        assertElements(ImmutableArray.of("str1", "str2", "str3"), "str1", "str2", "str3");
        assertElements(ImmutableArray.of("str1", "str2", "str3", "str4"), "str1", "str2", "str3", "str4");
        assertElements(ImmutableArray.of("str1", "str2", "str3", "str4", "str5"), "str1", "str2", "str3", "str4", "str5");
        assertElements(ImmutableArray.of("str1", "str2", "str3", "str4", "str5", "str6"), "str1", "str2", "str3", "str4", "str5", "str6");
        for (Integer[] data : data1()) {
            assertElements(ImmutableArray.of(data), (Object[]) data);
        }
    }
}
