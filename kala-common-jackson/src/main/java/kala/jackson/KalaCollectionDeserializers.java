package kala.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import kala.collection.SeqLike;

public class KalaCollectionDeserializers extends Deserializers.Base {
    @Override
    public JsonDeserializer<?> findCollectionLikeDeserializer(
            CollectionLikeType type,
            DeserializationConfig config,
            BeanDescription beanDesc,
            TypeDeserializer elementTypeDeserializer,
            JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        return null;
    }

    private static final class Instantiator extends StdValueInstantiator {

        public Instantiator(DeserializationConfig config, Class<?> valueType) {
            super(config, valueType);
        }

        public Instantiator(DeserializationConfig config, JavaType valueType) {
            super(config, valueType);
        }

        protected Instantiator(StdValueInstantiator src) {
            super(src);
        }
    }
}
