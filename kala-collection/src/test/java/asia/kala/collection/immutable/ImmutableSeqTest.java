package asia.kala.collection.immutable;

import asia.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import static asia.kala.collection.Assertions.assertElements;
import static asia.kala.collection.Assertions.assertIsEmpty;

public final class ImmutableSeqTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableSeq<E>> factory() {
        return ImmutableSeq.factory();
    }

    @Test
    public final void ofTest() {
        assertIsEmpty(ImmutableSeq.of());
        assertElements(ImmutableSeq.of("str1"), "str1");
        assertElements(ImmutableSeq.of("str1", "str2"), "str1", "str2");
        assertElements(ImmutableSeq.of("str1", "str2", "str3"), "str1", "str2", "str3");
        assertElements(ImmutableSeq.of("str1", "str2", "str3", "str4"), "str1", "str2", "str3", "str4");
        assertElements(ImmutableSeq.of("str1", "str2", "str3", "str4", "str5"), "str1", "str2", "str3", "str4", "str5");
        assertElements(ImmutableSeq.of("str1", "str2", "str3", "str4", "str5", "str6"), "str1", "str2", "str3", "str4", "str5", "str6");
        for (Integer[] data : data1()) {
            assertElements(ImmutableSeq.of(data), (Object[]) data);
        }
    }
}
