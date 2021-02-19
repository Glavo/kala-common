package org.glavo.kala.collection.base;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

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

}
