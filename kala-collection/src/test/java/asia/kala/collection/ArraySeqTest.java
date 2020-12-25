package asia.kala.collection;

import asia.kala.factory.CollectionFactory;

public class ArraySeqTest implements SeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ? extends ArraySeq<E>> factory() {
        return ArraySeq.factory();
    }
}
