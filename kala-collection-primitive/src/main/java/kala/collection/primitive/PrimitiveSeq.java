package kala.collection.primitive;

import kala.collection.AnySeq;
import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import org.jetbrains.annotations.NotNull;

public interface PrimitiveSeq<E> extends PrimitiveCollection<E>, PrimitiveSeqLike<E>, AnySeq<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends PrimitiveSeq<E>> iterableFactory();
}
