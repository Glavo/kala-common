package kala.collection.mutable.primitive;

import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.base.primitive.${Type}Traversable;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.primitive.${Type}Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
<#if IsSpecialized>

import java.util.stream.${Type}Stream;
</#if>

public interface Mutable${Type}Collection extends MutablePrimitiveCollection<${WrapperType}>, ${Type}Collection {
    //region Static Factories

    static @NotNull ${Type}CollectionFactory<?, Mutable${Type}Collection> factory() {
        return ${Type}CollectionFactory.narrow(Mutable${Type}Seq.factory());
    }

    static @NotNull Mutable${Type}Collection of() {
        return Mutable${Type}Seq.of();
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1) {
        return Mutable${Type}Seq.of(value1);
    }

    @Contract("_, _ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return Mutable${Type}Seq.of(value1, value2);
    }

    @Contract("_, _, _ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return Mutable${Type}Seq.of(value1, value2, value3);
    }

    @Contract("_, _, _, _ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return Mutable${Type}Seq.of(value1, value2, value3, value4);
    }

    @Contract("_, _, _, _, _ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return Mutable${Type}Seq.of(value1, value2, value3, value4, value5);
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType}... values) {
        return Mutable${Type}Seq.of(values);
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection from(${PrimitiveType} @NotNull [] values) {
        return Mutable${Type}Seq.from(values);
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection from(@NotNull ${Type}Traversable values) {
        return Mutable${Type}Seq.from(values);
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection from(@NotNull ${Type}Iterator it) {
        return Mutable${Type}Seq.from(it);
    }
<#if IsSpecialized>

    /*
    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection from(@NotNull ${Type}Stream stream) {
        return Mutable${Type}Seq.from(stream);
    }
     */
</#if>

    //endregion
    
    @Override
    default @NotNull String className() {
        return "Mutable${Type}Collection";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends Mutable${Type}Collection> iterableFactory() {
        return Mutable${Type}Collection.factory();
    }
}
