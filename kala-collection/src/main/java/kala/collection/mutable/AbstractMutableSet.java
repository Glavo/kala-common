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

import kala.collection.AbstractSet;
import org.jetbrains.annotations.Debug;
import org.jetbrains.annotations.NotNull;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class AbstractMutableSet<E> extends AbstractSet<E> implements MutableSet<E> {
    @Override
    public @NotNull MutableSet<E> clone() {
        return MutableSet.super.clone();
    }
}
