package kala.collection;

import kala.collection.base.Iterators;
import kala.Conditions;
import kala.collection.factory.MapFactory;
import kala.collection.immutable.ImmutableLinkedSeq;
import kala.collection.immutable.ImmutableMap;
import kala.collection.internal.view.IndexedSeqViews;
import kala.collection.base.Growable;
import kala.control.Option;
import kala.function.IndexedBiFunction;
import kala.function.IndexedConsumer;
import kala.function.IndexedFunction;
import kala.tuple.Tuple2;
import kala.tuple.primitive.IntObjTuple2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Objects;
import java.util.function.*;

public interface IndexedSeqLike<E> extends SeqLike<E>, RandomAccess {

    //region Collection Operations

    @Override
    default @NotNull Iterator<E> iterator() {
        final int size = size();

        if (size == 0) {
            return Iterators.empty();
        }

        return new Iterator<E>() {
            private int idx = 0;

            @Override
            public final boolean hasNext() {
                return idx < size;
            }

            @Override
            public final E next() {
                if (idx >= size) {
                    throw new NoSuchElementException();
                }
                return get(idx++);
            }
        };
    }

    @Override
    default @NotNull Iterator<E> iterator(int beginIndex) {
        final int size = size();
        Conditions.checkPositionIndex(beginIndex, size);

        if (beginIndex == size) {
            return Iterators.empty();
        }

        return new Iterator<E>() {
            private int idx = beginIndex;

            @Override
            public final boolean hasNext() {
                return idx < size;
            }

            @Override
            public final E next() {
                if (idx >= size) {
                    throw new NoSuchElementException();
                }
                return get(idx++);
            }
        };
    }

    @Override
    default @NotNull IndexedSeqView<E> view() {
        return new IndexedSeqViews.Of<>(this);
    }

    //endregion

    //region Size Info

    @Override
    default boolean isEmpty() {
        return size() == 0;
    }

    @Override
    default int knownSize() {
        return size();
    }

    //endregion

    //region Positional Access Operations

    @Override
    default boolean isDefinedAt(int index) {
        return index >= 0 && index < size();
    }

    @Override
    E get(int index);

    @Override
    default @Nullable E getOrNull(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        return get(index);
    }

    @Override
    default @NotNull Option<E> getOption(int index) {
        if (index < 0 || index >= size()) {
            return Option.none();
        }
        return Option.some(get(index));
    }

    //endregion

    //region Reversal Operations

    @Override
    default @NotNull Iterator<E> reverseIterator() {
        return new Iterator<E>() {
            private int idx = size() - 1;

            @Override
            public final boolean hasNext() {
                return idx >= 0;
            }

            @Override
            public final E next() {
                if (idx < 0) {
                    throw new NoSuchElementException();
                }
                return get(idx--);
            }
        };
    }

    //endregion

    //region Element Retrieval Operations

    @Override
    default E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return get(0);
    }

    @Override
    default E first(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = get(i);
            if (predicate.test(e)) {
                return e;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    default @Nullable E firstOrNull() {
        return isEmpty() ? null : get(0);
    }

    @Override
    default @Nullable E firstOrNull(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = get(i);
            if (predicate.test(e)) {
                return e;
            }
        }
        return null;
    }

    @Override
    default @NotNull Option<E> firstOption() {
        return isEmpty() ? Option.none() : Option.some(get(0));
    }

    @Override
    default @NotNull Option<E> firstOption(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = 0; i < size; i++) {
            E e = get(i);
            if (predicate.test(e)) {
                return Option.some(e);
            }
        }
        return Option.none();
    }

    @Override
    default E last() {
        final int size = size();
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return get(size - 1);
    }

    @Override
    default E last(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            E e = get(i);
            if (predicate.test(e)) {
                return e;
            }
        }
        throw new NoSuchElementException();
    }

    @Override
    default @Nullable E lastOrNull() {
        final int size = size();
        return size == 0 ? null : get(size - 1);
    }

    @Override
    default @Nullable E lastOrNull(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            E e = get(i);
            if (predicate.test(e)) {
                return e;
            }
        }
        return null;
    }

    @Override
    default @NotNull Option<E> lastOption() {
        final int size = size();
        return size == 0 ? Option.none() : Option.some(get(size - 1));
    }

    @Override
    default @NotNull Option<E> lastOption(@NotNull Predicate<? super E> predicate) {
        final int size = this.size();
        for (int i = size - 1; i >= 0; i--) {
            E e = get(i);
            if (predicate.test(e)) {
                return Option.some(e);
            }
        }
        return Option.none();
    }

    //endregion

    //region Element Conditions

    @Override
    default boolean contains(Object value) {
        final int size = size();

        if (size == 0) {
            return false;
        }

        if (value == null) {
            for (int i = 0; i < size; i++) {
                if (null == get(i)) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (value.equals(get(i))) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    default boolean anyMatch(@NotNull Predicate<? super E> predicate) {
        final int size = size();

        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    default boolean allMatch(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (!predicate.test(get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean noneMatch(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                return false;
            }
        }
        return true;
    }

    //endregion

    //region Search Operations

    @Override
    default int indexOf(Object value) {
        final int size = size();

        if (value == null) {
            for (int i = 0; i < size; i++) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    default int indexOf(Object value, int from) {
        final int size = size();

        if (from >= size) {
            return -1;
        }

        if (value == null) {
            for (int i = Math.max(from, 0); i < size; i++) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = Math.max(from, 0); i < size; i++) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    default int indexWhere(@NotNull Predicate<? super E> predicate) {
        final int size = size();
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default int indexWhere(@NotNull Predicate<? super E> predicate, int from) {
        final int size = size();

        if (from >= size) {
            return -1;
        }

        for (int i = Math.max(from, 0); i < size; i++) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default int lastIndexOf(Object value) {
        if (value == null) {
            for (int i = size() - 1; i >= 0; i--) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = size() - 1; i >= 0; i--) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    default int lastIndexOf(Object value, int end) {
        if (end < 0) {
            return -1;
        }
        if (value == null) {
            for (int i = Integer.min(end, size() - 1); i >= 0; i--) {
                if (null == get(i)) {
                    return i;
                }
            }
        } else {
            for (int i = Integer.min(end, size() - 1); i >= 0; i--) {
                if (value.equals(get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate) {
        for (int i = size() - 1; i >= 0; i--) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    @Override
    default int lastIndexWhere(@NotNull Predicate<? super E> predicate, int end) {
        if (end < 0) {
            return -1;
        }
        for (int i = end; i >= 0; i--) {
            if (predicate.test(get(i))) { // implicit null check of predicate
                return i;
            }
        }
        return -1;
    }

    default int binarySearch(E value) {
        return binarySearch(value, 0, size());
    }

    default int binarySearch(E value, Comparator<? super E> comparator) {
        return binarySearch(value, comparator, 0, size());
    }

    @SuppressWarnings("unchecked")
    default int binarySearch(E value, int beginIndex, int endIndex) {
        Conditions.checkPositionIndices(beginIndex, endIndex, size());
        int low = beginIndex;
        int high = endIndex - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final E midVal = get(mid);
            final int cmp = ((Comparable<E>) midVal).compareTo(value);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    default int binarySearch(E value, Comparator<? super E> comparator, int beginIndex, int endIndex) {
        if (comparator == null) {
            return binarySearch(value, beginIndex, endIndex);
        }

        Conditions.checkPositionIndices(beginIndex, endIndex, size());
        int low = beginIndex;
        int high = endIndex - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final E midVal = get(mid);
            final int cmp = comparator.compare(midVal, value);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }

    //endregion

    @Override
    default <G extends Growable<? super E>> @NotNull G filterTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        for (int i = 0; i < this.size(); i++) {
            E e = this.get(i);
            if (predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Override
    default <G extends Growable<? super E>> @NotNull G filterNotTo(@NotNull G destination, @NotNull Predicate<? super E> predicate) {
        for (int i = 0; i < this.size(); i++) {
            E e = this.get(i);
            if (!predicate.test(e)) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Override
    default <G extends Growable<? super E>> @NotNull G filterNotNullTo(@NotNull G destination) {
        for (int i = 0; i < this.size(); i++) {
            E e = this.get(i);
            if (e != null) {
                destination.plusAssign(e);
            }
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<? super U>> @NotNull G mapTo(@NotNull G destination, @NotNull Function<? super E, ? extends U> mapper) {
        for (int i = 0; i < this.size(); i++) {
            destination.plusAssign(mapper.apply(this.get(i)));
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<@NotNull ? super U>> @NotNull G mapNotNullTo(
            @NotNull G destination,
            @NotNull Function<? super E, ? extends U> mapper) {
        for (int i = 0; i < this.size(); i++) {
            U u = mapper.apply(this.get(i));
            if (u != null) {
                destination.plusAssign(u);
            }
        }
        return destination;
    }

    @Override
    default <U, G extends Growable<? super U>> @NotNull G mapIndexedTo(@NotNull G destination, @NotNull IndexedFunction<? super E, ? extends U> mapper) {
        for (int i = 0; i < this.size(); i++) {
            destination.plusAssign(mapper.apply(i, this.get(i)));
        }
        return destination;
    }

    @Override
    default @NotNull IndexedSeqView<IntObjTuple2<E>> withIndex() {
        return view().withIndex();
    }

    default <U> @NotNull SeqView<@NotNull Tuple2<E, U>> zipView(@NotNull IndexedSeqLike<? extends U> other) {
        return SeqLike.super.zipView(other);
    }

    //region Aggregate Operations

    @Override
    default int count(@NotNull Predicate<? super E> predicate) {
        final int size = size();

        int c = 0;
        for (int i = 0; i < size; i++) {
            if (predicate.test(get(i))) {
                ++c;
            }
        }
        return c;
    }

    @Override
    default E max(Comparator<? super E> comparator) {
        if (comparator == null) {
            return max();
        }
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E res = get(0);

        for (int i = 1; i < size; i++) {
            E e = get(i);
            if (comparator.compare(res, e) < 0) {
                res = e;
            }
        }

        return res;
    }

    @Override
    default @NotNull Option<E> maxOption(Comparator<? super E> comparator) {
        if (isEmpty()) {
            return Option.none();
        }
        return Option.some(max(comparator));
    }

    @Override
    default E min(Comparator<? super E> comparator) {
        if (comparator == null) {
            return min();
        }
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E res = get(0);

        for (int i = 1; i < size; i++) {
            E e = get(i);
            if (comparator.compare(res, e) > 0) {
                res = e;
            }
        }

        return res;
    }

    @Override
    default @NotNull Option<E> minOption(Comparator<? super E> comparator) {
        if (isEmpty()) {
            return Option.none();
        }
        return Option.some(min(comparator));
    }

    @Override
    default <U> U foldLeft(U zero, @NotNull BiFunction<? super U, ? super E, ? extends U> op) {
        final int size = size();

        for (int i = 0; i < size; i++) {
            zero = op.apply(zero, get(i));
        }
        return zero;
    }

    @Override
    default <U> U foldRight(U zero, @NotNull BiFunction<? super E, ? super U, ? extends U> op) {
        final int size = size();

        for (int i = size - 1; i >= 0; i--) {
            zero = op.apply(get(i), zero);
        }
        return zero;
    }

    @Override
    default <U> U foldLeftIndexed(U zero, @NotNull IndexedBiFunction<? super U, ? super E, ? extends U> op) {
        final int size = size();

        for (int i = 0; i < size; i++) {
            zero = op.apply(i, zero, get(i));
        }
        return zero;
    }

    @Override
    default <U> U foldRightIndexed(U zero, @NotNull IndexedBiFunction<? super E, ? super U, ? extends U> op) {
        final int size = size();

        for (int i = size - 1; i >= 0; i--) {
            zero = op.apply(i, get(i), zero);
        }
        return zero;
    }

    @Override
    default E reduceLeft(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E e = get(0);
        for (int i = 1; i < size; i++) {
            e = op.apply(e, get(i));
        }
        return e;
    }

    @Override
    default E reduceRight(@NotNull BiFunction<? super E, ? super E, ? extends E> op) throws NoSuchElementException {
        final int size = size();

        if (size == 0) {
            throw new NoSuchElementException();
        }

        E e = get(size - 1);
        for (int i = size - 2; i >= 0; i--) {
            e = op.apply(get(i), e);
        }
        return e;
    }

    @Override
    default @Nullable E reduceLeftOrNull(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int size = size();

        if (size == 0) {
            return null;
        }

        E e = get(0);
        for (int i = 1; i < size; i++) {
            e = op.apply(e, get(i));
        }
        return e;
    }

    @Override
    default @Nullable E reduceRightOrNull(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int size = size();

        if (size == 0) {
            return null;
        }

        E e = get(size - 1);
        for (int i = size - 2; i >= 0; i--) {
            e = op.apply(get(i), e);
        }
        return e;
    }

    @Override
    default @NotNull Option<E> reduceLeftOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int size = size();

        if (size == 0) {
            return Option.none();
        }

        E e = get(0);
        for (int i = 1; i < size; i++) {
            e = op.apply(e, get(i));
        }
        return Option.some(e);
    }

    @Override
    default @NotNull Option<E> reduceRightOption(@NotNull BiFunction<? super E, ? super E, ? extends E> op) {
        final int size = size();

        if (size == 0) {
            return Option.none();
        }

        E e = get(size - 1);
        for (int i = size - 2; i >= 0; i--) {
            e = op.apply(get(i), e);
        }
        return Option.some(e);
    }

    //endregion

    @Override
    default int copyToArray(int srcPos, Object @NotNull [] dest, int destPos, int limit) {
        if (srcPos < 0) {
            throw new IllegalArgumentException("srcPos(" + destPos + ") < 0");
        }
        if (destPos < 0) {
            throw new IllegalArgumentException("destPos(" + destPos + ") < 0");
        }

        final int dl = dest.length;
        final int size = size();

        if (destPos >= dl || srcPos >= size) {
            return 0;
        }

        final int n = Math.min(Math.min(size - srcPos, dl - destPos), limit);

        for (int i = 0; i < n; i++) {
            dest[i + destPos] = get(i + srcPos);
        }

        return n;
    }

    @Override
    default Object @NotNull [] toArray() {
        final int size = size();
        Object[] arr = new Object[size];

        for (int i = 0; i < size; i++) {
            arr[i] = get(i);
        }
        return arr;
    }

    @Override
    @SuppressWarnings("unchecked")
    default <U> U @NotNull [] toArray(@NotNull IntFunction<U[]> generator) {
        final int size = size();
        U[] arr = generator.apply(size); // implicit null check of generator

        for (int i = 0; i < size; i++) {
            arr[i] = (U) get(i);
        }
        return arr;
    }

    @Override
    default @NotNull ImmutableLinkedSeq<E> toImmutableLinkedSeq() {
        final int size = size();

        ImmutableLinkedSeq<E> list = ImmutableLinkedSeq.nil();
        for (int i = size - 1; i >= 0; i--) {
            list = list.cons(get(i));
        }
        return list;
    }

    @Override
    default @NotNull <K, V> ImmutableMap<K, V> toImmutableMap() {
        final int size = this.size();
        if (size == 0) {
            return ImmutableMap.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, V, Object, ImmutableMap<K, V>> factory =
                (MapFactory) kala.collection.Map.factory();
        final Object builder = factory.newBuilder();
        factory.sizeHint(builder, size);

        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked") final java.util.Map.Entry<K, V> v = (java.util.Map.Entry<K, V>) this.get(i);
            factory.addToBuilder(builder, v.getKey(), v.getValue());
        }

        return factory.build(builder);
    }

    @Override
    default <K, V>  kala.collection.@NotNull Map<K, V> associate(
            @NotNull Function<? super E, ? extends java.util.Map.Entry<? extends K, ? extends V>> transform) {
        final int size = this.size();
        if (size == 0) {
            return kala.collection.Map.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, V, Object, kala.collection.Map<K, V>> factory =
                (MapFactory) kala.collection.Map.factory();
        final Object builder = factory.newBuilder();
        factory.sizeHint(builder, size);

        for (int i = 0; i < size; i++) {
            final java.util.Map.Entry<? extends K, ? extends V> v = transform.apply(this.get(i));
            factory.addToBuilder(builder, v.getKey(), v.getValue());
        }

        return factory.build(builder);
    }

    @Override
    default <K>  kala.collection.@NotNull Map<K, E> associateBy(@NotNull Function<? super E, ? extends K> keySelector) {
        final int size = this.size();
        if (size == 0) {
            return kala.collection.Map.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, E, Object, kala.collection.Map<K, E>> factory =
                (MapFactory) kala.collection.Map.factory();
        final Object builder = factory.newBuilder();
        factory.sizeHint(builder, size);

        for (int i = 0; i < size; i++) {
            final E e = this.get(i);
            factory.addToBuilder(builder, keySelector.apply(e), e);
        }

        return factory.build(builder);
    }

    @Override
    default <K, V>  kala.collection.@NotNull Map<K, V> associateBy(
            @NotNull Function<? super E, ? extends K> keySelector, @NotNull Function<? super E, ? extends V> valueTransform) {
        final int size = this.size();
        if (size == 0) {
            return kala.collection.Map.empty();
        }

        @SuppressWarnings({"unchecked", "rawtypes"}) final MapFactory<K, V, Object, kala.collection.Map<K, V>> factory =
                (MapFactory) kala.collection.Map.factory();
        final Object builder = factory.newBuilder();
        factory.sizeHint(builder, size);

        for (int i = 0; i < size; i++) {
            final E e = this.get(i);
            factory.addToBuilder(builder, keySelector.apply(e), valueTransform.apply(e));
        }

        return factory.build(builder);
    }

    @Override
    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        final int size = size();

        try {
            buffer.append(prefix);
            if (size > 0) {
                buffer.append(Objects.toString(get(0)));
                for (int i = 1; i < size; i++) {
                    buffer.append(separator).append(Objects.toString(get(i)));
                }
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return buffer;
    }

    @Override
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix, @NotNull Function<? super E, ? extends CharSequence> transform) {
        final int size = size();

        try {
            buffer.append(prefix);
            if (size > 0) {
                buffer.append(transform.apply(get(0)));
                for (int i = 1; i < size; i++) {
                    buffer.append(separator).append(transform.apply(get(i)));
                }
            }
            buffer.append(postfix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return buffer;
    }

    @Override
    default void forEach(@NotNull Consumer<? super E> action) {
        final int size = this.size();

        for (int i = 0; i < size; i++) {
            action.accept(get(i));
        }
    }

    @Override
    default void forEachIndexed(@NotNull IndexedConsumer<? super E> action) {
        final int size = this.size();

        for (int i = 0; i < size; i++) {
            action.accept(i, get(i));
        }
    }

}
