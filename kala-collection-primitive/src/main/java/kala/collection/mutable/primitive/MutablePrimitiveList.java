package kala.collection.mutable.primitive;

import kala.collection.base.primitive.PrimitiveGrowable;
import kala.collection.mutable.MutableAnyList;
import kala.collection.mutable.MutableAnySeq;
import kala.collection.primitive.PrimitiveSeq;

public interface MutablePrimitiveList<E> extends MutablePrimitiveSeq<E>, MutableAnyList<E>, PrimitiveGrowable<E> {
}
