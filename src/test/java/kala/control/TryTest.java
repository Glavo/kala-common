package kala.control;

import kala.SerializationUtils;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class TryTest {

    @Test
    public void ofTest() {
        assertEquals(Try.success("foo"), Try.of(() -> "foo"));
        assertEquals(Try.success("foo"), Try.ofCallable(() -> "foo"));
        assertEquals(Try.success(null), Try.run(() -> {
        }));
        assertEquals(Try.success(null), Try.runRunnable(() -> {
        }));

        MyException ex = new MyException();
        assertEquals(Try.failure(ex), Try.of(() -> {
            throw ex;
        }));
        assertEquals(Try.failure(ex), Try.ofCallable(() -> {
            throw ex;
        }));
        assertEquals(Try.failure(ex), Try.run(() -> {
            throw ex;
        }));
        assertEquals(Try.failure(ex), Try.runRunnable(() -> {
            throw ex;
        }));

        assertThrows(OutOfMemoryError.class, () -> Try.of(() -> {
            throw new OutOfMemoryError();
        }));
        assertThrows(OutOfMemoryError.class, () -> Try.ofCallable(() -> {
            throw new OutOfMemoryError();
        }));
        assertThrows(OutOfMemoryError.class, () -> Try.run(() -> {
            throw new OutOfMemoryError();
        }));
        assertThrows(OutOfMemoryError.class, () -> Try.runRunnable(() -> {
            throw new OutOfMemoryError();
        }));
    }

    @Test
    public void mapTest() {
        MyException ex = new MyException();

        assertEquals(Try.success(3), Try.success("foo").map(String::length));
        assertEquals(Try.failure(ex), Try.failure(ex).map(arg -> {
            throw new Exception();
        }));
        assertEquals(Try.failure(ex), Try.success("foo").map(str -> {
            throw ex;
        }));
    }

    @Test
    public void serializationTest() throws IOException, ClassNotFoundException {
        assertEquals(Try.success("foo"), SerializationUtils.writeAndRead(Try.success("foo")));
    }

    private static final class MyException extends RuntimeException {
    }
}
