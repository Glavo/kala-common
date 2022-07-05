package kala.collection.primitive;

import kala.collection.SeqIterator;
import kala.collection.base.primitive.PrimitiveIterator;

public interface PrimitiveSeqIterator<T, T_CONSUMER> extends SeqIterator<T>, PrimitiveIterator<T, T_CONSUMER> {
}
