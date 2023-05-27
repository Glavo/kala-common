package kala.collection.internal.view;

import kala.collection.AbstractSetView;
import kala.collection.SetView;
import kala.collection.CollectionView;
import kala.collection.base.Iterators;
import kala.annotations.Covariant;
import kala.collection.Set;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

public final class SetViews {
    public static class Of<@Covariant E, C extends Set<E>> extends CollectionViews.Of<E, C> implements SetView<E> {
        public Of(@NotNull C collection) {
            super(collection);
        }
    }

    public static final class Filter<@Covariant E> extends AbstractSetView<E> {

        private final @NotNull CollectionView<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public Filter(@NotNull CollectionView<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.filter(source.iterator(), predicate);
        }
    }
}
