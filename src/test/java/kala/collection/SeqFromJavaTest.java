/*
 * Copyright 2025 Glavo
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
package kala.collection;

import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SeqFromJavaTest implements SeqTestTemplate {

    @Override
    @SuppressWarnings("unchecked")
    public <E> CollectionFactory<E, ?, ? extends Seq<? extends E>> factory() {
        return (CollectionFactory<E, ?, ? extends Seq<? extends E>>) SeqFromJavaFactory.INSTANCE;
    }

    @Override
    public Class<?> collectionType() {
        return null;
    }

    @Override
    public void serializationTest() {
    }

    private static final class SeqFromJavaFactory<E> implements CollectionFactory<E, ArrayList<E>, Seq<E>> {
        static final SeqFromJavaFactory<?> INSTANCE = new SeqFromJavaFactory<>();

        @Override
        public ArrayList<E> newBuilder() {
            return new ArrayList<>();
        }

        @Override
        public Seq<E> build(ArrayList<E> es) {
            return Seq.wrapJava(new ArrayList<>(es));
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
