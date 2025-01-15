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
package kala.collection.mutable;

import kala.collection.factory.CollectionFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class MutableListFromJavaTest implements MutableListTestTemplate {

    @Override
    @SuppressWarnings("unchecked")
    public <E> CollectionFactory<E, ?, MutableList<E>> factory() {
        return (Factory<E>) Factory.INSTANCE;
    }

    @Override
    public Class<?> collectionType() {
        return null;
    }

    private static final class Factory<E> implements CollectionFactory<E, ArrayList<E>, MutableList<E>> {
        static final Factory<?> INSTANCE = new Factory<>();

        @Override
        public ArrayList<E> newBuilder() {
            return new ArrayList<>();
        }

        @Override
        public MutableList<E> build(ArrayList<E> list) {
            return MutableList.wrapJava(new ArrayList<>(list));
        }

        @Override
        public void addToBuilder(@NotNull ArrayList<E> list, E value) {
            list.add(value);
        }

        @Override
        public ArrayList<E> mergeBuilder(@NotNull ArrayList<E> builder1, @NotNull ArrayList<E> builder2) {
            builder1.addAll(builder2);
            return builder1;
        }
    }
}