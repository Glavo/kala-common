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
        throw new UnsupportedOperationException(); // TODO
    }

    static @NotNull Mutable${Type}Collection of() {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _, _ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _, _, _ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_, _, _, _, _ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection of(${PrimitiveType}... values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection from(${PrimitiveType} @NotNull [] values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection from(@NotNull ${Type}Traversable values) {
        throw new UnsupportedOperationException(); // TODO
    }

    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection from(@NotNull ${Type}Iterator it) {
        throw new UnsupportedOperationException(); // TODO
    }
<#if IsSpecialized>

    /*
    @Contract("_ -> new")
    static @NotNull Mutable${Type}Collection from(@NotNull ${Type}Stream stream) {
        throw new UnsupportedOperationException(); // TODO
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
