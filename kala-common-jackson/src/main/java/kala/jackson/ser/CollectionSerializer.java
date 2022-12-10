package kala.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import kala.collection.Collection;

import java.io.IOException;

public class CollectionSerializer extends ContainerSerializer<Collection<?>> implements ContextualSerializer {
    private final JavaType contentType;
    private final ObjectArraySerializer delegate;

    public CollectionSerializer(CollectionLikeType type, ObjectArraySerializer delegate) {
        super(type);
        this.contentType = type.getContentType();
        this.delegate = delegate;
    }

    public CollectionSerializer(CollectionSerializer base, ObjectArraySerializer delegate) {
        super(base);
        this.contentType = base.getContentType();
        this.delegate = delegate;
    }


    @Override
    public boolean isEmpty(SerializerProvider provider, Collection<?> value) {
        return value.isEmpty();
    }

    @Override
    public JavaType getContentType() {
        return contentType;
    }

    @Override
    public boolean hasSingleElement(Collection<?> value) {
        return value.sizeEquals(1);
    }

    @Override
    protected ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
        ObjectArraySerializer ser = (ObjectArraySerializer) delegate._withValueTypeSerializer(vts);
        return ser == delegate ? this : withDelegate(ser);
    }

    @Override
    public JsonSerializer<?> getContentSerializer() {
        return delegate.getContentSerializer();
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        return withDelegate((ObjectArraySerializer) delegate.createContextual(prov, property));
    }

    @Override
    public void serialize(Collection<?> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        delegate.serialize(value.toArray(), gen, provider);
    }

    @Override
    public void serializeWithType(Collection<?> value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        delegate.serializeWithType(value.toArray(), gen, serializers, typeSer);
    }

    private CollectionSerializer withDelegate(ObjectArraySerializer newDelegate) {
        return (newDelegate == delegate) ? this : new CollectionSerializer(this, newDelegate);
    }
}
