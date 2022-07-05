package kala.collection.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.SeqIterator;
import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.mutable.MutableListIterator;
<#if !IsSpecialized>
import kala.function.${Type}Consumer;
</#if>
import org.jetbrains.annotations.NotNull;
<#if IsSpecialized>

import java.util.function.${Type}Consumer;
</#if>

public interface ${Type}SeqIterator extends PrimitiveSeqIterator<${WrapperType}, ${Type}Consumer>, ${Type}Iterator {
    /**
     * {@inheritDoc}
     */
    boolean hasNext();

    /**
     * {@inheritDoc}
     */
    ${PrimitiveType} next${Type}();

    /**
     * {@inheritDoc}
     */
    boolean hasPrevious();

    ${PrimitiveType} previous${Type}();

    /**
     * {@inheritDoc}
     */
    int nextIndex();

    /**
     * {@inheritDoc}
     */
    int previousIndex();

    @Override
    @Deprecated
    @ReplaceWith("next${Type}()")
    default @NotNull ${WrapperType} next() {
        return next${Type}();
    }

    @Override
    @Deprecated
    @ReplaceWith("previous${Type}()")
    default @NotNull ${WrapperType} previous() {
        return previous${Type}();
    }

    //region Modification Operations

    @Deprecated
    default void set(@NotNull ${WrapperType} e) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default void add(@NotNull ${WrapperType} e) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    default void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    //endregion

    default @NotNull ${Type}SeqIterator frozen() {
        return this;
    }
}
