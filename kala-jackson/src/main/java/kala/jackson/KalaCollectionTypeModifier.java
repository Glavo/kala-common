package kala.jackson;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.*;
import kala.collection.base.AnyTraversable;
import kala.control.AnyOption;
import kala.control.AnyTry;
import kala.control.Result;
import kala.internal.MapBase;
import kala.value.AnyValue;

import java.lang.reflect.Type;

public class KalaCollectionTypeModifier extends TypeModifier {
    @Override
    public JavaType modifyType(JavaType type, Type jdkType, TypeBindings context, TypeFactory typeFactory) {
        if (type.isReferenceType() || type.isContainerType())
            return type;

        if (type.isTypeOrSubTypeOf(AnyTraversable.class))
            return CollectionLikeType.upgradeFrom(type, type.findSuperType(AnyTraversable.class).containedTypeOrUnknown(0));

        if (type.isTypeOrSubTypeOf(MapBase.class)) {
            JavaType t = type.findSuperType(MapBase.class);
            return MapLikeType.upgradeFrom(t, t.containedTypeOrUnknown(0), t.containedTypeOrUnknown(1));
        }

        return type;
    }
}
