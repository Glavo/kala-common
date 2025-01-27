/*
 * Copyright 2025 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.mutable;

import kala.SerializationUtils;
import kala.collection.factory.MapFactory;
import kala.function.Hasher;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

// See also: https://github.com/scala/scala/blob/8cc248dc1305df4c17bb6b5738b700b60c9b5437/test/junit/scala/collection/mutable/HashMapTest.scala
public final class MutableHashMapTest implements MutableMapTestTemplate {

    @Override
    public <K, V> MapFactory<K, V, ?, MutableHashMap<K, V>> factory() {
        return MutableHashMap.factory();
    }

    @Test
    public void fromTest() {
        assertEquals(MutableHashMap.of("str1", 1, "str2", 2, "str3", 3),
                MutableHashMap.from(java.util.Map.of("str1", 1, "str2", 2, "str3", 3)));
    }

    @Test
    public void cloneTest() {
        Integer[][] data1 = data1();
        String[][] data1s = data1s();

        for (int i = 0; i < data1.length; i++) {
            Integer[] d1 = data1[i];
            String[] d1s = data1s[i];

            MutableHashMap<String, Integer> m = new MutableHashMap<>();
            for (int j = 0; j < d1.length; j++) {
                m.set(d1s[j], d1[j]);
            }

            MutableHashMap<String, Integer> newM = m.clone();
            assertEquals(m, newM);

            newM.put("(new key)", 100);
            newM.put(d1s[0], d1[0] + 100);

            assertEquals(d1.length, m.size());
            assertEquals(d1.length + 1, newM.size());

            assertEquals(d1[0], m.get(d1s[0]));
            assertEquals(d1[0] + 100, newM.get(d1s[0]));

            for (int j = 1; j < d1.length; j++) {
                String s = d1s[j];
                assertTrue(newM.containsKey(s));
                assertEquals(d1[j], newM.get(s));
            }
        }
    }

    @Test
    public void putTest() {
        // put null
        MutableHashMap<String, Integer> m = new MutableHashMap<>();
        m.put(null, 1234);

        assertTrue(m.containsKey(null));
        assertEquals(1234, m.get(null));
    }

    @Test
    public void putAllTest() {

        Integer[][] data1 = data1();
        String[][] data1s = data1s();

        for (int i = 0; i < data1.length; i++) {
            Integer[] d1 = data1[i];
            String[] d1s = data1s[i];

            MutableHashMap<String, Integer> m = new MutableHashMap<>();

            HashMap<String, Integer> hm = new HashMap<>();
            for (int j = 0; j < d1.length; j++) {
                hm.put(d1s[j], d1[j]);
            }

            m.putAll(hm);
            assertEquals(hm.size(), m.size());
            hm.forEach((k, v) -> {
                assertTrue(m.containsKey(k));
                assertEquals(v, m.get(k));
            });

            m.putAll(m);
            assertEquals(hm.size(), m.size());
            hm.forEach((k, v) -> {
                assertTrue(m.containsKey(k));
                assertEquals(v, m.get(k));
            });
        }
    }

    @Test
    public void serializationTest() throws Exception {
        {
            final var map1 = new MutableHashMap<>();
            final var map2 = SerializationUtils.writeAndRead(map1);

            assertNotSame(map1, map2);
            assertEquals(0, map2.size());
        }

        {
            final var map1 = MutableHashMap.of("A", 10, "B", 20, "C", 30);
            final var map2 = SerializationUtils.writeAndRead(map1);

            assertNotSame(map1, map2);
            assertEquals(map1.size(), map2.size());

            map1.forEach((k, v) -> assertEquals(v, map2.get(k)));
        }

        for (int i = 0; i < data1().length; i++) {
            var keys = data1()[i];
            var values = data1s()[i];

            final MutableHashMap<Integer, String> map1 = new MutableHashMap<>();
            for (int j = 0; j < keys.length; j++) {
                map1.set(keys[j], values[j]);
            }

            final var map2 = SerializationUtils.writeAndRead(map1);

            assertNotSame(map1, map2);
            assertEquals(map1.size(), map2.size());
            map1.forEach((k, v) -> assertEquals(v, map2.get(k)));
        }
    }

    static final class CustomHasherTest implements MutableMapTestTemplate {

        private static <K> Hasher<K> hasher() {
            return key -> Objects.hashCode(key) + 1;
        }

        @Override
        public <K, V> MapFactory<K, V, ?, MutableHashMap<K, V>> factory() {
            return MutableHashMap.factory(hasher());
        }

        @Override
        public Class<?> mapType() {
            return null;
        }
    }
}
