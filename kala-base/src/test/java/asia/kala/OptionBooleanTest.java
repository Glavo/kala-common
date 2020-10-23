package asia.kala;

import asia.kala.control.OptionBoolean;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OptionBooleanTest {
    @Test
    public void testSerialization() {
        assertAll(Stream.of(OptionBoolean.True, OptionBoolean.False, OptionBoolean.None).map(opt -> () -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream(512);
            new ObjectOutputStream(out).writeObject(opt);
            byte[] buffer = out.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(buffer);
            Object obj = new ObjectInputStream(in).readObject();
            assertSame(opt, obj);
        }));
    }
}
