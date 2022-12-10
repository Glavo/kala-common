package kala.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

final class SerializeUtils {
    private SerializeUtils() {
    }

    static void write(Object value, JavaType type, int containedTypeIndex, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        if (type.containedTypeCount() > containedTypeIndex) {
            JavaType containedType = type.containedType(containedTypeIndex);
            if (containedType != null && containedType.hasGenericTypes()) {
                JavaType st = provider.constructSpecializedType(containedType, value.getClass());
                provider.findTypedValueSerializer(st, true, null).serialize(value, gen, provider);
            } else {
                provider.findTypedValueSerializer(value.getClass(), true, null).serialize(value, gen, provider);
            }
        } else {
            gen.writeObject(value);
        }
    }
}
