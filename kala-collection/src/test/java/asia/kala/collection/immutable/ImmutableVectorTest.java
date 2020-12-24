package asia.kala.collection.immutable;


import asia.kala.factory.CollectionFactory;

public final class ImmutableVectorTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableVector<E>> factory() {
        return ImmutableVector.factory();
    }
}
