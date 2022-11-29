package kala.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.type.ReferenceType;
import kala.control.Either;
import kala.control.Option;
import kala.control.Result;
import kala.jackson.ser.EitherSerializer;
import kala.jackson.ser.OptionSerializer;
import kala.jackson.ser.ResultSerializer;
import kala.jackson.ser.TupleSerializer;
import kala.tuple.AnyTuple;

public class KalaBaseSerializers extends Serializers.Base {
    private static final long serialVersionUID = 0L;

    @Override
    public JsonSerializer<?> findReferenceSerializer(
            SerializationConfig config,
            ReferenceType type, BeanDescription beanDesc,
            TypeSerializer contentTypeSerializer, JsonSerializer<Object> contentValueSerializer) {
        Class<?> rawClass = type.getRawClass();
        boolean staticTyping = contentTypeSerializer == null && config.isEnabled(MapperFeature.USE_STATIC_TYPING);

        if (Option.class.isAssignableFrom(rawClass))
            return new OptionSerializer(type, staticTyping, contentTypeSerializer, contentValueSerializer);
        return null;
    }

    @Override
    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
        Class<?> rawClass = type.getRawClass();

        if (AnyTuple.class.isAssignableFrom(rawClass) && rawClass.getName().startsWith("kala.tuple."))
            return new TupleSerializer();

        if (Either.class.isAssignableFrom(rawClass))
            return new EitherSerializer(type);

        if (Result.class.isAssignableFrom(rawClass))
            return new ResultSerializer(type);

        return super.findSerializer(config, type, beanDesc);
    }
}
