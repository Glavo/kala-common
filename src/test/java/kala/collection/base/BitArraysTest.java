package kala.collection.base;

import kala.collection.base.primitive.BitArrays;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BitArraysTest {
    @Test
    public void iteratorTest() {
        assertArrayEquals(new boolean[]{}, BitArrays.iterator(new long[]{}, 0, 0).toArray());
        assertArrayEquals(new boolean[]{}, BitArrays.iterator(new long[]{0b00}, 0, 0).toArray());
        assertArrayEquals(new boolean[]{false}, BitArrays.iterator(new long[]{0b00}, 0, 1).toArray());
        assertArrayEquals(new boolean[]{}, BitArrays.iterator(new long[]{0b01}, 0, 0).toArray());
        assertArrayEquals(new boolean[]{true}, BitArrays.iterator(new long[]{0b01}, 0, 1).toArray());
        assertArrayEquals(new boolean[]{true, false, false, false}, BitArrays.iterator(new long[]{0b01}, 0, 4).toArray());
        assertArrayEquals(new boolean[]{false, false, false}, BitArrays.iterator(new long[]{0b01}, 1, 4).toArray());
        assertArrayEquals(new boolean[]{true, false, false}, BitArrays.iterator(new long[]{0b10}, 1, 4).toArray());


        Random random = new Random(0);
        for (int testArrayLength = 0; testArrayLength < Long.SIZE * 3; testArrayLength++) {
            boolean[] testArray = new boolean[testArrayLength];
            long[] testBitsArray = new long[testArrayLength / Long.SIZE + (testArrayLength % Long.SIZE == 0 ? 0 : 1)];

            for (int i = 0; i < testArrayLength; i++) {
                if (random.nextBoolean()) {
                    testArray[i] = true;
                    BitArrays.set(testBitsArray, i, true);
                }
            }

            assertArrayEquals(testArray, BitArrays.iterator(testBitsArray, 0, testArrayLength).toArray());
            if (testArrayLength >= 1)
                assertArrayEquals(Arrays.copyOfRange(testArray, 1, testArrayLength), BitArrays.iterator(testBitsArray, 1, testArrayLength).toArray());
            if (testArrayLength >= Long.SIZE + 1)
                assertArrayEquals(Arrays.copyOfRange(testArray, Long.SIZE + 1, testArrayLength), BitArrays.iterator(testBitsArray, Long.SIZE + 1, testArrayLength).toArray());
        }

    }
}
