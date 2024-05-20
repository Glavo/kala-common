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

import kala.collection.AnySet;
import kala.collection.base.primitive.*;
import kala.collection.factory.primitive.${Type}CollectionFactory;
import kala.collection.immutable.primitive.*;
import kala.collection.primitive.internal.view.${Type}SetViews;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public interface ${Type}Set extends PrimitiveSet<${WrapperType}>, ${Type}Collection, ${Type}SetLike  {

    //region Static Factories

    static ${Type}CollectionFactory<?, ${Type}Set> factory() {
        return ${Type}CollectionFactory.narrow(Immutable${Type}Set.factory());
    }

    static @NotNull ${Type}Set empty() {
        return Immutable${Type}Set.empty();
    }

    static @NotNull ${Type}Set of() {
        return Immutable${Type}Set.of();
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1) {
        return Immutable${Type}Set.of(value1);
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2) {
        return Immutable${Type}Set.of(value1, value2);
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3) {
        return Immutable${Type}Set.of(value1, value2, value3);
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4) {
        return Immutable${Type}Set.of(value1, value2, value3, value4);
    }

    static @NotNull ${Type}Set of(${PrimitiveType} value1, ${PrimitiveType} value2, ${PrimitiveType} value3, ${PrimitiveType} value4, ${PrimitiveType} value5) {
        return Immutable${Type}Set.of(value1, value2, value3, value4, value5);
    }

    static @NotNull ${Type}Set of(${PrimitiveType}... values) {
        return Immutable${Type}Set.of(values);
    }

    static @NotNull ${Type}Set from(${PrimitiveType} @NotNull [] values) {
        return Immutable${Type}Set.from(values);
    }

    static @NotNull ${Type}Set from(@NotNull ${Type}Traversable values) {
        return Immutable${Type}Set.from(values);
    }

    static @NotNull ${Type}Set from(@NotNull ${Type}Iterator it) {
        return Immutable${Type}Set.from(it);
    }

    //endregion

    static int hashCode(@NotNull ${Type}Set set) {
        int h = SET_HASH_MAGIC;
        ${Type}Iterator it = set.iterator();
        while (it.hasNext()) {
            h += ${WrapperType}.hashCode(it.next${Type}());
        }
        return h;
    }

    static boolean equals(@NotNull ${Type}Set set1, @NotNull AnySet<?> set2) {
        if (set1 == set2) return true;
        if (!set1.canEqual(set2) || !set2.canEqual(set1)) return false;

        if (set1.size() != set2.size()) return false;

        if (set2 instanceof ${Type}Set) {
            return set1.containsAll((${Type}Set) set2);
        } else {
            // TODO: return set1.containsAll(set2.asGeneric());
            for (Object v : set2.asGeneric()) {
                if (!(v instanceof ${WrapperType}) || !set1.contains((${WrapperType}) v))
                    return false;
            }
            return true;
        }
    }

<#if Type == "Boolean">
    boolean containsTrue();

    boolean containsFalse();

    default boolean isFull() {
        return containsTrue() && containsFalse();
    }

</#if>
    @Override
    default boolean contains(${PrimitiveType} value) {
<#if Type == "Boolean">
        return value ? containsTrue() : containsFalse();
<#else>
        return iterator().contains(value);
</#if>
    }

    @Override
    default @NotNull String className() {
        return "${Type}Set";
    }

    @Override
    default @NotNull ${Type}CollectionFactory<?, ? extends ${Type}Set> iterableFactory() {
        return factory();
    }

    @Override
    default @NotNull ${Type}SetView view() {
        return new ${Type}SetViews.Of<>(this);
    }

<#if Type == "Boolean">
    @Override
    default @NotNull BooleanIterator iterator() {
        if (containsFalse()) {
            return containsTrue() ? BooleanIterator.of(false, true) : BooleanIterator.of(false);
        }
        if (containsTrue()) {
            return BooleanIterator.of(true);
        }

        return BooleanIterator.empty();
    }

    //region Size Info

    @Override
    default boolean isEmpty() {
        return !containsFalse() && !containsTrue();
    }

    @Override
    default int size() {
        if (containsFalse()) {
            return containsTrue() ? 2 : 1;
        }

        return containsTrue() ? 1 : 0;
    }

    @Override
    default int knownSize() {
        return size();
    }

    //endregion

</#if>
    @Override
    default @NotNull Immutable${Type}Set filter(@NotNull ${Type}Predicate predicate) {
        return Immutable${Type}Set.from(view().filter(predicate));
    }

    @Override
    default @NotNull Immutable${Type}Set filterNot(@NotNull ${Type}Predicate predicate) {
        return Immutable${Type}Set.from(view().filterNot(predicate));
    }
}
