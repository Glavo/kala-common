package kala.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.*;
import kala.collection.base.AnyTraversable;
import kala.control.*;
import kala.internal.MapBase;
import kala.value.AnyValue;

import java.lang.reflect.Type;

public class KalaBaseTypeModifier extends TypeModifier {
    @Override
    public JavaType modifyType(JavaType type, Type jdkType, TypeBindings context, TypeFactory typeFactory) {
        if (type.isReferenceType() || type.isContainerType())
            return type;

        Class<?> cls;
        if (type.isTypeOrSubTypeOf(cls = AnyOption.class)
                || type.isTypeOrSubTypeOf(cls = AnyTry.class)
                || type.isTypeOrSubTypeOf(cls = AnyValue.class))
            return ReferenceType.upgradeFrom(type, type.findSuperType(cls).containedTypeOrUnknown(0));

        if (type.isTypeOrSubTypeOf(Result.class))
            return ReferenceType.upgradeFrom(type, type);

        if (type.isTypeOrSubTypeOf(AnyTraversable.class))
            return CollectionType.upgradeFrom(type, type.findSuperType(AnyTraversable.class).containedTypeOrUnknown(0));

        if (type.isTypeOrSubTypeOf(MapBase.class)) {
            JavaType t = type.findSuperType(MapBase.class);
            return MapLikeType.upgradeFrom(type, t.containedTypeOrUnknown(0), t.containedTypeOrUnknown(1));
        }

         return type;
    }
}
