package kala.collection;

import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public final class SimpleSeqTest implements SeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, SimpleSeq<E>> factory() {
        return (SimpleListFactory<E>) SimpleListFactory.INSTANCE;
    }

    @Override
    public <E> SimpleSeq<E> of(E... elements) {
        return new SimpleSeq<>(Arrays.asList(elements));
    }

    @Override
    public <E> SimpleSeq<E> from(E[] elements) {
        return new SimpleSeq<>(List.of(elements));
    }

    @Override
    public <E> SimpleSeq<E> from(Iterable<? extends E> elements) {
        final ArrayList<E> list = new ArrayList<>();
        for (E e : elements) {
            list.add(e);
        }
        return new SimpleSeq<>(list);
    }

    @Override
    public Class<?> collectionType() {
        return null;
    }

    @Override
    public void serializationTest() {
    }

    private static final class SimpleSeq<E> extends AbstractSeq<E> {
        private final java.util.List<E> list;

        SimpleSeq(List<E> list) {
            this.list = list;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return list.iterator();
        }
    }

    private static final class SimpleListFactory<E> implements CollectionFactory<E, ArrayList<E>, SimpleSeq<E>> {
        static final SimpleListFactory<?> INSTANCE = new SimpleListFactory<>();

        @Override
        public ArrayList<E> newBuilder() {
            return new ArrayList<>();
        }

        @Override
        public SimpleSeq<E> build(ArrayList<E> es) {
            return new SimpleSeq<>(es);
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
    }
}
