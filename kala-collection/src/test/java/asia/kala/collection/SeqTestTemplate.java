package asia.kala.collection;

import asia.kala.control.Option;
import asia.kala.factory.CollectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static asia.kala.collection.Assertions.*;

public interface SeqTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends Seq<? extends E>> factory();

    @Test
    default void isDefinedAtTest() {
        for (int i = -10; i < 10; i++) {
            assertFalse(factory().empty().isDefinedAt(i));
        }
        assertFalse(factory().empty().isDefinedAt(Integer.MIN_VALUE));
        assertFalse(factory().empty().isDefinedAt(Integer.MAX_VALUE));

        for (Integer[] data : data1()) {
            Seq<? extends Integer> seq = this.<Integer>factory().from(data);
            assertFalse(seq.isDefinedAt(-1));
            assertFalse(seq.isDefinedAt(Integer.MIN_VALUE));

            for (int i = 0; i < data.length; i++) {
                assertTrue(seq.isDefinedAt(i));
            }
            assertFalse(seq.isDefinedAt(data.length));
            assertFalse(seq.isDefinedAt(Integer.MAX_VALUE));
        }
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    default void getTest() {
        for (int i = -10; i < 10; i++) {
            final int iv = i;
            assertThrows(IndexOutOfBoundsException.class, () -> factory().empty().get(iv));
            assertNull(factory().empty().getOrNull(i));
            assertSame(Option.none(), factory().empty().getOption(i));
        }
        assertThrows(IndexOutOfBoundsException.class, () -> factory().empty().get(Integer.MIN_VALUE));
        assertThrows(IndexOutOfBoundsException.class, () -> factory().empty().get(Integer.MAX_VALUE));

        for (Integer[] data : data1()) {
            Seq<? extends Integer> seq = this.<Integer>factory().from(data);
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(Integer.MIN_VALUE));
            assertNull(seq.getOrNull(-1));
            assertNull(seq.getOrNull(Integer.MIN_VALUE));
            assertSame(Option.none(), seq.getOption(-1));
            assertSame(Option.none(), seq.getOption(Integer.MIN_VALUE));

            for (int i = 0; i < data.length; i++) {
                assertSame(data[i], seq.get(i));
                assertSame(data[i], seq.getOrNull(i));
                assertEquals(Option.some(data[i]), seq.getOption(i));
            }
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(data.length));
            assertThrows(IndexOutOfBoundsException.class, () -> seq.get(Integer.MAX_VALUE));
            assertNull(seq.getOrNull(data.length));
            assertNull(seq.getOrNull(Integer.MAX_VALUE));
            assertSame(Option.none(), seq.getOption(data.length));
            assertSame(Option.none(), seq.getOption(Integer.MAX_VALUE));
        }
    }

    @Test
    default void firstTest() {
        assertThrows(NoSuchElementException.class, () -> factory().empty().first());
        for (Integer[] data : data1()) {
            assertSame(data[0], factory().from(data).first());
        }
    }

    @Test
    default void lastTest() {
        assertThrows(NoSuchElementException.class, () -> factory().empty().last());
        for (Integer[] data : data1()) {
            assertSame(data[data.length - 1], factory().from(data).last());
        }
    }
}
