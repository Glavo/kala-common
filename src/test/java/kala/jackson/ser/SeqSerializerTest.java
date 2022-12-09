package kala.jackson.ser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import kala.collection.Seq;
import kala.jackson.KalaCollectionModule;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SeqSerializerTest {
    @Test
    public void test() throws IOException {
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new KalaCollectionModule())
                .build();

        JsonNode jsonNode = mapper.readTree(mapper.writeValueAsString(Seq.empty()));
        assertEquals(0, jsonNode.size());

        jsonNode = mapper.readTree(mapper.writeValueAsString(Seq.of(0, 1, 2)));
        assertEquals(3, jsonNode.size());
        assertEquals(0, jsonNode.get(0).asInt());
        assertEquals(1, jsonNode.get(1).asInt());
        assertEquals(2, jsonNode.get(2).asInt());
    }
}
