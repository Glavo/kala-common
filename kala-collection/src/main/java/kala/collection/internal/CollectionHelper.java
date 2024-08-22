package kala.collection.internal;

import kala.collection.ArraySeq;
import kala.collection.Seq;
import kala.collection.base.Traversable;
import kala.collection.immutable.ImmutableArray;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.internal.convert.FromJavaConvert;
import kala.annotations.StaticClass;
import kala.collection.mutable.MutableArrayList;
import kala.collection.mutable.MutableArray;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unchecked")
@StaticClass
@ApiStatus.Internal
public final class CollectionHelper {

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

    public static <E> Seq<E> asIndexedSeq(Object collection) {
        if (collection instanceof Seq<?> seq && seq.supportsFastRandomAccess()) {
            return (Seq<E>) seq;
        }
        if (collection instanceof java.util.List<?> list && list instanceof RandomAccess) {
            return new FromJavaConvert.IndexedSeqFromJava<>(((List<E>) list));
        }
        if (collection instanceof Traversable<?> traversable) {
            return (ArraySeq<E>) ArraySeq.wrap(traversable.toArray(Object[]::new));
        }
        if (collection instanceof Object[] array) {
            return ArraySeq.wrap(((E[]) array));
        }

        if (collection instanceof java.util.Collection<?> juc) {
            return (ArraySeq<E>) ArraySeq.wrap(juc.toArray());
        }
        if (collection instanceof Iterable<?> iterable) {
            return MutableArrayList.from(((Iterable<E>) iterable));
        }
        if (collection instanceof Iterator<?> iterator) {
            return MutableArrayList.from((Iterator<E>) iterator);
        }

        throw new IllegalArgumentException(collection + "cannot be converted to an array");
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
