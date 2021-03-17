package org.glavo.kala.collection.base;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

final class MapIterators {
    private MapIterators() {
    }

    static final MapIterator<?, ?> EMPTY = new MapIterator<Object, Object>() {
        @Override
        public final Object nextKey() {
            throw new NoSuchElementException();
        }

        @Override
        public final Object getValue() {
            return null;
        }

        @Override
        public boolean hasNext() {
            return false;
        }
    };

    static final class OfIterator<K, V> extends AbstractMapIterator<K, V> {

        private final Iterator<? extends Map.Entry<? extends K, ? extends V>> source;

        private V value;

        OfIterator(Iterator<? extends Map.Entry<? extends K, ? extends V>> source) {
            this.source = source;
        }

        @Override
        public final boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public final K nextKey() {
            Map.Entry<? extends K, ? extends V> n = source.next();
            this.value = n.getValue();
            return n.getKey();
        }

        @Override
        public final V getValue() {
            return value;
        }
    }

    static final class Mapped<E, K, V> extends AbstractIterator<E> {
        private final @NotNull MapIterator<? extends K, ? extends V> source;
        private final @NotNull BiFunction<? super K, ? super V, ? extends E> mapper;

        Mapped(@NotNull MapIterator<? extends K, ? extends V> source, @NotNull BiFunction<? super K, ? super V, ? extends E> mapper) {
            this.source = source;
            this.mapper = mapper;
        }

        @Override
        public final boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public final E next() {
            return mapper.apply(source.nextKey(), source.getValue());
        }
    }

}
