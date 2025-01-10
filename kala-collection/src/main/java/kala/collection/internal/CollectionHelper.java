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
package kala.collection.internal;

import kala.collection.ArraySeq;
import kala.collection.Collection;
import kala.collection.Seq;
import kala.collection.base.Traversable;
import kala.collection.factory.CollectionBuilder;
import kala.collection.factory.CollectionFactory;
import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableCollection;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.internal.convert.FromJavaConvert;
import kala.annotations.StaticClass;
import kala.collection.mutable.MutableArrayList;
import kala.collection.mutable.MutableArray;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
@StaticClass
@ApiStatus.Internal
public final class CollectionHelper {

    public static <E> CollectionFactory<E, ?, ? extends ImmutableCollection<E>> immutableCollectionFactoryBy(Collection<?> collection) {
        if (collection instanceof ImmutableCollection<?> immutableCollection) {
            return immutableCollection.iterableFactory();
        } else if (collection instanceof ArraySeq<?>) {
            return ImmutableArray.factory();
        } else {
            return ImmutableSeq.factory();
        }
    }

    public static <E> CollectionFactory<E, ?, ? extends ImmutableSeq<E>> immutableSeqFactoryBy(Seq<?> seq) {
        if (seq instanceof ImmutableSeq<?> immutableSeq) {
            return immutableSeq.iterableFactory();
        } else if (seq instanceof ArraySeq<?>) {
            return ImmutableArray.factory();
        } else {
            return ImmutableSeq.factory();
        }
    }

    public static <E> CollectionBuilder<E, ? extends ImmutableSeq<E>> newImmutableSeqBuilderBy(Seq<?> seq) {
        if (seq instanceof ImmutableSeq<?> immutableSeq) {
            return immutableSeq.<E>iterableFactory().newCollectionBuilder();
        } else {
            return ImmutableSeq.<E>factory().newCollectionBuilder();
        }
    }

    public static <E> ImmutableSeq<E> emptyImmutableSeqBy(@NotNull Seq<? extends E> seq) {
        if (seq instanceof ImmutableSeq<? extends E> immutableSeq) {
            return immutableSeq.<E>iterableFactory().empty();
        } else if (seq instanceof ArraySeq) {
            return ImmutableArray.empty();
        } else {
            return ImmutableSeq.empty();
        }
    }

    public static Object[] copyToArray(@NotNull Iterable<?> it) {
        if (it instanceof Traversable<?> traversable) {
            return traversable.toArray();
        }
        if (it instanceof java.util.Collection<?> collection) {
            return collection.toArray();
        }
        MutableArrayList<Object> buffer = new MutableArrayList<>();
        for (Object o : it) {
            buffer.append(o);
        }
        return buffer.toArray();
    }

    public static Object[] asArray(@NotNull Iterable<?> it) {
        return it instanceof MutableArray<?> array ? array.getArray() : copyToArray(it);
    }

    public static <E> kala.collection.Collection<E> asSizedCollection(Object obj) {
        if (obj instanceof Seq && ((Seq<?>) obj).supportsFastRandomAccess()) {
            return (Seq<E>) obj;
        }
        if (obj instanceof java.util.List<?> list) {
            return Seq.wrapJava((java.util.List<E>) list);
        }
        if (obj instanceof Traversable<?> traversable) {
            return (ArraySeq<E>) ArraySeq.wrap(traversable.toArray());
        }
        if (obj instanceof Object[] array) {
            return ArraySeq.wrap(((E[]) array));
        }
        if (obj instanceof java.util.Collection<?> collection) {
            return ArraySeq.wrap((E[]) collection.toArray());
        }
        if (obj instanceof Iterable<?> iterable) {
            return MutableArrayList.from(((Iterable<E>) iterable));
        }
        if (obj instanceof Iterator<?> iterator) {
            return MutableArrayList.from((Iterator<E>) iterator);
        }

        throw new IllegalArgumentException(obj + " cannot be converted to an sized collection");
    }

    public static <E> kala.collection.Collection<E> asCollection(Object collection) {
        if (collection instanceof kala.collection.Collection<?>) {
            return ((kala.collection.Collection<E>) collection);
        }

        if (collection instanceof java.util.Collection<?>) {
            return new FromJavaConvert.CollectionFromJava<>(((java.util.Collection<E>) collection));
        }

        if (collection instanceof Iterator<?> iterator) {
            if (!iterator.hasNext()) {
                return ImmutableSeq.empty();
            }
            MutableArrayList<E> buffer = new MutableArrayList<>();
            while (iterator.hasNext()) {
                buffer.append(((E) iterator.next()));
            }
            return buffer;
        }

        if (collection instanceof Iterable<?>) {
            MutableArrayList<E> buffer = new MutableArrayList<>();
            buffer.appendAll(((Iterable<E>) collection));
            return buffer;
        }
        return null;
    }

    public static <E> Seq<E> asSeq(Object collection) {
        if (collection instanceof Seq<?>) {
            return ((Seq<E>) collection);
        }

        if (collection instanceof java.util.List<?>) {
            return new FromJavaConvert.SeqFromJava<>((List<E>) collection);
        }

        if (collection instanceof Object[]) {
            return ArraySeq.wrap(((E[]) collection));
        }

        if (collection instanceof java.util.Collection) {
            return (ArraySeq<E>) ArraySeq.wrap(((java.util.Collection<?>) collection).toArray());
        }
        if (collection instanceof Iterable<?>) {
            return ImmutableArray.from(((Iterable<E>) collection));
        }
        if (collection instanceof Iterator<?>) {
            return ImmutableArray.from((Iterator<E>) collection);
        }

        throw new IllegalArgumentException();
    }
}
