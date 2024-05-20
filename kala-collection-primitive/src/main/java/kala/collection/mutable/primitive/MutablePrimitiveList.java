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
package kala.collection.mutable.primitive;

import kala.collection.base.primitive.PrimitiveGrowable;
import kala.collection.factory.primitive.PrimitiveCollectionFactory;
import kala.collection.mutable.MutableAnyList;
import kala.collection.mutable.MutableAnySeq;
import kala.collection.primitive.PrimitiveSeq;
import org.jetbrains.annotations.NotNull;

public interface MutablePrimitiveList<E> extends MutablePrimitiveSeq<E>, MutableAnyList<E>, PrimitiveGrowable<E> {
    @Override
    @NotNull PrimitiveCollectionFactory<E, ?, ? extends MutablePrimitiveList<E>> iterableFactory();
}
