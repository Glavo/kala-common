package org.glavo.kala.collection;

import org.glavo.kala.collection.factory.CollectionFactory;
import org.glavo.kala.collection.internal.ArrayBufferBasedFactory;
import org.glavo.kala.collection.mutable.ArrayBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public final class SimpleSeqTest implements SeqTestTemplate {
    @Override
    public <E> CollectionFactory<E, ?, SimpleSeq<E>> factory() {
        return (SimpleFactory<E>) SimpleFactory.INSTANCE;
    }

    @Override
    public <E> SimpleSeq<E> of(E... elements) {
        return new SimpleSeq<>(List.of(elements));
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

    static final class SimpleFactory<E> extends ArrayBufferBasedFactory<E, SimpleSeq<E>> {
        static final SimpleFactory<?> INSTANCE = new SimpleFactory<>();

        @Override
        public SimpleSeq<E> build(ArrayBuffer<E> builder) {
            return new SimpleSeq<>(List.copyOf(builder.asJava()));
        }
    }
}
