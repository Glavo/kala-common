package kala.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdConverter;
import kala.collection.Collection;
import kala.collection.base.MapIterator;
import kala.internal.MapBase;
import kala.jackson.ser.CollectionSerializer;

import java.util.LinkedHashMap;

public class KalaCollectionSerializers extends Serializers.Base {
    private static final long serialVersionUID = 0L;

    @Override
    public JsonSerializer<?> findMapLikeSerializer(
            SerializationConfig config, MapLikeType type,
            BeanDescription beanDesc, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer) {
        if (!MapBase.class.isAssignableFrom(type.getRawClass()))
            return null;

        return new StdDelegatingSerializer(new MapConverter(type, config));
    }

    @Override
    public JsonSerializer<?> findCollectionLikeSerializer(
            SerializationConfig config,
            CollectionLikeType type,
            BeanDescription beanDesc,
            TypeSerializer elementTypeSerializer,
            JsonSerializer<Object> elementValueSerializer) {
        if (Collection.class.isAssignableFrom(type.getRawClass())) {
            boolean staticTyping = config.isEnabled(MapperFeature.USE_STATIC_TYPING);
            ObjectArraySerializer ser = new ObjectArraySerializer(type.getContentType(),
                    staticTyping, elementTypeSerializer, elementValueSerializer);
            return new CollectionSerializer(type, ser);
        }

        return null;
    }

    private static final class MapConverter extends StdConverter<MapBase<?, ?>, java.util.Map<?, ?>> {
        final MapLikeType inputType;
        final SerializationConfig config;

        MapConverter(MapLikeType inputType, SerializationConfig config) {
            this.inputType = inputType;
            this.config = config;
        }

        @Override
        public java.util.Map<?, ?> convert(MapBase<?, ?> value) {
            boolean writeNullValues = config.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);

            LinkedHashMap<Object, Object> res = new LinkedHashMap<>();
            MapIterator<?, ?> it = value.iterator();
            while (it.hasNext()) {
                Object k = it.nextKey();
                Object v = it.getValue();

                if (v != null || writeNullValues)
                    res.put(k, v);
            }

            return res;
        }

        public MapLikeType getInputType(TypeFactory typeFactory) {
            return inputType;
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructMapType(java.util.Map.class, inputType.getKeyType(), inputType.getContentType())
                    .withTypeHandler(inputType.getTypeHandler())
                    .withValueHandler(inputType.getValueHandler());
        }
    }
}
