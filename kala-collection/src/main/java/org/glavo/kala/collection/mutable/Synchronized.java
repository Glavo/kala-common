package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.*;
import org.glavo.kala.collection.Collection;
import org.glavo.kala.collection.Map;
import org.glavo.kala.collection.Set;
import org.glavo.kala.collection.base.AnyTraversable;
import org.glavo.kala.collection.base.Traversable;
import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.immutable.ImmutableArray;
import org.glavo.kala.collection.immutable.ImmutableList;
import org.glavo.kala.collection.immutable.ImmutableSeq;
import org.glavo.kala.collection.immutable.ImmutableVector;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.*;
import org.glavo.kala.tuple.Tuple2;
import org.glavo.kala.tuple.primitive.IntObjTuple2;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

final class Synchronized {
    private Synchronized() {
    }

    public static class SynchronizedCollection<E, C extends MutableCollection<E>> extends AbstractMutableCollection<E> {
        final C source;
        final Object mutex;

        public SynchronizedCollection(C source) {
            this(source, source);
        }

        public SynchronizedCollection(C source, Object mutex) {
            this.source = source;
            this.mutex = mutex;
        }

        //region Collection Operations


        @Override
        public @NotNull String className() {
            return "SynchronizedCollection";
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return source.iterator();
        }

        @Override
        public final @NotNull Spliterator<E> spliterator() {
            return source.spliterator();
        }

        @Override
        public @NotNull View<E> view() {
            return source.view();
        }

        @Override
        public final @NotNull Stream<E> stream() {
            return source.stream();
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream();
        }

        @Override
        public @NotNull MutableCollection<E> asSynchronized() {
            return this;
        }

        @Override
        public @NotNull MutableCollection<E> asSynchronized(@NotNull Object mutex) {
            Objects.requireNonNull(mutex);
            return mutex == this.mutex ? this : new SynchronizedCollection<>(source, mutex);
        }

        //endregion

        //region Size Info

        @Override
        public boolean isEmpty() {
            synchronized (mutex) {
                return source.isEmpty();
            }
        }

        @Override
        public int size() {
            synchronized (mutex) {
                return source.size();
            }
        }

        @Override
        public int knownSize() {
            synchronized (mutex) {
                return source.knownSize();
            }
        }

        //endregion

        //region Size Compare Operations

        @Override
        public int sizeCompare(int otherSize) {
            synchronized (mutex) {
                return source.sizeCompare(otherSize);
            }
        }

        @Override
        public int sizeCompare(@NotNull Iterable<?> other) {
            synchronized (mutex) {
                return source.sizeCompare(other);
            }
        }

        @Override
        public boolean sizeIs(int otherSize) {
            synchronized (mutex) {
                return source.sizeIs(otherSize);
            }
        }

        @Override
        public boolean sizeEquals(int otherSize) {
            synchronized (mutex) {
                return source.sizeEquals(otherSize);
            }
        }

        @Override
        public boolean sizeEquals(@NotNull Iterable<?> other) {
            synchronized (mutex) {
                return source.sizeEquals(other);
            }
        }

        @Override
        public boolean sizeLessThan(int otherSize) {
            synchronized (mutex) {
                return source.sizeLessThan(otherSize);
            }
        }

        @Override
        public boolean sizeLessThan(@NotNull Iterable<?> other) {
            synchronized (mutex) {
                return source.sizeLessThan(other);
            }
        }

        @Override
        public boolean sizeLessThanOrEquals(int otherSize) {
            synchronized (mutex) {
                return source.sizeLessThanOrEquals(otherSize);
            }
        }

        @Override
        public boolean sizeLessThanOrEquals(@NotNull Iterable<?> other) {
            synchronized (mutex) {
                return source.sizeLessThanOrEquals(other);
            }
        }

        @Override
        public boolean sizeGreaterThan(int otherSize) {
            synchronized (mutex) {
                return source.sizeGreaterThan(otherSize);
            }
        }

        @Override
        public boolean sizeGreaterThan(@NotNull Iterable<?> other) {
            synchronized (mutex) {
                return source.sizeGreaterThan(other);
            }
        }

        @Override
        public boolean sizeGreaterThanOrEquals(int otherSize) {
            synchronized (mutex) {
                return source.sizeGreaterThanOrEquals(otherSize);
            }
        }

        @Override
        public boolean sizeGreaterThanOrEquals(@NotNull Iterable<?> other) {
            synchronized (mutex) {
                return source.sizeGreaterThanOrEquals(other);
            }
        }

        //endregion

        //region Element Retrieval Operations

        @Override
        public @NotNull Option<E> find(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.find(predicate);
            }
        }

        //endregion

        //region Element Conditions

        @Override
        public boolean contains(Object value) {
            synchronized (mutex) {
                return source.contains(value);
            }
        }

        @Override
        public boolean containsAll(Object @NotNull [] values) {
            synchronized (mutex) {
                return source.containsAll(values);
            }
        }

        @Override
        public boolean containsAll(@NotNull Iterable<?> values) {
            synchronized (mutex) {
                return source.containsAll(values);
            }
        }

        @Override
        public boolean sameElements(@NotNull Iterable<?> other) {
            synchronized (mutex) {
                return source.sameElements(other);
            }
        }

        @Override
        public boolean sameElements(@NotNull Iterable<?> other, boolean identity) {
            synchronized (mutex) {
                return source.sameElements(other, identity);
            }
        }

        @Override
        public boolean anyMatch(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.anyMatch(predicate);
            }
        }

        @Override
        public boolean allMatch(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.allMatch(predicate);
            }
        }

        @Override
        public boolean noneMatch(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.noneMatch(predicate);
            }
        }

        //endregion

        @Override
        @Contract(value = "_, _ -> param1", mutates = "param1")
        public <G extends Growable<? super E>> @NotNull G filterTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.filterTo(destination, predicate);
            }
        }

        @Override
        @Contract(value = "_, _ -> param1", mutates = "param1")
        public <G extends Growable<? super E>> @NotNull G filterNotTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.filterNotTo(destination, predicate);
            }
        }

        @Override
        @Contract(value = "_ -> param1", mutates = "param1")
        public <G extends Growable<? super E>> @NotNull G filterNotNullTo(@NotNull G destination) {
            synchronized (mutex) {
                return source.filterNotNullTo(destination);
            }
        }

        @Override
        @Contract(value = "_, _ -> param1", mutates = "param1")
        public <U, G extends Growable<? super U>> @NotNull G mapTo(@NotNull G destination, @NotNull Function<? super E, ? extends U> mapper) {
            synchronized (mutex) {
                return source.mapTo(destination, mapper);
            }
        }

        @Override
        public <U, G extends Growable<@NotNull ? super U>> @NotNull G mapNotNullTo(
                @NotNull G destination,
                @NotNull Function<? super E, ? extends U> mapper) {
            synchronized (mutex) {
                return source.mapNotNullTo(destination, mapper);
            }
        }

        //region Aggregate Operations

        @Override
        public int count(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.count(predicate);
            }
        }

        @Override
        public E max() {
            synchronized (mutex) {
                return source.max();
            }
        }

        @Override
        public E max(@NotNull Comparator<? super E> comparator) {
            synchronized (mutex) {
                return source.max(comparator);
            }
        }

        @Override
        @Nullable
        public E maxOrNull() {
            synchronized (mutex) {
                return source.maxOrNull();
            }
        }

        @Override
        @Nullable
        public E maxOrNull(@NotNull Comparator<? super E> comparator) {
            synchronized (mutex) {
                return source.maxOrNull(comparator);
            }
        }

        @Override
        public @NotNull Option<E> maxOption() {
            synchronized (mutex) {
                return source.maxOption();
            }
        }

        @Override
        public @NotNull Option<E> maxOption(@NotNull Comparator<? super E> comparator) {
            synchronized (mutex) {
                return source.maxOption(comparator);
            }
        }

        @Override
        public E min() {
            synchronized (mutex) {
                return source.min();
            }
        }

        @Override
        public E min(@NotNull Comparator<? super E> comparator) {
            synchronized (mutex) {
                return source.min(comparator);
            }
        }

        @Override
        @Nullable
        public E minOrNull() {
            synchronized (mutex) {
                return source.minOrNull();
            }
        }

        @Override
        @Nullable
        public E minOrNull(@NotNull Comparator<? super E> comparator) {
            synchronized (mutex) {
                return source.minOrNull(comparator);
            }
        }

        @Override
        public @NotNull Option<E> minOption() {
            synchronized (mutex) {
                return source.minOption();
            }
        }

        @Override
        public @NotNull Option<E> minOption(@NotNull Comparator<? super E> comparator) {
            synchronized (mutex) {
                return source.minOption(comparator);
            }
        }

        @Override
        public E fold(E zero, @NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            synchronized (mutex) {
                return source.fold(zero, op);
            }
        }

        @Override
        public <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op) {
            synchronized (mutex) {
                return source.foldLeft(zero, op);
            }
        }

        @Override
        public <U> U foldRight(U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op) {
            synchronized (mutex) {
                return source.foldRight(zero, op);
            }
        }

        @Override
        public E reduce(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
            synchronized (mutex) {
                return source.reduce(op);
            }
        }

        @Override
        public @NotNull Option<E> reduceOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            synchronized (mutex) {
                return source.reduceOption(op);
            }
        }

        @Override
        public E reduceLeft(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
            synchronized (mutex) {
                return source.reduceLeft(op);
            }
        }

        @Override
        public @NotNull Option<E> reduceLeftOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            synchronized (mutex) {
                return source.reduceLeftOption(op);
            }
        }

        @Override
        public E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
            synchronized (mutex) {
                return source.reduceRight(op);
            }
        }

        @Override
        public @NotNull Option<E> reduceRightOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
            synchronized (mutex) {
                return source.reduceRightOption(op);
            }
        }
        //endregion

        //region Copy Operations


        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param1")
        public int copyToArray(Object @NotNull [] dest) {
            synchronized (mutex) {
                return source.copyToArray(dest);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param1")
        public int copyToArray(Object @NotNull [] dest, int destPos) {
            synchronized (mutex) {
                return source.copyToArray(dest, destPos);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param1")
        public int copyToArray(Object @NotNull [] dest, int destPos, int limit) {
            synchronized (mutex) {
                return source.copyToArray(dest, destPos, limit);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param2")
        public int copyToArray(int srcPos, Object @NotNull [] dest) {
            synchronized (mutex) {
                return source.copyToArray(srcPos, dest);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param2")
        public int copyToArray(int srcPos, Object @NotNull [] dest, int destPos) {
            synchronized (mutex) {
                return source.copyToArray(srcPos, dest, destPos);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param2")
        public int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
            synchronized (mutex) {
                return source.copyToArray(srcPos, dest, destPos, limit);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param1")
        public int copyToArray(@NotNull MutableArray<? super E> dest) {
            synchronized (mutex) {
                return source.copyToArray(dest);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param1")
        public int copyToArray(@NotNull MutableArray<? super E> dest, int destPos) {
            synchronized (mutex) {
                return source.copyToArray(dest, destPos);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param1")
        public int copyToArray(@NotNull MutableArray<? super E> dest, int destPos, int limit) {
            synchronized (mutex) {
                return source.copyToArray(dest, destPos, limit);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param2")
        public int copyToArray(int srcPos, @NotNull MutableArray<? super E> dest) {
            synchronized (mutex) {
                return source.copyToArray(srcPos, dest);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param2")
        public int copyToArray(int srcPos, @NotNull MutableArray<? super E> dest, int destPos) {
            synchronized (mutex) {
                return source.copyToArray(srcPos, dest, destPos);
            }
        }

        @Override
        @Flow(sourceIsContainer = true, target = "dest", targetIsContainer = true)
        @Contract(mutates = "param2")
        public int copyToArray(int srcPos, @NotNull MutableArray<? super E> dest, int destPos, int limit) {
            synchronized (mutex) {
                return source.copyToArray(srcPos, dest, destPos, limit);
            }
        }

        //endregion

        //region Conversion Operations

        @Override
        public <R, Builder> R collect(@NotNull Collector<? super E, Builder, ? extends R> collector) {
            synchronized (mutex) {
                return source.collect(collector);
            }
        }

        @Override
        public <R, Builder> R collect(@NotNull CollectionFactory<? super E, Builder, ? extends R> factory) {
            synchronized (mutex) {
                return source.collect(factory);
            }
        }

        @Override
        public Object @NotNull [] toArray() {
            synchronized (mutex) {
                return source.toArray();
            }
        }

        @Override
        public <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
            synchronized (mutex) {
                return source.toArray(generator);
            }
        }

        @Override
        public <U> U @NotNull [] toArray(U @NotNull [] array) {
            synchronized (mutex) {
                return source.toArray(array);
            }
        }

        @Override
        public <U> U @NotNull [] toArray(@NotNull Class<U> type) {
            synchronized (mutex) {
                return source.toArray(type);
            }
        }

        @Override
        public @NotNull Seq<E> toSeq() {
            synchronized (mutex) {
                return source.toSeq();
            }
        }

        @Override
        public @NotNull ImmutableSeq<E> toImmutableSeq() {
            synchronized (mutex) {
                return source.toImmutableSeq();
            }
        }

        @Override
        public @NotNull ImmutableArray<E> toImmutableArray() {
            synchronized (mutex) {
                return source.toImmutableArray();
            }
        }

        @Override
        public @NotNull ImmutableList<E> toImmutableList() {
            synchronized (mutex) {
                return source.toImmutableList();
            }
        }

        @Override
        public @NotNull ImmutableVector<E> toImmutableVector() {
            synchronized (mutex) {
                return source.toImmutableVector();
            }
        }

        @Override
        public @NotNull <K, V> Map<K, V> associate(@NotNull Function<? super E, ? extends Tuple2<? extends K, ? extends V>> transform) {
            synchronized (mutex) {
                return source.associate(transform);
            }
        }

        //endregion

        //region Traverse Operations

        @Override
        public void forEach(@NotNull Consumer<? super E> action) {
            synchronized (mutex) {
                source.forEach(action);
            }
        }

        @Override
        public <Ex extends Throwable> void forEachChecked(@NotNull CheckedConsumer<? super E, ? extends Ex> action) throws Ex {
            synchronized (mutex) {
                source.forEachChecked(action);
            }
        }

        @Override
        public void forEachUnchecked(@NotNull CheckedConsumer<? super E, ?> action) {
            synchronized (mutex) {
                source.forEachUnchecked(action);
            }
        }

        //endregion

        //region Comparison and Hashing


        @Override
        public int hashCode() {
            synchronized (mutex) {
                return source.hashCode();
            }
        }

        @Override
        public boolean canEqual(Object other) {
            synchronized (mutex) {
                return source.canEqual(other);
            }
        }

        @Override
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            synchronized (mutex) {
                return source.equals(other);
            }
        }

        //endregion

        //region String Representation

        @Override
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
            synchronized (mutex) {
                return source.joinTo(buffer);
            }
        }

        @Override
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator) {
            synchronized (mutex) {
                return source.joinTo(buffer, separator);
            }
        }

        @Override
        public @NotNull String joinToString() {
            synchronized (mutex) {
                return source.joinToString();
            }
        }

        @Override
        public @NotNull String joinToString(CharSequence separator) {
            synchronized (mutex) {
                return source.joinToString(separator);
            }
        }

        @Override
        public @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
            synchronized (mutex) {
                return source.joinToString(separator, prefix, postfix);
            }
        }


        @Override
        @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix) {
            synchronized (mutex) {
                return source.joinTo(buffer, separator, prefix, postfix);
            }
        }

        //endregion
    }

    public static class SynchronizedSet<E, C extends MutableSet<E>> extends SynchronizedCollection<E, C> implements MutableSet<E> {

        public SynchronizedSet(C source) {
            super(source);
        }

        public SynchronizedSet(C source, Object mutex) {
            super(source, mutex);
        }

        //region Collection Operations


        @Override
        public @NotNull String className() {
            return "SynchronizedSet";
        }

        @Override
        public @NotNull <U> CollectionFactory<U, ?, ? extends MutableSet<U>> iterableFactory() {
            return source.iterableFactory();
        }

        @Override
        public @NotNull java.util.Set<E> asJava() {
            return MutableSet.super.asJava();
        }

        @Override
        public @NotNull SetView<E> view() {
            return source.view();
        }

        @Override
        public @NotNull MutableSet<E> asSynchronized() {
            return this;
        }

        @Override
        public @NotNull MutableSet<E> asSynchronized(@NotNull Object mutex) {
            Objects.requireNonNull(mutex);
            return mutex == this.mutex ? this : new SynchronizedSet<>(source, mutex);
        }

        //endregion


        @Contract(mutates = "this")
        public boolean add(E value) {
            synchronized (mutex) {
                return source.add(value);
            }
        }

        @Contract(mutates = "this")
        public boolean addAll(E @NotNull [] values) {
            synchronized (mutex) {
                return source.addAll(values);
            }
        }

        @Contract(mutates = "this")
        public boolean addAll(@NotNull Iterable<? extends E> values) {
            synchronized (mutex) {
                return source.addAll(values);
            }
        }

        @Contract(mutates = "this")
        public boolean remove(Object value) {
            synchronized (mutex) {
                return source.remove(value);
            }
        }

        @Contract(mutates = "this")
        public boolean removeAll(Object @NotNull [] values) {
            synchronized (mutex) {
                return source.removeAll(values);
            }
        }

        @Contract(mutates = "this")
        public boolean removeAll(@NotNull Iterable<?> values) {
            synchronized (mutex) {
                return source.removeAll(values);
            }
        }

        @Contract(mutates = "this")
        public boolean retainIf(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.retainIf(predicate);
            }
        }

        @Contract(mutates = "this")
        public boolean retainAll(@NotNull Iterable<? super E> values) {
            synchronized (mutex) {
                return source.retainAll(values);
            }
        }

        @Contract(mutates = "this")
        public boolean retainAll(E @NotNull [] values) {
            synchronized (mutex) {
                return source.retainAll(values);
            }
        }

        @Override
        public void clear() {
            synchronized (mutex) {
                source.clear();
            }
        }

        @Contract(mutates = "this")
        public void filterInPlace(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                source.filterInPlace(predicate);
            }
        }

        @Override
        public final Predicate<E> asPredicate() {
            return e -> {
                synchronized (mutex) {
                    return this.contains(e);
                }
            };
        }
    }

    public static class SynchronizedSeq<E, C extends MutableSeq<E>> extends SynchronizedCollection<E, C> implements MutableSeq<E> {
        public SynchronizedSeq(C source) {
            super(source);
        }

        public SynchronizedSeq(C source, Object mutex) {
            super(source, mutex);
        }

        //region Collection Operations

        @Override
        public @NotNull String className() {
            return "SynchronizedSeq";
        }

        @Override
        public @NotNull <U> CollectionFactory<U, ?, ? extends MutableSeq<U>> iterableFactory() {
            return source.iterableFactory();
        }

        public @NotNull SeqView<E> view() {
            return source.view();
        }

        @Override
        public @NotNull List<E> asJava() {
            return MutableSeq.super.asJava();
        }

        @Override
        public @NotNull MutableSeq<E> asSynchronized() {
            return this;
        }

        @Override
        public @NotNull MutableSeq<E> asSynchronized(@NotNull Object mutex) {
            Objects.requireNonNull(mutex);
            return mutex == this.mutex ? this : new SynchronizedSeq<>(source, mutex);
        }

        //endregion

        @Contract(pure = true)
        public boolean isDefinedAt(int index) {
            synchronized (mutex) {
                return source.isDefinedAt(index);
            }
        }

        @Flow(sourceIsContainer = true)
        @Contract(pure = true)
        public E get(int index) {
            synchronized (mutex) {
                return source.get(index);
            }
        }

        @Contract(pure = true)
        public @Nullable E getOrNull(int index) {
            synchronized (mutex) {
                return source.getOrNull(index);
            }
        }

        @Flow(sourceIsContainer = true, targetIsContainer = true)
        @Contract(pure = true)
        public @NotNull Option<E> getOption(int index) {
            synchronized (mutex) {
                return source.getOption(index);
            }
        }

        @NotNull
        public Iterator<E> reverseIterator() {
            synchronized (mutex) {
                return source.reverseIterator();
            }
        }

        public E first() {
            synchronized (mutex) {
                return source.first();
            }
        }

        public E last() {
            synchronized (mutex) {
                return source.last();
            }
        }

        @Contract(pure = true)
        public int indexOf(Object value) {
            synchronized (mutex) {
                return source.indexOf(value);
            }
        }

        @Contract(pure = true)
        public int indexOf(Object value, int from) {
            synchronized (mutex) {
                return source.indexOf(value, from);
            }
        }

        @Contract(pure = true)
        public int indexWhere(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.indexWhere(predicate);
            }
        }

        @Contract(pure = true)
        public int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
            synchronized (mutex) {
                return source.indexWhere(predicate, from);
            }
        }

        @Contract(pure = true)
        public int lastIndexOf(Object value) {
            synchronized (mutex) {
                return source.lastIndexOf(value);
            }
        }

        @Contract(pure = true)
        public int lastIndexOf(Object value, int end) {
            synchronized (mutex) {
                return source.lastIndexOf(value, end);
            }
        }

        @Contract(pure = true)
        public int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                return source.lastIndexWhere(predicate);
            }
        }

        @Contract(pure = true)
        public int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
            synchronized (mutex) {
                return source.lastIndexWhere(predicate, end);
            }
        }

        @Contract(value = "_, _ -> param1", mutates = "param1")
        public <U, G extends Growable<? super U>> @NotNull G mapIndexedTo(@NotNull G destination, @NotNull IndexedFunction<? super E, ? extends U> mapper) {
            synchronized (mutex) {
                return source.mapIndexedTo(destination, mapper);
            }
        }

        public @NotNull SeqView<IntObjTuple2<E>> withIndex() {
            synchronized (mutex) {
                return source.withIndex();
            }
        }

        public E foldIndexed(E zero, @NotNull IndexedBiFunction<? super E, ? super E, ? extends E> op) {
            synchronized (mutex) {
                return source.foldIndexed(zero, op);
            }
        }

        public <U> U foldLeftIndexed(U zero, @NotNull IndexedBiFunction<? super U, ? super E, ? extends U> op) {
            synchronized (mutex) {
                return source.foldLeftIndexed(zero, op);
            }
        }

        public <U> U foldRightIndexed(U zero, @NotNull IndexedBiFunction<? super E, ? super U, ? extends U> op) {
            synchronized (mutex) {
                return source.foldRightIndexed(zero, op);
            }
        }

        public void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
            synchronized (mutex) {
                source.forEachIndexed(action);
            }
        }

        public <Ex extends Throwable> void forEachIndexedChecked(@NotNull CheckedIndexedConsumer<? super E, ? extends Ex> action) throws Ex {
            synchronized (mutex) {
                source.forEachIndexedChecked(action);
            }
        }

        public void forEachIndexedUnchecked(@NotNull CheckedIndexedConsumer<? super E, ?> action) {
            synchronized (mutex) {
                source.forEachIndexedUnchecked(action);
            }
        }

        @Contract(mutates = "this")
        public void set(int index, E newValue) {
            synchronized (mutex) {
                source.set(index, newValue);
            }
        }

        @Contract(mutates = "this")
        public void mapInPlace(@NotNull Function<? super E, ? extends E> mapper) {
            synchronized (mutex) {
                source.mapInPlace(mapper);
            }
        }

        @Contract(mutates = "this")
        public void mapInPlaceIndexed(@NotNull IndexedFunction<? super E, ? extends E> mapper) {
            synchronized (mutex) {
                source.mapInPlaceIndexed(mapper);
            }
        }

        @Override
        public void replaceAll(@NotNull Function<? super E, ? extends E> mapper) {
            synchronized (mutex) {
                source.replaceAll(mapper);
            }
        }

        @Contract(mutates = "this")
        public void sort() {
            synchronized (mutex) {
                source.sort();
            }
        }

        @Contract(mutates = "this")
        public void sort(Comparator<? super E> comparator) {
            synchronized (mutex) {
                source.sort(comparator);
            }
        }

        @Contract(mutates = "this")
        public void reverse() {
            synchronized (mutex) {
                source.reverse();
            }
        }
    }

    public static class SynchronizedIndexedSeq<E, C extends MutableSeq<E> & IndexedSeq<E>> extends SynchronizedSeq<E, C> implements IndexedSeq<E> {

        public SynchronizedIndexedSeq(C source) {
            super(source);
        }

        public SynchronizedIndexedSeq(C source, Object mutex) {
            super(source, mutex);
        }

        @Override
        public @NotNull String className() {
            return "SynchronizedIndexedSeq";
        }

        @Override
        public @NotNull IndexedSeqView<E> view() {
            return source.view();
        }

        @Override
        public @NotNull MutableSeq<E> asSynchronized() {
            return this;
        }

        @Override
        public @NotNull MutableSeq<E> asSynchronized(@NotNull Object mutex) {
            Objects.requireNonNull(mutex);
            return mutex == this.mutex ? this : new SynchronizedIndexedSeq<>(source, mutex);
        }

        @Override
        public @NotNull IndexedSeqView<E> sliceView(int beginIndex, int endIndex) {
            synchronized (mutex) {
                return (IndexedSeqView<E>) source.sliceView(beginIndex, endIndex);
            }
        }

        @Override
        public @NotNull IndexedSeqView<IntObjTuple2<E>> withIndex() {
            synchronized (mutex) {
                return source.withIndex();
            }
        }
    }

    public static class SynchronizedBuffer<E, C extends Buffer<E>> extends SynchronizedSeq<E, C> implements Buffer<E> {

        public SynchronizedBuffer(C source) {
            super(source);
        }

        public SynchronizedBuffer(C source, Object mutex) {
            super(source, mutex);
        }

        @Override
        public @NotNull String className() {
            return "SynchronizedBuffer";
        }

        @Override
        public @NotNull <U> CollectionFactory<U, ?, ? extends Buffer<U>> iterableFactory() {
            return source.iterableFactory();
        }

        @Override
        public @NotNull Buffer<E> asSynchronized() {
            return this;
        }

        @Override
        public @NotNull Buffer<E> asSynchronized(@NotNull Object mutex) {
            Objects.requireNonNull(mutex);
            return mutex == this.mutex ? this : new SynchronizedBuffer<>(source, mutex);
        }

        @Contract(mutates = "this")
        public void append(E value) {
            synchronized (mutex) {
                source.append(value);
            }
        }

        @Contract(mutates = "this")
        public void appendAll(E @NotNull [] values) {
            synchronized (mutex) {
                source.appendAll(values);
            }
        }

        @Contract(mutates = "this")
        public void appendAll(@NotNull Iterable<? extends E> values) {
            synchronized (mutex) {
                source.appendAll(values);
            }
        }

        @Contract(mutates = "this")
        public void prepend(E value) {
            synchronized (mutex) {
                source.prepend(value);
            }
        }

        @Contract(mutates = "this")
        public void prependAll(E @NotNull [] values) {
            synchronized (mutex) {
                source.prependAll(values);
            }
        }

        @Contract(mutates = "this")
        public void prependAll(@NotNull Iterable<? extends E> values) {
            synchronized (mutex) {
                source.prependAll(values);
            }
        }

        @Contract(mutates = "this")
        public void insert(int index, E value) {
            synchronized (mutex) {
                source.insert(index, value);
            }
        }

        @Contract(mutates = "this")
        public void insertAll(int index, E @NotNull [] values) {
            synchronized (mutex) {
                source.insertAll(index, values);
            }
        }

        @Contract(mutates = "this")
        public void insertAll(int index, @NotNull Iterable<? extends E> values) {
            synchronized (mutex) {
                source.insertAll(index, values);
            }
        }

        @Flow(sourceIsContainer = true)
        @Contract(mutates = "this")
        public E removeAt(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
            synchronized (mutex) {
                return source.removeAt(index);
            }
        }

        @Contract(mutates = "this")
        public void removeAt(int index, int count) {
            synchronized (mutex) {
                source.removeAt(index, count);
            }
        }

        @Contract(mutates = "this")
        public void clear() {
            synchronized (mutex) {
                source.clear();
            }
        }

        @Contract(mutates = "this")
        public void dropInPlace(int n) {
            synchronized (mutex) {
                source.dropInPlace(n);
            }
        }

        @Contract(mutates = "this")
        public void dropWhileInPlace(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                source.dropWhileInPlace(predicate);
            }
        }

        @Contract(mutates = "this")
        public void takeInPlace(int n) {
            synchronized (mutex) {
                source.takeInPlace(n);
            }
        }

        @Contract(mutates = "this")
        public void takeWhileInPlace(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                source.takeWhileInPlace(predicate);
            }
        }
    }

    public static class SynchronizedIndexedBuffer<E, C extends Buffer<E> & IndexedSeq<E>> extends SynchronizedIndexedSeq<E, C>
            implements Buffer<E>, IndexedSeq<E> {

        public SynchronizedIndexedBuffer(C source) {
            super(source);
        }

        public SynchronizedIndexedBuffer(C source, Object mutex) {
            super(source, mutex);
        }

        @Override
        public @NotNull String className() {
            return "SynchronizedIndexedBuffer";
        }

        @Override
        public @NotNull <U> CollectionFactory<U, ?, ? extends Buffer<U>> iterableFactory() {
            return source.iterableFactory();
        }

        @Override
        public @NotNull Buffer<E> asSynchronized() {
            return this;
        }

        @Override
        public @NotNull Buffer<E> asSynchronized(@NotNull Object mutex) {
            Objects.requireNonNull(mutex);
            return mutex == this.mutex ? this : new SynchronizedIndexedBuffer<>(source, mutex);
        }

        @Contract(mutates = "this")
        public void append(E value) {
            synchronized (mutex) {
                source.append(value);
            }
        }

        @Contract(mutates = "this")
        public void appendAll(E @NotNull [] values) {
            synchronized (mutex) {
                source.appendAll(values);
            }
        }

        @Contract(mutates = "this")
        public void appendAll(@NotNull Iterable<? extends E> values) {
            synchronized (mutex) {
                source.appendAll(values);
            }
        }

        @Contract(mutates = "this")
        public void prepend(E value) {
            synchronized (mutex) {
                source.prepend(value);
            }
        }

        @Contract(mutates = "this")
        public void prependAll(E @NotNull [] values) {
            synchronized (mutex) {
                source.prependAll(values);
            }
        }

        @Contract(mutates = "this")
        public void prependAll(@NotNull Iterable<? extends E> values) {
            synchronized (mutex) {
                source.prependAll(values);
            }
        }

        @Contract(mutates = "this")
        public void insert(int index, E value) {
            synchronized (mutex) {
                source.insert(index, value);
            }
        }

        @Contract(mutates = "this")
        public void insertAll(int index, E @NotNull [] values) {
            synchronized (mutex) {
                source.insertAll(index, values);
            }
        }

        @Contract(mutates = "this")
        public void insertAll(int index, @NotNull Iterable<? extends E> values) {
            synchronized (mutex) {
                source.insertAll(index, values);
            }
        }

        @Flow(sourceIsContainer = true)
        @Contract(mutates = "this")
        public E removeAt(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
            synchronized (mutex) {
                return source.removeAt(index);
            }
        }

        @Contract(mutates = "this")
        public void removeAt(int index, int count) {
            synchronized (mutex) {
                source.removeAt(index, count);
            }
        }

        @Contract(mutates = "this")
        public void clear() {
            synchronized (mutex) {
                source.clear();
            }
        }

        @Contract(mutates = "this")
        public void dropInPlace(int n) {
            synchronized (mutex) {
                source.dropInPlace(n);
            }
        }

        @Contract(mutates = "this")
        public void dropWhileInPlace(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                source.dropWhileInPlace(predicate);
            }
        }

        @Contract(mutates = "this")
        public void takeInPlace(int n) {
            synchronized (mutex) {
                source.takeInPlace(n);
            }
        }

        @Contract(mutates = "this")
        public void takeWhileInPlace(@NotNull Predicate<? super E> predicate) {
            synchronized (mutex) {
                source.takeWhileInPlace(predicate);
            }
        }
    }
}
