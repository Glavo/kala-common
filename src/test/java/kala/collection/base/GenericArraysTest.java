package kala.collection.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenericArraysTest {



    @Test
    void windowedTest() {
        String[] values = {"A", "B", "C", "D", "E", "F", "G", "H"};

        assertArrayEquals(
                new String[][]{{"A", "B", "C"}, {"B", "C", "D"}, {"C", "D", "E"}, {"D", "E", "F"}, {"E", "F", "G"}, {"F", "G", "H"}},
                GenericArrays.windowed(values, 3, 1)
        );
    }
}
