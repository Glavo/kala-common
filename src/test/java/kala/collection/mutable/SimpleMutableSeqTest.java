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
package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import kala.index.Index;
import kala.index.Indexes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public final class SimpleMutableSeqTest implements MutableSeqTestTemplate {
    @SuppressWarnings("ClassEscapesDefinedScope")
    @Override
    public <E> CollectionFactory<E, ?, SimpleMutableSeq<E>> factory() {
        return (SimpleListFactory<E>) SimpleListFactory.INSTANCE;
    }

    @Override
    public Class<?> collectionType() {
        return null;
    }

    @Override
    public void serializationTest() {
    }

    private static final class SimpleMutableSeq<E> extends AbstractMutableSeq<E> {
        private final List<E> list;

        SimpleMutableSeq(List<E> list) {
            this.list = list;
        }

        @Override
        public @NotNull Iterator<E> iterator() {
            return list.iterator();
        }

        @Override
        public void set(@Index int index, E newValue) {
            list.set(Indexes.checkIndex(index, list.size()), newValue);
        }
    }

    private static final class SimpleListFactory<E> implements CollectionFactory<E, ArrayList<E>, SimpleMutableSeq<E>> {
        static final SimpleListFactory<?> INSTANCE = new SimpleListFactory<>();

        @Override
        public ArrayList<E> newBuilder() {
            return new ArrayList<>();
        }

        @Override
        public SimpleMutableSeq<E> build(ArrayList<E> es) {
            return new SimpleMutableSeq<>(es);
        }

        @Override
        public void addToBuilder(@NotNull ArrayList<E> es, E value) {
            es.add(value);
        }

        @Override
        public ArrayList<E> mergeBuilder(@NotNull ArrayList<E> builder1, @NotNull ArrayList<E> builder2) {
            builder1.addAll(builder2);
            return builder1;
        }
    }
}
