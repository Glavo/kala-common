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
package kala.collection.primitive;

import kala.collection.AnySeq;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(hasChildren = "isNotEmpty()", childrenArray = "toArray()")
public abstract class Abstract${Type}Seq extends Abstract${Type}Collection implements ${Type}Seq {
    @Override
    public int hashCode() {
        return ${Type}Seq.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AnySeq<?> && ${Type}Seq.equals(this, ((AnySeq<?>) obj));
    }
}
