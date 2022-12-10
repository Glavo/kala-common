package kala.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import kala.control.Result;

import java.io.IOException;

public class ResultSerializer extends SerializerBase<Result<?, ?>> {

    public ResultSerializer(JavaType type) {
        super(type);
    }

    @Override
    public void serialize(Result<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if (value.isOk()) {
            gen.writeFieldName("value");
            write(value.get(), 0, gen, provider);
        } else {
            gen.writeFieldName("err");
            write(value.getErr(), 1, gen, provider);
        }
        gen.writeEndObject();
    }
}
