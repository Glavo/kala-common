package asia.kala;

import asia.kala.control.Option;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class OptionTest {
    Option<?>[] opts = new Option<?>[]{
            Option.none(),
            Option.some("foo"),
            Option.some(10),
            Option.some(Arrays.asList("A", "B", "C")),
            Option.some(null)
    };

    @Test
    public void testSerialization() {
        assertAll(Arrays.stream(opts).map(opt -> () -> {
            ByteArrayOutputStream out = new ByteArrayOutputStream(512);
            new ObjectOutputStream(out).writeObject(opt);
            byte[] buffer = out.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(buffer);
            Object obj = new ObjectInputStream(in).readObject();
            assertEquals(opt, obj);
        }));
    }
}
