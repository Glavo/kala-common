package asia.kala.collection.immutable;


import asia.kala.factory.CollectionFactory;

public final class ImmutableArrayTest implements ImmutableSeqTestTemplate {
    @Override
    public final <E> CollectionFactory<E, ?, ImmutableArray<E>> factory() {
        return ImmutableArray.factory();
    }
}
