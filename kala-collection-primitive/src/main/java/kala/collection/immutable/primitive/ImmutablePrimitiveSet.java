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
package kala.collection.immutable.primitive;

import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import kala.collection.immutable.ImmutableAnySet;
import kala.collection.primitive.PrimitiveSet;
import org.jetbrains.annotations.NotNull;

public interface ImmutablePrimitiveSet<E> extends ImmutablePrimitiveCollection<E>, PrimitiveSet<E>, ImmutableAnySet<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends ImmutablePrimitiveSet<E>> iterableFactory();
}
