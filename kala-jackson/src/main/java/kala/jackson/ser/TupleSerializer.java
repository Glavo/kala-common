package kala.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import kala.tuple.AnyTuple;

import java.io.IOException;

public class TupleSerializer extends JsonSerializer<AnyTuple> {
    @Override
    public void serialize(AnyTuple value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartArray();
        for (int i = 0, arity = value.arity(); i < arity; i++) {
            serializers.defaultSerializeValue(value.elementAt(i), gen);
        }
        gen.writeEndArray();
    }
}
