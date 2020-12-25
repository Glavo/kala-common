package asia.kala.collection.mutable;

import asia.kala.factory.CollectionFactory;

public final class MutableArrayTest implements MutableSeqTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, MutableArray<E>> factory() {
        return MutableArray.factory();
    }

}
