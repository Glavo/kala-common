package kala.jackson.deser;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class CollectionDeserializerBase<T> extends ContainerDeserializerBase<T> implements ContextualSerializer {
    protected CollectionDeserializerBase(JavaType selfType, NullValueProvider nuller, Boolean unwrapSingle) {
        super(selfType, nuller, unwrapSingle);
    }

    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        return null;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return null;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        return null;
    }
}
