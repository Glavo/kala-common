package org.glavo.kala.collection.mutable;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class MutableHashMapTest {
    @Test
    void ofTest() {
        assertEquals(0, MutableHashMap.of().size());
        assertFalse(MutableHashMap.of().iterator().hasNext());

        MutableHashMap<String, Integer> map1 = MutableHashMap.of("key1", 1);
        assertEquals(1, map1.size());
        assertEquals(1, map1.get("key1"));
        assertThrows(NoSuchElementException.class, () -> map1.get("key2"));
    }
}
