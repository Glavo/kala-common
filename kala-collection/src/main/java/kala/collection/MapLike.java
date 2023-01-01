package kala.collection;

import kala.collection.base.Growable;
import kala.collection.base.MapIterator;
import kala.collection.immutable.*;
import kala.collection.internal.view.MapViews;
import kala.control.Option;
import kala.function.CheckedBiConsumer;
import kala.internal.MapBase;
import kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.*;

public interface MapLike<K, V> extends MapBase<K, V> {

    default @NotNull String className() {
        return "MapLike";
    }

    default @NotNull MapView<K, V> view() {
        return new MapViews.Of<>(this);
    }

    default @NotNull SetView<K> keysView() {
        return new MapViews.Keys<>(this);
    }

    default @NotNull CollectionView<V> valuesView() {
        return new MapViews.Values<>(this);
    }

    @NotNull MapIterator<K, V> iterator();

    //region Size Info

    default boolean isEmpty() {
        int ks = knownSize();
        if (ks < 0) {
            return !iterator().hasNext();
        } else {
            return ks == 0;
        }
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    default int size() {
        int ks = knownSize();
        if (ks >= 0) {
            return ks;
        }

        int c = 0;
        MapIterator<K, V> it = iterator();
        while (it.hasNext()) {
            it.nextKey();
            ++c;
        }
        return c;
    }

    default int knownSize() {
        return -1;
    }

    //endregion

    default V get(K key) {
        return getOption(key).get();
    }

    default @Nullable V getOrNull(K key) {
        return getOption(key).getOrNull();
    }

    default @NotNull Option<V> getOption(K key) {
        MapIterator<K, V> it = iterator();
        if (key == null) {
            while (it.hasNext()) {
                if (null == it.nextKey()) {
                    return Option.some(it.getValue());
                }
            }
        } else {
            while (it.hasNext()) {
                if (key.equals(it.nextKey())) {
                    return Option.some(it.getValue());
                }
            }
        }
        return Option.none();
    }

    default V getOrDefault(K key, V defaultValue) {
        return getOption(key).getOrDefault(defaultValue);
    }

    default V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
        return getOption(key).getOrElse(supplier);
    }

    default <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
        return getOption(key).getOrThrowException(exception);
    }

    default <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
        return getOption(key).getOrThrow(supplier);
    }

    //region Element Conditions

    default boolean contains(Tuple2<? extends K, ?> value) {
        return value != null && contains(value.component1(), value.component2());
    }

    default boolean contains(K key, Object value) {
        return getOption(key).contains(value);
    }

    default boolean containsKey(K key) {
        if (knownSize() == 0) {
            return false;
        }
        return iterator().containsKey(key);
    }

    default boolean containsValue(Object value) {
        if (knownSize() == 0) {
            return false;
        }
        return iterator().containsValue(value);
    }

    default boolean anyMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        if (knownSize() == 0) {
            return false;
        }
        return iterator().anyMatch(predicate);
    }

    default boolean allMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        if (knownSize() == 0) {
            return true;
        }
        return iterator().allMatch(predicate);
    }

    default boolean noneMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        if (knownSize() == 0) {
            return true;
        }
        return iterator().noneMatch(predicate);
    }

    //endregion

    default <R, G extends Growable<? super R>> @NotNull G mapTo(
            @NotNull G destination, @NotNull BiFunction<? super K, ? super V, ? extends R> mapper) {
        this.forEach((k, v) -> destination.plusAssign(mapper.apply(k, v)));
        return destination;
    }

    default @NotNull MapView.WithDefault<K, V> withDefault(@NotNull Function<? super K, ? extends V> defaultFunction) {
        Objects.requireNonNull(defaultFunction);
        return new MapViews.WithDefaultImpl<>(this, defaultFunction);
    }

    default @NotNull ImmutableMap<K, V> toImmutableMap() {
        return ImmutableMap.from(this);
    }

    default @NotNull ImmutableSeq<Tuple2<K, V>> toImmutableSeq() {
        return ImmutableSeq.from(iterator());
    }

    default @NotNull ImmutableArray<Tuple2<K, V>> toImmutableArray() {
        return ImmutableArray.from(toArray());
    }

    default @NotNull ImmutableLinkedSeq<Tuple2<K, V>> toImmutableLinkedSeq() {
        return ImmutableLinkedSeq.from(this.iterator());
    }

    default @NotNull ImmutableVector<Tuple2<K, V>> toImmutableVector() {
        return ImmutableVector.from(iterator());
    }

    @SuppressWarnings("unchecked")
    default @NotNull Tuple2<K, V>[] toArray() {
        final int size = this.size();
        Tuple2<K, V>[] res = (Tuple2<K, V>[]) new Tuple2<?, ?>[size];
        MapIterator<K, V> it = iterator();
        for (int i = 0; i < size; i++) {
            res[i] = it.next();
        }
        return res;
    }

    default void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        MapIterator<K, V> it = iterator();
        while (it.hasNext()) {
            consumer.accept(it.nextKey(), it.getValue());
        }
    }

    default <Ex extends Throwable> void forEachChecked(
            @NotNull CheckedBiConsumer<? super K, ? super V, ? extends Ex> consumer
    ) throws Ex {
        forEach(consumer);
    }

    default void forEachUnchecked(@NotNull CheckedBiConsumer<? super K, ? super V, ?> consumer) {
        forEach(consumer);
    }

    //region String Representation

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
        return joinTo(buffer, ", ");
    }

    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator) {
        return joinTo(buffer, separator, "", "");
    }

    @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix
    ) {
        if (knownSize() == 0) {
            try {
                buffer.append(prefix).append(postfix);
                return buffer;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return iterator().joinTo(buffer, separator, prefix, postfix);
    }

    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(buffer, ", ", transform);
    }

    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer, CharSequence separator,
            @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(buffer, separator, "", "", transform);
    }

    @Contract(value = "_, _, _, _, _ -> param1", mutates = "param1")
    default <A extends Appendable> @NotNull A joinTo(
            @NotNull A buffer,
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform
    ) {
        if (knownSize() == 0) {
            try {
                buffer.append(prefix).append(postfix);
                return buffer;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return iterator().joinTo(buffer, separator, prefix, postfix, transform);
    }

    default @NotNull String joinToString() {
        return joinTo(new StringBuilder()).toString();
    }

    default @NotNull String joinToString(CharSequence separator) {
        return joinTo(new StringBuilder(), separator).toString();
    }

    default @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return joinTo(new StringBuilder(), separator, prefix, postfix).toString();
    }

    default @NotNull String joinToString(@NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(new StringBuilder(), transform).toString();
    }

    default @NotNull String joinToString(CharSequence separator, @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(new StringBuilder(), separator, transform).toString();
    }

    default @NotNull String joinToString(
            CharSequence separator, CharSequence prefix, CharSequence postfix,
            @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
        return joinTo(new StringBuilder(), separator, prefix, postfix, transform).toString();
    }

    //endregion

}
