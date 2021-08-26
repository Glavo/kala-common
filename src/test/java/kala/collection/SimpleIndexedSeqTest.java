package kala.collection;

import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SimpleIndexedSeqTest implements SeqTestTemplate {
    @Override
    public void ofTest() {
    }

    @Override
    public void fromTest() {
    }

    @Override
    public void serializationTest() {
    }

    @Override
    public <E> CollectionFactory<E, ?, ? extends Seq<? extends E>> factory() {
        return new CollectionFactory<E, ArrayList<E>, IndexedSeq<E>>() {

            @Override
            public ArrayList<E> newBuilder() {
                return new ArrayList<>();
            }

            @Override
            public void addToBuilder(@NotNull ArrayList<E> es, E value) {
                es.add(value);
            }

            @Override
            public ArrayList<E> mergeBuilder(@NotNull ArrayList<E> builder1, @NotNull ArrayList<E> builder2) {
                builder1.addAll(builder2);
                return builder1;
            }

            @Override
            public IndexedSeq<E> build(@NotNull ArrayList<E> es) {
                return new SimpleIndexedSeq<>(es);
            }
        };
    }

    static final class SimpleIndexedSeq<E> extends AbstractSeq<E> implements IndexedSeq<E> {
        private final List<E> list;

        SimpleIndexedSeq(List<E> list) {
            this.list = list;
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public E get(int index) {
            return list.get(index);
        }
    }
}
