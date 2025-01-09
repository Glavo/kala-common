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
package kala.collection;

import kala.annotations.Covariant;
import kala.annotations.DelegateBy;
import kala.collection.base.OrderedTraversable;
import kala.collection.internal.convert.AsJavaConvert;
import kala.comparator.Comparators;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;

public interface SortedSet<@Covariant E> extends Set<E>, OrderedTraversable<E> {

    <U> @NotNull CollectionFactory<U, ?, ? extends SortedSet<U>> iterableFactory(Comparator<? super U> comparator);

    @Override
    default java.util.@NotNull SortedSet<E> asJava() {
        return new AsJavaConvert.SortedSetAsJava<>(this);
    }

    default Comparator<? super E> comparator() {
        return Comparators.naturalOrder();
    }

    @Contract(pure = true)
    default E getFirst() {
        return iterator().next();
    }

    @Contract(pure = true)
    default E getLast() {
        Iterator<E> iterator = iterator();
        E res = iterator.next();
        while (iterator.hasNext()) {
            res = iterator.next();
        }
        return res;
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("getLast()")
    default E max() {
        return getLast();
    }

    @Override
    @ApiStatus.NonExtendable
    @DelegateBy("getFirst()")
    default E min() {
        return getFirst();
    }
}
