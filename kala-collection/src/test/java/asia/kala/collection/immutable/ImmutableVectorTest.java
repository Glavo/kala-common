package asia.kala.collection.immutable;


import asia.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import static asia.kala.collection.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public final class ImmutableVectorTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableVector<E>> factory() {
        return ImmutableVector.factory();
    }

    @Test
    public final void ofTest() {
        assertIsEmpty(ImmutableVector.of());

        assertElements(ImmutableVector.of("str1"), "str1");
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1").getClass());

        assertElements(ImmutableVector.of("str1", "str2"), "str1", "str2");
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2").getClass());

        assertElements(ImmutableVector.of("str1", "str2", "str3"), "str1", "str2", "str3");
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2", "str3").getClass());

        assertElements(ImmutableVector.of("str1", "str2", "str3", "str4"), "str1", "str2", "str3", "str4");
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2", "str3", "str4").getClass());

        assertElements(ImmutableVector.of("str1", "str2", "str3", "str4", "str5"), "str1", "str2", "str3", "str4", "str5");
        assertSame(ImmutableVectors.Vector1.class, ImmutableVector.of("str1", "str2", "str3", "str4", "str5").getClass());

        assertElements(ImmutableVector.of("str1", "str2", "str3", "str4", "str5", "str6"), "str1", "str2", "str3", "str4", "str5", "str6");
        for (Integer[] data : data1()) {
            ImmutableVector<Integer> v = ImmutableVector.of(data);
            assertElements(v, (Object[]) data);
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
}
