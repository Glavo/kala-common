package kala.collection.immutable;

import kala.collection.AbstractSet;
import kala.annotations.Covariant;
import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractImmutableSet<@Covariant E> extends AbstractSet<E> implements ImmutableSet<E> {
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

    static <E, T, Builder> T removed(
            @NotNull ImmutableSet<? extends E> set,
            E value,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        Builder builder = factory.newBuilder();
        factory.sizeHint(builder, set, -1);
        if (value == null) {
            for (E e : set) {
                if (null != e)
                    factory.addToBuilder(builder, e);
            }
        } else {
            for (E e : set) {
                if (!value.equals(e))
                    factory.addToBuilder(builder, e);
            }
        }
        return factory.build(builder);
    }

    static <E, T, Builder> T removedAll(
            @NotNull ImmutableSet<? extends E> set,
            @NotNull Iterable<? extends E> values,
            @NotNull CollectionFactory<? super E, Builder, ? extends T> factory
    ) {
        ImmutableHashSet<? extends E> s = ImmutableHashSet.from(values);
        Builder builder = factory.newBuilder();
        for (E e : set) {
            if (!s.contains(e))
                factory.addToBuilder(builder, e);
        }
        return factory.build(builder);
    }
}
