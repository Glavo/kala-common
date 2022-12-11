package kala.jackson.ser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import kala.collection.Map;
import kala.jackson.KalaCollectionModule;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapSerializerTest {
    @Test
    public void test() throws IOException {
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new KalaCollectionModule())
                .build();

        JsonNode jsonNode = mapper.readTree(mapper.writeValueAsString(Map.empty()));
        assertEquals(0, jsonNode.size());

        jsonNode = mapper.readTree(mapper.writeValueAsString(Map.of("key0", 0, "key1", 1, "key2", 2)));
        assertEquals(3, jsonNode.size());
        assertEquals(0, jsonNode.get("key0").asInt());
        assertEquals(1, jsonNode.get("key1").asInt());
        assertEquals(2, jsonNode.get("key2").asInt());
    }
}
