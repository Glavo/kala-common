package kala.jackson.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kala.control.Result;
import kala.jackson.KalaBaseModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResultSerializerTest {
    @Test
    public void test() throws JsonProcessingException {
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new KalaBaseModule())
                .build();

        ObjectNode ok = ((ObjectNode) mapper.readTree(mapper.writeValueAsString(Result.ok(10))));
        assertEquals(1, ok.size());
        assertEquals(10, ok.get("value").asInt());

        ObjectNode err = ((ObjectNode) mapper.readTree(mapper.writeValueAsString(Result.err(10))));
        assertEquals(1, err.size());
        assertEquals(10, err.get("err").asInt());
    }
}
