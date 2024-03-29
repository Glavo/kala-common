package kala.collection.immutable;

import kala.collection.CollectionView;
import kala.collection.CollectionViewTestTemplate;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class SimpleImmutableSeqTest implements ImmutableSeqTestTemplate {

    @Override
    public Class<?> collectionType() {
        return null;
    }

    @Override
    public void serializationTest() {
    }

    @Override
    public <E> CollectionFactory<E, ?, ImmutableSeq<E>> factory() {
        return new CollectionFactory<E, ArrayList<E>, ImmutableSeq<E>>() {
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
            public SimpleImmutableSeq<E> build(@NotNull ArrayList<E> es) {
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
        public @NotNull Iterator<E> iterator() {
            return list.iterator();
        }
    }

    static final class ViewTest implements CollectionViewTestTemplate {

        @Override
        public <E> CollectionView<E> of(E... elements) {
            return new SimpleImmutableSeq<>(Arrays.asList(elements)).view();
        }

        @Override
        public <E> CollectionView<E> from(E[] elements) {
            return new SimpleImmutableSeq<>(Arrays.asList(elements)).view();
        }

        @Override
        public <E> CollectionView<E> from(Iterable<? extends E> elements) {
            final ArrayList<E> list = new ArrayList<>();
            for (E element : elements) {
                list.add(element);
            }
            return new SimpleImmutableSeq<>(list).view();
        }
    }
}
