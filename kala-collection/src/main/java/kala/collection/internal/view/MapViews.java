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
package kala.collection.internal.view;

import kala.collection.*;
import kala.collection.base.AbstractMapIterator;
import kala.collection.base.Iterators;
import kala.collection.base.MapIterator;
import kala.control.Option;
import kala.tuple.Tuple2;
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
        public final @NotNull <U> CollectionView<U> map(@NotNull BiFunction<? super K, ? super V, ? extends U> mapper) {
            return CollectionView.empty();
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

    public static class Mapped<E, K, V> extends AbstractCollectionView<E> {
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

    public static class Values<V> extends AbstractCollectionView<V> {
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

    public static class Updated<K, V> extends AbstractMapView<K, V> {
        protected final @NotNull MapLike<K, V> source;
        protected final K targetKey;
        protected final V newValue;

        public Updated(@NotNull MapLike<K, V> source, K targetKey, V newValue) {
            this.source = source;
            this.targetKey = targetKey;
            this.newValue = newValue;
        }

        @Override
        public int size() {
            return source.size() + (source.containsKey(targetKey) ? 0 : 1);
        }

        @Override
        public @NotNull MapIterator<K, V> iterator() {
            final var it = source.iterator();

            return new AbstractMapIterator<>() {
                private boolean first = true;

                private Boolean hasNext = null;
                private K nextKey;
                private V nextValue;

                @Override
                public boolean hasNext() {
                    if (first) {
                        return true;
                    }

                    if (hasNext != null) {
                        return hasNext;
                    }

                    K k;
                    while (it.hasNext()) {
                        k = it.nextKey();
                        if (!Objects.equals(targetKey, k)) {
                            nextKey = k;
                            nextValue = it.getValue();
                            hasNext = true;
                            return true;
                        }
                    }

                    return false;
                }

                @Override
                public K nextKey() {
                    checkStatus();
                    if (first) {
                        first = false;
                        nextValue = Updated.this.newValue;
                        return Updated.this.targetKey;
                    } else {
                        hasNext = null;
                        return nextKey;
                    }
                }

                @Override
                public V getValue() {
                    return nextValue;
                }
            };
        }

        @Override
        public boolean containsKey(K key) {
            return Objects.equals(key, this.targetKey) || source.containsKey(key);
        }

        @Override
        public @NotNull Option<V> getOption(K key) {
            return this.targetKey.equals(key) ? Option.some(newValue) : source.getOption(key);
        }
    }

    public static class Removed<K, V> extends AbstractMapView<K, V> {

        protected final @NotNull MapLike<K, V> source;
        protected final K key;

        public Removed(@NotNull MapLike<K, V> source, K key) {
            this.source = source;
            this.key = key;
        }

        @Override
        public int size() {
            return source.size() - (source.containsKey(key) ? 1 : 0);
        }

        @Override
        public boolean containsKey(K key) {
            return !Objects.equals(this.key, key) && source.containsKey(key);
        }

        @Override
        public @NotNull Option<V> getOption(K key) {
            if (Objects.equals(this.key, key)) {
                return Option.none();
            } else {
                return source.getOption(key);
            }
        }

        @Override
        public @NotNull MapIterator<K, V> iterator() {
            return source.iterator().removed(key);
        }
    }
}
