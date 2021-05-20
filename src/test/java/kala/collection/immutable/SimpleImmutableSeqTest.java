package kala.collection.immutable;

import kala.collection.View;
import kala.collection.ViewTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class SimpleImmutableSeqTest implements ImmutableSeqTestTemplate {

    @Override
    public final <E> CollectionFactory<E, ?, ImmutableSeq<E>> factory() {
        return new CollectionFactory<E, ArrayList<E>, ImmutableSeq<E>>() {
            @Override
            public final ArrayList<E> newBuilder() {
                return new ArrayList<>();
            }

            @Override
            public final void addToBuilder(@NotNull ArrayList<E> es, E value) {
                es.add(value);
            }

            @Override
            public final ArrayList<E> mergeBuilder(@NotNull ArrayList<E> builder1, @NotNull ArrayList<E> builder2) {
                builder1.addAll(builder2);
                return builder1;
            }

            @Override
            public final SimpleImmutableSeq<E> build(@NotNull ArrayList<E> es) {
                return new SimpleImmutableSeq<>(es);
            }
        };
    }

    static final class SimpleImmutableSeq<E> extends AbstractImmutableSeq<E> {

        private final List<E> list;

        SimpleImmutableSeq(List<E> list) {
            this.list = list;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return list.iterator();
        }
    }

    static final class ViewTest implements ViewTestTemplate {

        @Override
        public <E> View<E> of(E... elements) {
            return new SimpleImmutableSeq<>(Arrays.asList(elements)).view();
        }

        @Override
        public <E> View<E> from(E[] elements) {
            return new SimpleImmutableSeq<>(Arrays.asList(elements)).view();
        }

        @Override
        public <E> View<E> from(Iterable<? extends E> elements) {
            final ArrayList<E> list = new ArrayList<>();
            for (E element : elements) {
                list.add(element);
            }
            return new SimpleImmutableSeq<>(list).view();
        }
    }
}
