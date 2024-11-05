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

import kala.Conditions;
import kala.collection.*;
import kala.collection.base.Iterators;
import kala.annotations.Covariant;
import kala.collection.immutable.ImmutableSet;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

public final class SetViews {
    public static class Of<@Covariant E, C extends Set<E>> extends CollectionViews.Of<E, C> implements SetView<E> {
        public Of(@NotNull C collection) {
            super(collection);
        }
    }

    public static final class Added<@Covariant E> extends AbstractSetView<E> {

        private final @NotNull SetLike<E> source;
        private final E addedValue;

        public Added(@NotNull SetLike<E> source, E addedValue) {
            this.source = source;
            this.addedValue = addedValue;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.prepended(source.iterator(), addedValue);
        }

        @Override
        public boolean contains(Object value) {
            return Objects.equals(addedValue, value) || source.contains(value);
        }

        @Override
        public @NotNull SetLike<E> added(E value) {
            if (this.contains(value)) {
                return this;
            }

            var addedValues = ImmutableSet.of(value, addedValue);
            Conditions.assertEquals(2, addedValues.size());
            return new AddedAll<>(this, addedValues.toImmutableSet());
        }
    }

    public static final class AddedAll<@Covariant E> extends AbstractSetView<E> {
        private final @NotNull SetLike<E> source;
        private final ImmutableSet<E> addedValues;

        public AddedAll(@NotNull SetLike<E> source, ImmutableSet<E> addedValues) {
            this.source = source;
            this.addedValues = addedValues;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.concat(source.iterator(), addedValues.iterator());
        }

        @Override
        public boolean contains(Object value) {
            return source.contains(value) || addedValues.contains(value);
        }
    }

    public static final class Filter<@Covariant E> extends AbstractSetView<E> {

        private final @NotNull SetLike<E> source;

        private final @NotNull Predicate<? super E> predicate;

        public Filter(@NotNull SetLike<E> source, @NotNull Predicate<? super E> predicate) {
            this.source = source;
            this.predicate = predicate;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.filter(source.iterator(), predicate);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(Object value) {
            try {
                return predicate.test((E) value) && source.contains(value);
            } catch (ClassCastException ignored) {
                return false;
            }
        }
    }
}
