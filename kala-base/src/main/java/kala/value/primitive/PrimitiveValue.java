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
package kala.value.primitive;

import kala.collection.base.primitive.PrimitiveTraversable;
import kala.value.AnyValue;
import org.jetbrains.annotations.NotNull;

public sealed interface PrimitiveValue<T> extends AnyValue<@NotNull T>, PrimitiveTraversable<T>
        permits BooleanValue, ByteValue, ShortValue, IntValue, LongValue, CharValue, FloatValue, DoubleValue, MutablePrimitiveValue {
    @Override
    @NotNull T getValue();

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default int size() {
        return 1;
    }

    @Override
    default int knownSize() {
        return 1;
    }
}
