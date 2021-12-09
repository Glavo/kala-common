package kala.collection.mutable;

import kala.collection.Collection;
import kala.collection.MapTestTemplate;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public interface MutableMapTestTemplate extends MapTestTemplate {
    <K, V> MutableMap<K, V> create();

    @TestFactory
    default DynamicNode getOrPutTestFactory() {
        return DynamicContainer.dynamicContainer("getOrPutTest", List.of(
                dynamicTest("mutationInCallback", () -> {
                    MutableMap<String, String> hm = create();

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
                }),
                dynamicTest("evalOnce", () -> {
                    MutableMap<Integer, Integer> hm = create();
                    int[] i = new int[]{0};
                    hm.getOrPut(0, () -> {
                        i[0] += 1;
                        return i[0];
                    });
                    assertEquals(1, hm.get(0));
                }),
                dynamicTest("noEval", () -> {
                    MutableMap<Integer, Integer> hm = create();
                    hm.put(0, 0);
                    assertEquals(0, hm.getOrPut(0, Assertions::fail));
                }),
                dynamicTest("keyIdempotence", () -> {
                    MutableMap<String, String> hm = create();
                    String key = "key";
                    hm.getOrPut(key, () -> {
                        hm.getOrPut(key, () -> "value1");
                        return "value2";
                    });

                    assertEquals(1, hm.size());
                    assertEquals("value2", hm.get(key));
                })
        ));
    }
}
