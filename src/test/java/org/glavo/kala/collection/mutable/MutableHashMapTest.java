package org.glavo.kala.collection.mutable;

import org.glavo.kala.tuple.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

// See also: https://github.com/scala/scala/blob/8cc248dc1305df4c17bb6b5738b700b60c9b5437/test/junit/scala/collection/mutable/HashMapTest.scala
public class MutableHashMapTest {
    @Test
    void ofTest() {
        assertEquals(0, MutableHashMap.of().size());
        assertFalse(MutableHashMap.of().iterator().hasNext());

        MutableHashMap<String, Integer> map1 = MutableHashMap.of("key1", 1);
        assertEquals(1, map1.size());
        assertEquals(1, map1.get("key1"));
        assertThrows(NoSuchElementException.class, () -> map1.get("other"));

        MutableHashMap<String, Integer> map2 = MutableHashMap.of("key1", 1, "key2", 2);
        assertEquals(2, map2.size());
        assertEquals(1, map2.get("key1"));
        assertEquals(2, map2.get("key2"));
        assertThrows(NoSuchElementException.class, () -> map2.get("other"));

        MutableHashMap<String, Integer> map3 =
                MutableHashMap.of("key1", 1, "key2", 2, "key3", 3);
        assertEquals(3, map3.size());
        assertEquals(1, map3.get("key1"));
        assertEquals(2, map3.get("key2"));
        assertEquals(3, map3.get("key3"));
        assertThrows(NoSuchElementException.class, () -> map3.get("other"));

        MutableHashMap<String, Integer> map4 =
                MutableHashMap.of("key1", 1, "key2", 2, "key3", 3, "key4", 4);
        assertEquals(4, map4.size());
        assertEquals(1, map4.get("key1"));
        assertEquals(2, map4.get("key2"));
        assertEquals(3, map4.get("key3"));
        assertEquals(4, map4.get("key4"));
        assertThrows(NoSuchElementException.class, () -> map4.get("other"));

        MutableHashMap<String, Integer> map5 =
                MutableHashMap.of("key1", 1, "key2", 2, "key3", 3, "key4", 4, "key5", 5);
        assertEquals(5, map5.size());
        assertEquals(1, map5.get("key1"));
        assertEquals(2, map5.get("key2"));
        assertEquals(3, map5.get("key3"));
        assertEquals(4, map5.get("key4"));
        assertEquals(5, map5.get("key5"));
        assertThrows(NoSuchElementException.class, () -> map5.get("other"));
    }

    @Test
    void ofEntriesTest() {
        assertEquals(0, MutableHashMap.ofEntries().size());
        assertFalse(MutableHashMap.ofEntries().iterator().hasNext());

        MutableHashMap<String, Integer> map1 = MutableHashMap.ofEntries(Tuple.of("key1", 1));
        assertEquals(1, map1.size());
        assertEquals(1, map1.get("key1"));
        assertThrows(NoSuchElementException.class, () -> map1.get("other"));

        MutableHashMap<String, Integer> map2 = MutableHashMap.ofEntries(Tuple.of("key1", 1), Tuple.of("key2", 2));
        assertEquals(2, map2.size());
        assertEquals(1, map2.get("key1"));
        assertEquals(2, map2.get("key2"));
        assertThrows(NoSuchElementException.class, () -> map2.get("other"));

        MutableHashMap<String, Integer> map3 =
                MutableHashMap.ofEntries(Tuple.of("key1", 1), Tuple.of("key2", 2), Tuple.of("key3", 3));
        assertEquals(3, map3.size());
        assertEquals(1, map3.get("key1"));
        assertEquals(2, map3.get("key2"));
        assertEquals(3, map3.get("key3"));
        assertThrows(NoSuchElementException.class, () -> map3.get("other"));

        MutableHashMap<String, Integer> map4 =
                MutableHashMap.ofEntries(Tuple.of("key1", 1), Tuple.of("key2", 2), Tuple.of("key3", 3), Tuple.of("key4", 4));
        assertEquals(4, map4.size());
        assertEquals(1, map4.get("key1"));
        assertEquals(2, map4.get("key2"));
        assertEquals(3, map4.get("key3"));
        assertEquals(4, map4.get("key4"));
        assertThrows(NoSuchElementException.class, () -> map4.get("other"));

        MutableHashMap<String, Integer> map5 =
                MutableHashMap.ofEntries(
                        Tuple.of("key1", 1), Tuple.of("key2", 2),
                        Tuple.of("key3", 3), Tuple.of("key4", 4),
                        Tuple.of("key5", 5));
        assertEquals(5, map5.size());
        assertEquals(1, map5.get("key1"));
        assertEquals(2, map5.get("key2"));
        assertEquals(3, map5.get("key3"));
        assertEquals(4, map5.get("key4"));
        assertEquals(5, map5.get("key5"));
        assertThrows(NoSuchElementException.class, () -> map5.get("other"));

        MutableHashMap<String, Integer> map6 =
                MutableHashMap.ofEntries(
                        Tuple.of("key1", 1), Tuple.of("key2", 2),
                        Tuple.of("key3", 3), Tuple.of("key4", 4),
                        Tuple.of("key5", 5), Tuple.of("key6", 6));
        assertEquals(6, map6.size());
        assertEquals(1, map6.get("key1"));
        assertEquals(2, map6.get("key2"));
        assertEquals(3, map6.get("key3"));
        assertEquals(4, map6.get("key4"));
        assertEquals(5, map6.get("key5"));
        assertEquals(6, map6.get("key6"));
        assertThrows(NoSuchElementException.class, () -> map5.get("other"));
    }

    @Test
    void fromTest() {
        assertEquals(MutableHashMap.of("str1", 1, "str2", 2, "str3", 3),
                MutableHashMap.from(java.util.Map.of("str1", 1, "str2", 2, "str3", 3)));
    }

    @Nested
    @DisplayName("getOrPutTest")
    class GetOrPutTest {
        @Test
        void mutationInCallback() {
            var hm = new MutableHashMap<String, String>();

            Supplier<String> add = () -> {
                // add enough elements to resize the hash table in the callback
                for (int i = 1; i <= 100000; i++) {
                    hm.set(String.valueOf(i), "callback");
                }
                return "str";
            };

            hm.getOrPut("0", add);
            assertEquals(100001, hm.size());
            assertEquals("str", hm.get("0"));
        }

        @Test
        void evalOnce() {
            var hm = new MutableHashMap<Integer, Integer>();
            int[] i = new int[]{0};
            hm.getOrPut(0, () -> {
                i[0] += 1;
                return i[0];
            });
            assertEquals(1, hm.get(0));
        }

        @Test
        void noEval() {
            var hm = new MutableHashMap<Integer, Integer>();
            hm.put(0, 0);
            assertEquals(0, hm.getOrPut(0, Assertions::fail));
        }

        @Test
        void keyIdempotence() {
            var hm = new MutableHashMap<String, String>();
            String key = "key";
            hm.getOrPut(key, () -> {
                hm.getOrPut(key, () -> "value1");
                return "value2";
            });

            assertEquals(1, hm.size());
            assertEquals("value2", hm.get(key));
        }
    }
}
