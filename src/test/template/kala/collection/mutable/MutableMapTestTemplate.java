package kala.collection.mutable;

import kala.collection.MapTestTemplate;

public interface MutableMapTestTemplate extends MapTestTemplate {
    <K, V> MutableMap<K, V> create();

}
