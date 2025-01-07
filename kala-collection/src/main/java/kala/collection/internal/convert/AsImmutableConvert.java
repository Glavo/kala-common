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
package kala.collection.internal.convert;

import kala.annotations.StaticClass;
import kala.collection.base.Iterators;
import kala.collection.immutable.AbstractImmutableCollection;
import kala.collection.immutable.ImmutableSeq;
import kala.collection.mutable.MutableCollection;
import kala.collection.mutable.MutableSeq;
import kala.index.Index;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

@StaticClass
public final class AsImmutableConvert {

    public static class CollectionWrapper<E, C extends MutableCollection<E>> extends AbstractImmutableCollection<E> {

        protected final C source;

        public CollectionWrapper(C source) {
            this.source = source;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return Iterators.freeze(source.iterator());
        }

        @Override
        public @NotNull Spliterator<E> spliterator() {
            return source.spliterator();
        }

        @Override
        public @NotNull Stream<E> stream() {
            return source.stream();
        }

        @Override
        public @NotNull Stream<E> parallelStream() {
            return source.parallelStream();
        }

        @Override
        public int size() {
            return source.size();
        }
    }

    public static final class SeqWrapper<E, C extends MutableSeq<E>> extends CollectionWrapper<E, C> implements ImmutableSeq<E> {
        public SeqWrapper(C source) {
            super(source);
        }

        @Override
        public boolean supportsFastRandomAccess() {
            return source.supportsFastRandomAccess();
        }

        @Override
        public E get(@Index int index) {
            return source.get(index);
        }

        @Override
        public int indexOf(Object value) {
            return source.indexOf(value);
        }

        @Override
        public int lastIndexOf(Object value) {
            return source.lastIndexOf(value);
        }

        @Override
        public boolean contains(Object value) {
            return source.contains(value);
        }

        @Override
        public @NotNull Iterator<E> reverseIterator() {
            return Iterators.freeze(source.reverseIterator());
        }
    }

    private AsImmutableConvert() {
    }
}
