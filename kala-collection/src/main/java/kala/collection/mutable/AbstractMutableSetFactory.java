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

public abstract class AbstractMutableSetFactory<E, S extends MutableSet<E>> implements CollectionFactory<E, S, S> {
    @Override
    public void addToBuilder(@NotNull S es, E value) {
        es.add(value);
    }

    @Override
    public S mergeBuilder(@NotNull S builder1, @NotNull S builder2) {
        builder1.addAll(builder2);
        return builder1;
    }

    @Override
    public S build(@NotNull S es) {
        return es;
    }
}
