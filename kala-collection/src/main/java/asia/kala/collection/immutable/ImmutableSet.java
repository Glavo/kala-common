package asia.kala.collection.immutable;

import asia.kala.annotations.Covariant;
import asia.kala.collection.ArraySeq;
import asia.kala.collection.SortedSet;
import asia.kala.factory.CollectionFactory;
import asia.kala.collection.Set;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.Spliterators;

public interface ImmutableSet<@Covariant E> extends ImmutableCollection<E>, Set<E> {

    static <E> CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory() {
        throw new UnsupportedOperationException();// TODO
    }

    @NotNull
    default ImmutableSet<E> added(E value) {
        if (contains(value)) {
            return this;
        }
        if (this instanceof SortedSet<?>) {
            @SuppressWarnings("unchecked")
            CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory =
                    (CollectionFactory<E, ?, ? extends ImmutableSet<E>>)
                            ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());

            return AbstractImmutableSet.added(this, value, factory);
        }
        return AbstractImmutableSet.added(this, value, iterableFactory());
    }

    @NotNull
    default ImmutableSet<E> addedAll(@NotNull Iterable<? extends E> values) {
        if (this instanceof SortedSet<?>) {
            @SuppressWarnings("unchecked")
            CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory =
                    (CollectionFactory<E, ?, ? extends ImmutableSet<E>>)
                            ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator());

            return AbstractImmutableSet.addedAll(this, values, factory);
        }
        return AbstractImmutableSet.addedAll(this, values, iterableFactory());
    }

    @NotNull
    default ImmutableSet<E> addedAll(E @NotNull [] values) {
        return addedAll(ArraySeq.wrap(values));
    }

    //region ImmutableCollection members

    @Override
    default String className() {
        return "ImmutableSet";
    }

    @NotNull
    @Override
    default <U> CollectionFactory<U, ?, ? extends ImmutableSet<U>> iterableFactory() {
        return factory();
    }

    @NotNull
    @Override
    default Spliterator<E> spliterator() {
        final int knownSize = knownSize();
        if (knownSize != 0) {
            return Spliterators.spliterator(iterator(), knownSize, Spliterator.DISTINCT | Spliterator.IMMUTABLE);
        }
        return Spliterators.spliteratorUnknownSize(iterator(), Spliterator.DISTINCT | Spliterator.IMMUTABLE);
    }

    //endregion
}
