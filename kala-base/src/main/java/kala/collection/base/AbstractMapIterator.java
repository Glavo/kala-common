package kala.collection.base;

import kala.collection.base.AbstractIterator;
import kala.collection.base.MapIterator;
import kala.tuple.Tuple2;

public abstract class AbstractMapIterator<K, V> extends AbstractIterator<Tuple2<K, V>> implements MapIterator<K, V> {

}
