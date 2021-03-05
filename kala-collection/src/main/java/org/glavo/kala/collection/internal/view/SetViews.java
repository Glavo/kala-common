package org.glavo.kala.collection.internal.view;

import org.glavo.kala.annotations.Covariant;
import org.glavo.kala.collection.AbstractSetView;
import org.glavo.kala.collection.Set;
import org.glavo.kala.collection.SetView;
import org.glavo.kala.collection.View;
import org.glavo.kala.collection.base.Iterators;
import org.glavo.kala.collection.internal.view.Views;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Predicate;

public final class SetViews {
    public static class Of<@Covariant E, C extends Set<E>> extends Views.Of<E, C> implements SetView<E> {
        public Of(@NotNull C collection) {
            super(collection);
        }
    }

    public static final class Filter<@Covariant E> extends AbstractSetView<E> {

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
