package asia.kala.collection;

import asia.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static asia.kala.collection.Assertions.*;

public interface SeqTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends Seq<? extends E>> factory();

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    default void getTest() {
        for (int i = -10; i < 10; i++) {
            final int iv = i;
            assertThrows(IndexOutOfBoundsException.class, () -> factory().empty().get(iv));
        }
        assertThrows(IndexOutOfBoundsException.class, () -> factory().empty().get(Integer.MIN_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> factory().empty().get(Integer.MAX_VALUE));

        for (Integer[] data : TestData.data1) {
            Seq<? extends Integer> seq = this.<Integer>factory().from(data);
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(Integer.MIN_VALUE));

            for (int i = 0; i < data.length; i++) {
                assertSame(data[i], seq.get(i));
            }
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(data.length));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(Integer.MAX_VALUE));
        }
    }
}
