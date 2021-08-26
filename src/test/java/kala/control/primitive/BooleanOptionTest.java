package kala.control.primitive;

import kala.control.primitive.BooleanOption;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BooleanOptionTest {
    @Test
    public void serializationTest() {
        assertAll(Stream.of(BooleanOption.True, BooleanOption.False, BooleanOption.None).map(opt -> () -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream(512);
            new ObjectOutputStream(out).writeObject(opt);
            byte[] buffer = out.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(buffer);
            Object obj = new ObjectInputStream(in).readObject();
            assertSame(opt, obj);
        }));
    }
}
