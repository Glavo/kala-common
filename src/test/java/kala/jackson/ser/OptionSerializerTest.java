package kala.jackson.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kala.control.Either;
import kala.control.Option;
import kala.jackson.KalaBaseModule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionSerializerTest {
    @Test
    public void test() throws JsonProcessingException {
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new KalaBaseModule())
                .build();
        // TODO
    }
}
