package kala.collection.factory.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.base.primitive.${Type}Growable;
import kala.collection.factory.CollectionBuilder;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}CollectionBuilder<R> extends CollectionBuilder<${WrapperType}, R>, ${Type}Growable, ${Type}Consumer {

    @Override
    void plusAssign(${PrimitiveType} value);

    @Override
    R build();

    @Override
    @Deprecated
    @ReplaceWith("plusAssign(${PrimitiveType})")
    default void plusAssign(@NotNull ${WrapperType} value) {
        plusAssign(value.${PrimitiveType}Value());
    }

    @Override
    @Deprecated
    @ReplaceWith("plusAssign(${PrimitiveType})")
    default void accept(${PrimitiveType} value) {
        plusAssign(value);
    }
}
