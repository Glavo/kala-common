package kala.collection;

import kala.collection.internal.MutableArrayListBasedFactory;
import kala.collection.mutable.MutableArrayList;
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

    static final class SimpleSeq<E> extends AbstractSeq<E> {
        private final java.util.List<E> list;

        SimpleSeq(List<E> list) {
            this.list = list;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return list.iterator();
        }
    }

    static final class SimpleListFactory<E> extends MutableArrayListBasedFactory<E, SimpleSeq<E>> {
        static final SimpleListFactory<?> INSTANCE = new SimpleListFactory<>();

        @Override
        public SimpleSeq<E> build(MutableArrayList<E> builder) {
            return new SimpleSeq<>(new ArrayList<>(builder.asJava()));
        }
    }
}
