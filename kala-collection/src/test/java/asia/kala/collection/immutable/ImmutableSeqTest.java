package asia.kala.collection.immutable;

import asia.kala.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static asia.kala.collection.Assertions.*;

public final class ImmutableSeqTest implements ImmutableSeqTestTemplate {
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

    @Test
    public final void ofTest() {
        assertIsEmpty(ImmutableSeq.of());
        assertIterableEquals(List.of("str1"), ImmutableSeq.of("str1"));
        assertIterableEquals(List.of("str1", "str2"), ImmutableSeq.of("str1", "str2"));
        assertIterableEquals(List.of("str1", "str2", "str3"), ImmutableSeq.of("str1", "str2", "str3"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4"), ImmutableSeq.of("str1", "str2", "str3", "str4"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5"), ImmutableSeq.of("str1", "str2", "str3", "str4", "str5"));
        assertIterableEquals(List.of("str1", "str2", "str3", "str4", "str5", "str6"), ImmutableSeq.of("str1", "str2", "str3", "str4", "str5", "str6"));
        for (Integer[] data : data1()) {
            assertIterableEquals(Arrays.asList(data), ImmutableSeq.of(data));
        }
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
}
