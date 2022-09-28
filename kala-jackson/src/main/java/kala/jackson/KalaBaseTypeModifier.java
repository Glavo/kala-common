package kala.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeModifier;
import kala.control.Option;

import java.lang.reflect.Type;

public class KalaBaseTypeModifier extends TypeModifier {
    @Override
    public JavaType modifyType(JavaType type, Type jdkType, TypeBindings context, TypeFactory typeFactory) {
        if (type.isTypeOrSubTypeOf(Option.class)) {
            if (!(type instanceof ReferenceType)) {
                return ReferenceType.upgradeFrom(type, type.containedTypeOrUnknown(0));
            }
        }

        return type;
    }
}
