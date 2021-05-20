package kala.collection.immutable;

import kala.collection.ArraySeq;
import kala.collection.SortedSet;
import kala.annotations.Covariant;
import kala.collection.Set;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public abstract class AbstractImmutableSet<@Covariant E> extends AbstractImmutableCollection<E> implements ImmutableSet<E> {
    static <E, T, Builder> T added(
            @NotNull ImmutableSet<? extends E> set,
            E value,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, set, 1);
        factory.addAllToBuilder(builder, set);
        factory.addToBuilder(builder, value);
        return factory.build(builder);
    }

    static <E, T, Builder> T addedAll(
            @NotNull ImmutableSet<? extends E> set,
            @NotNull Iterable<? extends E> values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();

        factory.sizeHint(builder, set);
        factory.addAllToBuilder(builder, set);
        factory.sizeHint(builder, values);
        factory.addAllToBuilder(builder, values);

        return factory.build(builder);
    }

    protected final <To extends ImmutableSet<E>> To addedImpl(E value) {
        if (contains(value)) {
            return (To) this;
        }
        CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory =
                this instanceof SortedSet<?>
                        ? (CollectionFactory<E, ?, ? extends ImmutableSet<E>>) ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator())
                        : iterableFactory();


        return (To) AbstractImmutableSet.added(this, value, factory);
    }

    protected final <To extends ImmutableSet<E>> To addedAllImpl(@NotNull Iterable<? extends E> values) {
        CollectionFactory<E, ?, ? extends ImmutableSet<E>> factory =
                this instanceof SortedSet<?>
                        ? (CollectionFactory<E, ?, ? extends ImmutableSet<E>>) ((SortedSet<?>) this).iterableFactory(((SortedSet<?>) this).comparator())
                        : iterableFactory();


        return (To) AbstractImmutableSet.addedAll(this, values, factory);
    }

    protected final <To extends ImmutableSet<E>> To addedAllImpl(E @NotNull [] values) {
        return addedAllImpl(ArraySeq.wrap(values));
    }

    @Override
    public int hashCode() {
        return Set.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Set<?> && Set.equals(this, ((Set<?>) obj));
    }
}
