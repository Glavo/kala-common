package org.glavo.kala.collection;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.traversable.Iterators;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

final class SetViews {
    static class Of<@Covariant E, C extends Set<E>> extends Views.Of<E, C> implements SetView<E> {
        Of(@NotNull C collection) {
            super(collection);
        }
    }

    static final class Filter<@Covariant E> extends AbstractSetView<E> {

        private final @NotNull View<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public Filter(@NotNull View<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public final @NotNull Iterator<E> iterator() {
            return Iterators.filter(source.iterator(), predicate);
        }
    }
}
