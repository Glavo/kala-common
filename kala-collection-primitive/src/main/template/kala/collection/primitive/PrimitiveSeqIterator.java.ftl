/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kala.collection.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.base.primitive.${Type}Iterator;
import kala.collection.primitive.internal.${Type}SeqIterators;
<#if !IsSpecialized>
import kala.function.${Type}Consumer;
</#if>
import org.jetbrains.annotations.NotNull;
<#if IsSpecialized>

import java.util.function.${Type}Consumer;
</#if>

public interface ${Type}SeqIterator extends PrimitiveSeqIterator<${WrapperType}, ${Type}Consumer>, ${Type}Iterator {

    static @NotNull ${Type}SeqIterator empty() {
        return ${Type}SeqIterators.EMPTY;
    }

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

    // @Deprecated
    default void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    //endregion

    default @NotNull ${Type}SeqIterator frozen() {
        return this;
    }
}
