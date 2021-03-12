package org.glavo.kala.collection.internal.view;

import org.glavo.kala.collection.AbstractMapView;
import org.glavo.kala.collection.Map;
import org.glavo.kala.collection.MapLike;
import org.glavo.kala.collection.base.MapIterator;
import org.glavo.kala.control.Option;
import org.glavo.kala.function.CheckedBiConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public final class MapViews {
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
        @Nullable
        public V getOrNull(K key) {
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
        public boolean containsValue(V value) {
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
        @NotNull
        public String joinToString() {
            return source.joinToString();
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
    }
}
