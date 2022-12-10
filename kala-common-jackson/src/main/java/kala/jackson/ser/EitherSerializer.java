package kala.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import kala.control.Either;

import java.io.IOException;

public class EitherSerializer extends SerializerBase<Either<?, ?>> {
    public EitherSerializer(JavaType type) {
        super(type);
    }

    @Override
    public void serialize(Either<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if (value.isLeft()) {
            gen.writeFieldName("left");
            write(value.getLeftValue(), 0, gen, provider);
        } else {
            gen.writeFieldName("right");
            write(value.getRightValue(), 1, gen, provider);
        }
        gen.writeEndObject();
    }
}
