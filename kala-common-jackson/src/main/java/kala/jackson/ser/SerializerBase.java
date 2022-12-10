package kala.jackson.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class SerializerBase<T> extends StdSerializer<T> {
    protected final JavaType type;
    private final Class<?> baseClass;

    protected SerializerBase(JavaType type) {
        super(type);
        this.type = type;

        Type t = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.baseClass = t instanceof Class ? (Class<?>) t : t instanceof ParameterizedType ? (Class<?>) ((ParameterizedType) t).getRawType() : null;
    }

    void write(Object val, int containedTypeIndex, JsonGenerator gen, SerializerProvider provider) throws IOException {
        JavaType t = this.type.findSuperType(baseClass);
        if (val != null) {
            if (t.containedTypeCount() > containedTypeIndex) {
                JsonSerializer<Object> ser;
                JavaType containedType = t.containedType(containedTypeIndex);
                if (containedType != null && containedType.hasGenericTypes()) {
                    JavaType st = provider.constructSpecializedType(containedType, val.getClass());
                    ser = provider.findTypedValueSerializer(st, true, null);
                } else {
                    ser = provider.findTypedValueSerializer(val.getClass(), true, null);
                }
                ser.serialize(val, gen, provider);
            } else {
                gen.writeObject(val);
            }
        } else {
            gen.writeNull();
        }
    }
}
