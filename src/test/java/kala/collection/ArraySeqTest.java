package kala.collection;

import kala.collection.factory.CollectionFactory;

public class ArraySeqTest implements SeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, ? extends ArraySeq<E>> factory() {
        return ArraySeq.factory();
    }

    static final class ViewTest implements SeqViewTestTemplate {

        @Override
        public <E> SeqView<E> of(E... elements) {
            return ArraySeq.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(E[] elements) {
            return ArraySeq.from(elements).view();
        }

        @Override
        public <E> SeqView<E> from(Iterable<? extends E> elements) {
            return ArraySeq.<E>from(elements).view();
        }
    }
}
