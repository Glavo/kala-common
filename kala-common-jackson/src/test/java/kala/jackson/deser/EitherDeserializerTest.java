package kala.jackson.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import kala.control.Either;
import kala.jackson.KalaBaseModule;
import kala.reflect.TypeLiteral;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

public class EitherDeserializerTest {

    @Test
    public void test() throws JsonProcessingException {
        JsonMapper mapper = JsonMapper.builder()
                .addModule(new KalaBaseModule())
                .build();

        // TODO
    }
}
