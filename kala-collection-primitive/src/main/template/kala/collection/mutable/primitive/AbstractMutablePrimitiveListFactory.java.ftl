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

import kala.collection.factory.primitive.${Type}CollectionFactory;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMutable${Type}ListFactory<B extends Mutable${Type}List> implements ${Type}CollectionFactory<B, B> {
    @Override
    public void addToBuilder(@NotNull B builder, ${PrimitiveType} value) {
        builder.append(value);
    }

    @Override
    public B mergeBuilder(@NotNull B builder1, @NotNull B builder2) {
        builder1.appendAll(builder2);
        return builder1;
    }

    @Override
    public B build(@NotNull B builder) {
        return builder;
    }
}