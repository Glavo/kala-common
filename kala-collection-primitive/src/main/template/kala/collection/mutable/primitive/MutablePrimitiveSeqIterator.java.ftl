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
package kala.collection.mutable.primitive;

import kala.annotations.ReplaceWith;
import kala.collection.primitive.${Type}SeqIterator;
import kala.collection.primitive.internal.${Type}SeqIterators;
<#if !IsSpecialized>
import kala.function.${Type}Consumer;
</#if>
import org.jetbrains.annotations.NotNull;
<#if IsSpecialized>

import java.util.function.${Type}Consumer;
</#if>

public interface Mutable${Type}SeqIterator extends MutablePrimitiveSeqIterator<${WrapperType}, ${Type}Consumer>, ${Type}SeqIterator {

    static @NotNull Mutable${Type}SeqIterator empty() {
        return ${Type}SeqIterators.EMPTY_MUTABLE;
    }

    void set(${PrimitiveType} value);

    @Override
    default @NotNull ${Type}SeqIterator frozen() {
        return new ${Type}SeqIterators.Frozen${Type}SeqIterator(this);
    }

    @Override
    @Deprecated
    @ReplaceWith("set(${PrimitiveType})")
    default void set(@NotNull ${WrapperType} value) {
        this.set(value.${PrimitiveType}Value());
    }
}
