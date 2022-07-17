package kala.collection.primitive.internal.view;

import kala.collection.primitive.*;
import kala.collection.base.primitive.${Type}Iterator;
import kala.function.*;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.*;

public final class ${Type}SetViews {
    public static final ${Type}SetView EMPTY = new Empty();

    public static class Of<C extends ${Type}Set> extends ${Type}CollectionViews.Of<C> implements ${Type}SetView {
        public Of(@NotNull C collection) {
            super(collection);
        }
    }

    public static class Empty extends ${Type}CollectionViews.Empty implements ${Type}SetView {
    }

    public static final class Filter extends Abstract${Type}SetView {

        private final @NotNull ${Type}CollectionView source;

        private final @NotNull ${Type}Predicate predicate;

        public Filter(@NotNull ${Type}CollectionView source, @NotNull ${Type}Predicate predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public @NotNull ${Type}Iterator iterator() {
            return source.iterator().filter(predicate);
        }
    }
}
