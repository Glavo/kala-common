package org.glavo.kala.collection.mutable;

import org.glavo.kala.collection.MapTestTemplate;

public interface MutableMapTestTemplate extends MapTestTemplate {
    <K, V> MutableMap<K, V> create();

}
