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
import kala.collection.Map;
import kala.collection.Seq;
import kala.collection.Set;
import kala.collection.SortedSet;
import kala.collection.base.Traversable;
import kala.collection.factory.CollectionBuilder;
import kala.collection.factory.CollectionFactory;
import kala.collection.factory.MapFactory;
import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableCollection;
import kala.collection.immutable.ImmutableMap;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.immutable.ImmutableSet;
import kala.collection.immutable.ImmutableSortedArraySet;
import kala.collection.immutable.ImmutableSortedSet;
import kala.collection.internal.convert.FromJavaConvert;
import kala.annotations.StaticClass;
import kala.collection.mutable.MutableArrayList;
import kala.collection.mutable.MutableArray;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

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

    public static <K, V> MapFactory<K, V, ?, ? extends ImmutableMap<K, V>> immutableMapFactoryBy(Map<?, ?> map) {
        if (map instanceof ImmutableMap<?,?> immutableMap) {
            return immutableMap.mapFactory();
        } else {
            return ImmutableMap.factory();
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

    public static <E> CollectionFactory<E, ?, ? extends ImmutableSet<E>> immutableSetFactoryBy(Set<E> set) {
        if (set instanceof SortedSet<E> sortedSet) {
            if (set instanceof ImmutableSortedSet<E> immutableSortedSet) {
                return immutableSortedSet.sortedIterableFactory();
            } else {
                return ImmutableSortedArraySet.factory(sortedSet.comparator());
            }
        } else {
            if (set instanceof ImmutableSet<E> immutableSet) {
                return immutableSet.iterableFactory();
            } else {
                return ImmutableSet.factory();
            }
        }
    }

    public static boolean contains(Iterable<?> iterable, Object value) {
        if (iterable instanceof Traversable<?> traversable) {
            return traversable.contains(value);
        }
        if (iterable instanceof java.util.Collection<?> collection) {
            try {
                return collection.contains(value);
            } catch (NullPointerException | ClassCastException ignored) {
                return false;
            }
        }

        if (value == null) {
            for (Object o : iterable) {
                if (null == o) {
                    return true;
                }
            }
        } else {
            for (Object o : iterable) {
                if (value.equals(o)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Predicate<Object> containsPredicate(Iterable<?> values) {
        if (values instanceof Set<?> set) {
            return set::contains;
        }

        java.util.Set<?> set;
        if (values instanceof java.util.Set<?> it) {
            set = it;
        } else {
            var hashSet = new java.util.HashSet<>();
            values.forEach(hashSet::add);
            set = hashSet;
        }

        return value -> {
            try {
                return set.contains(value);
            } catch (NullPointerException | ClassCastException ignored) {
                return false;
            }
        };
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
