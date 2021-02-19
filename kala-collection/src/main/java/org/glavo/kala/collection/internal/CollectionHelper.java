package org.glavo.kala.collection.internal;

import org.glavo.kala.collection.ArraySeq;
import org.glavo.kala.collection.IndexedSeq;
import org.glavo.kala.collection.Seq;
import org.glavo.kala.collection.base.Traversable;
import org.glavo.kala.annotations.StaticClass;
import org.glavo.kala.collection.immutable.ImmutableArray;
import org.glavo.kala.collection.immutable.ImmutableSeq;
import org.glavo.kala.collection.mutable.ArrayBuffer;
import org.glavo.kala.collection.mutable.LinkedBuffer;
import org.glavo.kala.collection.mutable.MutableArray;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unchecked")
@StaticClass
@ApiStatus.Internal
public final class CollectionHelper {
    public static Object[] asArray(@NotNull Iterable<?> it) {
        if (it instanceof MutableArray<?>) {
            return ((MutableArray<?>) it).getArray();
        }
        if (it instanceof Traversable<?>) {
            return ((Traversable<?>) it).toArray();
        }
        if (it instanceof java.util.Collection<?>) {
            return ((java.util.Collection<?>) it).toArray();
        }
        ArrayBuffer<Object> buffer = new ArrayBuffer<>();
        for (Object o : it) {
            buffer.append(o);
        }
        return buffer.toArray();
    }

    public static <E> IndexedSeq<E> asIndexedSeq(Object collection) {
        if (collection instanceof IndexedSeq<?>) {
            return (IndexedSeq<E>) collection;
        }
        if (collection instanceof java.util.List<?> && collection instanceof RandomAccess) {
            return new FromJavaConvert.IndexedSeqFromJava<>(((List<E>) collection));
        }
        if (collection instanceof Traversable<?>) {
            return (ArraySeq<E>) ArraySeq.wrap(((Traversable<?>) collection).toArray(Object[]::new));
        }
        if (collection instanceof Object[]) {
            return ArraySeq.wrap(((E[]) collection));
        }

        if (collection instanceof java.util.Collection) {
            return (ArraySeq<E>) ArraySeq.wrap(((java.util.Collection<E>) collection).toArray());
        }
        if (collection instanceof Iterable<?>) {
            return ArrayBuffer.from(((Iterable<E>) collection));
        }
        if (collection instanceof Iterator<?>) {
            return ArrayBuffer.from((Iterator<E>) collection);
        }

        throw new IllegalArgumentException();
    }

    public static <E> org.glavo.kala.collection.Collection<E> asCollection(Object collection) {
        if (collection instanceof org.glavo.kala.collection.Collection<?>) {
            return ((org.glavo.kala.collection.Collection<E>) collection);
        }

        if (collection instanceof java.util.Collection<?>) {
            return new FromJavaConvert.CollectionFromJava<>(((java.util.Collection<E>) collection));
        }

        if (collection instanceof Iterator<?>) {
            Iterator<?> iterator = (Iterator<?>) collection;
            if (!iterator.hasNext()) {
                return ImmutableSeq.empty();
            }
            LinkedBuffer<E> buffer = new LinkedBuffer<>();
            while (iterator.hasNext()) {
                buffer.append(((E) iterator.next()));
            }
            return buffer;
        }

        if (collection instanceof Iterable<?>) {
            LinkedBuffer<E> buffer = new LinkedBuffer<>();
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
            return collection instanceof RandomAccess
                    ? new FromJavaConvert.IndexedSeqFromJava<>((List<E>) collection)
                    : new FromJavaConvert.SeqFromJava<>((List<E>) collection);
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
