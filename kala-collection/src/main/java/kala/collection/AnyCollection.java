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
package kala.collection;

import kala.Equatable;
import org.jetbrains.annotations.ApiStatus;

public interface AnyCollection<E> extends AnyCollectionLike<E>, Equatable {
    int SEQ_HASH_MAGIC = -1140647423;
    int SET_HASH_MAGIC = 1045751549;

    @Override
    @ApiStatus.NonExtendable
    default int knownSize() {
        // The sizes of collections should be known
        return size();
    }
}
