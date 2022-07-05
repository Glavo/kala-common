package kala.collection.mutable.primitive;

import kala.collection.mutable.MutableSeqIterator;
import kala.collection.primitive.PrimitiveSeqIterator;

public interface MutablePrimitiveSeqIterator<T, T_CONSUMER> extends PrimitiveSeqIterator<T, T_CONSUMER>, MutableSeqIterator<T> {
}
