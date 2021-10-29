package kala.control;

import kala.SerializationUtils;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class ResultTest {

    Result<String, ?> ok = Result.ok("value");
    Result<?, Integer> err = Result.err(10);

    @Test
    public void factoryTest() {
        assertTrue(ok.isDefined());
        assertFalse(err.isDefined());
        assertFalse(ok.isErr());
        assertTrue(err.isErr());
    }

    @Test
    public void getTest() {
        assertEquals("value", ok.get());
        assertThrows(NoSuchElementException.class, ok::getErr);
        assertThrows(NoSuchElementException.class, err::get);
        assertEquals(10, err.getErr());
    }

    @Test
    public void mapTest() {
        assertEquals(Result.ok(5), ok.map(String::length));
        assertSame(ok, ok.mapErr(it -> null));

        assertSame(err, err.map(it -> null));
        assertEquals(Result.err("10"), err.mapErr(String::valueOf));
    }

    @Test
    public void serializationTest() throws IOException, ClassNotFoundException {
        assertEquals(ok, SerializationUtils.writeAndRead(ok));
        assertEquals(err, SerializationUtils.writeAndRead(err));
    }
}
