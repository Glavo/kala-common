package asia.kala.collection.mutable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedBufferTest {

    @Test
    void testRemove() {
        var buffer1 = LinkedBuffer.of("A", "B", "C");

        assertEquals("A", buffer1.removeAt(0));
        assertEquals(LinkedBuffer.of("B", "C"), buffer1);

        assertEquals("C", buffer1.removeAt(1));
        assertEquals(LinkedBuffer.of("B"), buffer1);

        assertEquals("B", buffer1.removeAt(0));
        assertEquals(LinkedBuffer.of(), buffer1);


        var buffer2 = LinkedBuffer.of("A", "B", "C", "D", "E");

        buffer2.removeAt(0, 2);
        assertEquals(LinkedBuffer.of("C", "D", "E"), buffer2);

        buffer2.removeAt(1, 2);
        assertEquals(LinkedBuffer.of("C"), buffer2);
    }


}
