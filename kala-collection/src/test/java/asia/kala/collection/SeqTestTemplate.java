package asia.kala.collection;

import asia.kala.factory.CollectionFactory;

public interface SeqTestTemplate extends CollectionTestTemplate {
    @Override
    <E> CollectionFactory<E, ?, ? extends Seq<? extends E>> factory();
}
