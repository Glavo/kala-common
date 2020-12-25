package asia.kala.collection.immutable;

import asia.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static asia.kala.collection.Assertions.*;

public final class ImmutableListTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableList<E>> factory() {
        return ImmutableList.factory();
    }

    @Test
    public final void ofTest() {
        assertIsEmpty(ImmutableList.of());
        assertElements(ImmutableList.of("str1"), "str1");
        assertElements(ImmutableList.of("str1", "str2"), "str1", "str2");
        assertElements(ImmutableList.of("str1", "str2", "str3"), "str1", "str2", "str3");
        assertElements(ImmutableList.of("str1", "str2", "str3", "str4"), "str1", "str2", "str3", "str4");
        assertElements(ImmutableList.of("str1", "str2", "str3", "str4", "str5"), "str1", "str2", "str3", "str4", "str5");
        assertElements(ImmutableList.of("str1", "str2", "str3", "str4", "str5", "str6"), "str1", "str2", "str3", "str4", "str5", "str6");
        for (Integer[] data : data1()) {
            assertElements(ImmutableList.of(data), (Object[]) data);
        }
    }
}
