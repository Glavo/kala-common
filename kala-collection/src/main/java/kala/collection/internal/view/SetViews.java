/*
 * Copyright 2024 Glavo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
