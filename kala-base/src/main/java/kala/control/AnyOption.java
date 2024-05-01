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
package kala.control;

import kala.annotations.Covariant;
import kala.control.primitive.PrimitiveOption;

import java.io.Serializable;

public abstract sealed class AnyOption<@Covariant T> implements Serializable permits Option, PrimitiveOption {
    protected static final int HASH_MAGIC = -818206074;
    protected static final int NONE_HASH = 1937147281;

    protected AnyOption() {
    }
}
