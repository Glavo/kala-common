package org.glavo.kala.collection.internal.view;

import org.glavo.kala.collection.*;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.base.MapIterator;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.CheckedBiConsumer;
import org.glavo.kala.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.*;

public final class MapViews {
    public static class Empty<K, V> extends AbstractMapView<K, V> {

        public static final Empty<?, ?> INSTANCE = new Empty<>();

        @Override
        public final @NotNull MapIterator<K, V> iterator() {
            return MapIterator.empty();
        }

        @Override
        public final boolean isEmpty() {
            return true;
        }

        @Override
        public final int size() {
            return 0;
        }

        @Override
        public final int knownSize() {
            return 0;
        }

        @Override
        public final @NotNull <U> View<U> map(@NotNull BiFunction<? super K, ? super V, ? extends U> mapper) {
            return View.empty();
        }

        @Override
        public final @NotNull <NV> MapView<K, NV> mapValues(@NotNull BiFunction<? super K, ? super V, ? extends NV> mapper) {
            return MapView.empty();
        }

        @Override
        public String toString() {
            return className() + "{}";
        }
    }

    public static class Of<K, V, M extends MapLike<K, V>> extends AbstractMapView<K, V> {
        protected final @NotNull M source;

        public Of(@NotNull M source) {
            this.source = source;
        }

        @Override
        public @NotNull MapIterator<K, V> iterator() {
            return source.iterator();
        }

        @Override
        public boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public int knownSize() {
            return source.knownSize();
        }

        @Override
        public V get(K key) {
            return source.get(key);
        }

        @Override
        public @Nullable V getOrNull(K key) {
            return source.getOrNull(key);
        }

        @Override
        public @NotNull Option<V> getOption(K key) {
            return source.getOption(key);
        }

        @Override
        public V getOrDefault(K key, V defaultValue) {
            return source.getOrDefault(key, defaultValue);
        }

        @Override
        public V getOrElse(K key, @NotNull Supplier<? extends V> supplier) {
            return source.getOrElse(key, supplier);
        }

        @Override
        public <Ex extends Throwable> V getOrThrowException(K key, @NotNull Ex exception) throws Ex {
            return source.getOrThrowException(key, exception);
        }

        @Override
        public <Ex extends Throwable> V getOrThrow(K key, @NotNull Supplier<? extends Ex> supplier) throws Ex {
            return source.getOrThrow(key, supplier);
        }

        @Override
        public boolean containsKey(K key) {
            return source.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return source.containsValue(value);
        }

        @Override
        public boolean anyMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
            return source.anyMatch(predicate);
        }

        @Override
        public boolean allMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
            return source.allMatch(predicate);
        }

        @Override
        public boolean noneMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
            return source.noneMatch(predicate);
        }

        @Override
        public @NotNull Tuple2<K, V>[] toArray() {
            return source.toArray();
        }

        @Override
        public void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
            source.forEach(consumer);
        }

        @Override
        public <Ex extends Throwable> void forEachChecked(@NotNull CheckedBiConsumer<? super K, ? super V, ? extends Ex> consumer) throws Ex {
            source.forEachChecked(consumer);
        }

        @Override
        public void forEachUnchecked(@NotNull CheckedBiConsumer<? super K, ? super V, ?> consumer) {
            source.forEachUnchecked(consumer);
        }

        @Override
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer) {
            return source.joinTo(buffer);
        }

        @Override
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator) {
            return source.joinTo(buffer, separator);
        }

        @Override
        @Contract(value = "_, _, _, _ -> param1", mutates = "param1")
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix) {
            return source.joinTo(buffer, separator, prefix, postfix);
        }

        @Override
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
            return source.joinTo(buffer, transform);
        }

        @Override
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
            return source.joinTo(buffer, separator, transform);
        }

        @Override
        @Contract(value = "_, _, _, _, _ -> param1", mutates = "param1")
        public <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix, @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
            return source.joinTo(buffer, separator, prefix, postfix, transform);
        }

        @Override
        public @NotNull String joinToString() {
            return source.joinToString();
        }

        @Override
        public @NotNull String joinToString(CharSequence separator) {
            return source.joinToString(separator);
        }

        @Override
        public @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix) {
            return source.joinToString(separator, prefix, postfix);
        }

        @Override
        public @NotNull String joinToString(@NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
            return source.joinToString(transform);
        }

        @Override
        public @NotNull String joinToString(CharSequence separator, @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
            return source.joinToString(separator, transform);
        }

        @Override
        public @NotNull String joinToString(CharSequence separator, CharSequence prefix, CharSequence postfix, @NotNull BiFunction<? super K, ? super V, ? extends CharSequence> transform) {
            return source.joinToString(separator, prefix, postfix, transform);
        }

    }

    public static class WithDefaultImpl<K, V, M extends MapLike<K, V>> extends Of<K, V, M>
            implements MapView.WithDefault<K, V> {
        protected final @NotNull Function<? super K, ? extends V> defaultFunction;

        public WithDefaultImpl(@NotNull M source, @NotNull Function<? super K, ? extends V> defaultFunction) {
            super(source);
            this.defaultFunction = defaultFunction;
        }

        @Override
        public final @NotNull Function<? super K, ? extends V> getDefaultFunction() {
            return defaultFunction;
        }

        @Override
        public V get(K key) {
            Option<V> opt = source.getOption(key);
            return opt.isEmpty() ? defaultFunction.apply(key) : opt.get();
        }

        @Override
        public V getOrNull(K key) {
            return get(key);
        }

        @Override
        public @NotNull Option<V> getOption(K key) {
            Option<V> opt = source.getOption(key);
            return opt.isEmpty() ? Option.some(defaultFunction.apply(key)) : opt;
        }

        @Override
        public @NotNull MapView.WithDefault<K, V> withDefault(@NotNull Function<? super K, ? extends V> defaultFunction) {
            Objects.requireNonNull(defaultFunction);
            return new WithDefaultImpl<>(source, defaultFunction);
        }
    }

    public static class Mapped<E, K, V> extends AbstractView<E> {
        protected final @NotNull MapLike<K, V> source;
        protected final @NotNull BiFunction<? super K, ? super V, ? extends E> mapper;

        public Mapped(@NotNull MapLike<K, V> source, @NotNull BiFunction<? super K, ? super V, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return source.iterator().map(mapper);
        }

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final int knownSize() {
            return source.knownSize();
        }

        //endregion
    }

    public static class Keys<K> extends AbstractSetView<K> {
        protected final @NotNull MapLike<? extends K, ?> source;

        public Keys(@NotNull MapLike<? extends K, ?> source) {
            this.source = source;
        }

        @Override
        public @NotNull String className() {
            return "MapLike.KeysView";
        }

        @Override
        public @NotNull Iterator<K> iterator() {
            return Iterators.narrow(source.iterator().asKeysIterator());
        }

        @Override
        public boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public int knownSize() {
            return source.knownSize();
        }
    }

    public static class Values<V> extends AbstractView<V> {
        protected final @NotNull MapLike<?, ? extends V> source;

        public Values(@NotNull MapLike<?, ? extends V> source) {
            this.source = source;
        }

        @Override
        public @NotNull String className() {
            return "MapLike.ValuesView";
        }

        @Override
        public @NotNull Iterator<V> iterator() {
            return Iterators.narrow(source.iterator().asValuesIterator());
        }

        @Override
        public boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public int size() {
            return source.size();
        }

        @Override
        public int knownSize() {
            return source.knownSize();
        }
    }

    public static class MapValues<K, V, OldV> extends AbstractMapView<K, V> {
        protected final @NotNull MapLike<K, OldV> source;
        protected final @NotNull BiFunction<? super K, ? super OldV, ? extends V> mapper;

        public MapValues(@NotNull MapLike<K, OldV> source, @NotNull BiFunction<? super K, ? super OldV, ? extends V> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final @NotNull MapIterator<K, V> iterator() {
            return source.iterator().mapValues(mapper);
        }

        //region Size Info

        @Override
        public final boolean isEmpty() {
            return source.isEmpty();
        }

        @Override
        public final int size() {
            return source.size();
        }

        @Override
        public final int knownSize() {
            return source.knownSize();
        }

        //endregion
    }
}
