package asia.kala.collection;

import asia.kala.annotations.Covariant;
import asia.kala.iterator.Iterators;
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
        @NotNull
        private final View<E> source;

        @NotNull
        private final Predicate<? super E> predicate;

        public Filter(@NotNull View<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @NotNull
        @Override
        public final Iterator<E> iterator() {
            return Iterators.filter(source.iterator(), predicate);
        }
    }
}
