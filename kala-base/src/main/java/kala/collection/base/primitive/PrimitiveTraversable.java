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
package kala.collection.base.primitive;

import kala.collection.base.AnyTraversable;
import kala.collection.base.Traversable;
import kala.control.AnyOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PrimitiveTraversable<T> extends AnyTraversable<T> {

    default @NotNull Traversable<T> asGeneric() {
        return new AsGenericTraversable<>(this);
    }

    @Override
    @NotNull PrimitiveIterator<T, ?> iterator();

    //region Size Info

    /**
     * {@inheritDoc}
     */
    @Override
    default int size() {
        return iterator().size();
    }

    @Override
    default int sizeCompare(int otherSize) {
        if (otherSize < 0) {
            return 1;
        }
        final int knownSize = knownSize();
        if (knownSize >= 0) {
            return Integer.compare(knownSize, otherSize);
        }
        int i = 0;
        PrimitiveIterator<T, ?> it = iterator();
        while (it.hasNext()) {
            it.nextIgnoreResult();
            if (i == otherSize) {
                return 1;
            }
            i++;
        }
        return i - otherSize;
    }

    //endregion

    //region Aggregate Operations

    @Nullable T maxOrNull();

    @NotNull AnyOption<T> maxOption();

    @Nullable T minOrNull();

    @NotNull AnyOption<T> minOption();

    //endregion

    //region String Representation

    @Override
    default <A extends Appendable> @NotNull A joinTo(@NotNull A buffer, CharSequence separator, CharSequence prefix, CharSequence postfix) {
        return iterator().joinTo(buffer, separator, prefix, postfix);
    }

    //endregion
}
