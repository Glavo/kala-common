package kala.jackson.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kala.control.Either;
import kala.jackson.KalaBaseModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EitherSerializerTest {
    @Test
    public void test() throws JsonProcessingException {
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new KalaBaseModule())
                .build();

        ObjectNode left = ((ObjectNode) mapper.readTree(mapper.writeValueAsString(Either.left(10))));
        assertEquals(1, left.size());
        assertEquals(10, left.get("left").asInt());

        ObjectNode right = ((ObjectNode) mapper.readTree(mapper.writeValueAsString(Either.right(10))));
        assertEquals(1, right.size());
        assertEquals(10, right.get("right").asInt());
    }
}
