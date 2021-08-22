package kala.value;

import kala.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class LazyValueTest {

    @Test
    public void ofTest() {
        boolean[] ref = {false};

        LazyValue<String> value = LazyValue.of(() -> {
            ref[0] = true;
            return "value";
        });

        assertFalse(ref[0]);
        assertFalse(value.isReady());

        assertEquals("value", value.get());
        assertTrue(ref[0]);
    }

    @Test
    public void ofValueTest() {
        LazyValue<String> value = LazyValue.ofValue("value");

        assertTrue(value.isReady());
        assertEquals("value", value.get());
    }

    @Test
    public void mapTest() {
        LazyValue<String> value = LazyValue.of(() -> "value");
        LazyValue<Integer> valueLength = value.map(String::length);

        assertFalse(value.isReady());
        assertFalse(valueLength.isReady());

        assertEquals(5, valueLength.get());

        assertTrue(value.isReady());
        assertTrue(valueLength.isReady());
    }

    @Test
    public void iteratorTest() {
        final Iterator<String> it = LazyValue.of(() -> "value").iterator();
        assertTrue(it.hasNext());
        assertEquals("value", it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void toStringTest() {
        LazyValue<String> value = LazyValue.of(() -> "value");

        assertEquals("LazyValue[<not computed>]", value.toString());
        value.get();
        assertEquals("LazyValue[value]", value.toString());
    }

    @Test
    public void serializationTest() throws IOException, ClassNotFoundException {
        LazyValue<String> value = SerializationUtils.writeAndRead(LazyValue.of(() -> "value"));

        assertTrue(value.isReady());
        assertEquals("value", value.get());
    }
}
