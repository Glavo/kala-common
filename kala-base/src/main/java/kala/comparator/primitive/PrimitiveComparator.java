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
package kala.comparator.primitive;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public sealed interface PrimitiveComparator<T, C extends PrimitiveComparator<T, C>>
        extends Comparator<T> permits BooleanComparator, IntComparator, LongComparator, ShortComparator, ByteComparator, CharComparator, DoubleComparator, FloatComparator {

    @NotNull C nullsFirst();

    @NotNull C nullsLast();

    @Override
    @NotNull C reversed();
}
