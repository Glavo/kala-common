package asia.kala.traversable;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class JavaArrayTest {
    @Test
    void testWindowed() {
        String[] values = {"A", "B", "C", "D", "E", "F", "G", "H"};

        assertArrayEquals(
                new String[][]{{"A", "B", "C"}, {"B", "C", "D"}, {"C", "D", "E"}, {"D", "E", "F"}, {"E", "F", "G"}, {"F", "G", "H"}},
                JavaArray.windowed(values, 3, 1)
        );
    }
}
